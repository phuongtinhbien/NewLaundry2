package com.example.vuphu.newlaundry.Order.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.newlaundry.Order.Activity.DetailPrepareOrderClothesActivity;
import com.example.vuphu.newlaundry.Order.IFOBPrepareOrder;
import com.example.vuphu.newlaundry.Order.OBOrderDetail;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.vuphu.newlaundry.Utils.StringKey.ITEM;
import static com.example.vuphu.newlaundry.Utils.StringKey.KG;

public class ListOrderDetailAdapter extends RecyclerView.Adapter<ListOrderDetailViewHolder> {

    IFOBPrepareOrder ifobPrepareOrder;
    List<OBOrderDetail> list;
//    List<OBOrderDetail> selectedList;
    Activity context;

    public ListOrderDetailAdapter(List<OBOrderDetail> list, Activity context, IFOBPrepareOrder ifobPrepareOrder) {
        this.list = list;
        this.context = context;
        this.ifobPrepareOrder = ifobPrepareOrder;
    }

    @NonNull
    @Override
    public ListOrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_cloth_mini,parent, false);
        return new ListOrderDetailViewHolder(v) ;
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ListOrderDetailViewHolder holder, final int position) {
        final OBOrderDetail obOrderDetail = list.get(position);
        holder.title.setText(obOrderDetail.getProduct().getTitle());
        String unit_name = "";
        if(!TextUtils.isEmpty(obOrderDetail.getUnitID())) {
            holder.img.setVisibility(View.GONE);
            if(obOrderDetail.getUnitID().equals(KG)) {
                unit_name = context.getResources().getString(R.string.kg);
            } else {
                unit_name = context.getResources().getString(R.string.item);
            }

            if(obOrderDetail.getUnitID().equals(ITEM)) {
                holder.count.setVisibility(View.VISIBLE);
                holder.count.setText(obOrderDetail.getCount() + " " + context.getResources().getString(R.string.item));
            }
            else if(obOrderDetail.getUnitID().equals(KG)) {
                holder.count.setVisibility(View.GONE);
            }
            holder.btnDel.setVisibility(View.VISIBLE);
            holder.btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ifobPrepareOrder.clickDel(position);
                }
            });
        } else {
            holder.img.setVisibility(View.VISIBLE);
            Picasso.get().load(Uri.parse(obOrderDetail.getProduct().getAvatar())).into(holder.img);
        }
        if(!TextUtils.isEmpty(unit_name)) {
            holder.serviceName.setChipText(obOrderDetail.getServiceName() + " - " + unit_name);
        } else {
            holder.serviceName.setChipText(obOrderDetail.getServiceName());
        }

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

    public void refreshAdapter(List<OBOrderDetail> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
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
