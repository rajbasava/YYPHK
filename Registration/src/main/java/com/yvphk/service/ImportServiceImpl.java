/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.service;

import com.yvphk.common.Util;
import com.yvphk.model.dao.EventDAO;
import com.yvphk.model.dao.ParticipantDAO;
import com.yvphk.model.form.Event;
import com.yvphk.model.form.EventPayment;
import com.yvphk.model.form.EventRegistration;
import com.yvphk.model.form.HistoryRecord;
import com.yvphk.model.form.ImportFile;
import com.yvphk.model.form.Login;
import com.yvphk.model.form.Participant;
import com.yvphk.model.form.RegisteredParticipant;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ImportServiceImpl implements ImportService
{
    public static final String ContentTypeXLSX= "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String ContentTypeXLS= "application/vnd.ms-excel";
    public static final String Registrations= "Registrations";
    public static final String Payments= "Payments";

    private static final String[] ParticipantImportFields =
            new String[] {"Name","Email","Mobile","Home","Foundation","Vip","VipDesc"};
    private static final String[] RegistrationImportFields =
            new String[] {"AmountPayable","Review","Level","Reference","Application","Certificates","Comments"};
    private static final String[] PaymentImportFields =
            new String[] {"AmountPaid","Mode","ReceiptInfo","ReceiptDate","Comments"};

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

            Sheet registrationsSheet = workBook.getSheet(Registrations);
            Sheet paymentsSheet = workBook.getSheet(Payments);

            if (registrationsSheet == null || paymentsSheet == null) {
                return;
            }

            Map registrationsMap =  processSheet(registrationsSheet, Registrations);
            Map paymentsMap =  processSheet(paymentsSheet, Payments);

            Map mergedMap = mergeMaps(registrationsMap, paymentsMap);

            processRegistrations(mergedMap, login, importFile.getEventId());

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
        Map Structure:
        Key - Map
     */
    private Integer processRegistrations (Map mergedMap, Login login, Integer eventId)
    {
        Event event = eventDAO.getEvent(eventId);
        Set keys = mergedMap.keySet();
        Iterator itr = keys.iterator();
        while (itr.hasNext()) {
            Integer key = (Integer) itr.next();
            ArrayList list = (ArrayList) mergedMap.get(key);
            List regData = (List)((Map)list.get(0)).get(Registrations);
            List paymentData = (List)((Map)list.get(1)).get(Payments);
            RegisteredParticipant registeredParticipant = new RegisteredParticipant();
            processEventRegistration(regData, paymentData, registeredParticipant);
            registeredParticipant.initialize(login.getEmail());
            registeredParticipant.setAction(RegisteredParticipant.ActionRegister);
            registeredParticipant.getRegistration().setEvent(event);
            participantDAO.registerParticipant(registeredParticipant);
        }
        return null;
    }


    private void processEventRegistration (List regData, List paymentData,
                                           RegisteredParticipant registeredParticipant)
    {
        int count = 1;

        try {
            processFields(regData, registeredParticipant, "Participant",
                    Participant.class, ParticipantImportFields, count);

            processFields(regData, registeredParticipant, "Registration",
                    EventRegistration.class, RegistrationImportFields,
                    count + ParticipantImportFields.length);

            processFields(paymentData, registeredParticipant, "Payment",
                    EventPayment.class, PaymentImportFields, count);

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
                                RegisteredParticipant registeredParticipant,
                                String methodName,
                                Class clazz,
                                String[] fields,
                                int count)
            throws InvocationTargetException, IllegalAccessException, InstantiationException
    {
        for (int i=0; i < data.size(); i++ ) {
            List row  = (List) data.get(i);
            Object obj = clazz.newInstance();
            for (int j=0; j<fields.length; j++) {
                Cell cell = (Cell) row.get(count+j);
                String fieldName = fields[j];
                if (fieldName.indexOf("Date") >= 0) {
                    Method method = Util.getDeclaredSetter(clazz, fieldName, Date.class);
                    method.invoke(obj, cell.getDateCellValue());
                }
                else if (fieldName.indexOf("Amount") >= 0) {
                    Method method = Util.getDeclaredSetter(clazz, fieldName, Long.class);
                    method.invoke(obj, new Long((long)cell.getNumericCellValue()));
                }
                else if (fieldName.equals("Review")||
                        fieldName.equals("Vip") ||
                        fieldName.equals("Application") ||
                        fieldName.equals("Certificates")) {
                    Method method = Util.getDeclaredSetter(clazz, fieldName, boolean.class);
                    cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
                    method.invoke(obj, cell.getBooleanCellValue());
                }
                else if (fieldName.equals("Comments")) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String comment = cell.getStringCellValue();
                    if (!Util.nullOrEmptyOrBlank(comment)) {
                        HistoryRecord record = new HistoryRecord();
                        record.setComment(comment);
                        registeredParticipant.setHistoryRecord(record);
                    }
                }
                else {
                    Method method = Util.getDeclaredSetter(clazz, fieldName, String.class);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    method.invoke(obj, cell.getStringCellValue());
                }
            }
            Method method = Util.getDeclaredSetter(RegisteredParticipant.class, methodName, clazz);
            method.invoke(registeredParticipant, obj);
        }
    }

    private Map mergeMaps (Map registrationsMap, Map paymentsMap)
    {
        Map mergedMap = new LinkedHashMap();
        Set registrationKeys = registrationsMap.keySet();
        Iterator registrationItr = registrationKeys.iterator();
        while (registrationItr.hasNext()) {
            Integer key = (Integer) registrationItr.next();
            ArrayList list = new ArrayList();
            list.add(registrationsMap.get(key));
            list.add(paymentsMap.get(key));
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
            while (cellIter.hasNext()) {
                Cell cell = (Cell) cellIter.next();
                singleRow.add(cell);
            }
            Map data = new LinkedHashMap();
            Cell cell = (Cell) singleRow.get(0);


            if (!first) {
                Integer key = new Integer((int) cell.getNumericCellValue());
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
            first = false;
        }
        return dataMap;
    }

}
