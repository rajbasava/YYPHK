/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/
package com.yvphk.model.dao;

import com.yvphk.model.form.Event;
import com.yvphk.model.form.EventFee;
import com.yvphk.model.form.RowMeta;

import java.util.List;

public interface EventDAO extends CommonDAO
{
    public void addEvent (Event event);

    public Event getEvent (Integer eventId);

    public List<Event> allEvents ();

    public void removeEvent (Integer id);

    public void addFee (EventFee fee, Integer eventId);

    public List<EventFee> getEventFees (Integer eventId);

    public void removeEventFee (Integer eventFeeId);

    public EventFee getEventFee (Integer eventFeeId);

    public RowMeta getFirstEmptyRowMeta (Event event);

    public List<RowMeta> getAllEmptyRowMetas (Event event);

    public List<String> getAllRowMetaNames ();
}
