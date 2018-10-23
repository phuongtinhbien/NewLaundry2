package com.example.vuphu.newlaundry.Order.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.newlaundry.Categories.OBCategory;
import com.example.vuphu.newlaundry.Categories.iFCategory;
import com.example.vuphu.newlaundry.ChipViewHolder.ChipViewHolder;
import com.example.vuphu.newlaundry.R;
import com.robertlevonyan.views.chip.OnSelectClickListener;

import java.util.ArrayList;
import java.util.List;

public class ListChipAdapter extends RecyclerView.Adapter<ChipViewHolder> {

    List<OBCategory> list;
    Context context;
    iFCategory iFCategory;

    public  ListChipAdapter(List<OBCategory> list, Context context, iFCategory iFCategory) {
        this.list = list;
        this.context = context;
        this.iFCategory = iFCategory;
    }

    public ListChipAdapter() {
    }

    @NonNull
    @Override
    public ChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_filter_chip,parent, false);
        return new ChipViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ChipViewHolder holder, int position) {
        final OBCategory obCategory = list.get(position);
        holder.chip.setChipText(obCategory.getName());
        holder.chip.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onSelectClick(View v, boolean selected) {
                if(selected == true) {
                    iFCategory.categoryClick(obCategory.getId());
                }
                else {
                    iFCategory.categoryUnclick();
                }

                // TODO: Xu li chon nhieu item ...

            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
