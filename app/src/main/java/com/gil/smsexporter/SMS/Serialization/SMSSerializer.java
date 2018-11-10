package com.gil.smsexporter.SMS.Serialization;

import com.gil.smsexporter.SMS.SMSMessage;

import java.util.List;

public interface SMSSerializer {
    String serialize(SMSMessage message);
    SMSMessage deserialize(String serializedMessage);

    String serializeList(List<SMSMessage> messagesList);
    List<SMSMessage> deserializeList(String serializedMessages);
}
