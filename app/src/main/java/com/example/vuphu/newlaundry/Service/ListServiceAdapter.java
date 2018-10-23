package com.example.vuphu.newlaundry.Service;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.newlaundry.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListServiceAdapter extends RecyclerView.Adapter<ListServiceItemViewHolder> {
    List<OBService> listService;
    Activity context;
    iFService iservice;

    public ListServiceAdapter(List<OBService> listService, Activity context, iFService iservice) {
        this.listService = listService;
        this.context = context;
        this.iservice = iservice;
    }

    public ListServiceAdapter() {
    }

    @NonNull
    @Override
    public ListServiceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_service_full, parent, false);
        return new ListServiceItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListServiceItemViewHolder holder, final int position) {
        final OBService obService = listService.get(position);
        Picasso.get().load(Uri.parse(obService.getIcon())).into(holder.item_service_image);
        holder.item_service_name.setText(obService.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iservice.itemClick(position);
            }
        });
        holder.item_service_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iservice.detail(obService.getDesc());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listService.size();
    }
}
