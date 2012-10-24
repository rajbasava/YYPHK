/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/
package com.yvphk.model.form;

import java.io.Serializable;

public class ParticipantCriteria implements Serializable
{
    private String name;
    private String mobile;
    private String email;
    private String level;
    private String foundation;
    private Integer seat;
    private String otherFoundation;
    private String amountPaidCategory;
    private String reference;
    private Integer eventId;
    private boolean consolidated;
    private boolean vip;

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getMobile ()
    {
        return mobile;
    }

    public void setMobile (String mobile)
    {
        this.mobile = mobile;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getLevel ()
    {
        return level;
    }

    public void setLevel (String level)
    {
        this.level = level;
    }

    public String getFoundation ()
    {
        return foundation;
    }

    public void setFoundation (String foundation)
    {
        this.foundation = foundation;
    }

    public Integer getSeat ()
    {
        return seat;
    }

    public void setSeat (Integer seat)
    {
        this.seat = seat;
    }

    public String getOtherFoundation ()
    {
        return otherFoundation;
    }

    public void setOtherFoundation (String otherFoundation)
    {
        this.otherFoundation = otherFoundation;
    }

    public String getAmountPaidCategory ()
    {
        return amountPaidCategory;
    }

    public void setAmountPaidCategory (String amountPaidCategory)
    {
        this.amountPaidCategory = amountPaidCategory;
    }

    public String getReference ()
    {
        return reference;
    }

    public void setReference (String reference)
    {
        this.reference = reference;
    }

    public Integer getEventId ()
    {
        return eventId;
    }

    public void setEventId (Integer eventId)
    {
        this.eventId = eventId;
    }

    public boolean isConsolidated ()
    {
        return consolidated;
    }

    public void setConsolidated (boolean consolidated)
    {
        this.consolidated = consolidated;
    }

    public boolean isVip ()
    {
        return vip;
    }

    public void setVip (boolean vip)
    {
        this.vip = vip;
    }
}