package com.example.vuphu.newlaundry.Main.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.vuphu.newlaundry.R;

public class Service_weight_ViewHolder extends RecyclerView.ViewHolder {
    TextView service_name, weight, price;
    FrameLayout badge;
    public Service_weight_ViewHolder(@NonNull View itemView) {
        super(itemView);
        service_name = itemView.findViewById(R.id.item_service_txt_title);
        weight = itemView.findViewById(R.id.item_prepare_order_count);
        badge = itemView.findViewById(R.id.badge);
        price = itemView.findViewById(R.id.item_service_txt_price);
    }
}
