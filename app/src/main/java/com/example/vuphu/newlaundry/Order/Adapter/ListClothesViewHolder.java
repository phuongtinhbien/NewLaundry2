package com.example.vuphu.newlaundry.Order.Adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.vuphu.newlaundry.Order.OBOrderDetail;
import com.example.vuphu.newlaundry.R;
import com.robertlevonyan.views.chip.Chip;

public class ListClothesViewHolder extends ViewHolder {
    TextView title;
    TextView price;
    Chip serviceName;
    TextView count;
    //Number Picker
    FrameLayout badge;

  /*  EditText value;
    Button   decrement, increment;*/
    OBOrderDetail detail;
    public ListClothesViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.item_prepare_order_txt_title);
        price = itemView.findViewById(R.id.item_prepare_order_txt_price);
        count = itemView.findViewById(R.id.item_prepare_order_count);
        badge = itemView.findViewById(R.id.badge);
        serviceName = itemView.findViewById(R.id.chip_service_name_final);
    }

}
