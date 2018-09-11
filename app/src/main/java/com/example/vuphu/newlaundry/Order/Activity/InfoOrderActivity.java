package com.example.vuphu.newlaundry.Order.Activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.vuphu.newlaundry.Order.Adapter.ListClothesAdapter;
import com.example.vuphu.newlaundry.Order.OBOrderDetail;
import com.example.vuphu.newlaundry.Payment.OBPayment;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Service.ListServiceChooseAdapter;
import com.example.vuphu.newlaundry.Service.OBService;
import com.example.vuphu.newlaundry.Utils.Util;
import com.github.florent37.androidslidr.Slidr;

import java.util.ArrayList;
import java.util.List;

public class InfoOrderActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private RecyclerView listClothes;
    private Slidr deliveryDate;
    private TextView deliveryYourChoice;

    private ListClothesAdapter adapter;
    private List<OBOrderDetail> orderDetailList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_order);
        initToolbar();
        init();

    }
    private void init() {
        listClothes = findViewById(R.id.list_prepare_order_clothes);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        listClothes.setLayoutManager(linearLayoutManager2);
        listClothes.setHasFixedSize(true);

        List<OBPayment> paymentList = new ArrayList<>();
        OBPayment itemPayment = new OBPayment();
        itemPayment.setName("Money Cash");
        itemPayment.setDesc("A lot type of payment for you choose");
        paymentList.add(itemPayment);
        paymentList.add(itemPayment);
        paymentList.add(itemPayment);

        orderDetailList = new ArrayList<>();
        prepareList();
        adapter = new ListClothesAdapter(this, orderDetailList);

        listClothes.setAdapter(adapter);
        //Delivery Date
        deliveryDate = findViewById(R.id.item_prepare_order_seek_day);
        deliveryYourChoice = findViewById(R.id.prepare_order_devlivery_3_name);
        setDeliveryYourChoice();





    }

    @TargetApi(Build.VERSION_CODES.M)
    public void setDeliveryYourChoice(){
        deliveryDate.setMax(Util.getDayOfMonth());
        deliveryDate.setMin(Util.getToDay());
        deliveryDate.setCurrentValue(Util.getToDay());
        deliveryDate.setTextFormatter(new Slidr.TextFormatter() {
            @Override
            public String format(float value) {

                return "Delivery on "+String.valueOf((int)value) + "/"+Util.getMonth();
            }
        });
        deliveryDate.setListener(new Slidr.Listener() {
            @Override
            public void valueChanged(Slidr slidr, float currentValue) {
                    deliveryYourChoice.setText((int)currentValue+"/"+Util.getMonth()+"/"+Util.getYear());

            }

            @Override
            public void bubbleClicked(Slidr slidr) {

            }
        });
    }

    private void initToolbar() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Information Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void prepareList() {

        for (int i = 0; i < 10; i++) {
            OBOrderDetail detail = new OBOrderDetail();
            detail.setTitle("Product " + i);
            detail.setPricing("200" + i);
            if (i % 2 == 0)
                detail.setCategory("Category Type 1");
            else
                detail.setCategory("Category Type 2");
            orderDetailList.add(detail);
        }
    }
}
