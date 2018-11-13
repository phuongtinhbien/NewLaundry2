package com.example.vuphu.newlaundry.Main.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.vuphu.newlaundry.R;
import com.robertlevonyan.views.chip.Chip;

class OrderViewHolder extends RecyclerView.ViewHolder {
    Chip status;
    TextView date, branchAdress, reciever;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        status = itemView.findViewById(R.id.status_order);
        date = itemView.findViewById(R.id.item_order_date);
        branchAdress =  itemView.findViewById(R.id.item_order_branch_address);
        reciever = itemView.findViewById(R.id.item_order_reciever);
    }
}
