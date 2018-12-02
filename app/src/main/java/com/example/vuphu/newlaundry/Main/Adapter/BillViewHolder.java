package com.example.vuphu.newlaundry.Main.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.vuphu.newlaundry.R;

public class BillViewHolder extends RecyclerView.ViewHolder {
    TextView txt_stt, txt_id_service, txt_clothes, txt_DVT, txt_Unit_price, txt_amount_received, txt_amount_delivery, txt_total_temp;
    public BillViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_stt = itemView.findViewById(R.id.txt_stt);
        txt_id_service = itemView.findViewById(R.id.txt_id_service);
        txt_clothes = itemView.findViewById(R.id.txt_clothes);
        txt_DVT = itemView.findViewById(R.id.txt_DVT);
        txt_Unit_price = itemView.findViewById(R.id.txt_Unit_price);
        txt_amount_received = itemView.findViewById(R.id.txt_amount_received);
        txt_amount_delivery = itemView.findViewById(R.id.txt_amount_delivery);
        txt_total_temp = itemView.findViewById(R.id. txt_total_temp);
    }
}
