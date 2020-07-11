package com.goodcompany.salamport.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String text;
    private long from;
    private long to;
    private Date sendTime;

    public Message(String text, long from, long to, Date sendTime) {
        this.text = text;
        this.from = from;
        this.to = to;
        this.sendTime = sendTime;
    }

    public String getText() {
        return text;
    }

    public long getFrom() {
        return from;
    }

    public long getTo() {
        return to;
    }

    public Date getSendTime() {
        return sendTime;
    }
}
