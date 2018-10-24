package com.example.vuphu.newlaundry.Order.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;

import java.util.ArrayList;

public class BagActivity extends AppCompatActivity implements IFOBPrepareOrder {
    private static final int REQUEST_CODE = 7;
    private Toolbar toolbar;
    private Button checkOut;
    private RecyclerView listChooseClothes;
    private ListOrderDetailAdapter adapter;
    private ArrayList<OBOrderDetail> list;
    private TextView countTotal;
    private int position;

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
                  if(list.size() > 0){
                      startActivity(new Intent(getApplicationContext(), PrepareOrderAddressActivity.class));
                  }
                  else {
                      Popup popup = new Popup(BagActivity.this);
                      popup.createFailDialog("Bag is empty", "ORDER", new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              onBackPressed();
                              finish();
                          }
                      });
                      popup.show();
                  }

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
        if(obOrderDetail != null){
            position = list.indexOf(obOrderDetail);
            Intent intent = new Intent(BagActivity.this, DetailPrepareOrderClothesActivity.class);
            intent.putExtra("OBOrderDetail", obOrderDetail);
            intent.putExtra("Edit", true);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            OBOrderDetail obOrderDetailResult = (OBOrderDetail) data.getSerializableExtra("OBOrderDetailResult");
            Log.i("Bag", obOrderDetailResult.getCount() + "");
            list.set(position, obOrderDetailResult);
            PreferenceUtil.setListOrderDetail(list, this);
            adapter.notifyDataSetChanged();
        }
    }

    public boolean checkDuplicateClothes(String str1, String str2) {
        if(str1 != null && str2 != null) {
            if(str1.equals(str2)) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            if(str1 == null && str2 == null) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    @Override
    public void clickDel(int position) {
        list.remove(position);
        adapter.notifyDataSetChanged();
        PreferenceUtil.setListOrderDetail(list, BagActivity.this);
        countTotal.setText(adapter.sumCount() + " item");
    }
}
