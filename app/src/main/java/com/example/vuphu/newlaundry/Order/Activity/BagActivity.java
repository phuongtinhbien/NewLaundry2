package com.example.vuphu.newlaundry.Order.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vuphu.newlaundry.Order.Adapter.ListOrderDetailAdapter;
import com.example.vuphu.newlaundry.Order.IFOBPrepareOrder;
import com.example.vuphu.newlaundry.Order.OBOrder;
import com.example.vuphu.newlaundry.Order.OBOrderDetail;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;

import java.util.ArrayList;

public class BagActivity extends AppCompatActivity implements IFOBPrepareOrder {

    private Toolbar toolbar;
    private Button checkOut;
    private RecyclerView listChooseClothes;
    private ListOrderDetailAdapter adapter;
    private ArrayList<OBOrderDetail> list;
    private TextView countTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);
        initToolbar();
        init();
    }

    private void init() {
        countTotal = findViewById(R.id.item_prepare_order_total_items);
        listChooseClothes = findViewById(R.id.list_choose_cloth);
        list = PreferenceUtil.getListOrderDetail(BagActivity.this);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listChooseClothes.setLayoutManager(gridLayoutManager);
        adapter = new ListOrderDetailAdapter(list, BagActivity.this, this);
        listChooseClothes.setAdapter(adapter);
        countTotal.setText(adapter.sumCount() + " item");
        checkOut = findViewById(R.id.see_your_bag);
        checkOut.setText("Check out");
        checkOut.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  startActivity(new Intent(getApplicationContext(), PrepareOrderAddressActivity.class));
              }
          }

        );
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Your bag");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_clear);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void clickClothes(OBOrderDetail obOrderDetail) {

    }

    @Override
    public void clickDel(int position) {
        list.remove(position);
        adapter.notifyDataSetChanged();
        PreferenceUtil.setListOrderDetail(list, BagActivity.this);
        countTotal.setText(adapter.sumCount() + " item");
    }
}
