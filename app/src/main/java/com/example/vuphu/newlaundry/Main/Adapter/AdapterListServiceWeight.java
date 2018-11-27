package com.example.vuphu.newlaundry.Main.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.newlaundry.Main.OBService_Weight;
import com.example.vuphu.newlaundry.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.example.vuphu.newlaundry.Utils.StringKey.UNDEFINE;

public class AdapterListServiceWeight extends RecyclerView.Adapter<Service_weight_ViewHolder> {
    private ArrayList<OBService_Weight> list;
    private Context context;
    private String CURRENCY = " VND";
    private DecimalFormat dec;

    public AdapterListServiceWeight(ArrayList<OBService_Weight> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Service_weight_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_service_weight,parent, false);
        return new Service_weight_ViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull Service_weight_ViewHolder holder, int position) {
        dec = new DecimalFormat("###,###,###");
        OBService_Weight obService_weight = list.get(position);
        holder.service_name.setText(obService_weight.getServiceName());
        double priceT = obService_weight.getPrice()*obService_weight.getWeight();
        if(priceT == 0) {
            holder.price.setText(UNDEFINE + CURRENCY);
        } else {
            holder.price.setText(dec.format(priceT) + CURRENCY);
        }
        if(obService_weight.getWeight() == 0) {
            holder.weight.setText(UNDEFINE);
        }
        else {
            holder.weight.setText(Double.toString(obService_weight.getWeight()));
        }
    }

    public double sumPrice() {
        double sum = 0;
        for (OBService_Weight obService_weight : list) {
            sum += obService_weight.getPrice()*obService_weight.getWeight();
        }
        return sum;
    }

    public double sumWeight() {
        double sum = 0;
        for (OBService_Weight obService_weight : list) {
            sum += obService_weight.getWeight();
        }
        return sum;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
