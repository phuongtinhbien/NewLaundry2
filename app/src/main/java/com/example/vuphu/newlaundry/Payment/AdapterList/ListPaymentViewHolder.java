package com.example.vuphu.newlaundry.Payment.AdapterList;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.vuphu.newlaundry.R;
import com.uniquestudio.library.CircleCheckBox;

public class ListPaymentViewHolder extends RecyclerView.ViewHolder {

    public CircleCheckBox checkBox;
    public TextView payment;
    public ListPaymentViewHolder(@NonNull View itemView) {
        super(itemView);
        checkBox = itemView.findViewById(R.id.item_choose_check_box);
        payment = itemView.findViewById(R.id.item_choose_txt_title);

    }
}
