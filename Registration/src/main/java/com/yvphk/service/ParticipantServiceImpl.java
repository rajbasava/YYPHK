/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
 
    Responsible: byummadisingh
*/
package com.yvphk.service;

import com.yvphk.model.dao.ParticipantDAO;
import com.yvphk.model.form.EventRegistration;
import com.yvphk.model.form.Participant;
import com.yvphk.model.form.ParticipantCriteria;
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
    public EventRegistration registerParticipant (RegisteredParticipant registeredParticipant)
    {
        return participantDAO.registerParticipant(registeredParticipant);
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
    public List<EventRegistration> listRegistrations (ParticipantCriteria participantCriteria)
    {
        return participantDAO.listRegistrations(participantCriteria);
    }

    @Transactional
    public void processBatchEntry (List<RegisteredParticipant> participants)
    {
        participantDAO.processBatchEntry(participants);
    }

}