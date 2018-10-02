package com.example.vuphu.newlaundry.Order.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.newlaundry.Order.Activity.DetailPrepareOrderClothesActivity;
import com.example.vuphu.newlaundry.Order.OBOrderDetail;
import com.example.vuphu.newlaundry.R;

import java.util.ArrayList;
import java.util.List;

public class ListOrderDetailAdapter extends RecyclerView.Adapter<ListOrderDetailViewHolder> {


    private static String CURRENCY = "VND";
    List<OBOrderDetail> list;
    List<OBOrderDetail> selectedList;
    Activity context;

    public ListOrderDetailAdapter(Activity applicationContext, List<OBOrderDetail> listItem) {
        this.context = applicationContext;
        this.list = listItem;
        this.selectedList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ListOrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_cloth_mini,parent, false);
        return new ListOrderDetailViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ListOrderDetailViewHolder holder, int position) {

        holder.title.setText(list.get(position).getTitle());
        holder.price.setText(CURRENCY + " " + list.get(position).getPricing());
        Log.i("data", list.get(position).getCount()+"");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, DetailPrepareOrderClothesActivity.class));
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
}
