/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.model.form;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class ImportFile
{
    private CommonsMultipartFile file;
    private Integer eventId;

    public CommonsMultipartFile getFile ()
    {
        return file;
    }

    public void setFile (CommonsMultipartFile file)
    {
        this.file = file;
    }

    public Integer getEventId ()
    {
        return eventId;
    }

    public void setEventId (Integer eventId)
    {
        this.eventId = eventId;
    }
}
