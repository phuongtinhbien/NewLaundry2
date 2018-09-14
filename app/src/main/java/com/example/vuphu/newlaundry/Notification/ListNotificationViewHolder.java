package com.example.vuphu.newlaundry.Notification;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.vuphu.newlaundry.R;

public class ListNotificationViewHolder extends RecyclerView.ViewHolder {

    TextView content;
    TextView time;
    public ListNotificationViewHolder(@NonNull View itemView) {
        super(itemView);

        content = itemView.findViewById(R.id.item_notify_content_short);
        time = itemView.findViewById(R.id.item_notify_time);
        itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                content.setTextColor(R.color.cool_grey);
                time.setTextColor(R.color.cool_grey);
            }
        });
    }
}
