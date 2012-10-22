/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.web.controller;

import com.yvphk.model.form.ParticipantCriteria;
import com.yvphk.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class DownloadController
{
    @Autowired
    private DownloadService downloadService;

    @RequestMapping(value = "/xls")
    public void getXLS (HttpServletResponse response,
                        Map<String, Object> map,
                        ParticipantCriteria participantCriteria)
    {
        downloadService.downloadXLS(response, participantCriteria);
    }
}
