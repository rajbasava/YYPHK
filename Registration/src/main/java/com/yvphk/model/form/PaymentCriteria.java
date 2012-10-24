/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/
package com.yvphk.model.form;

import java.io.Serializable;
import java.util.Date;

public class PaymentCriteria implements Serializable
{
    private Integer eventId;
    private String foundation;
    private String otherFoundation;
    private String level;
    private String mode;
    private String reference;
    private Date fromReceiptDate;
    private Date toReceiptDate;
    private boolean pdcNotClear;
    private Date fromPdcDate;
    private Date toPdcDate;

    public Integer getEventId ()
    {
        return eventId;
    }

    public void setEventId (Integer eventId)
    {
        this.eventId = eventId;
    }

    public String getFoundation ()
    {
        return foundation;
    }

    public void setFoundation (String foundation)
    {
        this.foundation = foundation;
    }

    public String getOtherFoundation ()
    {
        return otherFoundation;
    }

    public void setOtherFoundation (String otherFoundation)
    {
        this.otherFoundation = otherFoundation;
    }

    public String getLevel ()
    {
        return level;
    }

    public void setLevel (String level)
    {
        this.level = level;
    }

    public String getMode ()
    {
        return mode;
    }

    public void setMode (String mode)
    {
        this.mode = mode;
    }

    public String getReference ()
    {
        return reference;
    }

    public void setReference (String reference)
    {
        this.reference = reference;
    }

    public Date getFromReceiptDate ()
    {
        return fromReceiptDate;
    }

    public void setFromReceiptDate (Date fromReceiptDate)
    {
        this.fromReceiptDate = fromReceiptDate;
    }

    public Date getToReceiptDate ()
    {
        return toReceiptDate;
    }

    public void setToReceiptDate (Date toReceiptDate)
    {
        this.toReceiptDate = toReceiptDate;
    }

    public boolean isPdcNotClear ()
    {
        return pdcNotClear;
    }

    public void setPdcNotClear (boolean pdcNotClear)
    {
        this.pdcNotClear = pdcNotClear;
    }

    public Date getFromPdcDate ()
    {
        return fromPdcDate;
    }

    public void setFromPdcDate (Date fromPdcDate)
    {
        this.fromPdcDate = fromPdcDate;
    }

    public Date getToPdcDate ()
    {
        return toPdcDate;
    }

    public void setToPdcDate (Date toPdcDate)
    {
        this.toPdcDate = toPdcDate;
    }
}
