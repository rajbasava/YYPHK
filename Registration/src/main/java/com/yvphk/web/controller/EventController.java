/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.web.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yvphk.model.form.*;
import com.yvphk.service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yvphk.common.ParticipantLevel;
import com.yvphk.common.SeatingType;
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

    @Autowired
    private VolunteerService volunteerService;

    @RequestMapping("/event")
    public String listEvent (Map<String, Object> map)
    {
        map.put("event", new Event());
        map.put("eventList", eventService.allEvents());
        map.put("allParticipantLevels", ParticipantLevel.allParticipantLevels());
        map.put("allSeatingTypes", SeatingType.allSeatingTypes());
        map.put("allRowMetaNames", eventService.getAllRowMetaNames());
        return "event";
    }

    @RequestMapping(value = "/addEvent", method = RequestMethod.POST)
    public ModelAndView addEvent (@ModelAttribute("event") Event event, BindingResult errors,
                            HttpServletRequest request)
    {
    	ModelAndView mv = null;
    	EventValidator val = new EventValidator();
    	val.validate(event, errors);
    	if(errors.hasErrors()){
    		mv = new ModelAndView("event");
    		mv.addObject("errors", errors);
    		mv.addObject("eventList", eventService.allEvents());
    		mv.addObject("allParticipantLevels", ParticipantLevel.allParticipantLevels());
    		mv.addObject("allSeatingTypes", SeatingType.allSeatingTypes());
    		mv.addObject("allRowMetaNames", eventService.getAllRowMetaNames());
    		return mv;
    	}
    	Login login = (Login) request.getSession().getAttribute(Login.ClassName);
        event.initialize(login.getEmail());
        eventService.addEvent(event);
        mv = new ModelAndView("redirect:/event.htm");
        return mv;
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

    @RequestMapping("/showEventDetailUI")
    public String showEventDetailUI (Map<String, Object> map, HttpServletRequest request)
    {
        String strEventId = request.getParameter("eventId");
        if (Util.nullOrEmptyOrBlank(strEventId)) {
            strEventId = (String) request.getAttribute("eventId");
            if (Util.nullOrEmptyOrBlank(strEventId)) {
                return null;
            }
        }
        Integer eventId = Integer.parseInt(strEventId);
        Kit kit = eventService.getEventKit(eventId);
        if (kit == null) {
            kit = new Kit();
        }
        kit.setEventId(eventId);
        map.put("eventKit", kit);
        map.put("event", eventService.getEvent(eventId));
        return "eventDetailUI";
    }

    @RequestMapping("/showKitsUI")
    public String showKitsUI (Map<String, Object> map, HttpServletRequest request)
    {
        String strEventId = request.getParameter("eventId");
        if (Util.nullOrEmptyOrBlank(strEventId)) {
            strEventId = (String) request.getAttribute("eventId");
            if (Util.nullOrEmptyOrBlank(strEventId)) {
                return null;
            }
        }
        Integer eventId = Integer.parseInt(strEventId);
        Kit kit = eventService.getEventKit(eventId);
        if (kit == null) {
            kit = new Kit();
        }
        kit.setEventId(eventId);
        List<VolunteerKit> volunteerKitList = eventService.getVolunteerKits(kit.getId());
        int unallotedKitsCount = 0;
        int allotedKitsCount = 0;
        int kitsGivenCount = 0;
        int kitsLeftCount = 0;
        for(VolunteerKit volunteerKit: volunteerKitList) {
            allotedKitsCount += volunteerKit.getKitCount();
            kitsGivenCount += volunteerKit.getKitsGiven();
            kitsLeftCount += volunteerKit.getKitCount() - volunteerKit.getKitsGiven();
        }
        unallotedKitsCount = kit.getStock() - allotedKitsCount;
        map.put("unallotedKitsCount", unallotedKitsCount);
        map.put("allotedKitsCount", allotedKitsCount);
        map.put("kitsGivenCount", kitsGivenCount);
        map.put("kitsLeftCount", kitsLeftCount);
        map.put("eventKit", kit);
        map.put("volunteerKits", volunteerKitList);
        map.put("event", eventService.getEvent(eventId));
        return "kitsUI";
    }

    @RequestMapping("/showEventKitsUI")
    public String showEventKitsUI (Map<String, Object> map, HttpServletRequest request)
    {
        String strEventId = request.getParameter("eventId");
        if (Util.nullOrEmptyOrBlank(strEventId)) {
            return null;
        }
        Integer eventId = Integer.parseInt(strEventId);
        Kit kit = eventService.getEventKit(eventId);
        if (kit == null) {
            kit = new Kit();
        }
        kit.setEventId(eventId);
        map.put("eventKit", kit);
        map.put("event", eventService.getEvent(eventId));
        return "eventKitsUI";
    }

    @RequestMapping("/showVolKitsUI")
    public String showVolKitsUI (Map<String, Object> map, HttpServletRequest request)
    {
        String strVolKitId = request.getParameter("id");
        if (Util.nullOrEmptyOrBlank(strVolKitId)) {
            return null;
        }
        Integer voldKitId = Integer.parseInt(strVolKitId);
        VolunteerKit volunteerKit = eventService.getVolunteerKit(voldKitId);

        map.put("event", eventService.getEvent(volunteerKit.getKit().getEvent().getId()));
        map.put("volunteerKit", volunteerKit);
        return "volKitsUI";
    }

    @RequestMapping("/showAddVolKitsUI")
    public String showAddVolKitsUI (Map<String, Object> map, HttpServletRequest request)
    {
        String strEventId = request.getParameter("eventId");
        if (Util.nullOrEmptyOrBlank(strEventId)) {
            return null;
        }
        Integer eventId = Integer.parseInt(strEventId);
        Kit kit = eventService.getEventKit(eventId);
        if (kit != null) {
            VolunteerKit volunteerKit = new VolunteerKit();
            volunteerKit.setKit(kit);
            String strUnallotedKitsCount = request.getParameter("unallotedKitsCount");
            if (!Util.nullOrEmptyOrBlank(strUnallotedKitsCount)) {
                Integer unallotedKitsCount = Integer.parseInt(strUnallotedKitsCount);
                map.put("unallotedKitsCount", unallotedKitsCount);
            }
            map.put("volunteerKit", volunteerKit);
            map.put("volunteerList", volunteerService.listVolunteerWithoutKits(kit.getId()));
            return "addVolKitsUI";
        }
        return "redirect:/event.htm";
    }

    public static Map<String, String> volunteersMap(List<Volunteer> volunteerList)
    {
        Map<String, String> volunteerMap = new LinkedHashMap<String, String>();
        for(Volunteer vol: volunteerList) {
            volunteerMap.put(String.valueOf(vol.getId()), vol.getName());
        }
        return volunteerMap;
    }

    @RequestMapping("/refreshKitsUI")
    public String refreshKitsUI (Map<String, Object> map, HttpServletRequest request)
    {
        String strEventId = request.getParameter("eventId");
        if (!Util.nullOrEmptyOrBlank(strEventId)) {
            request.setAttribute("eventId", strEventId);
        }
        return "forward:/showKitsUI.htm";
    }

    @RequestMapping("/backToEventsAction")
    public String backToEventsAction (Map<String, Object> map, HttpServletRequest request)
    {
        return "redirect:/event.htm";
    }

    @RequestMapping("/backToKitsAction")
    public String backToKitsAction (Map<String, Object> map, HttpServletRequest request)
    {
        String strEventId = request.getParameter("eventId");
        if (!Util.nullOrEmptyOrBlank(strEventId)) {
            request.setAttribute("eventId", strEventId);
        }
        return "forward:/showKitsUI.htm";
    }

    @RequestMapping(value = "/addVolKitsAction", method = RequestMethod.POST)
    public String addVolKitsAction (@ModelAttribute("volunteerKit") VolunteerKit volunteerKit,
                                HttpServletRequest request)
    {
        Login login = (Login) request.getSession().getAttribute(Login.ClassName);
        volunteerKit.initialize(login.getEmail());
        LoggedInVolunteer loggedInVolunteer = volunteerService.getVolunteer(volunteerKit.getVolunteerId()).getLogin();
        volunteerKit.setLoggedInVolunteer(loggedInVolunteer);
        volunteerService.addVolunteerKit(volunteerKit);
        String strEventId = String.valueOf(volunteerKit.getKit().getEvent().getId());
        if (!Util.nullOrEmptyOrBlank(strEventId)) {
            request.setAttribute("eventId", strEventId);
        }
        return "forward:/showKitsUI.htm";
    }

    @RequestMapping(value = "/allotEventKitsAction", method = RequestMethod.POST)
    public String allotEventKitsAction (Kit kit,
                               HttpServletRequest request)
    {
        Login login = (Login) request.getSession().getAttribute(Login.ClassName);
        if(kit.getTimeCreated() == null) {
            kit.initialize(login.getEmail());
        } else {
            kit.initializeForUpdate(login.getEmail());
        }
        eventService.manageEventKit(kit);
        request.setAttribute("eventId", kit.getEventId());
        return "forward:/showEventDetailUI.htm";
    }

    @RequestMapping(value = "/allotVolKitsAction", method = RequestMethod.POST)
    public String allotVolKitsAction (Map<String, Object> map, VolunteerKit volunteerKit,
                               HttpServletRequest request)
    {
        Login login = (Login) request.getSession().getAttribute(Login.ClassName);
        if(volunteerKit.getTimeCreated() == null) {
            volunteerKit.initialize(login.getEmail());
        } else {
            volunteerKit.initializeForUpdate(login.getEmail());
        }
        eventService.allotVolunteerKits(volunteerKit);

        String strEventId = request.getParameter("kit.event.id");
        if (!Util.nullOrEmptyOrBlank(strEventId)) {
            request.setAttribute("eventId", strEventId);
        }
        return "forward:/showKitsUI.htm";
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

    @RequestMapping("/allocateSeats")
    public String allocateSeats (HttpServletRequest request, Map<String, Object> map)
    {
        String strEventId = request.getParameter("eventId");
        if (Util.nullOrEmptyOrBlank(strEventId)) {
            return "redirect:/event.htm";
        }

        Integer eventId = Integer.parseInt(strEventId);
        Event event = eventService.getEvent(eventId);
        if (event != null) {
            eventService.allocateSeats(event);
        }
        return "redirect:/event.htm";
    }
}
