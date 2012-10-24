/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.service;

import com.yvphk.model.form.ParticipantCriteria;
import com.yvphk.model.form.PaymentCriteria;

import javax.servlet.http.HttpServletResponse;

public interface DownloadService
{
    public void downloadRegistrationsReport (HttpServletResponse response, ParticipantCriteria criteria);

    public void downloadPaymentsReport (HttpServletResponse response, PaymentCriteria criteria);
}
