package com.gil.smsexporter.SMS.Serialization;

import com.gil.smsexporter.SMS.SMSMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class JsonSMSSerializer implements SMSSerializer {

    @Override
    public String serialize(SMSMessage message) {
        JSONObject jsonMessage = new JSONObject();
        try {
            jsonMessage.put("sender", message.getSender());
            jsonMessage.put("date", String.valueOf(message.getDate().getTime()));
            jsonMessage.put("body", message.getMessage());

            return jsonMessage.toString();
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public SMSMessage deserialize(String serializedMessage) {
        try {
            JSONObject jsonMessage = new JSONObject(serializedMessage);
            return new SMSMessage(
                    jsonMessage.getString("sender"),
                    jsonMessage.getString("body"),
                    Long.valueOf(jsonMessage.getString("date"))
            );
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String serializeList(List<SMSMessage> messagesList) {
        JSONArray jsonMessages = new JSONArray();
        for(SMSMessage message : messagesList) {
            JSONObject jsonMessage = new JSONObject();
            try {
                jsonMessage.put("sender", message.getSender());
                jsonMessage.put("date", message.getDate().getTime());
                jsonMessage.put("body", message.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            jsonMessages.put(jsonMessage);
        }

        JSONObject jsonResult = new JSONObject();
        try {
            jsonResult.put("messages", jsonMessages);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return jsonResult.toString();
    }

    @Override
    public List<SMSMessage> deserializeList(String serializedMessages) {
        // Knowingly not implemented yet
        return null;
    }
}
