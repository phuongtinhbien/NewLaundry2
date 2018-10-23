package com.example.vuphu.newlaundry.Order.Adapter;

import android.support.design.button.MaterialButton;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.vuphu.newlaundry.Order.OBOrderDetail;
import com.example.vuphu.newlaundry.R;
import com.github.florent37.androidslidr.Slidr;
import com.robertlevonyan.views.chip.Chip;

public class ListOrderDetailViewHolder extends ViewHolder {
    ImageView img;
    TextView title, count;
    MaterialButton btnDel;

    public ListOrderDetailViewHolder(View itemView) {
        super(itemView);
        img = itemView.findViewById(R.id.img_product);
        title = itemView.findViewById(R.id.item_prepare_order_txt_title);
        btnDel = itemView.findViewById(R.id.btnDel);
        count = itemView.findViewById(R.id.order_count);
    }




}
