package com.example.vuphu.newlaundry.Product.ApdapterList;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.vuphu.newlaundry.Product.OBProduct;
import com.example.vuphu.newlaundry.R;

import java.util.List;

public class ListProductsAdapter extends RecyclerView.Adapter<ListProductsViewHolder> {


    List<OBProduct> listService;
    Activity context;

    public ListProductsAdapter(Activity applicationContext, List<OBProduct> listItem) {
        this.context = applicationContext;
        this.listService = listItem;
    }

    @NonNull
    @Override
    public ListProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_service,parent, false);
        return new ListProductsViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ListProductsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listService.size();
    }
}
