/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.web.controller;

import com.yvphk.common.AmountPaidCategory;
import com.yvphk.common.ParticipantLevel;
import com.yvphk.common.PaymentMode;
import com.yvphk.model.form.Event;
import com.yvphk.model.form.RegistrationCriteria;
import com.yvphk.model.form.PaymentCriteria;
import com.yvphk.service.DownloadService;
import com.yvphk.service.EventService;
import com.yvphk.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class DownloadController extends CommonController
{
    @Autowired
    private DownloadService downloadService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private EventService eventService;

    @RequestMapping(value = "/exportRegistrations")
    public void exportRegistrations (HttpServletResponse response,
                                     Map<String, Object> map,
                                     RegistrationCriteria registrationCriteria)
    {
        downloadService.downloadRegistrationsReport(response, registrationCriteria);
    }

    @RequestMapping(value = "/exportConsolRegistrations")
    public void exportConsolRegistrations (HttpServletResponse response,
                                           Map<String, Object> map,
                                           RegistrationCriteria registrationCriteria)
    {
        registrationCriteria.setConsolidated(true);
        downloadService.downloadRegistrationsReport(response, registrationCriteria);
    }

    @RequestMapping(value = "/exportRegistrationsForImp")
    public void exportRegistrationsForImp (HttpServletResponse response,
                                           Map<String, Object> map,
                                           RegistrationCriteria registrationCriteria)
    {
        downloadService.downloadRegistrationsReportForImport(response, registrationCriteria);
    }

    @RequestMapping(value = "/rptRegistrations")
    public String rptRegistrations (Map<String, Object> map)
    {
        map.put("registrationCriteria", new RegistrationCriteria());
        map.put("allParticipantLevels", ParticipantLevel.allParticipantLevels());
        map.put("allFoundations", allFoundations());
        map.put("allReferenceGroups", getAllReferenceGroups(participantService.listReferenceGroups()));
        map.put("allAmountPaidCategories", AmountPaidCategory.allAmountPaidCategories());
        map.put("allEvents", getAllEventMap(eventService.allEvents()));
        map.put("allStatuses", getRegistrationStatusMap());
        return "rptRegistrations";
    }

    @RequestMapping(value = "/exportPayments")
    public void getXLS (HttpServletResponse response,
                        Map<String, Object> map,
                        PaymentCriteria paymentCriteria)
    {
        downloadService.downloadPaymentsReport(response, paymentCriteria);
    }

    @RequestMapping(value = "/exportSeats")
    public void exportSeats (HttpServletResponse response,
                             HttpServletRequest request)
    {
        String strEventId = request.getParameter("eventId");
        Integer eventId = Integer.parseInt(strEventId);
        Event event = eventService.getEvent(eventId);

        downloadService.downloadSeats(response, event);
    }

    @RequestMapping(value = "/rptPayments")
    public String rptPayments (Map<String, Object> map)
    {
        map.put("paymentCriteria", new PaymentCriteria());
        map.put("allParticipantLevels", ParticipantLevel.allParticipantLevels());
        map.put("allFoundations", allFoundations());
        map.put("allReferenceGroups", getAllReferenceGroups(participantService.listReferenceGroups()));
        map.put("allEvents", getAllEventMap(eventService.allEvents()));
        map.put("allPaymentModes", PaymentMode.allPaymentModes());
        return "rptPayments";
    }
}
