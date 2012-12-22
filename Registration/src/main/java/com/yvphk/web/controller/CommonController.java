/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.web.controller;

import com.yvphk.model.form.Event;
import com.yvphk.model.form.EventRegistration;
import com.yvphk.model.form.ReferenceGroup;
import com.yvphk.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CommonController
{
    @Autowired
    private EventService eventService;

    @InitBinder
    public void initBinder (WebDataBinder binder)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
        binder.registerCustomEditor(Date.class, editor);
    }

    public Map<String, String> getAllEventMap (List<Event> events)
    {
        LinkedHashMap eventMap = new LinkedHashMap<String, String>();
        for (Event event : events) {
            eventMap.put(event.getId(), event.getName());
        }
        return eventMap;
    }

    public Map<String, String> getAllReferenceGroups (List<ReferenceGroup> groups)
    {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (ReferenceGroup referenceGroup: groups) {
            String name = referenceGroup.getName();
            map.put(name, name);
        }
        return map;
    }

    public Map<String, String> getRegistrationStatusMap ()
    {
        LinkedHashMap registrationStatusMap = new LinkedHashMap<String, String>();
        registrationStatusMap.put(EventRegistration.StatusRegistered, EventRegistration.StatusRegistered);
        registrationStatusMap.put(EventRegistration.StatusCancelled, EventRegistration.StatusCancelled);
        registrationStatusMap.put(EventRegistration.StatusOnHold, EventRegistration.StatusOnHold);
        return registrationStatusMap;
    }

    public List<String> allFoundations ()
    {
        return eventService.getAllFoundations();
    }
}
