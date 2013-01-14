/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.service;

import com.yvphk.common.SeatingType;
import com.yvphk.model.dao.EventDAO;
import com.yvphk.model.dao.ParticipantDAO;
import com.yvphk.model.form.*;
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
    public void saveOrUpdateEvent (Event event)
    {
        eventDAO.saveOrUpdateEvent(event);
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

    @Override
    @Transactional
    public Kit getEventKit (Integer eventId)
    {
        return eventDAO.getEventKit(eventId);
    }

    @Override
    @Transactional
    public void manageEventKit(Kit kit)
    {
        eventDAO.manageEventKit(kit);
    }

    @Override
    @Transactional
    public List<VolunteerKit> getVolunteerKits (Integer kitId)
    {
        return eventDAO.getVolunteerKits(kitId);
    }

    @Override
    @Transactional
    public VolunteerKit getVolunteerKit (Integer voldKitId)
    {
        return eventDAO.getVolunteerKit(voldKitId);
    }

    @Override
    @Transactional
    public void allotVolunteerKits(VolunteerKit volunteerKit)
    {
        eventDAO.allotVolunteerKits(volunteerKit);
    }

    @Override
    @Transactional
    public List<String> getAllRowMetaNames ()
    {
        return eventDAO.getAllRowMetaNames();
    }

    @Override
    @Transactional
    public List<String> getAllFoundations ()
    {
        return eventDAO.getAllFoundations();
    }

    @Override
    @Transactional
    public void allocateSeats (Event event)
    {
        String seatingType = event.getSeatingType();
        Object obj = SeatingType.createService(seatingType);
        if (obj != null && obj instanceof SeatingService) {
            SeatingService service = (SeatingService) obj;
            service.allocateSeats(event);
        }
    }

    @Override
    public synchronized ParticipantSeat nextSeat (Event event, EventRegistration registration)
    {
        if (!event.isSeatAllocated()) {
            return null;
        }

        ParticipantSeat seat = null;
        String seatingType = event.getSeatingType();
        Object obj = SeatingType.createService(seatingType);
        if (obj != null && obj instanceof SeatingService) {
            SeatingService service = (SeatingService) obj;
            seat = service.nextSeat(event, registration);
        }
        return seat;
    }
}
