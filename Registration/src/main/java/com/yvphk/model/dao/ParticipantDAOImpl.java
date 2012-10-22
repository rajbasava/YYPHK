/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
 
    Responsible: byummadisingh
*/
package com.yvphk.model.dao;

import com.yvphk.common.AmountPaidCategory;
import com.yvphk.common.Foundation;
import com.yvphk.common.Util;
import com.yvphk.model.form.BaseForm;
import com.yvphk.model.form.Event;
import com.yvphk.model.form.HistoryRecord;
import com.yvphk.model.form.EventPayment;
import com.yvphk.model.form.EventRegistration;
import com.yvphk.model.form.Participant;
import com.yvphk.model.form.ParticipantCriteria;
import com.yvphk.model.form.ParticipantSeat;
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
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Repository
public class ParticipantDAOImpl implements ParticipantDAO
{
    @Autowired
    private SessionFactory sessionFactory;

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
            registration.setParticipant(participant);
            session.save(registration);


            ParticipantSeat participantSeat = registeredParticipant.getCurrentSeat();
            if (participantSeat ==  null) {
                participantSeat = new ParticipantSeat();
            }
            if (participantSeat.getSeat() == null) {
                Integer greatestSeat = getGreatestSeat(event);   // todo seating should be changed per event and multi seating if the multi seat enabled based on the level
                Integer greatestSeatNo = new Integer(1);
                if (greatestSeat != null) {
                    greatestSeatNo = greatestSeat.intValue() + 1;
                }
                participantSeat.setSeat(greatestSeatNo);
            }
            participantSeat.setLevel(event.getEligibilityLevel());
            participantSeat.setEvent(event);
            participantSeat.setRegistration(registration);
            session.save(participantSeat);
        }
        else if (RegisteredParticipant.ActionUpdate.equals(registeredParticipant.getAction())) {
            registration.setTimeUpdated(new Date());
            //  todo update changes properties of registration objects to comments
            session.update(participant);
            registration.setParticipant(participant);
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
            addPayment(payment, registration.getId());
        }

        return registration;
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

    public void addPayment (EventPayment payment, Integer registrationId)
    {
        if (payment.getAmountPaid() == null || payment.getAmountPaid() == 0) {
            return;
        }

        Session session = sessionFactory.openSession();
        EventRegistration registration = (EventRegistration) session.load(EventRegistration.class, registrationId);

        payment.setRegistration(registration);
        session.save(payment);

        Long totalAmoutPaid = getTotalPayments(registration);
        if (totalAmoutPaid == null) {
            totalAmoutPaid = new Long(0);
        }

        registration.setTotalAmountPaid(totalAmoutPaid);
        registration.setAmountDue(registration.getAmountPayable() - registration.getTotalAmountPaid());

        session.save(registration);

        session.flush();
        session.close();
    }

    private Long getTotalPayments (EventRegistration registration)
    {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(EventPayment.class);
        criteria.add(Restrictions.eq("registration", registration));
        criteria.setProjection(Projections.sum("amountPaid"));
        Long totalAmount = (Long) criteria.uniqueResult();
        session.close();
        return  totalAmount;
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

    public List<Participant> listParticipants (ParticipantCriteria participantCriteria)
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

    public List<EventRegistration> listRegistrations (ParticipantCriteria participantCriteria)
    {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(EventRegistration.class);
        criteria.createAlias("event", "event");
        criteria.setFetchMode("payments", FetchMode.EAGER);
        criteria.createAlias("participant", "participant");
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        if (participantCriteria.getSeat() != null) {
            criteria.createAlias("seats", "seats");
            criteria.add(Restrictions.eq("seats.seat", participantCriteria.getSeat()));
        }

        if (!Util.nullOrEmptyOrBlank(participantCriteria.getName())) {
            criteria.add(Restrictions.ilike("participant.name", participantCriteria.getName(), MatchMode.ANYWHERE));
        }

        if (!Util.nullOrEmptyOrBlank(participantCriteria.getEmail())) {
            criteria.add(Restrictions.ilike("participant.email", participantCriteria.getEmail(), MatchMode.ANYWHERE));
        }

        if (!Util.nullOrEmptyOrBlank(participantCriteria.getFoundation())) {
            String foundation = participantCriteria.getFoundation();
            if (Foundation.Others.getName().equalsIgnoreCase(foundation)) {
                if (!Util.nullOrEmptyOrBlank(participantCriteria.getOtherFoundation())) {
                    foundation = participantCriteria.getOtherFoundation();
                }
            }
            criteria.add(Restrictions.ilike("participant.foundation", foundation, MatchMode.ANYWHERE));
        }

        if (!Util.nullOrEmptyOrBlank(participantCriteria.getMobile())) {
            criteria.add(Restrictions.like("participant.mobile", "%" + participantCriteria.getMobile() + "%"));
        }

        if (!Util.nullOrEmptyOrBlank(participantCriteria.getLevel())) {
            criteria.add(Restrictions.eq("level", participantCriteria.getLevel()));
        }

        if (!Util.nullOrEmptyOrBlank(participantCriteria.getReference())) {
            criteria.add(Restrictions.eq("reference", participantCriteria.getReference()));
        }

        if (!Util.nullOrEmptyOrBlank(participantCriteria.getAmountPaidCategory())) {
            String[] args = {"totalAmountPaid", "amountPayable"};
            String conditionTemplate =
                    AmountPaidCategory.getConditionTemplate(participantCriteria.getAmountPaidCategory(), true);
            MessageFormat format = new MessageFormat(conditionTemplate);
            String sql = format.format(args);
            criteria.add(Restrictions.sqlRestriction(sql));
        }
        List<EventRegistration> results = criteria.list();

        session.close();
        return results;
    }

    private Integer getGreatestSeat (String level)
    {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(ParticipantSeat.class);
        criteria.setProjection(Projections.max("seat"));

        if (!Util.nullOrEmptyOrBlank(level)) {
            criteria.add(Restrictions.like("level", level));
        }

        List seats = criteria.list();
        session.close();
        if (!seats.isEmpty()) {
            return (Integer) seats.get(0);
        }
        else {
            return null;
        }
    }

    private Integer getGreatestSeat (Event event)
    {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(ParticipantSeat.class);
        criteria.setProjection(Projections.max("seat"));

        criteria.add(Restrictions.like("event", event));

        List seats = criteria.list();
        session.close();
        if (!seats.isEmpty()) {
            return (Integer) seats.get(0);
        }
        else {
            return null;
        }
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

}