/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.service;

import com.yvphk.model.form.EventRegistration;
import com.yvphk.model.form.ParticipantSeat;
import com.yvphk.model.form.RegisteredParticipant;
import com.yvphk.model.form.RegistrationForm;
import com.yvphk.model.form.RowMeta;

public interface ImportableService
{
    public EventRegistration registerParticipant (RegisteredParticipant registeredParticipant);

    public void addRowMeta (RowMeta rowMeta);

    public void addParticipantSeat (ParticipantSeat participantSeat);

    public void updateRegistration (RegistrationForm registrationForm);
}
