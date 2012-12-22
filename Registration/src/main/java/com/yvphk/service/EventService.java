/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.service;

import com.yvphk.model.form.Event;
import com.yvphk.model.form.EventFee;

import java.util.List;

public interface EventService
{
    public void addEvent (Event event);

    public Event getEvent (Integer eventId);

    public List<Event> allEvents ();

    public void removeEvent (Integer id);

    public void addEventFee (EventFee fee, Integer eventId);

    public List<EventFee> getEventFees (Integer eventId);

    public void removeEventFee (Integer eventFeeId);

    public EventFee getEventFee (Integer eventFeeId);

    public List<String> getAllRowMetaNames ();

    public List<String> getAllFoundations ();

    public void allocateSeats (Event event);

}
