/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/
package com.yvphk.service;

import com.yvphk.model.dao.ParticipantDAO;
import com.yvphk.model.form.Event;
import com.yvphk.model.form.EventPayment;
import com.yvphk.model.form.EventRegistration;
import com.yvphk.model.form.HistoryRecord;
import com.yvphk.model.form.Login;
import com.yvphk.model.form.Participant;
import com.yvphk.model.form.ParticipantCriteria;
import com.yvphk.model.form.ParticipantSeat;
import com.yvphk.model.form.RegistrationCriteria;
import com.yvphk.model.form.PaymentCriteria;
import com.yvphk.model.form.ReferenceGroup;
import com.yvphk.model.form.RegisteredParticipant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParticipantServiceImpl implements ParticipantService
{
    @Autowired
    private ParticipantDAO participantDAO;

    @Transactional
    public EventRegistration registerParticipant (RegisteredParticipant registeredParticipant, Login login)
    {
        return participantDAO.registerParticipant(registeredParticipant, login);
    }

    @Transactional
    public Participant getParticipant (Integer participantId)
    {
        return participantDAO.getParticipant(participantId);
    }

    @Transactional
    public EventRegistration getEventRegistration (Integer registrationId)
    {
        return participantDAO.getEventRegistration(registrationId);
    }

    @Transactional
    public List<Participant> listParticipants (ParticipantCriteria participantCriteria)
    {
        return participantDAO.listParticipants(participantCriteria);
    }

    @Transactional
    public List<EventRegistration> listRegistrations (RegistrationCriteria registrationCriteria)
    {
        return participantDAO.listRegistrations(registrationCriteria);
    }

    @Transactional
    public void addReferenceGroup (ReferenceGroup referenceGroup)
    {
        participantDAO.addReferenceGroup(referenceGroup);
    }

    @Transactional
    public ReferenceGroup getReferenceGroup (String name)
    {
        return participantDAO.getReferenceGroup(name);
    }

    @Transactional
    public List<ReferenceGroup> listReferenceGroups ()
    {
        return participantDAO.listReferenceGroups();
    }

    @Transactional
    public List<EventPayment> listPayments (PaymentCriteria paymentCriteria)
    {
        return participantDAO.listPayments(paymentCriteria);
    }

    @Transactional
    public void processPayment (EventPayment payment, Integer registrationId, boolean isAdd)
    {
        participantDAO.processPayment(payment, registrationId, isAdd);
    }

    @Transactional
    public void cancelRegistration (EventRegistration registration, HistoryRecord historyRecord)
    {
        participantDAO.cancelRegistration(registration, historyRecord);
    }

    @Transactional
    public void onHoldRegistration (EventRegistration registration, HistoryRecord historyRecord)
    {
        participantDAO.onHoldRegistration(registration, historyRecord);
    }

    @Transactional
    public void changeToRegistered (EventRegistration registration, HistoryRecord historyRecord)
    {
        participantDAO.changeToRegistered(registration, historyRecord);
    }

    @Transactional
    public void replaceParticipant (EventRegistration registration,
                                    Participant participantToReplace,
                                    HistoryRecord record)
    {
        participantDAO.replaceParticipant(registration, participantToReplace, record);
    }

    @Transactional
    public void addParticipant (Participant participant)
    {
        participantDAO.addParticipant(participant);
    }

    @Transactional
    public List<ParticipantSeat> getAllSeats (Event event)
    {
        return participantDAO.getAllSeats(event);
    }

    @Transactional
    public void addParticipantSeat (ParticipantSeat participantSeat)
    {
        participantDAO.addParticipantSeat(participantSeat);
    }

}