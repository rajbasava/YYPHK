/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/
package com.yvphk.common;

import com.yvphk.model.form.EventPayment;
import com.yvphk.model.form.EventRegistration;
import com.yvphk.model.form.Participant;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ExcelReport
{

    private static final String Registrations = "Registrations";
    private static final String Payments = "Payments";

    public static Workbook generateReport (List<EventRegistration> registrations)
    {
        List<String> registrationFieldNames = Arrays.asList(EventRegistration.ReportFields);
        List<String> participantFieldNames = Arrays.asList(Participant.ReportFields);
        List<String> paymentFieldNames = Arrays.asList(EventPayment.ReportFields);

        List<String> regisSheetFieldNames = new ArrayList<String>(participantFieldNames);
        regisSheetFieldNames.addAll(registrationFieldNames);
        Workbook workbook = buildReport(regisSheetFieldNames, paymentFieldNames);
        populateReport(workbook, registrations, participantFieldNames, registrationFieldNames, paymentFieldNames);
        return workbook;

    }

    private static void populateReport (Workbook workbook,
                                        List<EventRegistration> registrations,
                                        List<String> participantFieldNames,
                                        List<String> registrationFieldNames,
                                        List<String> paymentFieldNames)
    {

        Sheet registrationSheet = workbook.getSheet(Registrations);
        CellStyle registrationBodyCellStyle = registrationSheet.getWorkbook().createCellStyle();
        registrationBodyCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        registrationBodyCellStyle.setWrapText(true);

        Sheet paymentsSheet = workbook.getSheet(Payments);
        CellStyle paymentsBodyCellStyle = paymentsSheet.getWorkbook().createCellStyle();
        paymentsBodyCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        paymentsBodyCellStyle.setWrapText(true);

        DataFormat df = workbook.createDataFormat();
        CellStyle cs = workbook.createCellStyle();
        cs.setDataFormat(df.getFormat("d-MMM-yy"));

        int counter = 0;
        int pmtCount = 3;
        // Create body
        for (int i = 0; i < registrations.size(); i++) {
            counter = i + 3;
            EventRegistration registration = registrations.get(i);
            Row regRow = registrationSheet.createRow((short) counter);

            Cell regRowCell = regRow.createCell(0);
            regRowCell.setCellValue(registration.getId());
            regRowCell.setCellStyle(registrationBodyCellStyle);

            int colIndex = 1;
            for (String fieldName : participantFieldNames) {
                Cell cell1 = regRow.createCell(colIndex);
                cell1.setCellValue(String.valueOf(Util.getFieldValue(registration.getParticipant(), fieldName)));
                cell1.setCellStyle(registrationBodyCellStyle);
                if (fieldName.equals("Vip")) {
                    cell1.setCellType(Cell.CELL_TYPE_BOOLEAN);
                }
                ++colIndex;
            }

            for (String fieldName : registrationFieldNames) {
                Cell cell1 = regRow.createCell(colIndex);
                if (fieldName.equalsIgnoreCase("Event")) {
                    cell1.setCellValue(String.valueOf(registration.getEvent().getName()));
                }
                else if (fieldName.indexOf("Amount") >= 0) {
                    cell1.setCellType(Cell.CELL_TYPE_NUMERIC);
                    cell1.setCellValue(Double.valueOf(String.valueOf(Util.getFieldValue(registration, fieldName))));
                }
                else if (fieldName.equals("Review") ||
                        fieldName.equals("EventKit") ||
                        fieldName.equals("FoodCoupon") ||
                        fieldName.equals("Application") ||
                        fieldName.equals("Certificates")) {
                    cell1.setCellValue(String.valueOf(Util.getFieldValue(registration, fieldName)));
                    cell1.setCellType(Cell.CELL_TYPE_BOOLEAN);
                }
                else {
                    cell1.setCellValue(String.valueOf(Util.getFieldValue(registration, fieldName)));
                }
                cell1.setCellStyle(registrationBodyCellStyle);
                ++ colIndex;
            }

            Set<EventPayment> payments = registration.getPayments();

            for (Iterator<EventPayment> iterator = payments.iterator(); iterator.hasNext(); ) {
                EventPayment payment = iterator.next();

                Row pmtRow = paymentsSheet.createRow((short) pmtCount);

                Cell pmtRowCell = pmtRow.createCell(0);
                pmtRowCell.setCellValue(registration.getId());
                pmtRowCell.setCellStyle(registrationBodyCellStyle);

                colIndex = 1;
                for (String fieldName : paymentFieldNames) {
                    Cell cell1 = pmtRow.createCell(colIndex);
                    if (fieldName.indexOf("Amount") >= 0) {
                        cell1.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell1.setCellValue(Double.valueOf(String.valueOf(Util.getFieldValue(payment, fieldName))));
                        cell1.setCellStyle(registrationBodyCellStyle);
                    }
                    else if ((fieldName.indexOf("Date") >= 0) ||
                            (fieldName.indexOf("Time") >= 0)) {
                        Date date = (Date)Util.getFieldValue(payment, fieldName);
                        if (date != null) {
                            cell1.setCellValue(date);
                            cell1.setCellStyle(cs);
                        }
                        else {
                            cell1.setCellValue("");
                            cell1.setCellStyle(registrationBodyCellStyle);
                        }
                    }
                    else {
                        cell1.setCellValue(String.valueOf(Util.getFieldValue(payment, fieldName)));
                        cell1.setCellStyle(registrationBodyCellStyle);
                    }

                    ++ colIndex;
                }
                ++ pmtCount;
            }
        }
    }

    private static HSSFWorkbook buildReport (List<String> regisSheetFieldNames,
                                             List<String> paymentSheetFieldNames)
    {
        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet registrationsSheet = workbook.createSheet(Registrations);
        HSSFSheet paymentsSheet = workbook.createSheet(Payments);

        // Build title, date, and column headers
        buildReportSheet(registrationsSheet, 0, 0, regisSheetFieldNames);
        buildReportSheet(paymentsSheet, 0, 0, paymentSheetFieldNames);

        return workbook;
    }

    private static void buildReportSheet (HSSFSheet worksheet,
                                          int startRowIndex,
                                          int startColIndex,
                                          List<String> fieldNames)
    {
        for (int i = 0; i < fieldNames.size(); i++) {
            worksheet.setColumnWidth(i, 5000);
        }

        // Build the title and date headers
        buildTitle(worksheet, startRowIndex, startColIndex, fieldNames);
        // Build the column headers
        buildHeaders(worksheet, startRowIndex, startColIndex, fieldNames);
    }

    /**
     * Builds the report title and the date header
     *
     * @param worksheet
     * @param startRowIndex starting row offset
     * @param startColIndex starting column offset
     */
    private static void buildTitle (HSSFSheet worksheet,
                                    int startRowIndex,
                                    int startColIndex,
                                    List<String> fieldNames)
    {
        // Create font style for the report title
        Font fontTitle = worksheet.getWorkbook().createFont();
        fontTitle.setBoldweight(Font.BOLDWEIGHT_BOLD);
        fontTitle.setFontHeight((short) 280);

        // Create cell style for the report title
        HSSFCellStyle cellStyleTitle = worksheet.getWorkbook().createCellStyle();
        cellStyleTitle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyleTitle.setWrapText(true);
        cellStyleTitle.setFont(fontTitle);

        // Create report title
        HSSFRow rowTitle = worksheet.createRow((short) startRowIndex);
        rowTitle.setHeight((short) 500);
        HSSFCell cellTitle = rowTitle.createCell(startColIndex);
        cellTitle.setCellValue("Event Registration Report");
        cellTitle.setCellStyle(cellStyleTitle);

        // Create merged region for the report title
        worksheet.addMergedRegion(new CellRangeAddress(0, 0, 0, fieldNames.size()));

        // Create date header
        HSSFRow dateTitle = worksheet.createRow((short) startRowIndex + 1);
        HSSFCell cellDate = dateTitle.createCell(startColIndex);
        cellDate.setCellValue("This report was generated at " + new Date());
    }

    /**
     * Builds the column headers
     *
     * @param worksheet
     * @param startRowIndex starting row offset
     * @param startColIndex starting column offset
     */
    private static void buildHeaders (HSSFSheet worksheet,
                                      int startRowIndex,
                                      int startColIndex,
                                      List<String> fieldNames)
    {
        // Create font style for the headers
        Font font = worksheet.getWorkbook().createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // Create cell style for the headers
        HSSFCellStyle headerCellStyle = worksheet.getWorkbook().createCellStyle();
        headerCellStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headerCellStyle.setWrapText(true);
        headerCellStyle.setFont(font);
        headerCellStyle.setBorderBottom(CellStyle.BORDER_THIN);

        // Create the column headers
        Row rowHeader = worksheet.createRow((short) startRowIndex + 2);
        rowHeader.setHeight((short) 500);

        Cell cell = rowHeader.createCell(startColIndex);
        cell.setCellValue("Id");
        cell.setCellStyle(headerCellStyle);


        for (int i = 0; i < fieldNames.size(); i++) {
            Cell cell1 = rowHeader.createCell(startColIndex + i + 1);
            cell1.setCellValue(fieldNames.get(i));
            cell1.setCellStyle(headerCellStyle);
        }
    }

}
