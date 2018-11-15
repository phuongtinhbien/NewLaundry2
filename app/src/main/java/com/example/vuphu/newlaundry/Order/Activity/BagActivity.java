package com.example.vuphu.newlaundry.Order.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vuphu.newlaundry.GetCustomerQuery;
import com.example.vuphu.newlaundry.Order.Adapter.ListOrderDetailAdapter;
import com.example.vuphu.newlaundry.Order.IFOBPrepareOrder;
import com.example.vuphu.newlaundry.Order.OBOrder;
import com.example.vuphu.newlaundry.Order.OBOrderDetail;
import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.vuphu.newlaundry.Utils.StringKey.ITEM;
import static com.example.vuphu.newlaundry.Utils.StringKey.KG;
import static com.example.vuphu.newlaundry.Utils.StringKey.LATITUDE;
import static com.example.vuphu.newlaundry.Utils.StringKey.LONGITUDE;
import static com.example.vuphu.newlaundry.Utils.StringKey.TOTAL_PRICE;
import static com.example.vuphu.newlaundry.Utils.StringKey.TOTAL_WEIGHT;

public class BagActivity extends AppCompatActivity implements IFOBPrepareOrder {
    private static final int REQUEST_CODE = 7;
    private Toolbar toolbar;
    private Button checkOut;
    private RecyclerView listChooseClothes;
    private ListOrderDetailAdapter adapter;
    private ArrayList<OBOrderDetail> list;
    private TextView countTotal, totalPrice, totalWeight;
    private int position;
    private DecimalFormat dec;
    private static GetCustomerQuery.CustomerById customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);
        initToolbar();
        init();
    }

    private void init() {
        customer = PreferenceUtil.getCurrentUser(getApplicationContext());
        dec = new DecimalFormat("##,###,###,###");
        countTotal = findViewById(R.id.item_prepare_order_total_items);
        totalPrice = findViewById(R.id.item_prepare_order_total);
        totalWeight = findViewById(R.id.item_prepare_order_total_weight);
        listChooseClothes = findViewById(R.id.list_choose_cloth);
        list = PreferenceUtil.getListOrderDetail(BagActivity.this);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listChooseClothes.setLayoutManager(gridLayoutManager);
        adapter = new ListOrderDetailAdapter(list, BagActivity.this, this);
        listChooseClothes.setAdapter(adapter);
        countTotal.setText(adapter.sumCount() + " " + getResources().getString(R.string.item));
        totalPrice.setText(dec.format(adapter.sumPrice()) + " VND");
        totalWeight.setText(adapter.sumWeight() + " " + getResources().getString(R.string.kg));
        checkOut = findViewById(R.id.see_your_bag);
        checkOut.setText(R.string.checkout);
        checkOut.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if(list.size() > 0){
                      convertAddressToLocation();
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

    private void convertAddressToLocation() {
        Geocoder coder = new Geocoder(BagActivity.this, Locale.getDefault());
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(customer.address(), 5);

            Address location = address.get(0);

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        if(p1 != null) {
            Intent intent = new Intent(getApplicationContext(), PrepareOrderAddressActivity.class);
            if(!TextUtils.isEmpty(totalPrice.getText())) {
                intent.putExtra(TOTAL_PRICE, totalPrice.getText());
                intent.putExtra(LATITUDE, p1.latitude);
                intent.putExtra(LONGITUDE, p1.longitude);
                startActivity(intent);
            }
        }
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.your_bag);
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
        if(list.get(position).getUnitID().equals(KG)) {
            boolean isDeleteWeight = true;
            for(int i=0; i<list.size(); i++) {
                if(i != position) {
                    if(list.get(i).getUnitID().equals(KG) && list.get(i).getIdService().equals(list.get(position).getIdService())) {
                        isDeleteWeight = false;
                        break;
                    }
                }
            }
            if(isDeleteWeight) {
                PreferenceUtil.removeKey(BagActivity.this, list.get(position).getIdService());
            }
        }
        list.remove(position);
        adapter.notifyDataSetChanged();
        PreferenceUtil.setListOrderDetail(list, BagActivity.this);
        countTotal.setText(adapter.sumCount() + " " + getResources().getString(R.string.item));
        totalPrice.setText(dec.format(adapter.sumPrice()) + " VND");
        totalWeight.setText(adapter.sumWeight() + " " + getResources().getString(R.string.kg));
    }
}
