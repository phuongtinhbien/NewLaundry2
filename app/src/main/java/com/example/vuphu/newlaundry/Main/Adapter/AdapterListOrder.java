package com.example.vuphu.newlaundry.Main.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.newlaundry.Main.InfoOrderDetailActivity;
import com.example.vuphu.newlaundry.Main.OBOrderFragment;
import com.example.vuphu.newlaundry.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.vuphu.newlaundry.Utils.StringKey.DATE_SYMBOL;
import static com.example.vuphu.newlaundry.Utils.StringKey.ID_ORDER;
import static com.example.vuphu.newlaundry.Utils.StringKey.STATUS;

public class AdapterListOrder extends RecyclerView.Adapter<OrderViewHolder> {
    private Context context;
    private ArrayList<OBOrderFragment> listOrder;

    public AdapterListOrder(Context context, ArrayList<OBOrderFragment> listOrder) {
        this.context = context;
        this.listOrder = listOrder;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_your_order,parent, false);
        return new OrderViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OBOrderFragment obOrderFragment = listOrder.get(position);
        holder.status.setChipText(obOrderFragment.getStatus());
        String strDate = obOrderFragment.getDate().substring(8, 10) + DATE_SYMBOL + obOrderFragment.getDate().substring(5, 7) + DATE_SYMBOL + obOrderFragment.getDate().substring(0, 4);
        holder.date.setText(strDate);
        holder.branchName.setText(obOrderFragment.getBranchName());
        holder.branchAdress.setText(obOrderFragment.getBranchAddress());
        holder.reciever.setText(obOrderFragment.getReciever());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InfoOrderDetailActivity.class);
                intent.putExtra(ID_ORDER, obOrderFragment.getId());
                intent.putExtra(STATUS, obOrderFragment.getStatus());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }
}
