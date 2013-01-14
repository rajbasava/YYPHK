/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/
package com.yvphk.service;

import com.yvphk.model.form.Login;
import com.yvphk.model.form.Volunteer;
import com.yvphk.model.form.VolunteerKit;

import java.util.List;
import java.util.Map;


public interface VolunteerService
{
    public void addVolunteer (Volunteer volunteer);

    public void addVolunteerKit (VolunteerKit volunteerKit);

    public List<Volunteer> listVolunteer ();

    public Map<String, String> listVolunteerWithoutKits (Integer eventKitId);

    public void removeVolunteer (Integer id);

    public boolean processLogin (Login login);

    public void processLogout (Login login);

    public Volunteer getVolunteer (Integer volunteerId);
}
