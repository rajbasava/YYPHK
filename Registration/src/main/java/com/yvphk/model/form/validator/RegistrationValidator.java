/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/
package com.yvphk.model.form.validator;

import com.yvphk.common.Util;
import com.yvphk.model.form.Login;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.yvphk.model.form.RegisteredParticipant;

/**
 * Validator to validate the fields of the User Registration Form.
 */
public class RegistrationValidator implements Validator
{

    @Override
    public boolean supports (Class<?> clazz)
    {
        return RegisteredParticipant.class.isAssignableFrom(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate (Object target, Errors errors)
    {
        if (! (target instanceof RegisteredParticipant)) {
            throw new IllegalArgumentException("Target should be of RegisteredParticipant type");
        }

        RegisteredParticipant participant = (RegisteredParticipant) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "participant.name", "participant.name");
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "participant.mobile", "participant.mobile");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "participant.foundation", "participant.foundation");

        if (participant.getParticipant().isVip()) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "participant.vipDesc", "participant.vipDesc");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eventId", "eventId");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "registration.level", "registration.level");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "registration.amountPayable", "registration.amountPayable");

        Login login = Util.getCurrentUser();
        if (login.getAccess().isRegVolunteer() &&
                !(participant.getRegistration().getTotalAmountPaid() >= participant.getRegistration().getAmountPayable())) {
            errors.reject("registration.noAccess", "registration.noAccess");
        }

        if (participant.getCurrentPayment() != null &&
                participant.getCurrentPayment().getAmountPaid() != null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "currentPayment.receiptInfo", "currentPayment.receiptInfo");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "currentPayment.mode", "currentPayment.mode");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "currentPayment.receiptDate", "currentPayment.receiptDate");
        }

    }


}
