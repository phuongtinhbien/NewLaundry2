package com.example.vuphu.newlaundry.Notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.vuphu.newlaundry.R;

import java.util.List;

public class ListNotificationAdapter extends RecyclerView.Adapter<ListNotificationViewHolder> {

    Context context;
    List<OBNotification> listNotification;

    public ListNotificationAdapter(Context context, List<OBNotification> listNotification) {
        this.context = context;
        this.listNotification = listNotification;
    }

    @NonNull
    @Override
    public ListNotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_notification,parent, false);
        return new ListNotificationViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ListNotificationViewHolder holder, int position) {
        holder.content.setText(listNotification.get(position).getContent());
        holder.time.setText(listNotification.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return listNotification.size();
    }
}
