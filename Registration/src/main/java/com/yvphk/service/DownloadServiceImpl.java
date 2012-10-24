/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.service;

import com.yvphk.common.ExcelReport;
import com.yvphk.common.Report;
import com.yvphk.common.Util;
import com.yvphk.model.form.EventPayment;
import com.yvphk.model.form.EventRegistration;
import com.yvphk.model.form.ParticipantCriteria;
import com.yvphk.model.form.PaymentCriteria;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class DownloadServiceImpl implements DownloadService
{
    @Autowired
    private ParticipantService participantService;

    @Transactional
    public void downloadRegistrationsReport (HttpServletResponse response, ParticipantCriteria participantCriteria)
    {
        List<EventRegistration> registrations = participantService.listRegistrations(participantCriteria);

        Workbook workbook = null;
        String fileName = "";
        if (participantCriteria.isConsolidated()) {
            workbook = ExcelReport.generateReport(registrations, Report.ConsolidatedRegistrations);
            fileName = Report.ConsolidatedRegistrations.getReportName()+"Export.xls";
        }
        else {
            workbook = ExcelReport.generateReport(registrations, Report.Registrations);
            fileName = Report.Registrations.getReportName()+"Export.xls";
        }

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
        String fileName = Report.Payments.getReportName()+"Export.xls";

        response.setHeader("Content-Disposition", "inline; filename=" + fileName);
        // Make sure to set the correct content type
        response.setContentType("application/vnd.ms-excel");
        //7. Write to the output stream
        Util.write(response, workbook);
    }
}
