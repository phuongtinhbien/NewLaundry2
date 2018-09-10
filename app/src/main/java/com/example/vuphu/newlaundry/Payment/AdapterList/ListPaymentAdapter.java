package com.example.vuphu.newlaundry.Payment.AdapterList;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.vuphu.newlaundry.Payment.OBPayment;
import com.example.vuphu.newlaundry.R;

import java.util.List;

public class ListPaymentAdapter extends RecyclerView.Adapter<ListPaymentViewHolder> {
    List<OBPayment> listPayment;
    Activity context;

    public ListPaymentAdapter(Activity applicationContext, List<OBPayment> listItem) {
        this.context = applicationContext;
        this.listPayment = listItem;
    }

    @NonNull
    @Override
    public ListPaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_choose,parent, false);
        return new ListPaymentViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ListPaymentViewHolder holder, int position) {

        holder.payment.setText(listPayment.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return listPayment.size();
    }
}
