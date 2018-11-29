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
import com.example.vuphu.newlaundry.Main.ReceiptActivity;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.vuphu.newlaundry.Utils.StringKey.APPROVED;
import static com.example.vuphu.newlaundry.Utils.StringKey.DATE_SYMBOL;
import static com.example.vuphu.newlaundry.Utils.StringKey.ID_BRANCH;
import static com.example.vuphu.newlaundry.Utils.StringKey.ID_ORDER;
import static com.example.vuphu.newlaundry.Utils.StringKey.PENDING;
import static com.example.vuphu.newlaundry.Utils.StringKey.STATUS;
import static com.example.vuphu.newlaundry.Utils.Util.translateStatus;

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
        holder.status.setChipText(translateStatus(obOrderFragment.getStatus(), context) + " - " + obOrderFragment.getId());
        String strDate = Util.parseDate(obOrderFragment.getDate().substring(0, 10), "yyyy-MM-dd", "dd/MM/yyyy");
        holder.date.setText(strDate);
        holder.branchName.setText(obOrderFragment.getBranchName());
        holder.branchAdress.setText(obOrderFragment.getBranchAddress());
        holder.reciever.setText(obOrderFragment.getReciever());
        holder.view_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InfoOrderDetailActivity.class);
                intent.putExtra(ID_ORDER, obOrderFragment.getId());
                intent.putExtra(STATUS, obOrderFragment.getStatus());
                intent.putExtra(ID_BRANCH, obOrderFragment.getIdBranch());
                context.startActivity(intent);
            }
        });
        if(!obOrderFragment.getStatus().equals(PENDING)) {
            holder.view_receipt.setVisibility(View.VISIBLE);
            holder.view_receipt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ReceiptActivity.class);
                    intent.putExtra(ID_ORDER, obOrderFragment.getId());
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }
}
