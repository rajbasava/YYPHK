/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.service;

import com.yvphk.common.ExcelReport;
import com.yvphk.common.Util;
import com.yvphk.model.form.EventRegistration;
import com.yvphk.model.form.ParticipantCriteria;
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
    public void downloadXLS (HttpServletResponse response, ParticipantCriteria participantCriteria)
    {
        List<EventRegistration> registrations = participantService.listRegistrations(participantCriteria);

        Workbook workbook = ExcelReport.generateReport(registrations);

        // Set the response properties
        String fileName = "EventRegistrationsExport.xls";
        response.setHeader("Content-Disposition", "inline; filename=" + fileName);
        // Make sure to set the correct content type
        response.setContentType("application/vnd.ms-excel");
        //7. Write to the output stream
        Util.write(response, workbook);
    }
}
