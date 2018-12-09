package com.example.vuphu.newlaundry.Order.Activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.GetCustomerQuery;
import com.example.vuphu.newlaundry.GetListUnitPriceMutation;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Main.MainActivity;
import com.example.vuphu.newlaundry.Order.Adapter.ListOrderDetailAdapter;
import com.example.vuphu.newlaundry.Order.IFOBPrepareOrder;
import com.example.vuphu.newlaundry.Order.OBOrderDetail;
import com.example.vuphu.newlaundry.Order.OBPrice;
import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.example.vuphu.newlaundry.type.UnitPriceInput;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.vuphu.newlaundry.Utils.StringKey.EDIT;
import static com.example.vuphu.newlaundry.Utils.StringKey.ITEM;
import static com.example.vuphu.newlaundry.Utils.StringKey.KG;
import static com.example.vuphu.newlaundry.Utils.StringKey.LATITUDE;
import static com.example.vuphu.newlaundry.Utils.StringKey.LONGITUDE;
import static com.example.vuphu.newlaundry.Utils.StringKey.OB_ORDERDETAIL;
import static com.example.vuphu.newlaundry.Utils.StringKey.OB_UNIT_PRICE_ITEM;
import static com.example.vuphu.newlaundry.Utils.StringKey.OB_UNIT_PRICE_KG;
import static com.example.vuphu.newlaundry.Utils.StringKey.TOTAL_PRICE;
import static com.example.vuphu.newlaundry.Utils.Util.checkDuplicateClothes;

public class BagActivity extends AppCompatActivity implements IFOBPrepareOrder {
    private static final int REQUEST_CODE = 7;
    private Toolbar toolbar;
    private Button checkOut;
    private RecyclerView listChooseClothes;
    private ListOrderDetailAdapter adapter;
    private ArrayList<OBOrderDetail> list;
    private TextView countTotal, totalPrice, no_sum;
    private String token;
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
        token = PreferenceUtil.getAuthToken(BagActivity.this);
        dec = new DecimalFormat("##,###,###,###");
        countTotal = findViewById(R.id.item_prepare_order_total_items);
        totalPrice = findViewById(R.id.item_prepare_order_total);
        no_sum = findViewById(R.id.no_sum);
        listChooseClothes = findViewById(R.id.list_choose_cloth);
        list = PreferenceUtil.getListOrderDetail(BagActivity.this);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listChooseClothes.setLayoutManager(gridLayoutManager);
        adapter = new ListOrderDetailAdapter(list, BagActivity.this, this);
        listChooseClothes.setAdapter(adapter);
        if(adapter.sumPrice() != 0) {
            no_sum.setVisibility(View.GONE);
            totalPrice.setText(dec.format(adapter.sumPrice()) + " VND");
        } else {
            if(adapter.getItemCount() > 0) {
                no_sum.setVisibility(View.VISIBLE);
            }
            else {
                no_sum.setVisibility(View.GONE);
            }
            totalPrice.setText(getResources().getString(R.string.total_price));
        }
        countTotal.setText(adapter.sumCount() + " " + getResources().getString(R.string.item));
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
                      popup.createFailDialog(getResources().getString(R.string.bag_empty), getResources().getString(R.string.order), new View.OnClickListener() {
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
                finish();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            case R.id.menu_other_service: {
               startActivity(new Intent(BagActivity.this, MainActivity.class));
               finish();
            }
            default: return false;
        }
    }

    @Override
    public void clickClothes(OBOrderDetail obOrderDetail) {
        if(obOrderDetail != null){
            position = list.indexOf(obOrderDetail);
            List<UnitPriceInput> list = new ArrayList<>();
            if(obOrderDetail.getUnitID().equals(ITEM)) {
                UnitPriceInput unitPriceInputItem = UnitPriceInput.builder()
                        .productId(null)
                        .unitId(KG)
                        .serviceTypeId(obOrderDetail.getIdService())
                        .build();
                list.add(unitPriceInputItem);
            }
            else if(obOrderDetail.getUnitID().equals(KG)) {
                UnitPriceInput unitPriceInputKG = UnitPriceInput.builder()
                        .productId(obOrderDetail.getProduct().getId())
                        .unitId(ITEM)
                        .serviceTypeId(obOrderDetail.getIdService())
                        .build();
                list.add(unitPriceInputKG);
            }

            GraphqlClient.getApolloClient(token, false).mutate(GetListUnitPriceMutation.builder()
                    .list(list)
                    .build())
                    .enqueue(new ApolloCall.Callback<GetListUnitPriceMutation.Data>() {
                  @Override
                  public void onResponse(@NotNull Response<GetListUnitPriceMutation.Data> response) {
                      List<GetListUnitPriceMutation.UnitPrice> unitPrices = response.data().getlistproductprice().unitPrices();
                      if(unitPrices.size() > 0) {
                          if(obOrderDetail.getUnitID().equals(ITEM)) {
                              OBPrice obPriceKg = new OBPrice(unitPrices.get(0).id(), unitPrices.get(0).price());
                              OBPrice obPriceItem = new OBPrice(obOrderDetail.getPriceID(), obOrderDetail.getPrice());
                              Intent intent = new Intent(BagActivity.this, DetailPrepareOrderClothesActivity.class);
                              intent.putExtra(OB_ORDERDETAIL, obOrderDetail);
                              intent.putExtra(EDIT, true);
                              intent.putExtra(OB_UNIT_PRICE_ITEM, obPriceItem);
                              intent.putExtra(OB_UNIT_PRICE_KG, obPriceKg);
                              startActivityForResult(intent, REQUEST_CODE);
                          } else if(obOrderDetail.getUnitID().equals(KG)) {
                              OBPrice obPriceItem = new OBPrice(unitPrices.get(0).id(), unitPrices.get(0).price());
                              OBPrice obPriceKg = new OBPrice(obOrderDetail.getPriceID(), obOrderDetail.getPrice());
                              Intent intent = new Intent(BagActivity.this, DetailPrepareOrderClothesActivity.class);
                              intent.putExtra(OB_ORDERDETAIL, obOrderDetail);
                              intent.putExtra(EDIT, true);
                              intent.putExtra(OB_UNIT_PRICE_ITEM, obPriceItem);
                              intent.putExtra(OB_UNIT_PRICE_KG, obPriceKg);
                              startActivityForResult(intent, REQUEST_CODE);
                          }
                      }
                  }

                  @Override
                  public void onFailure(@NotNull ApolloException e) {
                      Log.e("getListUnitPrice", e.getCause() +" - "+e);
                  }
              }
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            OBOrderDetail obOrderDetailResult = (OBOrderDetail) data.getSerializableExtra(OB_ORDERDETAIL);
            Log.i("obOrderDetailResult", obOrderDetailResult.getUnit() + "---" + obOrderDetailResult.getColor());
            boolean flag = true;
            int pos = -1;
            for(OBOrderDetail ob : list) {
                if(checkDuplicateClothes(ob.getColorID(), obOrderDetailResult.getColorID())
                        && checkDuplicateClothes(ob.getLabelID(), obOrderDetailResult.getLabelID())
                        && checkDuplicateClothes(ob.getMaterialID(), obOrderDetailResult.getMaterialID())
                        && checkDuplicateClothes(ob.getProduct().getId(), obOrderDetailResult.getProduct().getId())
                        && checkDuplicateClothes(ob.getIdService(), obOrderDetailResult.getIdService())
                        && checkDuplicateClothes(ob.getUnitID(), obOrderDetailResult.getUnitID())) {
                    pos = list.indexOf(ob);
                    if(ob.getUnitID().equals(ITEM)) {
                        long count = obOrderDetailResult.getCount() + ob.getCount();
                        obOrderDetailResult.setCount(count);
                    }
                    list.set(pos, obOrderDetailResult);
                    flag = false;
                    break;
                }
            }
            if(flag) {
                list.add(obOrderDetailResult);
            }
            if(position != pos) {
                list.remove(position);
            }
            PreferenceUtil.setListOrderDetail(list, this);
            Log.i("ListOrderDetail", "Size: " + list.size());
            adapter.notifyDataSetChanged();
            if(adapter.sumPrice() != 0) {
                no_sum.setVisibility(View.GONE);
                totalPrice.setText(dec.format(adapter.sumPrice()) + " VND");
            } else {
                if(adapter.getItemCount() > 0) {
                    no_sum.setVisibility(View.VISIBLE);
                }
                else {
                    no_sum.setVisibility(View.GONE);
                }
                totalPrice.setText(getResources().getString(R.string.total_price));
            }
            countTotal.setText(adapter.sumCount() + " " + getResources().getString(R.string.item));
        }
    }


    @Override
    public void clickDel(int position) {
        list.remove(position);
        adapter.notifyDataSetChanged();
        PreferenceUtil.setListOrderDetail(list, BagActivity.this);
        countTotal.setText(adapter.sumCount() + " " + getResources().getString(R.string.item));
        if(adapter.sumPrice() != 0) {
            no_sum.setVisibility(View.GONE);
            totalPrice.setText(dec.format(adapter.sumPrice()) + " VND");
        } else {
            if(adapter.getItemCount() > 0) {
                no_sum.setVisibility(View.VISIBLE);
            }
            else {
                no_sum.setVisibility(View.GONE);
            }
            totalPrice.setText(getResources().getString(R.string.total_price));
        }
    }
}
