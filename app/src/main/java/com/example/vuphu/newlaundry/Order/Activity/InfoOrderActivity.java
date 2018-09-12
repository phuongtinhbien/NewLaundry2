package com.example.vuphu.newlaundry.Order.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vuphu.newlaundry.InputDialogFragment;
import com.example.vuphu.newlaundry.ItemListDialogFragment;
import com.example.vuphu.newlaundry.Order.Adapter.ListClothesAdapter;
import com.example.vuphu.newlaundry.Order.OBOrderDetail;
import com.example.vuphu.newlaundry.Payment.OBPayment;
import com.example.vuphu.newlaundry.PickupTimeDeliveryDialogFragment;
import com.example.vuphu.newlaundry.Promotion.ItemPromotionListDialogFragment;
import com.example.vuphu.newlaundry.Promotion.OBPromotion;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Service.ListServiceChooseAdapter;
import com.example.vuphu.newlaundry.Service.OBService;
import com.example.vuphu.newlaundry.Utils.Util;
import com.github.florent37.androidslidr.Slidr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InfoOrderActivity extends AppCompatActivity implements ItemListDialogFragment.Listener,
        ItemPromotionListDialogFragment.Listener, InputDialogFragment.InputListener,
        PickupTimeDeliveryDialogFragment.PickupTimeDeliveryListener{

    private static final String TYPE_LIST_PAYMENT = "PM";
    ArrayList<String> paymentList = new ArrayList<>();
    private Toolbar toolbar;
    private RecyclerView listClothes;
    private Slidr deliveryDate;
    private TextView deliveryYourChoice;
    private Button checkOut;
    final ArrayList<OBPromotion> promotionList = new ArrayList<>();
    private ListClothesAdapter adapter;
    private List<OBOrderDetail> orderDetailList = new ArrayList<>();
    private TextView paymentValue, promotionValue, noteValue;
    private LinearLayout payment, promotion, note;
    private MaterialButton chooseSchedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_order);
        initToolbar();
        init();

    }
    private void init() {
        checkOut = findViewById(R.id.btn_check_out);
        listClothes = findViewById(R.id.list_prepare_order_clothes);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        listClothes.setLayoutManager(linearLayoutManager2);
        listClothes.setHasFixedSize(true);

        orderDetailList = new ArrayList<>();
        prepareList();
        adapter = new ListClothesAdapter(this, orderDetailList);

        listClothes.setAdapter(adapter);

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FinalOrderActivity.class));
            }
        });

        paymentValue = findViewById(R.id.item_prepare_order_payment);
        promotionValue = findViewById(R.id.item_prepare_order_promotion);
        noteValue = findViewById(R.id.item_prepare_order_note);

        payment = findViewById(R.id.check_out_payment);
        promotion = findViewById(R.id.check_out_promotion);
        note = findViewById(R.id.check_out_note);

        paymentList.addAll(Arrays.asList(getResources().getStringArray(R.array.arr_payment)));
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemListDialogFragment bottomSheetDialogFragment = ItemListDialogFragment.newInstance(TYPE_LIST_PAYMENT,paymentList);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });



        for (int i=0;i<5;i++)
            promotionList.add(new OBPromotion("5% off Ride Economy Class",null,"PROMOTIONCODE",null,null));
        promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemPromotionListDialogFragment promotionListDialogFragment = ItemPromotionListDialogFragment.newInstance(promotionList);
                promotionListDialogFragment.show(getSupportFragmentManager(),promotionListDialogFragment.getTag());
            }
        });

        note .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputDialogFragment inputDialogFragment = InputDialogFragment.newInstance();
                inputDialogFragment.show(getSupportFragmentManager(), inputDialogFragment.getTag());
            }
        });

        chooseSchedule = findViewById(R.id.item_prepare_order_btn_schedule);
        chooseSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickupTimeDeliveryDialogFragment pickupTimeDeliveryDialogFragment = PickupTimeDeliveryDialogFragment.newInstance();
                pickupTimeDeliveryDialogFragment.show(getSupportFragmentManager(), pickupTimeDeliveryDialogFragment.getTag());
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

    @Override
    public void onItemClicked(String type, int position) {
        paymentValue.setText(paymentList.get(position));

    }

    @Override
    public void onItemPromotionClicked(int position) {
        promotionValue.setText(promotionList.get(position).getCode());
    }

    @Override
    public void onInputClicked(String value) {
        noteValue.setText(value);
    }

    @Override
    public void onPickupTimeDeliveryClicked(int position) {

    }
}
