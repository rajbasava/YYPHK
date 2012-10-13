/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.service;

import com.yvphk.model.form.ParticipantCriteria;

import javax.servlet.http.HttpServletResponse;

public interface DownloadService
{
    public void downloadXLS (HttpServletResponse response, ParticipantCriteria criteria);
}
