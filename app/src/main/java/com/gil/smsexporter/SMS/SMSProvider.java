package com.gil.smsexporter.SMS;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SMSProvider {

    private String fromSenderFilter;
    private Cursor cursor = null;
    private Context context;
    private boolean hasMoreToRead;

    public static SMSProvider all(Context context) {
        return new SMSProvider(context, "%");
    }

    public static SMSProvider from(Context context, String sender) {
        return new SMSProvider(context, sender);
    }

    private SMSProvider(Context context, String sender) {
        this.context = context;
        this.fromSenderFilter = sender;
    }

    private void initCursor() {
        cursor = context.getContentResolver().query(
                Uri.parse("content://sms/inbox"),
                new String[]{ "body" , "address", "date" },
                "address LIKE ?",
                new String[] {
                        fromSenderFilter
                },
                null,
                null
        );
        hasMoreToRead = cursor.moveToFirst();
    }

    private void lazyInit() {
        if(cursor == null) initCursor();
    }

    public boolean hasNext() {
        lazyInit();
        return hasMoreToRead;
    }

    public SMSMessage getNextMessage() {
        lazyInit();

        // Read SMS data
        String body = cursor.getString(cursor.getColumnIndex("body"));
        String sender = cursor.getString(cursor.getColumnIndex("address"));
        String date = cursor.getString(cursor.getColumnIndex("date"));

        // Advance cursor
        hasMoreToRead = cursor.moveToNext();

        // Return SMSMessage representation of the SMS data
        return new SMSMessage(
                sender,
                body,
                new Date(Long.valueOf(date))
        );
    }

    public List<SMSMessage> readAllMessages() {
        List<SMSMessage> result = new ArrayList<>();
        while(hasNext()) {
            result.add(
                    getNextMessage()
            );
        }
        return result;
    }
}
