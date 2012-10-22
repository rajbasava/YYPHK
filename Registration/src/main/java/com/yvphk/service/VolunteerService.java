/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/
package com.yvphk.service;

import com.yvphk.model.form.Login;
import com.yvphk.model.form.Volunteer;

import java.util.List;


public interface VolunteerService
{
    public void addVolunteer (Volunteer volunteer);

    public List<Volunteer> listVolunteer ();

    public void removeVolunteer (Integer id);

    public boolean processLogin (Login login);

    public void processLogout (Login login);
}
