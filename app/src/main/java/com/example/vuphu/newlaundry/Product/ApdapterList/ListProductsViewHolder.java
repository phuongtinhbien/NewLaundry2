package com.example.vuphu.newlaundry.Product.ApdapterList;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ListProductsViewHolder extends ViewHolder {


    ImageView imgCategory;
    TextView titleCategory;
    TextView priceCateotry;
    TextView count;
    EditText production;
    EditText material;
    EditText color;
    EditText note;
    public ListProductsViewHolder(View itemView) {
        super(itemView);
    }
}
