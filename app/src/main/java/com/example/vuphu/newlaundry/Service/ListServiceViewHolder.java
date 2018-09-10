package com.example.vuphu.newlaundry.Service;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.vuphu.newlaundry.R;

public class ListServiceViewHolder extends RecyclerView.ViewHolder{

    public ImageView icon;
    public TextView  name;
    public TextView  desc;
    public ListServiceViewHolder(View itemView) {
        super(itemView);
        icon = itemView.findViewById(R.id.item_service_icon);
        name = itemView.findViewById(R.id.item_service_txt_title);
        desc = itemView.findViewById(R.id.item_service_txt_sub_title);
    }
}
