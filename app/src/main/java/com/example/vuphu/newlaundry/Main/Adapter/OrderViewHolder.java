package com.example.vuphu.newlaundry.Main.Adapter;

import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vuphu.newlaundry.R;
import com.robertlevonyan.views.chip.Chip;

class OrderViewHolder extends RecyclerView.ViewHolder {
    Chip status;
    TextView date, branchAdress, branchName,reciever;
    Button view_order, view_receipt, view_bill, confirm_receiver;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        view_order = itemView.findViewById(R.id.view_orderdetail);
        view_receipt = itemView.findViewById(R.id.view_receipt);
        status = itemView.findViewById(R.id.status_order);
        date = itemView.findViewById(R.id.item_order_date);
        branchName = itemView.findViewById(R.id.item_order_branch_name);
        branchAdress =  itemView.findViewById(R.id.item_order_branch_address);
        reciever = itemView.findViewById(R.id.item_order_reciever);
        view_bill = itemView.findViewById(R.id.view_bill);
        confirm_receiver = itemView.findViewById(R.id.confirm_receiver);
    }
}
