package com.example.vuphu.newlaundry.Main.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.newlaundry.Main.OBOrderFragment;
import com.example.vuphu.newlaundry.R;

import java.util.ArrayList;

public class AdapterListOrder extends RecyclerView.Adapter<OrderViewHolder> {
    private Context context;
    private ArrayList<OBOrderFragment> listOrder;

    public AdapterListOrder(Context context, ArrayList<OBOrderFragment> listOrder) {
        this.context = context;
        this.listOrder = listOrder;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_your_order,parent, false);
        return new OrderViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OBOrderFragment obOrderFragment = listOrder.get(position);
        holder.status.setChipText(obOrderFragment.getStatus());
        String strDate = obOrderFragment.getDate().substring(8, 10) + "/" + obOrderFragment.getDate().substring(5, 7) + "/" + obOrderFragment.getDate().substring(0, 4);
        holder.date.setText(strDate);
        holder.branchAdress.setText(obOrderFragment.getBranchAddress());
        holder.reciever.setText(obOrderFragment.getReciever());
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }
}
