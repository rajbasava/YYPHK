/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/
package com.yvphk.model.dao;

import com.yvphk.model.form.*;

import java.util.List;

public interface ParticipantDAO extends CommonDAO
{
    public Participant addParticipant (Participant Participant);

    public EventRegistration registerParticipant (RegisteredParticipant registeredParticipant, Login login);

    public Participant getParticipant (Integer userId);

    public EventRegistration getEventRegistration (Integer registrationId);

    public List<Participant> listParticipants (ParticipantCriteria participantCriteria);

    public List<EventRegistration> listRegistrations (RegistrationCriteria registrationCriteria);

    public List<EventRegistration> allUnallocatedRegistrations (Event event, boolean vip, boolean indian);

    public void addReferenceGroup (ReferenceGroup referenceGroup);

    public ReferenceGroup getReferenceGroup (String name);

    public List<ReferenceGroup> listReferenceGroups ();

    public List<EventPayment> listPayments (PaymentCriteria paymentCriteria);

    public void processPayment (EventPayment payment, Integer registrationId, boolean isAdd);

    public List<ParticipantSeat> getAllSeats (Event event, String level);

    public void cancelRegistration (EventRegistration registration, HistoryRecord historyRecord);

    public void onHoldRegistration (EventRegistration registration, HistoryRecord historyRecord);

    public void changeToRegistered (EventRegistration registration, HistoryRecord historyRecord);

    public void replaceParticipant (EventRegistration registration,
                                    Participant participantToReplace,
                                    HistoryRecord record);

    public List<ParticipantSeat> getAllocatedSeats (Event event, String alpha, String suffix);

    public void removeEventRegistrations (Integer id);

}
