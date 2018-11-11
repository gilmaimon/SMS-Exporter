package com.gil.smsexporter.SMS;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.gil.smsexporter.R;
import com.gil.smsexporter.SMS.Serialization.SMSSerializer;
import com.gil.smsexporter.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SMSMessagesFragment extends Fragment {

    private List<SMSMessage> mMessages = new ArrayList<>();
    private OnSMSMessageClickedListener mListener = null;
    private SMSSerializer mSerializer = null;

    public static SMSMessagesFragment instance(
            @NonNull List<SMSMessage> messages,
            final OnSMSMessageClickedListener listener,
            @NonNull SMSSerializer serializer) {
        SMSMessagesFragment fragment = new SMSMessagesFragment();

        fragment.setMessages(messages);
        fragment.setListener(listener);
        fragment.setSerializer(serializer);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.recycler_view_layout, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView(
                (RecyclerView) getView().findViewById(R.id.recycler_view)
        );
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sms_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_export) {
            String serializedMessages = mSerializer.serializeList(mMessages);
            try {
                String path = Utils.SaveFileToDocsDirectory(
                        "SMSExport_" + mMessages.get(0).getSender() + "_" + System.currentTimeMillis(),
                        serializedMessages
                );

                Toast.makeText(getActivity(), "Exported SMS Messages To:\n" + path, Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error Exporting", Toast.LENGTH_SHORT).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        RecyclerView.Adapter mAdapter = new SMSMessagesRecyclerViewAdapter(getActivity(), mMessages, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mListener == null) return;
                mListener.onSMSMessageClicked(mMessages.get(i), i);
            }
        });

        recyclerView.setAdapter(mAdapter);
    }

    public List<SMSMessage> getMessages() {
        return mMessages;
    }

    public void setMessages(List<SMSMessage> mMessages) {
        this.mMessages = mMessages;
    }

    public void setListener(OnSMSMessageClickedListener mListener) {
        this.mListener = mListener;
    }

    public void removeListener(OnSMSMessageClickedListener listener) {
        this.mListener = null;
    }

    public void setSerializer(SMSSerializer mSerializer) {
        this.mSerializer = mSerializer;
    }
}
