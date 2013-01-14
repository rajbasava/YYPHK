/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.service;

import com.yvphk.common.ApplicationContextUtils;
import com.yvphk.common.ImportObjectMeta;
import com.yvphk.common.Util;
import com.yvphk.model.dao.EventDAO;
import com.yvphk.model.dao.ParticipantDAO;
import com.yvphk.model.form.Event;
import com.yvphk.model.form.HistoryRecord;
import com.yvphk.model.form.ImportFile;
import com.yvphk.model.form.Importable;
import com.yvphk.model.form.Login;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ImportServiceImpl implements ImportService
{
    public static final String ContentTypeXLSX =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String ContentTypeXLS =
            "application/vnd.ms-excel";

    @Autowired
    private ParticipantDAO participantDAO;

    @Autowired
    private EventDAO eventDAO;

    @Transactional
    public void processImportFile (ImportFile importFile, Login login)
    {
        CommonsMultipartFile file = importFile.getFile();
        String contentType = file.getFileItem().getContentType();
        if (Util.nullOrEmptyOrBlank(contentType)) {
            return;
        }

        try {
            Workbook workBook = null;
            if (ContentTypeXLSX.equalsIgnoreCase(contentType)) {
                workBook = new XSSFWorkbook(file.getInputStream());
            }
            else if (ContentTypeXLS.equalsIgnoreCase(contentType)) {
                workBook = new HSSFWorkbook(file.getInputStream());
            }

            Sheet sheet = null;
            int sheetCount = workBook.getNumberOfSheets();
            int sheetMetaCount = importFile.getMeta().getSheets().size();

            if (sheetCount  != sheetMetaCount) {
                return;
            }

            Map mapsToMerge = new HashMap();
            for (int i = 0; i < sheetCount; i++) {
                String sheetName = workBook.getSheetName(i);
                sheet = workBook.getSheet(sheetName);
                if (sheet != null) {
                    ImportObjectMeta.ImportObjectSheet sheetMeta = importFile.getMeta().getSheets().get(i);
                    Map processedSheetMap = processSheet(sheet, sheetMeta.getSheetName());
                    mapsToMerge.put(i, processedSheetMap);
                }
            }

            Map mergedMap = mergeMaps(mapsToMerge);

            processMergedMap(mergedMap, login, importFile);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Map Structure:
     * Key - Map
     */
    private Integer processMergedMap (Map mergedMap,
                                      Login login,
                                      ImportFile importFile)
    {
        Event event = null;
        if (importFile.getEventId() != null) {
            event = eventDAO.getEvent(importFile.getEventId());
        }
        ImportObjectMeta meta = importFile.getMeta();
        Set keys = mergedMap.keySet();
        Iterator itr = keys.iterator();
        int sheetMetaCount = meta.getSheets().size();
        ImportObjectMeta.ImportObjectSheet sheet1Meta = meta.getSheets().get(0);

        ImportObjectMeta.ImportObjectSheet sheet2Meta = null;
        if (sheetMetaCount > 1) {
            sheet2Meta = meta.getSheets().get(1);
        }
        int i = 0;
        while (itr.hasNext()) {
            Integer key = (Integer) itr.next();
            ArrayList list = (ArrayList) mergedMap.get(key);
            Map map = new HashMap();

            List sheet1Data = (List) ((Map) list.get(0)).get(sheet1Meta.getSheetName());
            map.put(sheet1Meta.getSheetName(), sheet1Data);
            if (sheetMetaCount > 1 ) {
                Map tmpMap = (Map) list.get(1);
                List sheet2Data = new ArrayList();
                if (tmpMap != null) {
                    sheet2Data = (List) tmpMap.get(sheet2Meta.getSheetName());
                    map.put(sheet2Meta.getSheetName(), sheet2Data);
                }
            }

            Importable importable =
                    (Importable) Util.createInstance(meta.getBaseClass());
            processImportableData(map, importable, meta);
            importable.initializeForImport(login.getEmail());
            if (event != null) {
                importable.applyEvent(event);
            }
            ApplicationContext context = ApplicationContextUtils.getApplicationContext();
            Object daoObj = context.getBean(ImportableService.class);
            try {
                Method method = daoObj.getClass().getDeclaredMethod(meta.getMethodName(), importable.getClass());
                method.invoke(daoObj, importable);
            }
            catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    private void processImportableData (Map map,
                                        Importable importable,
                                        ImportObjectMeta meta)
    {
        try {
            List<ImportObjectMeta.ImportObjectSheet>  sheets = meta.getSheets();
            for (ImportObjectMeta.ImportObjectSheet sheet: sheets) {
                processFields((List) map.get(sheet.getSheetName()), importable, sheet);
            }
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    private void processFields (List data,
                                Importable importable,
                                ImportObjectMeta.ImportObjectSheet sheet)
            throws InvocationTargetException, IllegalAccessException, InstantiationException
    {
        List<String> fields = sheet.getFieldPaths();
        for (int i = 0; i < data.size(); i++) {
            List row = (List) data.get(i);
            for (int j = 0; j < fields.size(); j++) {
                Cell cell = (Cell) row.get(j+1); // to avoid the look up id field
                String fieldName = fields.get(j);
                if (fieldName.indexOf("Date") >= 0) {
                    Util.setDottedFieldValue(fields.get(j), importable, cell.getDateCellValue());
                }
                else if (fieldName.indexOf("amount") >= 0) {
                    Util.setDottedFieldValue(fields.get(j), importable, new Long((long) cell.getNumericCellValue()));
                }
                else if (fieldName.indexOf("review") >= 0 ||
                        fieldName.indexOf("vip") >= 0 ||
                        fieldName.indexOf("application") >= 0 ||
                        fieldName.indexOf("certificates") >= 0 ||
                        fieldName.indexOf("pdcNotClear") >= 0) {
                    cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
                    Util.setDottedFieldValue(fields.get(j), importable, cell.getBooleanCellValue());
                }
                else if (fieldName.indexOf("historyRecord") >= 0) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String comment = cell.getStringCellValue();
                    if (! Util.nullOrEmptyOrBlank(comment)) {
                        HistoryRecord record = new HistoryRecord();
                        record.setComment(comment);
                        Util.setDottedFieldValue(fields.get(j), importable, record);
                    }
                }
                else {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    Util.setDottedFieldValue(fields.get(j), importable, cell.getStringCellValue());
                }
            }

            if (!Util.nullOrEmptyOrBlank(sheet.getFieldPathGetter())) {
                Object obj = Util.getDottedFieldValue(sheet.getFieldPathGetter(), importable);
                Util.setDottedFieldValue(sheet.getFieldPathSetter(), importable, obj, false);
                Util.setDottedFieldValue(sheet.getFieldPathGetter(), importable, null, false);
            }
        }
    }

    private Map mergeMaps (Map mapsToMerge)
    {
        boolean availableMapsToMerge = mapsToMerge.size() > 1;
        Map mergedMap = new LinkedHashMap();
        Map firstMap = (Map) mapsToMerge.get(0);

        Map secondMap = null;
        if (availableMapsToMerge) {
            secondMap = (Map) mapsToMerge.get(1);
        }

        Set firstMapKeys = firstMap.keySet();
        Iterator firstMapItr = firstMapKeys.iterator();
        while (firstMapItr.hasNext()) {
            Integer key = (Integer) firstMapItr.next();
            ArrayList list = new ArrayList();
            list.add(firstMap.get(key));
            if (availableMapsToMerge) {
                list.add(secondMap.get(key));
            }
            mergedMap.put(key, list);
        }
        return mergedMap;
    }


    private Map processSheet (Sheet registrationsSheet, String keyStr)
    {
        Iterator regisSheetRowItr = registrationsSheet.rowIterator();
        boolean first = true;
        Map dataMap = new LinkedHashMap();
        while (regisSheetRowItr.hasNext()) {
            Row row = (Row) regisSheetRowItr.next();
            Iterator cellIter = row.cellIterator();
            ArrayList singleRow = new ArrayList();
            for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);
                singleRow.add(cell);
            }
            Map data = new LinkedHashMap();
            Cell cell = (Cell) singleRow.get(0);

            if (!first) {
                Integer key = new Integer((int) cell.getNumericCellValue());
                if (key.intValue() > 0) {
                    if (dataMap.containsKey(key)) {
                        LinkedHashMap tempMap = (LinkedHashMap) dataMap.get(key);
                        ArrayList listRows = (ArrayList) tempMap.get(keyStr);
                        listRows.add(singleRow);
                        data.put(keyStr, listRows);
                    }
                    else {
                        ArrayList lst = new ArrayList();
                        lst.add(singleRow);
                        data.put(keyStr, lst);
                    }
                    dataMap.put(key, data);
                }
            }
            first = false;
        }
        return dataMap;
    }

}
