package com.example.vuphu.newlaundry.Main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.GetBillQuery;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Main.Adapter.AdapterBill;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.example.vuphu.newlaundry.Utils.StringKey.ID_RECEIPT;
import static com.example.vuphu.newlaundry.Utils.StringKey.ITEM;
import static com.example.vuphu.newlaundry.Utils.StringKey.KG;
import static com.example.vuphu.newlaundry.Utils.Util.formatDecimal;
import static com.example.vuphu.newlaundry.Utils.Util.parseDate;

public class BillActivity extends AppCompatActivity {
    private TextView txt_bill_name, txt_time_bill, txt_name_customer, txt_phone_number, txt_branch_name, txt_branch_address,
            txt_place_pickup, txt_place_delivery, txt_total_not_sale_off, txt_sale_off, txt_total_price;
    private RecyclerView list_clothes_bill;
    private ArrayList<OBBill> list_bill;
    private String idReceipt;
    private String billName, timeBill, nameCustomer, phonenumber, branchname, branchaddress, placepickup, placedelivery;
    private AdapterBill adapterBill;
    private int sale_of_value = 0;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        initToolbar();
        addControls();
        initilizedData();
    }

    private void initilizedData() {
        String token = PreferenceUtil.getAuthToken(getApplicationContext());
        GraphqlClient.getApolloClient(token, false).query(GetBillQuery.builder().receipt(idReceipt).build())
                .enqueue(new ApolloCall.Callback<GetBillQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GetBillQuery.Data> response) {
                        if(response.data().allBills() != null) {
                            Log.i("getBill", response.data().allBills().nodes().toString());
                            GetBillQuery.Node node = response.data().allBills().nodes().get(0);
                            billName = getResources().getString(R.string.bill_name) + node.id();
                            Log.i("getBill", billName);
                            if(TextUtils.isEmpty(node.updateDate())) {
                                timeBill = parseDate(node.createDate().substring(0, 10), "yyyy-MM-dd", "dd/MM/yyyy") + "-" + node.createDate().substring(11, 19);
                            }
                            else {
                                timeBill = parseDate(node.updateDate().substring(0, 10), "yyyy-MM-dd", "dd/MM/yyyy") + "-" + node.updateDate().substring(11, 19);
                            }
                            Log.i("getBill", timeBill);
                            if(node.receiptByReceiptId().customerOrderByOrderId() != null) {
                                nameCustomer = node.receiptByReceiptId().customerOrderByOrderId().customerByCustomerId().fullName();
                                Log.i("getBill", nameCustomer);
                                phonenumber = node.receiptByReceiptId().customerOrderByOrderId().customerByCustomerId().phone();
                                Log.i("getBill", phonenumber);
                                branchname = node.receiptByReceiptId().customerOrderByOrderId().branchByBranchId().branchName();
                                Log.i("getBill", branchname);
                                branchaddress = node.receiptByReceiptId().customerOrderByOrderId().branchByBranchId().address();
                                Log.i("getBill", branchaddress);
                                placepickup = node.receiptByReceiptId().customerOrderByOrderId().pickUpPlace();
                                Log.i("getBill", placepickup);
                                placedelivery = node.receiptByReceiptId().customerOrderByOrderId().deliveryPlace();
                                Log.i("getBill", placedelivery);
                                if(node.receiptByReceiptId().customerOrderByOrderId().promotionByPromotionId() != null) {
                                    sale_of_value = Integer.parseInt(node.receiptByReceiptId().customerOrderByOrderId().promotionByPromotionId().sale());
                                }
                                Log.i("getBill", Integer.toString(sale_of_value));
                            }
                            List<GetBillQuery.Node1> list = node.billDetailsByBillId().nodes();
                            for(GetBillQuery.Node1 node1 : list) {
                                OBBill obBill = new OBBill();
                                obBill.setUnitId(node1.unitByUnitId().id());
                                obBill.setIdService(node1.serviceTypeByServiceTypeId().id());
                                obBill.setServicename(node1.serviceTypeByServiceTypeId().serviceTypeName());
                                obBill.setNameClothes(node1.productByProductId().productName());
                                obBill.setUnitprice(node1.unitPriceByUnitPrice().price());
                                if(obBill.getUnitId().equals(ITEM)) {
                                    obBill.setAmount(node1.amount().longValue());
                                    obBill.setAmountReceived(node1.receivedAmount().longValue());
                                    obBill.setUnitName(getResources().getString(R.string.item));
                                }
                                else if(obBill.getUnitId().equals(KG)) {
                                    obBill.setWeight(node1.amount());
                                    obBill.setWeightReceived(node1.receivedAmount());
                                    obBill.setUnitName(getResources().getString(R.string.kg));
                                }
                                Log.i("getBill", obBill.toString());
                                list_bill.add(obBill);
                            }
                            BillActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initilizedView();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });
    }

    private void initilizedView() {
        if(!TextUtils.isEmpty(billName)) {
            txt_bill_name.setText(billName);
        }
        if(!TextUtils.isEmpty(timeBill)) {
            txt_time_bill.setText(timeBill);
        }
        if(!TextUtils.isEmpty(nameCustomer)) {
            txt_name_customer.setText(nameCustomer);
        }
        if(!TextUtils.isEmpty(phonenumber)) {
            txt_phone_number.setText(phonenumber);
        }
        if(!TextUtils.isEmpty(branchname)) {
            txt_branch_name.setText(branchname);
        }
        if(!TextUtils.isEmpty(branchaddress)) {
            txt_branch_address.setText(branchaddress);
        }
        if(!TextUtils.isEmpty(placepickup)) {
            txt_place_pickup.setText(placepickup);
        }
        if(!TextUtils.isEmpty(placedelivery)) {
            txt_place_delivery.setText(placedelivery);
        }

        adapterBill = new AdapterBill(getApplicationContext(), list_bill);
        list_clothes_bill.setAdapter(adapterBill);

        txt_total_not_sale_off.setText(formatDecimal(adapterBill.totalPrice()) + " VND");
        txt_sale_off.setText(sale_of_value + "%");
        txt_total_price.setText(formatDecimal(adapterBill.totalPrice()*(100-sale_of_value)/100) + " VND");
    }

    private void addControls() {
        txt_bill_name = findViewById(R.id.txt_bill_name);
        txt_time_bill = findViewById(R.id.txt_time_bill);
        txt_name_customer = findViewById(R.id.txt_name_customer);
        txt_phone_number = findViewById(R.id.txt_phone_number);
        txt_branch_name = findViewById(R.id.txt_branch_name);
        txt_branch_address = findViewById(R.id.txt_branch_address);
        txt_place_pickup = findViewById(R.id.txt_place_pickup);
        txt_place_delivery = findViewById(R.id.txt_place_delivery);
        txt_total_not_sale_off = findViewById(R.id.txt_total_not_sale_off);
        txt_sale_off = findViewById(R.id.txt_sale_off);
        txt_total_price = findViewById(R.id.txt_total_price);
        list_clothes_bill = findViewById(R.id.list_clothes_bill);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        list_clothes_bill.setLayoutManager(linearLayoutManager);
        Intent intent = getIntent();
        list_bill = new ArrayList<>();
        if(intent.hasExtra(ID_RECEIPT)) {
            idReceipt = intent.getStringExtra(ID_RECEIPT);
        }
    }

    private void initToolbar() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.title_bill);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            finish();
            return true;
        }
        return false;
    }
}
