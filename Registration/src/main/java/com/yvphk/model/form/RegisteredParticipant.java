/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/
package com.yvphk.model.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RegisteredParticipant implements Serializable
{
    public static final String ActionRegister = "Register";
    public static final String ActionUpdate = "Update";

    private Participant participant;
    private EventRegistration registration;
    private EventPayment currentPayment;
    private List<EventPayment> allPayments = new ArrayList<EventPayment>();
    private HistoryRecord currentHistoryRecord;
    private List<HistoryRecord> allHistoryRecords = new ArrayList<HistoryRecord>();
    private ParticipantSeat currentSeat;
    private List<ParticipantSeat> allSeats;
    private String action;
    private Integer eventId;
    private Integer eventFeeId;
    private String otherFoundation;
    private String status;


    public void initialize (String email)
    {
        getParticipant().initialize(email);
        getRegistration().initialize(email);
        initializePayments(email);
        initializeHistoryRecords(email);
    }

    public void initializePayments (String email)
    {
        List<EventPayment> list = getAllPayments();
        for (EventPayment payment : list) {
            payment.initialize(email);
        }
    }
    public void initializeHistoryRecords (String email)
    {
        List<HistoryRecord> list = getAllHistoryRecords();
        for (HistoryRecord record : list) {
            record.initialize(email);
        }
    }

    public void initializeForUpdate (String email)
    {
        getParticipant().initializeForUpdate(email);
        getRegistration().initializeForUpdate(email);
        initializePayments(email);
        initializeHistoryRecords(email);
    }

    public Participant getParticipant ()
    {
        return participant;
    }

    public void setParticipant (Participant participant)
    {
        this.participant = participant;
    }

    public EventRegistration getRegistration ()
    {
        return registration;
    }

    public void setRegistration (EventRegistration registration)
    {
        this.registration = registration;
    }

    public EventPayment getCurrentPayment ()
    {
        return currentPayment;
    }

    public void setCurrentPayment (EventPayment currentPayment)
    {
        this.currentPayment = currentPayment;
        setPayment(currentPayment);
    }

    public List<EventPayment> getAllPayments ()
    {
        return allPayments;
    }

    public void setAllPayments (List<EventPayment> allPayments)
    {
        this.allPayments = allPayments;
    }

    public HistoryRecord getCurrentHistoryRecord ()
    {
        return currentHistoryRecord;
    }

    public void setCurrentHistoryRecord (HistoryRecord currentHistoryRecord)
    {
        this.currentHistoryRecord = currentHistoryRecord;
        setHistoryRecord(currentHistoryRecord);
    }

    public List<HistoryRecord> getAllHistoryRecords ()
    {
        return allHistoryRecords;
    }

    public void setAllHistoryRecords (List<HistoryRecord> allHistoryRecords)
    {
        this.allHistoryRecords = allHistoryRecords;
    }

    public ParticipantSeat getCurrentSeat ()
    {
        return currentSeat;
    }

    public void setCurrentSeat (ParticipantSeat currentSeat)
    {
        this.currentSeat = currentSeat;
    }

    public List<ParticipantSeat> getAllSeats ()
    {
        return allSeats;
    }

    public void setAllSeats (List<ParticipantSeat> allSeats)
    {
        this.allSeats = allSeats;
    }

    public String getAction ()
    {
        return action;
    }

    public void setAction (String action)
    {
        this.action = action;
    }

    public Integer getEventId ()
    {
        return eventId;
    }

    public void setEventId (Integer eventId)
    {
        this.eventId = eventId;
    }

    public Integer getEventFeeId ()
    {
        return eventFeeId;
    }

    public void setEventFeeId (Integer eventFeeId)
    {
        this.eventFeeId = eventFeeId;
    }

    public void setPayment (EventPayment payment)
    {
        allPayments.add(payment);
    }

    public void setHistoryRecord (HistoryRecord record)
    {
        allHistoryRecords.add(record);
    }

    public String getOtherFoundation ()
    {
        return otherFoundation;
    }

    public void setOtherFoundation (String otherFoundation)
    {
        this.otherFoundation = otherFoundation;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }
}