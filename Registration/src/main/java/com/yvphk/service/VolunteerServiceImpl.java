/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
 
    Responsible: byummadisingh
*/
package com.yvphk.service;

import com.yvphk.model.dao.VolunteerDAO;
import com.yvphk.model.form.Login;
import com.yvphk.model.form.Volunteer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VolunteerServiceImpl implements VolunteerService
{
    @Autowired
    private VolunteerDAO volunteerDAO;

    @Transactional
    public void addVolunteer (Volunteer volunteer)
    {
        volunteerDAO.addVolunteer(volunteer);
    }

    @Transactional
    public List<Volunteer> listVolunteer ()
    {
        return volunteerDAO.listVolunteer();
    }

    @Transactional
    public void removeVolunteer (Integer id)
    {
        volunteerDAO.removeVolunteer(id);
    }

    @Transactional
    public boolean processLogin (Login login)
    {
        return volunteerDAO.processLogin(login);
    }

    @Transactional
    public void processLogout (Login login)
    {
        volunteerDAO.processLogout(login);
    }
}