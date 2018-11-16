package com.gil.smsexporter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gil.smsexporter.Contacts.ContactsFragment;
import com.gil.smsexporter.Contacts.OnContactClickedListener;
import com.gil.smsexporter.SMS.OnSMSMessageClickedListener;
import com.gil.smsexporter.SMS.SMSMessage;
import com.gil.smsexporter.SMS.SMSMessagesFragment;
import com.gil.smsexporter.SMS.SMSProvider;
import com.gil.smsexporter.SMS.Serialization.JsonSMSSerializer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    final int SMS_PERMISSION_REQUEST_CODE = 1;
    final int STORAGE_PERMISSION_REQUEST_CODE = 2;

    private View fragmentsPlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        fragmentsPlaceholder = findViewById(R.id.contentPlaceholder);

        requirePermission(Manifest.permission.READ_SMS, SMS_PERMISSION_REQUEST_CODE, new Runnable() {
            @Override
            public void run() {
                SMSPermissionsGranted();
            }
        });
    }

    Set<String> getAllSMSSenders() {
        SMSProvider smsProvider = SMSProvider.all(this);
        Set<String> senders = new HashSet<>();

        while(smsProvider.hasNext()) {
            SMSMessage message = smsProvider.getNextMessage();
            senders.add(message.getSender());
        }
        return senders;
    }

    Fragment buildSenderMessagesFragment(String sender) {
        SMSProvider smsProvider = SMSProvider.from(this, sender);
        List<SMSMessage> messages = smsProvider.readAllMessages();

        return SMSMessagesFragment.instance(messages, null, new JsonSMSSerializer());
    }

    void SMSPermissionsGranted() {
        requirePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_REQUEST_CODE, new Runnable() {
            @Override
            public void run() {
                StoragePermissionsGranted();
            }
        });
    }

    void StoragePermissionsGranted() {
        startShowingMessages();
    }

    void startShowingMessages() {
        final ArrayList<String> contacts = new ArrayList<>(getAllSMSSenders());
        ContactsFragment contactsFragment = ContactsFragment.instance(contacts, new OnContactClickedListener() {
            @Override
            public void onContactClicked(String contact, int position) {
                Toast.makeText(MainActivity.this, contact, Toast.LENGTH_SHORT).show();
                Fragment messagesFragment = buildSenderMessagesFragment(contact);
                getSupportFragmentManager().beginTransaction()
                        .replace(fragmentsPlaceholder.getId(), messagesFragment)
                        .addToBackStack("Messages")
                        .commit();
                getSupportActionBar().setTitle(contact);
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(fragmentsPlaceholder.getId(), contactsFragment)
                .commit();
    }

    void requirePermission(final String permission, int requestCode, final Runnable onGranted) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
        } else {
            onGranted.run();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SMSPermissionsGranted();
            } else {
                Toast.makeText(this, "App must have SMS permissions", Toast.LENGTH_LONG).show();
                finish();
            }
        }


        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                StoragePermissionsGranted();
            } else {
                Toast.makeText(this, "App must have Storage permissions", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
