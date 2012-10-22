/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.

    Responsible: byummadisingh
*/

package com.yvphk.model.dao;

import com.yvphk.model.form.Event;
import com.yvphk.model.form.EventFee;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Repository
public class EventDAOImpl implements EventDAO
{
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addEvent (Event event)
    {
        sessionFactory.getCurrentSession().save(event);
    }

    @Override
    public Event getEvent (Integer eventId)
    {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Event.class);
        criteria.setFetchMode("fees", FetchMode.EAGER);

        criteria.add(Restrictions.eq("id", eventId));
        List<Event> events = criteria.list();

        session.close();
        if (events == null ||
                events.isEmpty()) {
            return null;
        }

        return events.get(0);
    }

    @Override
    public List<Event> allEvents ()
    {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Event.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.addOrder(Order.asc("timeCreated"));
        List<Event> events = criteria.list();
        session.close();
        return events;
    }

    @Override
    public void removeEvent (Integer id)
    {
        if (id == null) {
            return;
        }

        Session session = sessionFactory.openSession();

        Event event = (Event) session.load(Event.class, id);

        if (event == null) {
            return;
        }

        Set<EventFee> fees = event.getFees();

        for (Iterator<EventFee> iterator = fees.iterator(); iterator.hasNext(); ) {
            EventFee fee = iterator.next();
            removeEventFee((Integer) fee.getId(), session);
        }

        session.delete(event);
        session.flush();
        session.close();

    }

    @Override
    public void addFee (EventFee fee, Integer eventId)
    {
        if (eventId == null) {
            return;
        }
        Session session = sessionFactory.openSession();

        Event event = (Event) session.load(Event.class, eventId);

        if (event == null) {
            return;
        }

        fee.setEvent(event);
        session.save(fee);

        session.close();
    }

    @Override
    public List<EventFee> getEventFees (Integer eventId)
    {
        if (eventId == null) {
            return null;
        }

        Event event = (Event) sessionFactory.getCurrentSession().load(
                Event.class, eventId);

        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(EventFee.class);
        criteria.add(Restrictions.eq("event", event));
        criteria.addOrder(Order.asc("timeCreated"));
        List<EventFee> eventFees = criteria.list();
        session.close();

        return eventFees;
    }

    @Override
    public void removeEventFee (Integer eventFeeId)
    {
        Session session = sessionFactory.openSession();

        removeEventFee(eventFeeId, session);
        session.flush();
        session.close();
    }

    @Override
    public EventFee getEventFee (Integer eventFeeId)
    {
        Session session = sessionFactory.openSession();

        Criteria criteria = session.createCriteria(EventFee.class);
        criteria.add(Restrictions.eq("id", eventFeeId));
        EventFee fee = (EventFee) criteria.uniqueResult();

        session.flush();
        session.close();

        return fee;

    }

    private void removeEventFee (Integer eventFeeId, Session session)
    {
        if (eventFeeId == null) {
            return;
        }

        EventFee fee = (EventFee) session.load(EventFee.class, eventFeeId);

        if (fee == null) {
            return;
        }

        session.delete(fee);
    }
}
