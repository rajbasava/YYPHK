/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/
package com.yvphk.common;

import java.util.ArrayList;
import java.util.List;

public class Report
{
    public static Report Registrations =
            new Report("Registrations", "com.yvphk.model.form.EventRegistration");

    public static Report ConsolidatedRegistrations =
            new Report("ConsolidatedRegistrations", "com.yvphk.model.form.EventRegistration");

    public static Report Payments =
            new Report("Payments", "com.yvphk.model.form.EventPayment");

    private String reportName;
    private String classType;
    private List<ReportSheet> sheets = new ArrayList<ReportSheet>();

    private Report (String reportName,
                    String classType)
    {
        this.reportName = reportName;
        this.classType = classType;
        this.initialize();
    }

    private void initialize()
    {
        if ("Registrations".equals(this.getReportName())) {
            ReportSheet registrationSheet = getRegistrationSheet();
            this.addSheet(registrationSheet);
        }
        else if ("ConsolidatedRegistrations".equals(this.getReportName())) {
            ReportSheet registrationSheet = getRegistrationSheet();
            this.addSheet(registrationSheet);

            List<String> registrationPMTFieldPaths = new ArrayList<String>();
            registrationPMTFieldPaths.add("registration.id");
            registrationPMTFieldPaths.add("amountPaid");
            registrationPMTFieldPaths.add("mode");
            registrationPMTFieldPaths.add("receiptInfo");
            registrationPMTFieldPaths.add("receiptDate");
            registrationPMTFieldPaths.add("pdcNotClear");
            registrationPMTFieldPaths.add("pdc");
            registrationPMTFieldPaths.add("pdcDate");
            registrationPMTFieldPaths.add("timeCreated");
            ReportSheet registrationPMTSheet =
                    new ReportSheet("Payments", registrationPMTFieldPaths,
                            "payments", "com.yvphk.model.form.EventPayment");
            this.addSheet(registrationPMTSheet);
        }
        else if ("Payments".equals(this.getReportName())) {
            List<String> paymentFieldPaths = new ArrayList<String>();
            paymentFieldPaths.add("registration.id");
            paymentFieldPaths.add("registration.participant.name");
            paymentFieldPaths.add("registration.participant.email");
            paymentFieldPaths.add("registration.participant.mobile");
            paymentFieldPaths.add("registration.event.name");
            paymentFieldPaths.add("registration.amountPayable");
            paymentFieldPaths.add("amountPaid");
            paymentFieldPaths.add("mode");
            paymentFieldPaths.add("receiptInfo");
            paymentFieldPaths.add("receiptDate");
            paymentFieldPaths.add("pdcNotClear");
            paymentFieldPaths.add("pdc");
            paymentFieldPaths.add("pdcDate");
            paymentFieldPaths.add("timeCreated");
            ReportSheet paymentSheet =
                    new ReportSheet("Payments", paymentFieldPaths);
            this.addSheet(paymentSheet);
        }
    }

    private ReportSheet getRegistrationSheet ()
    {
        List<String> registrationFieldPaths = new ArrayList<String>();
        registrationFieldPaths.add("id");
        registrationFieldPaths.add("participant.name");
        registrationFieldPaths.add("participant.email");
        registrationFieldPaths.add("participant.mobile");
        registrationFieldPaths.add("participant.home");
        registrationFieldPaths.add("participant.foundation");
        registrationFieldPaths.add("participant.vip");
        registrationFieldPaths.add("participant.vipDesc");
        registrationFieldPaths.add("event.name");
        registrationFieldPaths.add("amountPayable");
        registrationFieldPaths.add("totalAmountPaid");
        registrationFieldPaths.add("amountDue");
        registrationFieldPaths.add("pendingPdc");
        registrationFieldPaths.add("review");
        registrationFieldPaths.add("level");
        registrationFieldPaths.add("reference");
        registrationFieldPaths.add("application");
        registrationFieldPaths.add("certificates");
        registrationFieldPaths.add("foodCoupon");
        registrationFieldPaths.add("eventKit");
        registrationFieldPaths.add("registrationDate");
        registrationFieldPaths.add("status");
        registrationFieldPaths.add("Category");
        return new ReportSheet("Registrations", registrationFieldPaths);
    }


    public String getReportName ()
    {
        return reportName;
    }

    public void setReportName (String reportName)
    {
        this.reportName = reportName;
    }

    public String getClassType ()
    {
        return classType;
    }

    public void setClassType (String classType)
    {
        this.classType = classType;
    }

    public List<ReportSheet> getSheets ()
    {
        return sheets;
    }

    public void addSheet (ReportSheet sheet)
    {
        getSheets().add(sheet);
    }

    public class ReportSheet
    {
        private String sheetName;
        private List<String> fieldPaths;
        private String baseFieldPath;
        private String classType;


        private ReportSheet (String sheetName,
                             List<String> fieldPaths)
        {
            this.sheetName = sheetName;
            this.fieldPaths = fieldPaths;
        }

        private ReportSheet (String sheetName,
                             List<String> fieldPaths,
                             String baseFieldPath,
                             String classType)
        {
            this.sheetName = sheetName;
            this.fieldPaths = fieldPaths;
            this.baseFieldPath = baseFieldPath;
            this.classType = classType;
        }

        public String getSheetName ()
        {
            return sheetName;
        }

        public void setSheetName (String sheetName)
        {
            this.sheetName = sheetName;
        }

        public List<String> getFieldPaths ()
        {
            return fieldPaths;
        }

        public void setFieldPaths (List<String> fieldPaths)
        {
            this.fieldPaths = fieldPaths;
        }

        public String getBaseFieldPath ()
        {
            return baseFieldPath;
        }

        public void setBaseFieldPath (String baseFieldPath)
        {
            this.baseFieldPath = baseFieldPath;
        }

        public String getClassType ()
        {
            return classType;
        }

        public void setClassType (String classType)
        {
            this.classType = classType;
        }
    }
}
