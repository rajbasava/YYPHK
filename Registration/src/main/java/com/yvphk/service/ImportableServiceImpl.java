/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.service;

import com.yvphk.model.dao.EventDAO;
import com.yvphk.model.dao.ParticipantDAO;
import com.yvphk.model.form.EventRegistration;
import com.yvphk.model.form.ParticipantSeat;
import com.yvphk.model.form.RegisteredParticipant;
import com.yvphk.model.form.RegistrationForm;
import com.yvphk.model.form.RowMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImportableServiceImpl implements ImportableService
{
    @Autowired
    private ParticipantDAO participantDAO;

    @Autowired
    private EventDAO eventDAO;

    public EventRegistration registerParticipant (RegisteredParticipant registeredParticipant)
    {
        return participantDAO.registerParticipant(registeredParticipant);
    }

    public void addRowMeta (RowMeta rowMeta)
    {
        eventDAO.addRowMeta(rowMeta);
    }

    public void addParticipantSeat (ParticipantSeat participantSeat)
    {
        participantDAO.addParticipantSeat(participantSeat);
    }

    public void updateRegistration (RegistrationForm registrationForm)
    {
        participantDAO.updateRegistration(registrationForm);
    }

}
