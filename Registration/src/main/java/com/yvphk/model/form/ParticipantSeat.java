/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/
package com.yvphk.model.form;

import com.yvphk.common.ParticipantLevel;
import com.yvphk.common.Util;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity
@Table(name = "phk_seat")
public class ParticipantSeat extends BaseForm
{
    public static final String ClassName = "com.yvphk.model.form.ParticipantSeat";

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "EVENTREGSTRNID")
    private EventRegistration registration;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "EVENTID")
    private Event event;

    @Column(name = "LEVEL")
    private String level;

    @Column(name = "SEAT")
    private Integer seat;

    @Column(name = "ALPHA")
    private String alpha;

    @Column(name = "SUFFIX")
    private String suffix;

    @Column(name = "CUSTOM")
    private boolean custom;

    @Transient
    private Integer registrationId;

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

    public EventRegistration getRegistration ()
    {
        return registration;
    }

    public void setRegistration (EventRegistration registration)
    {
        this.registration = registration;
    }

    public String getLevel ()
    {
        return level;
    }

    public void setLevel (String level)
    {
        this.level = level;
    }

    public String getLevelName()
    {
        if (Util.nullOrEmptyOrBlank(getLevel())) {
            return null;
        }

        return ParticipantLevel.getName(getLevel());
    }

    public Integer getSeat ()
    {
        return seat;
    }

    public void setSeat (Integer seat)
    {
        this.seat = seat;
    }

    public Event getEvent ()
    {
        return event;
    }

    public void setEvent (Event event)
    {
        this.event = event;
    }

    public String getAlpha ()
    {
        return alpha;
    }

    public void setAlpha (String alpha)
    {
        this.alpha = alpha;
    }

    public String getSuffix ()
    {
        return suffix;
    }

    public void setSuffix (String suffix)
    {
        this.suffix = suffix;
    }

    public boolean isCustom ()
    {
        return custom;
    }

    public void setCustom (boolean custom)
    {
        this.custom = custom;
    }

    public Integer getRegistrationId ()
    {
        return registrationId;
    }

    public void setRegistrationId (Integer registrationId)
    {
        this.registrationId = registrationId;
    }
}