/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.

    Responsible: byummadisingh
*/

package com.yvphk.model.form;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public abstract class BaseForm implements Serializable
{

    public void initialize (String email)
    {
        setPreparedBy(email);
        setTimeCreated(new Date());
        setTimeUpdated(new Date());
        setActive(true);
    }

    public void initializeForUpdate (String email)
    {
        setTimeUpdated(new Date());
        setActive(true);
    }

    public abstract Integer getId ();

    public abstract String getType ();

    public void setPreparedBy (String preparedBy)
    {

    }

    public void setTimeCreated (Date timeCreated)
    {

    }

    public void setTimeUpdated (Date timeUpdated)
    {

    }

    public void setActive (boolean active)
    {

    }

}
