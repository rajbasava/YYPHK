/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/
package com.yvphk.model.dao;

import com.yvphk.common.AmountPaidCategory;
import com.yvphk.common.ParticipantLevel;
import com.yvphk.common.Util;
import com.yvphk.model.form.BaseForm;
import com.yvphk.model.form.Event;
import com.yvphk.model.form.HistoryRecord;
import com.yvphk.model.form.EventPayment;
import com.yvphk.model.form.EventRegistration;
import com.yvphk.model.form.Participant;
import com.yvphk.model.form.ParticipantCriteria;
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
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
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
public class ParticipantDAOImpl extends CommonDAOImpl implements ParticipantDAO
{
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ReloadableResourceBundleMessageSource messageSource;

    public Participant addParticipant (Participant participant)
    {
        Session session = sessionFactory.openSession();
        //todo check the uniqueness of the participant before adding.
        session.save(participant);
        createAndAddHistoryRecord(
                messageSource.getMessage("key.participantAdded",
                        new Object[] {participant.getId(),
                                participant.getName()},
                        null),
                Util.getCurrentUser().getEmail(),
                participant,
                session);
        session.flush();
        session.close();
        return participant;
    }

    public EventRegistration registerParticipant (RegisteredParticipant registeredParticipant)
    {
        Participant participant = registeredParticipant.getParticipant();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        EventRegistration registration = registeredParticipant.getRegistration();

        if (RegisteredParticipant.ActionRegister.equals(registeredParticipant.getAction())) {

            String level = registeredParticipant.getRegistration().getLevel();
            Event event = registeredParticipant.getRegistration().getEvent();
            if (Util.nullOrEmptyOrBlank(level) || event == null) {
                return null;
            }
            //todo check the uniqueness of the participant before adding.
            addParticipant(participant);
            registration.setParticipant(participant);
            registration.setStatus(EventRegistration.StatusRegistered);
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
            if (participantSeat !=  null) {
                session.save(participantSeat);
            }
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

        if (payments.isEmpty()) {
            updateTotalPayments(registration);
            updatePDCCount(registration);
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
        Long totalAmountPaid = (Long) criteria.uniqueResult();

        if (totalAmountPaid == null) {
            totalAmountPaid = new Long(0);
        }

        registration.setTotalAmountPaid(totalAmountPaid);
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

    public void cancelRegistration (EventRegistration registration)
    {
        Session session = sessionFactory.openSession();
        registration.setStatus(EventRegistration.StatusCancelled);
        session.update(registration);
        createAndAddHistoryRecord(
                messageSource.getMessage("key.registrationCancelled",
                        new Object[] {registration.getId(),
                                registration.getParticipant().getName()},
                        null),
                Util.getCurrentUser().getEmail(),
                registration, session);
        session.flush();
        session.close();
    }

    public void onHoldRegistration (EventRegistration registration)
    {
        Session session = sessionFactory.openSession();
        registration.setStatus(EventRegistration.StatusOnHold);
        session.update(registration);
        createAndAddHistoryRecord(
                messageSource.getMessage("key.registrationCancelled",
                        new Object[] {registration.getId(),
                                registration.getParticipant().getName()},
                        null),
                Util.getCurrentUser().getEmail(),
                registration, session);
        session.flush();
        session.close();
    }

    public void changeToRegistered (EventRegistration registration)
    {
        Session session = sessionFactory.openSession();
        registration.setStatus(EventRegistration.StatusRegistered);
        session.update(registration);
        createAndAddHistoryRecord(
                messageSource.getMessage("key.registrationUpdated",
                        new Object[] {registration.getId(),
                                registration.getParticipant().getName(),
                                    registration.getEvent().getName()},
                        null),
                Util.getCurrentUser().getEmail(),
                registration, session);
        session.flush();
        session.close();
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

    public List<Participant> listParticipants (ParticipantCriteria participantCriteria)
    {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Participant.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);


        if (!Util.nullOrEmptyOrBlank(participantCriteria.getName())) {
            criteria.add(Restrictions.ilike("name", participantCriteria.getName(), MatchMode.ANYWHERE));
        }

        if (!Util.nullOrEmptyOrBlank(participantCriteria.getEmail())) {
            criteria.add(Restrictions.ilike("email", participantCriteria.getEmail(), MatchMode.ANYWHERE));
        }

        if (!Util.nullOrEmptyOrBlank(participantCriteria.getFoundation())) {
            String foundation = participantCriteria.getFoundation();
            criteria.add(Restrictions.ilike("foundation", foundation, MatchMode.ANYWHERE));
        }

        if (!Util.nullOrEmptyOrBlank(participantCriteria.getMobile())) {
            criteria.add(Restrictions.like("mobile", "%" + participantCriteria.getMobile() + "%"));
        }

        if (participantCriteria.isVip()) {
            criteria.add(Restrictions.eq("vip", participantCriteria.isVip()));
        }

        List<Participant> results = criteria.list();

        session.close();
        return results;
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
        criteria.add(Restrictions.eq("active", true));

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

        if (!Util.nullOrEmptyOrBlank(registrationCriteria.getFoodCoupon())) {
            criteria.add(Restrictions.eq("foodCoupon", Boolean.valueOf(registrationCriteria.getFoodCoupon()).booleanValue()));
        }

        if (!Util.nullOrEmptyOrBlank(registrationCriteria.getEventKit())) {
            criteria.add(Restrictions.eq("eventKit", Boolean.valueOf(registrationCriteria.getEventKit()).booleanValue()));
        }

        if (registrationCriteria.getFromRegistrationDate() != null) {
            criteria.add(Restrictions.ge("registrationDate", registrationCriteria.getFromRegistrationDate()));
        }

        if (registrationCriteria.getToRegistrationDate() != null) {
            criteria.add(Restrictions.le("registrationDate", registrationCriteria.getToRegistrationDate()));
        }

        if (!Util.nullOrEmptyOrBlank(registrationCriteria.getStatus())) {
            criteria.add(Restrictions.eq("status", registrationCriteria.getStatus()));
        }

        List<EventRegistration> results = criteria.list();

        session.close();
        return results;
    }

    public List<EventRegistration> allUnallocatedRegistrations (Event event, boolean vip, boolean indian)
    {
        if (event == null) {
            return null;
        }

        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(EventRegistration.class);
        criteria.createAlias("event", "event");
        criteria.createAlias("participant", "participant");
        criteria.createAlias("seats", "seats", CriteriaSpecification.LEFT_JOIN);

        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.addOrder(Order.asc("registrationDate"));

        criteria.add(Restrictions.eq("event.id", event.getId()));
        criteria.add(Restrictions.isNull("seats.seat"));
        criteria.add(Restrictions.eq("participant.vip", vip));

        if (indian) {
            Criterion indCond = Restrictions.eq("participant.country", "India");
            Criterion nullCond = Restrictions.isNull("participant.country");
            criteria.add(Restrictions.or(indCond, nullCond));
        }
        else {
            Criterion forgnCond = Restrictions.ne("participant.country", "India");
            Criterion notNullCond = Restrictions.isNotNull("participant.country");
            criteria.add(Restrictions.or(forgnCond, notNullCond));
        }

        criteria.add(Restrictions.eq("status", EventRegistration.StatusRegistered));
        criteria.add(Restrictions.eq("active", true));

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
            criteria.add(Restrictions.eq("registration.event.active", true));
        }

        if (!Util.nullOrEmptyOrBlank(paymentCriteria.getFoundation())) {
            String foundation = paymentCriteria.getFoundation();
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

    public void replaceParticipant (EventRegistration registration, Participant participantToReplace)
    {
        Session session = sessionFactory.openSession();
        session.refresh(registration);
        Participant oldParticipant = registration.getParticipant();
        registration.setParticipant(participantToReplace);
        createAndAddHistoryRecord(
                messageSource.getMessage("key.registrationReplace",
                        new Object[] {registration.getId(),
                                registration.getEvent().getName(),
                                participantToReplace.getName(),
                                oldParticipant.getName()},
                        null),
                Util.getCurrentUser().getEmail(),
                registration,
                session);
        session.update(registration);
        session.flush();
        session.close();
    }

    @Override
    public void removeEventRegistrations(Integer id) {
        Session session = sessionFactory.openSession();
        Event event = (Event) session.load(Event.class, id);
        Criteria criteria = session.createCriteria(EventRegistration.class);
        criteria.add(Restrictions.eq("event", event));
        List<EventRegistration> results = criteria.list();
        for (EventRegistration reg: results) {
            reg.setActive(false);
            session.update(reg);
        }
        session.flush();
        session.close();
    }

    public List<ParticipantSeat> getAllocatedSeats (Event event, String alpha, String suffix)
    {
        if (event == null) {
            return null;
        }

        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(ParticipantSeat.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.addOrder(Order.asc("seat"));

        criteria.add(Restrictions.eq("event", event));

        if (!Util.nullOrEmptyOrBlank(alpha)) {
            criteria.add(Restrictions.eq("alpha", alpha));
        }

        if (!Util.nullOrEmptyOrBlank(suffix)) {
            criteria.add(Restrictions.eq("suffix", suffix));
        }

        List<ParticipantSeat> results = criteria.list();
        session.flush();
        session.close();
        return results;
    }

}