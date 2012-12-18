/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.service;

import com.yvphk.model.dao.EventDAO;
import com.yvphk.model.dao.ParticipantDAO;
import com.yvphk.model.form.Event;
import com.yvphk.model.form.EventFee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventServiceImpl implements EventService
{

    @Autowired
    private EventDAO eventDAO;

    @Autowired
    private ParticipantDAO participantDAO;


    @Override
    @Transactional
    public void addEvent (Event event)
    {
        eventDAO.addEvent(event);
    }

    @Override
    @Transactional
    public Event getEvent (Integer eventId)
    {
        return eventDAO.getEvent(eventId);
    }

    @Override
    @Transactional
    public List<Event> allEvents ()
    {
        return eventDAO.allEvents();
    }

    @Override
    @Transactional
    public void removeEvent (Integer id)
    {
        eventDAO.removeEvent(id);
        participantDAO.removeEventRegistrations(id);
    }

    @Override
    @Transactional
    public void addEventFee (EventFee fee, Integer eventId)
    {
        eventDAO.addFee(fee, eventId);
    }

    @Override
    @Transactional
    public List<EventFee> getEventFees (Integer eventId)
    {
        return eventDAO.getEventFees(eventId);
    }

    @Override
    @Transactional
    public void removeEventFee (Integer eventFeeId)
    {
        eventDAO.removeEventFee(eventFeeId);
    }

    @Override
    @Transactional
    public EventFee getEventFee (Integer eventFeeId)
    {
        return eventDAO.getEventFee(eventFeeId);
    }
}
