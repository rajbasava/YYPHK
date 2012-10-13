/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.

    Responsible: byummadisingh
*/
package com.yvphk.model.dao;

import com.yvphk.model.form.Event;
import com.yvphk.model.form.EventFee;

import java.util.List;

public interface EventDAO
{
    public void addEvent (Event event);

    public Event getEvent (Integer eventId);

    public List<Event> allEvents ();

    public void removeEvent (Integer id);

    public void addFee (EventFee fee, Integer eventId);

    public List<EventFee> getEventFees (Integer eventId);

    public void removeEventFee (Integer eventFeeId);

    public EventFee getEventFee (Integer eventFeeId);
}
