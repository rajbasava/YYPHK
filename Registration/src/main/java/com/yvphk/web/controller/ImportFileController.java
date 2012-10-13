/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.

    Responsible: byummadisingh
*/

package com.yvphk.web.controller;

import com.yvphk.model.form.Event;
import com.yvphk.model.form.ImportFile;
import com.yvphk.model.form.Login;
import com.yvphk.service.EventService;
import com.yvphk.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ImportFileController
{
    @Autowired
    private ImportService importService;

    @Autowired
    private EventService eventService;

    @RequestMapping(value = "/processImportFile")
    public String processImportFile (ImportFile importFile,
                                     Map<String, Object> map,
                                     HttpServletRequest request)
    {
        Login login = (Login) request.getSession().getAttribute(Login.ClassName);
        MultipartFile file = importFile.getFile();
        if (file != null) {
            // todo validations for type of the file
            importService.processImportFile(importFile, login);
        }
        map.put("results", file.getOriginalFilename());
        return "importResults";
    }

    @RequestMapping(value = "/import")
    public String importFile (Map<String, Object> map, HttpServletRequest request)
    {
        map.put("allEvents", getAllEvents());
        map.put("importFile", new ImportFile());
        return "import";
    }

    //todo common method - move to a common place
    private Map<String, String> getAllEvents ()
    {
        List<Event> events = eventService.allEvents();
        LinkedHashMap eventMap = new LinkedHashMap<String, String>();
        for (Event event : events) {
            eventMap.put(event.getId(), event.getName());
        }
        return eventMap;
    }


}
