/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/
package com.yvphk.model.dao;

import com.yvphk.common.AmountPaidCategory;
import com.yvphk.common.Foundation;
import com.yvphk.common.ParticipantLevel;
import com.yvphk.common.Util;
import com.yvphk.model.form.BaseForm;
import com.yvphk.model.form.Event;
import com.yvphk.model.form.HistoryRecord;
import com.yvphk.model.form.EventPayment;
import com.yvphk.model.form.EventRegistration;
import com.yvphk.model.form.Participant;
import com.yvphk.model.form.RegistrationCriteria;
import com.yvphk.model.form.ParticipantSeat;
import com.yvphk.model.form.PaymentCriteria;
import com.yvphk.model.form.ReferenceGroup;
import com.yvphk.model.form.RegisteredParticipant;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Repository
public class ParticipantDAOImpl implements ParticipantDAO
{
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ReloadableResourceBundleMessageSource messageSource;

    public Participant addParticipant (RegisteredParticipant registeredParticipant)
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public EventRegistration registerParticipant (RegisteredParticipant registeredParticipant)
    {
        Participant participant = registeredParticipant.getParticipant();
        if (Foundation.Others.getName().equals(participant.getFoundation())) {
            participant.setFoundation(registeredParticipant.getOtherFoundation());
        }

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        EventRegistration registration = registeredParticipant.getRegistration();

        if (RegisteredParticipant.ActionRegister.equals(registeredParticipant.getAction())) {

            String level = registeredParticipant.getRegistration().getLevel();
            Event event = registeredParticipant.getRegistration().getEvent();
            if (Util.nullOrEmptyOrBlank(level) || event == null) {
                return null;
            }
            session.save(participant);
            createAndAddHistoryRecord(
                    messageSource.getMessage("key.participantAdded",
                            new Object[] {participant.getId(),
                                    participant.getName()},
                            null),
                    Util.getCurrentUser().getEmail(),
                    registration,
                    session);
            registration.setParticipant(participant);
            if (registration.getRegistrationDate() == null) {
                registration.setRegistrationDate(new Date());
            }
            session.save(registration);
            createAndAddHistoryRecord(
                    messageSource.getMessage("key.registrationAdded",
                            new Object[] {registration.getId(),
                                    registration.getParticipant().getId(),
                                    registration.getEvent().getName()},
                            null),
                    Util.getCurrentUser().getEmail(),
                    registration,
                    session);

            ParticipantSeat participantSeat = registeredParticipant.getCurrentSeat();
            if (participantSeat ==  null) {
                participantSeat = new ParticipantSeat();
            }
            addParticipantSeats(participantSeat, registration, session);
        }
        else if (RegisteredParticipant.ActionUpdate.equals(registeredParticipant.getAction())) {
            //  todo update changes properties of registration objects to comments
            session.update(participant);
            registration.setParticipant(participant);
            createAndAddHistoryRecord(
                    messageSource.getMessage("key.registrationUpdated",
                            new Object[] {registration.getId(),
                                    registration.getParticipant().getId(),
                                    registration.getEvent().getName()},
                            null),
                    Util.getCurrentUser().getEmail(),
                    registration,
                    session);
            session.update(registration);
        }

        List<HistoryRecord> records = registeredParticipant.getAllHistoryRecords();
        for (HistoryRecord record: records) {
            addHistoryRecord(record, registration, session);
        }

        transaction.commit();

        session.flush();
        session.close();

        List<EventPayment> payments = registeredParticipant.getAllPayments();
        for(EventPayment payment : payments) {
            processPayment(payment, registration.getId(), true);
        }

        return registration;
    }

    private void addParticipantSeats (ParticipantSeat participantSeat,
                                      EventRegistration registration,
                                      Session session)
    {
        Event event = registration.getEvent();
        populateSeatNo(participantSeat, registration, event.getEligibilityLevel());
        session.save(participantSeat);
        if (!registration.getEvent().getEligibilityLevel().equals(registration.getLevel())) {
            if (!registration.getEvent().isSeatPerLevel()) {
                return;
            }

            List<String> lessLevels =
                    ParticipantLevel.getAllLessLevels(
                            registration.getEvent().getEligibilityLevel(),
                            registration.getLevel());

            for (String level: lessLevels) {
                ParticipantSeat seat = new ParticipantSeat();
                populateSeatNo(seat, registration, level);
                session.save(seat);
            }
        }
    }

    private void populateSeatNo (ParticipantSeat participantSeat,
                                 EventRegistration registration, String level)
    {
        Event event = registration.getEvent();
        if (participantSeat.getSeat() == null) {
            Integer greatestSeat = getGreatestSeat(event, level);
            Integer greatestSeatNo = new Integer(1);
            if (greatestSeat != null) {
                greatestSeatNo = greatestSeat.intValue() + 1;
            }
            participantSeat.setSeat(greatestSeatNo);
        }
        participantSeat.setLevel(level);
        participantSeat.setEvent(registration.getEvent());
        participantSeat.setRegistration(registration);
    }

    private void updatePDCCount (EventRegistration registration)
    {
        int pendingPdcCount = 0;
        Session session = sessionFactory.openSession();
        session.refresh(registration);

        Set<EventPayment> paymentsSet = registration.getPayments();
        if (paymentsSet == null) {
            return;
        }
        Iterator<EventPayment> itr = paymentsSet.iterator();
        while (itr.hasNext()) {
            EventPayment payment = itr.next();
            if (payment.isPdcNotClear()) {
                pendingPdcCount++;
            }
        }

        registration.setPendingPdc(pendingPdcCount);

        session.update(registration);

        session.flush();
        session.close();
    }

    public void addHistoryRecord (HistoryRecord historyRecord,
                                  BaseForm object,
                                  Session session)
    {
        if (!Util.nullOrEmptyOrBlank(historyRecord.getComment())) {
            historyRecord.setObject(object);
            session.save(historyRecord);
        }
    }

    public void createAndAddHistoryRecord (String comment,
                                           String preparedBy,
                                           BaseForm object,
                                           Session session)
    {
        HistoryRecord record = new HistoryRecord();
        record.setComment(comment);
        record.initialize(preparedBy);
        record.setObject(object);
        session.save(record);
    }

    public void processPayment (EventPayment payment,
                                Integer registrationId,
                                boolean isAdd)
    {
        if (payment.getAmountPaid() == null || payment.getAmountPaid() == 0) {
            return;
        }

        Session session = sessionFactory.openSession();
        EventRegistration registration = (EventRegistration) session.load(EventRegistration.class, registrationId);

        payment.setRegistration(registration);

        if (isAdd) {
            session.save(payment);
            createAndAddHistoryRecord(
                    messageSource.getMessage("key.paymentAdded",
                            new Object[] {payment.getId(), payment.getAmountPaid()},
                            null),
                    payment.getPreparedBy(),
                    registration, session);
        }
        else {
            session.update(payment);
            createAndAddHistoryRecord(
                    messageSource.getMessage("key.paymentUpdated",
                            new Object[] {payment.getId(), payment.getAmountPaid()},
                            null),
                    Util.getCurrentUser().getEmail(),
                    registration, session);
        }

        session.flush();
        session.close();

        updateTotalPayments(registration);

        updatePDCCount(registration);

    }

    private void updateTotalPayments (EventRegistration registration)
    {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(EventPayment.class);
        criteria.add(Restrictions.eq("registration", registration));
        criteria.setProjection(Projections.sum("amountPaid"));
        Long totalAmoutPaid = (Long) criteria.uniqueResult();

        if (totalAmoutPaid == null) {
            totalAmoutPaid = new Long(0);
        }

        registration.setTotalAmountPaid(totalAmoutPaid);
        registration.setAmountDue(registration.getAmountPayable() - registration.getTotalAmountPaid());

        session.update(registration);
        session.flush();
        session.close();
    }

    public Participant getParticipant (Integer participantId)
    {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Participant.class);
        criteria.setFetchMode("comments", FetchMode.EAGER);
        criteria.setFetchMode("seats", FetchMode.EAGER);
        criteria.add(Restrictions.eq("id", participantId));
        List results = criteria.list();
        Participant participant = null;

        if (results != null || !results.isEmpty()) {
            participant = (Participant) results.get(0);
        }
        session.close();
        return participant;
    }

    public EventRegistration getEventRegistration (Integer registrationId)
    {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(EventRegistration.class);
        criteria.setFetchMode("event", FetchMode.EAGER);
        criteria.setFetchMode("payments", FetchMode.EAGER);
        criteria.setFetchMode("seats", FetchMode.EAGER);
        criteria.setFetchMode("participant", FetchMode.EAGER);
        criteria.add(Restrictions.eq("id", registrationId));
        List results = criteria.list();
        EventRegistration registration = null;

        if (results != null || !results.isEmpty()) {
            registration = (EventRegistration) results.get(0);
        }
        registration.setHistoryRecords(
                getHistoryRecords(registration.getId(), registration.getType(), session));
        session.close();
        return registration;
    }

    public List<Participant> listParticipants (RegistrationCriteria registrationCriteria)
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<HistoryRecord> getHistoryRecords (Integer objectId, String objectType, Session session)
    {
        Criteria criteria = session.createCriteria(HistoryRecord.class);
        criteria.add(Restrictions.eq("objectId", objectId));
        criteria.add(Restrictions.eq("objectType", objectType));
        List<HistoryRecord> results = criteria.list();
        return results;
    }

    public List<EventRegistration> listRegistrations (RegistrationCriteria registrationCriteria)
    {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(EventRegistration.class);
        criteria.createAlias("event", "event");
        criteria.setFetchMode("payments", FetchMode.EAGER);
        criteria.createAlias("participant", "participant");
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        if (registrationCriteria.getSeat() != null) {
            criteria.createAlias("seats", "seats");
            criteria.add(Restrictions.eq("seats.seat", registrationCriteria.getSeat()));

            if (!Util.nullOrEmptyOrBlank(registrationCriteria.getLevel())) {
                criteria.add(Restrictions.eq("seats.level", registrationCriteria.getLevel()));
            }
        }
        else {
            if (!Util.nullOrEmptyOrBlank(registrationCriteria.getLevel())) {
                criteria.add(Restrictions.eq("level", registrationCriteria.getLevel()));
            }
        }

        if (!Util.nullOrEmptyOrBlank(registrationCriteria.getName())) {
            criteria.add(Restrictions.ilike("participant.name", registrationCriteria.getName(), MatchMode.ANYWHERE));
        }

        if (registrationCriteria.getEventId() != null) {
            criteria.add(Restrictions.eq("event.id", registrationCriteria.getEventId()));
        }

        if (!Util.nullOrEmptyOrBlank(registrationCriteria.getEmail())) {
            criteria.add(Restrictions.ilike("participant.email", registrationCriteria.getEmail(), MatchMode.ANYWHERE));
        }

        if (!Util.nullOrEmptyOrBlank(registrationCriteria.getFoundation())) {
            String foundation = registrationCriteria.getFoundation();
            if (Foundation.Others.getName().equalsIgnoreCase(foundation)) {
                if (!Util.nullOrEmptyOrBlank(registrationCriteria.getOtherFoundation())) {
                    foundation = registrationCriteria.getOtherFoundation();
                }
            }
            criteria.add(Restrictions.ilike("participant.foundation", foundation, MatchMode.ANYWHERE));
        }

        if (!Util.nullOrEmptyOrBlank(registrationCriteria.getMobile())) {
            criteria.add(Restrictions.like("participant.mobile", "%" + registrationCriteria.getMobile() + "%"));
        }

        if (registrationCriteria.isVip()) {
            criteria.add(Restrictions.eq("participant.vip", registrationCriteria.isVip()));
        }

        if (!Util.nullOrEmptyOrBlank(registrationCriteria.getReference())) {
            criteria.add(Restrictions.eq("reference", registrationCriteria.getReference()));
        }

        if (!Util.nullOrEmptyOrBlank(registrationCriteria.getAmountPaidCategory())) {
            String[] args = {"totalAmountPaid", "amountPayable"};
            String conditionTemplate =
                    AmountPaidCategory.getConditionTemplate(registrationCriteria.getAmountPaidCategory(), true);
            MessageFormat format = new MessageFormat(conditionTemplate);
            String sql = format.format(args);
            criteria.add(Restrictions.sqlRestriction(sql));
        }
        List<EventRegistration> results = criteria.list();

        session.close();
        return results;
    }

    private Integer getGreatestSeat (Event event, String level)
    {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(ParticipantSeat.class);
        criteria.setProjection(Projections.max("seat"));

        criteria.add(Restrictions.like("event", event));
        criteria.add(Restrictions.eq("level", level));

        List seats = criteria.list();
        session.close();
        if (!seats.isEmpty()) {
            return (Integer) seats.get(0);
        }
        else {
            return null;
        }
    }

    public List<ParticipantSeat> getAllSeats (Event event, String level)
    {
        Session session = sessionFactory.openSession();

        Criteria criteria = session.createCriteria(ParticipantSeat.class);
        criteria.add(Restrictions.eq("event", event));
        criteria.add(Restrictions.eq("level", level));
        criteria.addOrder(Order.asc("seat"));

        List<ParticipantSeat> seats = criteria.list();

        return seats;
    }

    public void processBatchEntry (List<RegisteredParticipant> participants)
    {
        for (RegisteredParticipant participant : participants)
            addParticipant(participant);
    }

    public void addReferenceGroup (ReferenceGroup referenceGroup)
    {
        sessionFactory.getCurrentSession().save(referenceGroup);
    }

    public ReferenceGroup getReferenceGroup (String name)
    {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(ReferenceGroup.class);

        criteria.add(Restrictions.eq("uniquename", name));
        List<ReferenceGroup> referenceGroups = criteria.list();

        session.close();
        if (referenceGroups == null ||
                referenceGroups.isEmpty()) {
            return null;
        }

        return referenceGroups.get(0);

    }

    public List<ReferenceGroup> listReferenceGroups ()
    {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(ReferenceGroup.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.addOrder(Order.asc("timeCreated"));
        List<ReferenceGroup> referenceGroups = criteria.list();
        session.close();
        return referenceGroups;
    }

    public List<EventPayment> listPayments (PaymentCriteria paymentCriteria)
    {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(EventPayment.class);
        criteria.createAlias("registration", "registration");
        criteria.createAlias("registration.participant", "participant");
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        if (paymentCriteria.getEventId() != null) {
            criteria.add(Restrictions.eq("registration.event.id", paymentCriteria.getEventId()));
        }

        if (!Util.nullOrEmptyOrBlank(paymentCriteria.getFoundation())) {
            String foundation = paymentCriteria.getFoundation();
            if (Foundation.Others.getName().equalsIgnoreCase(foundation)) {
                if (!Util.nullOrEmptyOrBlank(paymentCriteria.getOtherFoundation())) {
                    foundation = paymentCriteria.getOtherFoundation();
                }
            }
            criteria.add(Restrictions.ilike("participant.foundation", foundation, MatchMode.ANYWHERE));
        }

        if (!Util.nullOrEmptyOrBlank(paymentCriteria.getLevel())) {
            criteria.add(Restrictions.eq("registration.level", paymentCriteria.getLevel()));
        }

        if (!Util.nullOrEmptyOrBlank(paymentCriteria.getReference())) {
            criteria.add(Restrictions.eq("registration.reference", paymentCriteria.getReference()));
        }
        if (!Util.nullOrEmptyOrBlank(paymentCriteria.getMode())) {
            criteria.add(Restrictions.eq("mode", paymentCriteria.getMode()));
        }

        if (paymentCriteria.isPdcNotClear()) {
            criteria.add(Restrictions.eq("pdcNotClear", paymentCriteria.isPdcNotClear()));
        }

        if (paymentCriteria.getFromReceiptDate() != null) {
            criteria.add(Restrictions.ge("receiptDate", paymentCriteria.getFromReceiptDate()));
        }

        if (paymentCriteria.getToReceiptDate() != null) {
            criteria.add(Restrictions.le("receiptDate", paymentCriteria.getToReceiptDate()));
        }

        if (paymentCriteria.getFromPdcDate() != null) {
            criteria.add(Restrictions.ge("pdcDate", paymentCriteria.getFromPdcDate()));
        }

        if (paymentCriteria.getToPdcDate() != null) {
            criteria.add(Restrictions.le("pdcDate", paymentCriteria.getToPdcDate()));
        }

        List<EventPayment> results = criteria.list();

        session.close();
        return results;
    }

}