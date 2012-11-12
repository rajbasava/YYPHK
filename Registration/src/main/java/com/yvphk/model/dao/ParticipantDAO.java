/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/
package com.yvphk.model.dao;

import com.yvphk.model.form.Event;
import com.yvphk.model.form.EventPayment;
import com.yvphk.model.form.EventRegistration;
import com.yvphk.model.form.Participant;
import com.yvphk.model.form.RegistrationCriteria;
import com.yvphk.model.form.ParticipantSeat;
import com.yvphk.model.form.PaymentCriteria;
import com.yvphk.model.form.ReferenceGroup;
import com.yvphk.model.form.RegisteredParticipant;

import java.util.List;

public interface ParticipantDAO
{
    public Participant addParticipant (RegisteredParticipant registeredParticipant);

    public EventRegistration registerParticipant (RegisteredParticipant registeredParticipant);

    public Participant getParticipant (Integer userId);

    public EventRegistration getEventRegistration (Integer registrationId);

    public List<Participant> listParticipants (RegistrationCriteria registrationCriteria);

    public List<EventRegistration> listRegistrations (RegistrationCriteria registrationCriteria);

    public void processBatchEntry (List<RegisteredParticipant> participants);

    public void addReferenceGroup (ReferenceGroup referenceGroup);

    public ReferenceGroup getReferenceGroup (String name);

    public List<ReferenceGroup> listReferenceGroups ();

    public List<EventPayment> listPayments (PaymentCriteria paymentCriteria);

    public void processPayment (EventPayment payment, Integer registrationId, boolean isAdd);

    public List<ParticipantSeat> getAllSeats (Event event, String level);

}
