package com.example.vuphu.newlaundry.Order.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.newlaundry.Order.OBOrderDetail;
import com.example.vuphu.newlaundry.R;

import java.util.List;

public class ListClothesAdapter extends RecyclerView.Adapter<ListClothesViewHolder> {


    private static String CURRENCY = "VND";
    List<OBOrderDetail> list;
    Activity context;

    public ListClothesAdapter(Activity applicationContext, List<OBOrderDetail> listItem) {
        this.context = applicationContext;
        this.list = listItem;
    }

    @NonNull
    @Override
    public ListClothesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_cloth,parent, false);
        return new ListClothesViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ListClothesViewHolder holder, int position) {
        OBOrderDetail obOrderDetail = list.get(position);
        holder.title.setText(obOrderDetail.getProduct().getTitle());
        holder.price.setText(CURRENCY + " " + obOrderDetail.getProduct().getPricing());
        holder.serviceName.setChipText(obOrderDetail.getServiceName());
        holder.count.setText(Long.toString(obOrderDetail.getCount()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public long sumCount() {
        long count = 0;
        for (OBOrderDetail obOrderDetail: list){
            count += obOrderDetail.getCount();
        }
        return count;
    }
}
