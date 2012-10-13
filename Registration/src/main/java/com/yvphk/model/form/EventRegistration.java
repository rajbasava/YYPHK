/*
    Copyright (c) 2012-2015 Yoga Vidya Pranic Healing Foundation of Karnataka.
    All rights reserved. Patents pending.

    Responsible: byummadisingh
*/

package com.yvphk.model.form;

import com.yvphk.common.ParticipantLevel;
import com.yvphk.common.Util;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "PHK_EVENTREGSTRN")
public class EventRegistration extends BaseForm
{
    public static final String ClassName = "com.yvphk.model.form.EventRegistration";

    public static final String[] ImportFields =
            new String[] {"AmountPayable","Review","Level","Reference","Application","Certificates","Comments"};

    public static final String[] ReportFields =
            new String[] {"Event","AmountPayable","TotalAmountPaid","AmountDue","Review","Level","Reference","Application","Certificates","FoodCoupon","EventKit"};

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Integer id;

    @Column(name = "AMOUNTPAYABLE")
    private Long amountPayable;

    @Column(name = "TOTALAMOUNTPAID")
    private Long totalAmountPaid;

    @Column(name = "AMOUNTDUE")
    private Long amountDue;

    @Column(name = "REVIEW")
    private boolean review;

    @Column(name = "FOODCOUPON")
    private boolean foodCoupon;

    @Column(name = "EVENTKIT")
    private boolean eventKit;

    @Column(name = "APPLICATION")
    private boolean application;

    @Column(name = "CERTIFICATES")
    private boolean certificates;

    @Column(name = "LEVEL")
    private String level;

    @Column(name = "REFERENCE")
    private String reference;

    @Column(name = "PREPAREDBY")
    private String preparedBy;

    @Column(name = "TIMECREATED")
    private Date timeCreated;

    @Column(name = "TIMEUPDATED")
    private Date timeUpdated;

    @Column(name = "ACTIVE")
    private boolean active;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "EVENTID")
    private Event event;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "PARTICIPANTID")
    private Participant participant;

    @OneToMany(mappedBy = "registration", fetch = FetchType.EAGER)
    private Set<EventPayment> payments;

    @Transient
    private List<HistoryRecord> historyRecords;

    @OneToMany(mappedBy = "registration", fetch = FetchType.EAGER)
    private Set<ParticipantSeat> seats;


    public Integer getId ()
    {
        return id;
    }

    public void setId (Integer id)
    {
        this.id = id;
    }

    public String getType ()
    {
        return ClassName;
    }

    public Long getAmountPayable ()
    {
        return amountPayable;
    }

    public void setAmountPayable (Long amountPayable)
    {
        this.amountPayable = amountPayable;
    }

    public Long getTotalAmountPaid ()
    {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid (Long totalAmountPaid)
    {
        this.totalAmountPaid = totalAmountPaid;
    }

    public Long getAmountDue()
    {
        return amountDue;
    }

    public void setAmountDue(Long amountDue)
    {
        this.amountDue = amountDue;
    }

    public boolean isReview ()
    {
        return review;
    }

    public void setReview (boolean review)
    {
        this.review = review;
    }

    public boolean isFoodCoupon ()
    {
        return foodCoupon;
    }

    public void setFoodCoupon (boolean foodCoupon)
    {
        this.foodCoupon = foodCoupon;
    }

    public boolean isEventKit ()
    {
        return eventKit;
    }

    public void setEventKit (boolean eventKit)
    {
        this.eventKit = eventKit;
    }

    public boolean isApplication ()
    {
        return application;
    }

    public void setApplication (boolean application)
    {
        this.application = application;
    }

    public boolean isCertificates ()
    {
        return certificates;
    }

    public void setCertificates (boolean certificates)
    {
        this.certificates = certificates;
    }

    public String getLevel ()
    {
        return level;
    }

    public void setLevel (String level)
    {
        this.level = level;
    }

    public String getLevelName()
    {
        if (Util.nullOrEmptyOrBlank(getLevel())) {
            return null;
        }

        return ParticipantLevel.getName(getLevel());
    }

    public String getPreparedBy ()
    {
        return preparedBy;
    }

    public void setPreparedBy (String preparedBy)
    {
        this.preparedBy = preparedBy;
    }

    public Date getTimeCreated ()
    {
        return timeCreated;
    }

    public void setTimeCreated (Date timeCreated)
    {
        this.timeCreated = timeCreated;
    }

    public Date getTimeUpdated ()
    {
        return timeUpdated;
    }

    public void setTimeUpdated (Date timeUpdated)
    {
        this.timeUpdated = timeUpdated;
    }

    public boolean isActive ()
    {
        return active;
    }

    public void setActive (boolean active)
    {
        this.active = active;
    }

    public Event getEvent ()
    {
        return event;
    }

    public void setEvent (Event event)
    {
        this.event = event;
    }

    public Participant getParticipant ()
    {
        return participant;
    }

    public void setParticipant (Participant participant)
    {
        this.participant = participant;
    }

    public Set<EventPayment> getPayments ()
    {
        return payments;
    }

    public void setPayments (Set<EventPayment> payments)
    {
        this.payments = payments;
    }

    public List<HistoryRecord> getHistoryRecords ()
    {
        return historyRecords;
    }

    public void setHistoryRecords (List<HistoryRecord> historyRecords)
    {
        this.historyRecords = historyRecords;
    }

    public Set<ParticipantSeat> getSeats ()
    {
        return seats;
    }

    public void setSeats (Set<ParticipantSeat> seats)
    {
        this.seats = seats;
    }

    public String getReference ()
    {
        return reference;
    }

    public void setReference (String reference)
    {
        this.reference = reference;
    }
}
