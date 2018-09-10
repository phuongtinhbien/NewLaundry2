package com.example.vuphu.newlaundry.Service;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.vuphu.newlaundry.Order.Activity.PrepareOrderActivity;
import com.example.vuphu.newlaundry.R;

import java.util.List;

public class ListServiceAdapter extends RecyclerView.Adapter<ListServiceViewHolder> {


    List<OBService> listService;
    Activity context;

    public ListServiceAdapter(Activity applicationContext, List<OBService> listItem) {
        this.context = applicationContext;
        this.listService = listItem;
    }

    @NonNull
    @Override
    public ListServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_service,parent, false);
        return new ListServiceViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ListServiceViewHolder holder, int position) {
        holder.name.setText(listService.get(position).getName());
        holder.desc.setText(listService.get(position).getDesc());
        holder.icon.setImageResource(Integer.parseInt(listService.get(position).getIcon()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PrepareOrderActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listService.size();
    }
}
