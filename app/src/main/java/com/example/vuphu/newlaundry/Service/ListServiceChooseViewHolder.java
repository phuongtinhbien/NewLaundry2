package com.example.vuphu.newlaundry.Service;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.vuphu.newlaundry.R;
import com.uniquestudio.library.CircleCheckBox;

public class ListServiceChooseViewHolder extends RecyclerView.ViewHolder{


    public CircleCheckBox checkBox;
    public TextView name;
    public ListServiceChooseViewHolder(View itemView) {
        super(itemView);
        checkBox = itemView.findViewById(R.id.item_choose_check_box);
        name = itemView.findViewById(R.id.item_choose_txt_title);
    }
}
