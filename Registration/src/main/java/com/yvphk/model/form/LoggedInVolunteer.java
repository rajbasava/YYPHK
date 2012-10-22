/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/
package com.yvphk.model.form;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "PHK_VOLLOGIN")
public class LoggedInVolunteer extends BaseForm
{
    public static final String ClassName = "com.yvphk.model.form.LoggedInVolunteer";

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "VOLUNTEERID")
    private Volunteer volunteer;

    @Column(name = "COUNTER")
    private String counter;

    @Column(name = "LOGGEDIN")
    private Date loggedin;

    @Column(name = "LOGGEDOUT")
    private Date loggedout;

    public Integer getId ()
    {
        return id;
    }

    public void setId (Integer id)
    {
        this.id = id;
    }

    @Override
    public String getType ()
    {
        return ClassName;
    }

    public Volunteer getVolunteer ()
    {
        return volunteer;
    }

    public void setVolunteer (Volunteer volunteer)
    {
        this.volunteer = volunteer;
    }

    public String getCounter ()
    {
        return counter;
    }

    public void setCounter (String counter)
    {
        this.counter = counter;
    }

    public Date getLoggedin ()
    {
        return loggedin;
    }

    public void setLoggedin (Date loggedin)
    {
        this.loggedin = loggedin;
    }

    public Date getLoggedout ()
    {
        return loggedout;
    }

    public void setLoggedout (Date loggedout)
    {
        this.loggedout = loggedout;
    }
}