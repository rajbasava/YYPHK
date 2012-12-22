/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.
*/

package com.yvphk.model.dao;

import com.yvphk.model.form.Event;
import com.yvphk.model.form.EventFee;
import com.yvphk.model.form.RowMeta;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
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
public class EventDAOImpl extends CommonDAOImpl implements EventDAO
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
        criteria.add(Restrictions.eq("active", true));
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
        criteria.add(Restrictions.eq("active", true));
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

        event.setActive(false);
        session.update(event);
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
        criteria.add(Restrictions.eq("active", true));
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
        criteria.add(Restrictions.eq("active", true));
        EventFee fee = (EventFee) criteria.uniqueResult();

        session.flush();
        session.close();

        return fee;

    }

    private void removeEventFee (Integer eventFeeId, Session session)
    {
        removeEventFee(eventFeeId, session, true);
    }

    private void removeEventFee (Integer eventFeeId, Session session, boolean isSoftDelete)
    {
        if (eventFeeId == null) {
            return;
        }

        EventFee fee = (EventFee) session.load(EventFee.class, eventFeeId);

        if (fee == null) {
            return;
        }

        if (isSoftDelete) {
            fee.setActive(false);
            session.update(fee);
        }
        else {
            session.delete(fee);
        }
    }

    public RowMeta getFirstEmptyRowMeta (Event event)
    {
        Session session = sessionFactory.openSession();

        Criteria criteria = session.createCriteria(RowMeta.class);
        criteria.add(Restrictions.eq("name", event.getRowMetaName()));
        criteria.add(Restrictions.eq("rowFull", false));
        criteria.addOrder(Order.asc("sortOrder"));
        criteria.setMaxResults(1);
        RowMeta rowMeta = (RowMeta) criteria.uniqueResult();

        session.flush();
        session.close();

        return rowMeta;
    }

    public List<RowMeta> getAllEmptyRowMetas (Event event)
    {
        Session session = sessionFactory.openSession();

        Criteria criteria = session.createCriteria(RowMeta.class);
        criteria.add(Restrictions.eq("name", event.getRowMetaName()));
        criteria.add(Restrictions.eq("rowFull", false));
        criteria.addOrder(Order.asc("sortOrder"));
        List<RowMeta> rowMetas = criteria.list();

        session.flush();
        session.close();

        return rowMetas;
    }

    public List<String> getAllRowMetaNames ()
    {
        Session session = sessionFactory.openSession();

        Query query = session.createSQLQuery("select distinct name from phk_rowmeta where active = :active");
        query.setParameter("active","1");
        List<String> rowMetaNames = query.list();

        session.flush();
        session.close();

        return rowMetaNames;
    }
}
