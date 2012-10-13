/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
 
    Responsible: byummadisingh
*/
package com.yvphk.web.controller;

import com.yvphk.common.ParticipantLevel;
import com.yvphk.common.Util;
import com.yvphk.model.form.Event;
import com.yvphk.model.form.EventFee;
import com.yvphk.model.form.EventRegistration;
import com.yvphk.model.form.HistoryRecord;
import com.yvphk.model.form.Login;
import com.yvphk.model.form.Option;
import com.yvphk.model.form.Participant;
import com.yvphk.model.form.ParticipantCriteria;
import com.yvphk.model.form.ParticipantSeat;
import com.yvphk.model.form.RegisteredParticipant;
import com.yvphk.service.EventService;
import com.yvphk.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletRequest;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ParticipantController
{
    @Autowired
    private ParticipantService participantService;

    @Autowired
    private EventService eventService;

    @RequestMapping("/register")
    public String newParticipant (Map<String, Object> map)
    {
        RegisteredParticipant registeredParticipant = new RegisteredParticipant();
        registeredParticipant.setAction(RegisteredParticipant.ActionRegister);
        map.put("registeredParticipant", registeredParticipant);
        map.put("allParticipantLevels", ParticipantLevel.allParticipantLevels());
        map.put("allEvents", getAllEvents());
        return "register";
    }

    @RequestMapping("/search")
    public String search (Map<String, Object> map)
    {
        map.put("participantCriteria", new ParticipantCriteria());
        map.put("allParticipantLevels", ParticipantLevel.allParticipantLevels());
        return "search";
    }

    @RequestMapping("/list")
    public String searchParticipant (Map<String, Object> map,
                                     ParticipantCriteria participantCriteria)
    {
        map.put("participantCriteria", participantCriteria);
        if (participantCriteria != null) {
            map.put("registrationList", participantService.listRegistrations(participantCriteria));
            map.put("allParticipantLevels", ParticipantLevel.allParticipantLevels());
        }
        return "search";
    }

    @RequestMapping(value = "/addRegistration", method = RequestMethod.POST)
    public String addRegistration (RegisteredParticipant registeredParticipant,
                                  Map<String, Object> map,
                                  HttpServletRequest request)
    {
        Login login = (Login) request.getSession().getAttribute(Login.ClassName);
        String action = registeredParticipant.getAction();

        registeredParticipant.getRegistration().setEvent(
                eventService.getEvent(registeredParticipant.getEventId()));

        if (RegisteredParticipant.ActionRegister.equals(action)) {
            registeredParticipant.initialize(login.getEmail());
        }

        if (RegisteredParticipant.ActionUpdate.equals(action)) {
            registeredParticipant.initializeForUpdate(login.getEmail());
        }

        EventRegistration registration = participantService.registerParticipant(registeredParticipant);

        if (RegisteredParticipant.ActionUpdate.equals(action)) {
            return "redirect:/search.htm";
        }

        registeredParticipant = populateRegisteredParticipant(String.valueOf(registration.getId()));
        map.put("registeredParticipant", registeredParticipant);
        return "summary";
    }

    @RequestMapping("/updateRegistration")
    public String updateParticipant (HttpServletRequest request, Map<String, Object> map)
    {
        String strRegistrationId = request.getParameter("registrationId");
        RegisteredParticipant registeredParticipant = populateRegisteredParticipant(strRegistrationId);
        if (registeredParticipant != null) {
            map.put("registeredParticipant", registeredParticipant);
            map.put("allParticipantLevels", ParticipantLevel.allParticipantLevels());
            map.put("allEvents", getAllEvents());
            map.put("allEventFees", getAllEventFees(registeredParticipant.getEventId()));
            return "registerTab";
        }
        return "null";
    }

    @RequestMapping(value = "/getAllEventFees", produces = "application/json; charset=utf-8")
    public @ResponseBody List<Option> eventFees (HttpServletRequest request)
    {
        String strEventId = request.getParameter("eventId");
        if (!Util.nullOrEmptyOrBlank(strEventId)) {
            return getAllEventFees(Integer.parseInt(strEventId));
        }
        return null;
    }

    @RequestMapping(value = "/fetchEventFee", produces = "application/json; charset=utf-8")
    public @ResponseBody Option fetchEventFee (HttpServletRequest request)
    {
        String strEventFeeId = request.getParameter("eventFeeId");
        if (!Util.nullOrEmptyOrBlank(strEventFeeId)) {
            EventFee fee = eventService.getEventFee(Integer.parseInt(strEventFeeId));
            return new Option(fee.getId(), String.valueOf(fee.getAmount()));
        }
        return null;
    }

    private RegisteredParticipant populateRegisteredParticipant (String strRegistrationId)
    {
        if (!Util.nullOrEmptyOrBlank(strRegistrationId)) {
            Integer registrationId = Integer.parseInt(strRegistrationId);
            EventRegistration registration = participantService.getEventRegistration(registrationId);
            RegisteredParticipant registeredParticipant = new RegisteredParticipant();
            registeredParticipant.setRegistration(registration);
            registeredParticipant.setParticipant(registration.getParticipant());
            registeredParticipant.setAllHistoryRecords(registration.getHistoryRecords());
            registeredParticipant.setEventId(registration.getEvent().getId());
            registeredParticipant.setAction(RegisteredParticipant.ActionUpdate);
            return registeredParticipant;
        }

        return null;
    }

    @RequestMapping(value = "/batchEntry", method = RequestMethod.GET)
    public String batchEntry ()
    {
        return "batchEntry";
    }

    @RequestMapping(value = "/batchEntry", method = RequestMethod.POST)
    public String processBatchEntry (HttpServletRequest request, Map<String, Object> map)
    {
        Login login = (Login) request.getSession().getAttribute(Login.ClassName);
        String preparedBy = login.getEmail();

        List<RegisteredParticipant> participantList = new ArrayList<RegisteredParticipant>();

        String parentNode = "/ParticipantsData";
        final String LAST_ELEMENT_IN_RECORD = "Level";

        Method method = null;
        String nodeName = "", nodeValue = "";

        XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            NodeList nodes = (NodeList) xPath.evaluate(
                    parentNode,
                    new InputSource(new StringReader(request.getParameter("content"))),
                    XPathConstants.NODESET
            );
            for (int i = 0; i < nodes.getLength(); i++) {
                Element iNodes = (Element) nodes.item(i);
                NodeList node = (NodeList) xPath.evaluate("Participant/*", iNodes, XPathConstants.NODESET);
                Participant participant = Participant.class.newInstance();
                RegisteredParticipant registeredParticipant = new RegisteredParticipant();
                registeredParticipant.setAction(RegisteredParticipant.ActionRegister);
                for (int k = 0; k < node.getLength(); k++) {
                    Element element = (Element) node.item(k);
                    nodeName = element.getNodeName();
                    nodeValue = element.getFirstChild().getNodeValue();
                    if (nodeName.equalsIgnoreCase("Amount") ||
                            nodeName.equalsIgnoreCase("AmountPaid") ||
                            nodeName.equalsIgnoreCase("DueAmount")) {
                        method = Participant.class.getDeclaredMethod("set" + nodeName, Integer.class);
                        method.invoke(participant, Integer.parseInt(nodeValue));
                    }
                    else if (nodeName.equalsIgnoreCase("Comments")) {
                        HistoryRecord historyRecord = new HistoryRecord();
                        historyRecord.setComment(nodeValue);
                        historyRecord.setPreparedBy(preparedBy);
                        registeredParticipant.setCurrentHistoryRecord(historyRecord);
                    }
                    else if (nodeName.equalsIgnoreCase("Seat")) {
                        ParticipantSeat seat = new ParticipantSeat();
                        seat.setSeat(Integer.parseInt(nodeValue));
                        registeredParticipant.setCurrentSeat(seat);
                    }
                    else if (nodeName.equalsIgnoreCase("Foodcoupon") ||
                            nodeName.equalsIgnoreCase("Eventkit")) {
                        method = Participant.class.getDeclaredMethod("set" + nodeName, boolean.class);
                        method.invoke(participant, Boolean.parseBoolean(nodeValue));
                    }
                    else {
                        method = Participant.class.getDeclaredMethod("set" + nodeName, String.class);
                        method.invoke(participant, nodeValue);
                    }

                    if (nodeName.equalsIgnoreCase(LAST_ELEMENT_IN_RECORD)) {
                        participant.setPreparedBy(preparedBy);
                        registeredParticipant.setParticipant(participant);
                        participantList.add(registeredParticipant);
                        // creating new objects for next iteration.
                        participant = Participant.class.newInstance();
                        registeredParticipant = RegisteredParticipant.class.newInstance();
                        registeredParticipant.setAction(RegisteredParticipant.ActionRegister);
                    }
                }
            }
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (XPathExpressionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        participantService.processBatchEntry(participantList);
        map.put("batchResults", "Batch upload successful. Imported " + participantList.size() + " records.");
        return "batchResults";
    }

    private Map<String, String> getAllEvents ()
    {
        List<Event> events = eventService.allEvents();
        LinkedHashMap eventMap = new LinkedHashMap<String, String>();
        for (Event event : events) {
            eventMap.put(event.getId(), event.getName());
        }
        return eventMap;
    }

    private List<com.yvphk.model.form.Option> getAllEventFees (Integer eventId)
    {
        List<EventFee> eventFeeList = eventService.getEventFees(eventId);
        ArrayList<com.yvphk.model.form.Option> options = new ArrayList<com.yvphk.model.form.Option>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        for (EventFee eventFee : eventFeeList) {
            String cutOffDateStr = formatter.format(eventFee.getCutOffDate());
            StringBuffer buffer = new StringBuffer();
            buffer.append(eventFee.getName());
            buffer.append(" - ");
            buffer.append(cutOffDateStr);
            buffer.append(" - ");
            buffer.append(eventFee.getAmount());
            if (eventFee.isReview()) {
                buffer.append(" - ");
                buffer.append("Review");
            }
            Option option = new Option(eventFee.getId(), buffer.toString());
            options.add(option);
        }
        return options;
    }

    @RequestMapping(value = "/registerTab", method = RequestMethod.GET)
    public String register ()
    {
        return "registerTab";
    }


}