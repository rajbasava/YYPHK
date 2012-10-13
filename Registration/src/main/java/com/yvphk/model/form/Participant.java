/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.

    Responsible: byummadisingh
*/

package com.yvphk.model.form;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PHK_PARTICIPANT")
public class Participant extends BaseForm
{
    public static final String ClassName = "com.yvphk.model.form.Participant";

    public static final String[] ImportFields =
            new String[] {"Name","Email","Mobile","Home","Foundation","Vip","VipDesc"};

    public static final String[] ReportFields =
            new String[] {"Name","Email","Mobile","Home","Foundation","Vip","VipDesc"};

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "MOBILE")
    private String mobile;

    @Column(name = "HOME")
    private String home;

    @Column(name = "FOUNDATION")
    private String foundation;

    @Column(name = "VIP")
    private boolean vip;

    @Column(name = "VIPDESC")
    private String vipDesc;

    @Column(name = "PREPAREDBY")
    private String preparedBy;

    @Column(name = "TIMECREATED")
    private Date timecreated;

    @Column(name = "TIMEUPDATED")
    private Date timeupdated;

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

    @Override
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

    public String getHome ()
    {
        return home;
    }

    public void setHome (String home)
    {
        this.home = home;
    }

    public String getFoundation ()
    {
        return foundation;
    }

    public void setFoundation (String foundation)
    {
        this.foundation = foundation;
    }

    public boolean isVip ()
    {
        return vip;
    }

    public void setVip (boolean vip)
    {
        this.vip = vip;
    }

    public String getVipDesc ()
    {
        return vipDesc;
    }

    public void setVipDesc (String vipDesc)
    {
        this.vipDesc = vipDesc;
    }

    public String getPreparedBy()
    {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy)
    {
        this.preparedBy = preparedBy;
    }

    public Date getTimecreated ()
    {
        return timecreated;
    }

    public void setTimecreated (Date timecreated)
    {
        this.timecreated = timecreated;
    }

    public Date getTimeupdated ()
    {
        return timeupdated;
    }

    public void setTimeupdated (Date timeupdated)
    {
        this.timeupdated = timeupdated;
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
