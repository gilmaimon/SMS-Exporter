package com.gil.smsexporter.SMS;

import java.util.Date;

public class SMSMessage {

    private String sender;
    private String message;
    private Date date;

    SMSMessage(String sender, String message, Date date) {
        this.sender = sender;
        this.message = message;
        this.date = date;
    }

    public SMSMessage(String sender, String message, long date) {
        this(sender, message, new Date(date));
    }


    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}
