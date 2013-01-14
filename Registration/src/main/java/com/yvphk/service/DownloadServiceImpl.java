/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.service;

import com.yvphk.common.ExcelReport;
import com.yvphk.common.Report;
import com.yvphk.common.Util;
import com.yvphk.model.form.Event;
import com.yvphk.model.form.EventPayment;
import com.yvphk.model.form.EventRegistration;
import com.yvphk.model.form.ParticipantSeat;
import com.yvphk.model.form.RegistrationCriteria;
import com.yvphk.model.form.PaymentCriteria;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DownloadServiceImpl implements DownloadService
{
    @Autowired
    private ParticipantService participantService;

    @Transactional
    public void downloadRegistrationsReport (HttpServletResponse response, RegistrationCriteria registrationCriteria)
    {
        List<EventRegistration> registrations = participantService.listRegistrations(registrationCriteria);

        Workbook workbook = null;
        String fileName = "";
        if (registrationCriteria.isConsolidated()) {
            workbook = ExcelReport.generateReport(registrations, Report.ConsolidatedRegistrations);
            fileName = generateFileName(Report.ConsolidatedRegistrations.getReportName());
        }
        else {
            workbook = ExcelReport.generateReport(registrations, Report.Registrations);
            fileName = generateFileName(Report.Registrations.getReportName());
        }

        response.setHeader("Content-Disposition", "inline; filename=" + fileName);
        // Make sure to set the correct content type
        response.setContentType("application/vnd.ms-excel");
        //7. Write to the output stream
        Util.write(response, workbook);
    }

    public void downloadRegistrationsReportForImport (HttpServletResponse response,
                                                      RegistrationCriteria registrationCriteria)
    {
        registrationCriteria.setConsolidated(true);
        List<EventRegistration> registrations = participantService.listRegistrations(registrationCriteria);

        Workbook workbook = ExcelReport.generateReport(registrations, Report.RegistrationsForImport);
        String fileName = generateFileName(Report.RegistrationsForImport.getReportName());

        response.setHeader("Content-Disposition", "inline; filename=" + fileName);
        // Make sure to set the correct content type
        response.setContentType("application/vnd.ms-excel");
        //7. Write to the output stream
        Util.write(response, workbook);


    }

    public void downloadPaymentsReport (HttpServletResponse response, PaymentCriteria criteria)
    {
        List<EventPayment> registrations = participantService.listPayments(criteria);

        Workbook workbook = ExcelReport.generateReport(registrations, Report.Payments);
        String fileName = generateFileName(Report.Payments.getReportName());

        response.setHeader("Content-Disposition", "inline; filename=" + fileName);
        // Make sure to set the correct content type
        response.setContentType("application/vnd.ms-excel");
        //7. Write to the output stream
        Util.write(response, workbook);
    }

    public void downloadSeats (HttpServletResponse response, Event event)
    {
        List<ParticipantSeat> seats= participantService.getAllSeats(event);

        Workbook workbook = ExcelReport.generateReport(seats, Report.Seats);
        String fileName = generateFileName(Report.Seats.getReportName());

        response.setHeader("Content-Disposition", "inline; filename=" + fileName);
        // Make sure to set the correct content type
        response.setContentType("application/vnd.ms-excel");
        //7. Write to the output stream
        Util.write(response, workbook);
    }

    private String generateFileName (String reportName)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String strDate = dateFormat.format(new Date());
        StringBuffer buffer = new StringBuffer();
        buffer.append(reportName);
        buffer.append("_");
        buffer.append(strDate);
        buffer.append("_");
        buffer.append("Export.xls");
        return buffer.toString();
    }
}
