/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.service;

import com.yvphk.model.form.*;

import java.util.List;

public interface EventService
{
    public void saveOrUpdateEvent (Event event);

    public Event getEvent (Integer eventId);

    public List<Event> allEvents ();

    public void removeEvent (Integer id);

    public void addEventFee (EventFee fee, Integer eventId);

    public List<EventFee> getEventFees (Integer eventId);

    public void removeEventFee (Integer eventFeeId);

    public EventFee getEventFee (Integer eventFeeId);

    public Kit getEventKit (Integer eventId);

    public void manageEventKit(Kit kit);

    public List<VolunteerKit> getVolunteerKits (Integer kitId);

    public VolunteerKit getVolunteerKit (Integer voldKitId);

    public void allotVolunteerKits(VolunteerKit volunteerKit);

    public List<String> getAllRowMetaNames ();

    public List<String> getAllFoundations ();

    public void allocateSeats (Event event);

    public ParticipantSeat nextSeat (Event event, EventRegistration registration);

}
