/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
 
    Responsible: byummadisingh
*/
package com.yvphk.model.form;

import java.io.Serializable;

public class Login implements Serializable
{
    public static final String ClassName = "com.yvphk.model.form.Login";

    private String email;
    private String password;
    private String counter;
    private String permission;
    private Integer volunteerId;
    private long lastAccessed;

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    public String getCounter ()
    {
        return counter;
    }

    public void setCounter (String counter)
    {
        this.counter = counter;
    }

    public String getPermission ()
    {
        return permission;
    }

    public void setPermission (String permission)
    {
        this.permission = permission;
    }

    public Integer getVolunteerId ()
    {
        return volunteerId;
    }

    public void setVolunteerId (Integer volunteerId)
    {
        this.volunteerId = volunteerId;
    }

    public long getLastAccessed ()
    {
        return lastAccessed;
    }

    public void setLastAccessed (long lastAccessed)
    {
        this.lastAccessed = lastAccessed;
    }
}