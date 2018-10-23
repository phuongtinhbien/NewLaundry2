package com.example.vuphu.newlaundry.Service;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vuphu.newlaundry.R;
import com.robertlevonyan.views.chip.Chip;

public class ListServiceItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView item_service_image;
    public TextView item_service_name;
    public Chip item_service_chip;
    public ListServiceItemViewHolder(@NonNull View itemView) {
        super(itemView);
        item_service_image = itemView.findViewById(R.id.item_service);
        item_service_name = itemView.findViewById(R.id.item_service_name);
        item_service_chip = itemView.findViewById(R.id.item_service_chip);
    }
}
