package com.gil.smsexporter.Contacts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.gil.smsexporter.R;

import java.util.List;

class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ContactViewHolder> {

    private Context context;
    private List<String> data;
    private AdapterView.OnItemClickListener listener;

    ContactsRecyclerViewAdapter(Context context, List<String> data, AdapterView.OnItemClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewGroup vg;
        public ContactViewHolder(ViewGroup vg, TextView v) {
            super(vg);
            textView = v;
            this.vg = vg;
        }
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ViewGroup vg = (ViewGroup) LayoutInflater.from(context)
                .inflate(R.layout.contact_item, parent, false);

        TextView tv = (TextView) vg.findViewById(R.id.textView);

        ContactViewHolder vh = new ContactViewHolder(
                vg,
                tv
        );
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder contactViewHolder, final int position) {
        contactViewHolder.textView.setText(
                data.get(position)
        );

        contactViewHolder.vg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener == null) return;
                listener.onItemClick(null, view, position, view.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}