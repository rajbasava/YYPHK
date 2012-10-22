/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.model.form;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "PHK_REFERENCE")
public class ReferenceGroup extends BaseForm
{
    public static final String ClassName = "com.yvphk.model.form.ReferenceGroup";

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Integer id;

    @Column(name = "UNIQUENAME")
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "MOBILE")
    private String mobile;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "PREPAREDBY")
    private String preparedBy;

    @Column(name = "TIMECREATED")
    private Date timeCreated;

    @Column(name = "TIMEUPDATED")
    private Date timeUpdated;

    @Column(name = "ACTIVE")
    private boolean active;

    public Integer getId ()
    {
        return id;
    }

    public void setId (Integer id)
    {
        this.id = id;
    }

    public String getType ()
    {
        return ClassName;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getMobile ()
    {
        return mobile;
    }

    public void setMobile (String mobile)
    {
        this.mobile = mobile;
    }

    public String getRemarks ()
    {
        return remarks;
    }

    public void setRemarks (String remarks)
    {
        this.remarks = remarks;
    }

    public String getPreparedBy ()
    {
        return preparedBy;
    }

    public void setPreparedBy (String preparedBy)
    {
        this.preparedBy = preparedBy;
    }

    public Date getTimeCreated ()
    {
        return timeCreated;
    }

    public void setTimeCreated (Date timeCreated)
    {
        this.timeCreated = timeCreated;
    }

    public Date getTimeUpdated ()
    {
        return timeUpdated;
    }

    public void setTimeUpdated (Date timeUpdated)
    {
        this.timeUpdated = timeUpdated;
    }

    public boolean isActive ()
    {
        return active;
    }

    public void setActive (boolean active)
    {
        this.active = active;
    }
}
