package com.example.vuphu.newlaundry.Service;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.newlaundry.R;

import java.util.List;

public class ListServiceChooseAdapter extends RecyclerView.Adapter<ListServiceChooseViewHolder> {


    List<OBService> listService;
    Activity context;

    public ListServiceChooseAdapter(Activity applicationContext, List<OBService> listItem) {
        this.context = applicationContext;
        this.listService = listItem;
    }

    @NonNull
    @Override
    public ListServiceChooseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_choose, parent, false);
        return new ListServiceChooseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListServiceChooseViewHolder holder, int position) {
        holder.name.setText(listService.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return listService.size();
    }
}
