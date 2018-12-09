package com.example.vuphu.newlaundry.Main.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.newlaundry.Main.OBBill;
import com.example.vuphu.newlaundry.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.vuphu.newlaundry.Utils.StringKey.ITEM;
import static com.example.vuphu.newlaundry.Utils.StringKey.KG;
import static com.example.vuphu.newlaundry.Utils.Util.formatDecimal;

public class AdapterBill extends RecyclerView.Adapter<BillViewHolder> {
    private Context context;
    private ArrayList<OBBill> list;

    public AdapterBill(Context context, ArrayList<OBBill> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        OBBill obBill = list.get(position);
        holder.txt_stt.setText(Integer.toString(position + 1));
        holder.txt_id_service.setText(obBill.getServicename());
        holder.txt_clothes.setText(obBill.getNameClothes());
        holder.txt_DVT.setText(obBill.getUnitName());
        holder.txt_Unit_price.setText(Double.toString(obBill.getUnitprice()));
        if(obBill.getUnitId().equals(ITEM)) {
            holder.txt_amount_received.setText(Long.toString(obBill.getAmountReceived()));
            holder.txt_amount_delivery.setText(Long.toString(obBill.getAmount()));
            holder.txt_total_temp.setText(formatDecimal(obBill.getAmount()*obBill.getUnitprice()));
        }
        else if(obBill.getUnitId().equals(KG)){
            holder.txt_amount_received.setText(Double.toString(obBill.getWeightReceived()));
            holder.txt_amount_delivery.setText(Double.toString(obBill.getWeight()));
            holder.txt_total_temp.setText(formatDecimal(obBill.getWeight()*obBill.getUnitprice()));
        }
    }

    public double totalPrice() {
        ArrayList<String> service = new ArrayList<>();
        double total = 0;
        for(OBBill obBill : list) {
            if(obBill.getUnitId().equals(KG)) {
                if(!service.contains(obBill.getIdService())) {
                    total += obBill.getWeight()*obBill.getUnitprice();
                }
            }
            else {
                total += obBill.getAmount()*obBill.getUnitprice();
            }
        }
        return total;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
