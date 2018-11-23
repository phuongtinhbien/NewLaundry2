package com.example.vuphu.newlaundry.Order.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.newlaundry.Order.IFOBPrepareOrder;
import com.example.vuphu.newlaundry.Order.OBOrderDetail;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.vuphu.newlaundry.Utils.StringKey.ITEM;
import static com.example.vuphu.newlaundry.Utils.StringKey.KG;

public class ListClothesAdapter extends RecyclerView.Adapter<ListClothesViewHolder> {


    private static String CURRENCY = "VND";
    List<OBOrderDetail> list;
    Activity context;
    private DecimalFormat dec;
    IFOBPrepareOrder ifobPrepareOrder;

    public ListClothesAdapter(Activity applicationContext, List<OBOrderDetail> listItem, IFOBPrepareOrder ifobPrepareOrder) {
        this.context = applicationContext;
        this.list = listItem;
        this.ifobPrepareOrder = ifobPrepareOrder;
    }

    @NonNull
    @Override
    public ListClothesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_cloth,parent, false);
        return new ListClothesViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ListClothesViewHolder holder, int position) {
        dec = new DecimalFormat("##,###,###,###");
        OBOrderDetail obOrderDetail = list.get(position);
        holder.title.setText(obOrderDetail.getProduct().getTitle());
        String unit_name = "";
        if(obOrderDetail.getUnitID().equals(KG)) {
            unit_name = context.getResources().getString(R.string.kg);
            holder.price.setText(dec.format(obOrderDetail.getPrice()) + " " + CURRENCY + "/" + unit_name);
            holder.count.setVisibility(View.GONE);
            holder.badge.setVisibility(View.GONE);
        } else if(obOrderDetail.getUnitID().equals(ITEM)) {
            holder.count.setText(Long.toString(obOrderDetail.getCount()));
            holder.price.setText(dec.format(obOrderDetail.getPrice()*obOrderDetail.getCount()) + " " + CURRENCY);
            unit_name = context.getResources().getString(R.string.item);
        }
        holder.serviceName.setChipText(obOrderDetail.getServiceName() + "-" + unit_name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifobPrepareOrder.clickClothes(obOrderDetail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public long sumCount() {
        long count = 0;
        for (OBOrderDetail obOrderDetail: list){
            if(obOrderDetail.getUnitID().equals(ITEM)) {
                count += obOrderDetail.getCount();
            }
        }
        return count;
    }
    public long sumPrice() {
        boolean flag = false;
        long sum = 0;
        for (OBOrderDetail obOrderDetail: list) {
            if(obOrderDetail.getUnitID().equals(ITEM)){
                sum += obOrderDetail.getPrice()*obOrderDetail.getCount();
            } else {
                flag = true;
                break;
            }
        }
        if(flag) {
            sum = 0;
        }
        return sum;
    }
}
