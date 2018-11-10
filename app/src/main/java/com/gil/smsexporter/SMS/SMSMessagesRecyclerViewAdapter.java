package com.gil.smsexporter.SMS;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.gil.smsexporter.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

class SMSMessagesRecyclerViewAdapter extends RecyclerView.Adapter<SMSMessagesRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<SMSMessage> data;
    private AdapterView.OnItemClickListener listener;

    SMSMessagesRecyclerViewAdapter(Context context, List<SMSMessage> data, AdapterView.OnItemClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateTv, contentTv;
        ViewGroup vg;
        MyViewHolder(ViewGroup vg, TextView date, TextView content) {
            super(vg);
            dateTv = date;
            contentTv = content;
            this.vg = vg;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ViewGroup vg = (ViewGroup) LayoutInflater.from(context)
                .inflate(R.layout.sms_message_item, parent, false);

        TextView contentTv = (TextView) vg.findViewById(R.id.contentTextView);
        TextView dateTv = (TextView) vg.findViewById(R.id.dateTextView);

        return new MyViewHolder(vg, dateTv, contentTv);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        myViewHolder.contentTv.setText(
                data.get(position).getMessage()
        );

        DateFormat format = SimpleDateFormat.getDateInstance();

        myViewHolder.dateTv.setText(
                format.format(data.get(position).getDate())
        );

        myViewHolder.vg.setOnClickListener(new View.OnClickListener() {
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