/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yvphk.common.ParticipantLevel;
import com.yvphk.common.Util;
import com.yvphk.model.form.Event;
import com.yvphk.model.form.EventFee;
import com.yvphk.model.form.Login;
import com.yvphk.model.form.validator.EventValidator;
import com.yvphk.service.EventService;

@Controller
public class EventController extends CommonController
{
    @Autowired
    private EventService eventService;

    @RequestMapping("/event")
    public String listEvent (Map<String, Object> map)
    {
        map.put("event", new Event());
        map.put("eventList", eventService.allEvents());
        map.put("allParticipantLevels", ParticipantLevel.allParticipantLevels());
        return "event";
    }

    @RequestMapping(value = "/addEvent", method = RequestMethod.POST)
    public String addEvent (@ModelAttribute("event") Event event, BindingResult errors,
                            HttpServletRequest request)
    {
    	EventValidator val = new EventValidator();
    	val.validate(event, errors);
    	if(errors.hasErrors()){
    		return "event";
    	}
    	Login login = (Login) request.getSession().getAttribute(Login.ClassName);
        event.initialize(login.getEmail());
        eventService.addEvent(event);
        return "redirect:/event.htm";
    }

    @RequestMapping("/eventFee")
    public String listEventFee (Map<String, Object> map, HttpServletRequest request)
    {
        String strEventId = request.getParameter("eventId");
        if (Util.nullOrEmptyOrBlank(strEventId)) {
            return null;
        }
        Integer eventId = Integer.parseInt(strEventId);
        EventFee fee = new EventFee();
        fee.setEventId(eventId);

        map.put("eventFee", fee);
        map.put("eventFeeList", eventService.getEventFees(eventId));
        map.put("event", eventService.getEvent(eventId));
        map.put("allParticipantLevels", ParticipantLevel.allParticipantLevels());
        return "eventFee";
    }

    @RequestMapping(value = "/addEventFee", method = RequestMethod.POST)
    public String addEventFee (@ModelAttribute("event") EventFee eventFee,
                               HttpServletRequest request)
    {
        Login login = (Login) request.getSession().getAttribute(Login.ClassName);
        eventFee.initialize(login.getEmail());
        eventService.addEventFee(eventFee, eventFee.getEventId());
        request.setAttribute("eventId", eventFee.getEventId());
        return "forward:/eventFee.htm";
    }

    @RequestMapping("/deleteEvent")
    public String removeEvent (HttpServletRequest request)
    {
        String strEventId = request.getParameter("eventId");
        if (!Util.nullOrEmptyOrBlank(strEventId)) {
            Integer eventId = Integer.parseInt(strEventId);
            eventService.removeEvent(eventId);
        }
        return "redirect:/event.htm";
    }

    @RequestMapping("/deleteEventFee")
    public String removeEventFee (HttpServletRequest request)
    {
        String strEventFeeId = request.getParameter("eventFeeId");
        String strEventId = request.getParameter("eventId");
        if (!Util.nullOrEmptyOrBlank(strEventFeeId)) {
            Integer eventFeeId = Integer.parseInt(strEventFeeId);
            eventService.removeEventFee(eventFeeId);
        }
        request.setAttribute("eventId", strEventId);
        return "forward:/eventFee.htm";
    }

}
