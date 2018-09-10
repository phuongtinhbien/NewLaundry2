package com.example.vuphu.newlaundry.ChipViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.example.vuphu.newlaundry.R;
import com.robertlevonyan.views.chip.Chip;

public class ChipViewHolder extends RecyclerView.ViewHolder{

    public Chip chip;
    public ChipViewHolder(@NonNull View itemView) {
        super(itemView);
        chip = itemView.findViewById(R.id.chip);
    }
}
