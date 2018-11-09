package com.example.vuphu.newlaundry.Order.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.CurrentUserQuery;
import com.example.vuphu.newlaundry.GetCustomerQuery;
import com.example.vuphu.newlaundry.GetPromotionBranchsQuery;
import com.example.vuphu.newlaundry.GetTimeSchedulesQuery;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.InputDialogFragment;
import com.example.vuphu.newlaundry.ItemListDialogFragment;
import com.example.vuphu.newlaundry.Order.Adapter.ListClothesAdapter;
import com.example.vuphu.newlaundry.Order.OBBranch;
import com.example.vuphu.newlaundry.Order.OBOrderDetail;
import com.example.vuphu.newlaundry.Order.OBTimeSchedule;
import com.example.vuphu.newlaundry.Payment.OBPayment;
import com.example.vuphu.newlaundry.PickupTimeDeliveryDialogFragment;
import com.example.vuphu.newlaundry.Promotion.ItemPromotionListDialogFragment;
import com.example.vuphu.newlaundry.Promotion.OBPromotion;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Service.ListServiceChooseAdapter;
import com.example.vuphu.newlaundry.Service.OBService;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.example.vuphu.newlaundry.Utils.Util;
import com.github.florent37.androidslidr.Slidr;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoOrderActivity extends AppCompatActivity implements ItemListDialogFragment.Listener,
        ItemPromotionListDialogFragment.Listener,
        InputDialogFragment.InputListener,
        PickupTimeDeliveryDialogFragment.GetPickupTimeDelivery {
    private static final String TYPE_LIST_PAYMENT = "PM";
    private String token;
    private static CurrentUserQuery.CurrentUser currentUser;
    private static GetCustomerQuery.CustomerById customer;
    ArrayList<String> paymentList = new ArrayList<>();
    private Toolbar toolbar;
    private RecyclerView listClothes;
//    private Slidr deliveryDate;
//    private TextView deliveryYourChoice;
    private CircleImageView avatar;
    private Button checkOut;
    private ArrayList<OBTimeSchedule> listTime;
    final ArrayList<OBPromotion> promotionList = new ArrayList<>();
    private ListClothesAdapter adapter;
    private List<OBOrderDetail> orderDetailList = new ArrayList<>();
    private String datePickupValue, dateDeliveryValue;
    private OBTimeSchedule TimePickupOB, TimeDeliveryOB;
    private TextView paymentValue, promotionValue, noteValue, name, email, phone, pickupPlace, deliveryPlace, pickUpdate, deliveryDate, pickupTime, deliveryTime, totalItem;
    private LinearLayout payment, promotion, note;
    private MaterialButton chooseSchedule;
    protected OBBranch obBranch;

    private static final String PICKUP_KEY = "PICKUP_KEY";
    private static final String DROPOFF_KEY = "DROPOFF_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_order);
        initToolbar();
        init();

    }
    private void init() {
        name = findViewById(R.id.nav_txt_name);
        avatar = findViewById(R.id.nav_img_avatar);
        email = findViewById(R.id.nav_txt_email);
        phone = findViewById(R.id.nav_txt_phone);
        pickupPlace = findViewById(R.id.info_order_pickup_place);
        deliveryPlace = findViewById(R.id.info_order_delivery_place);
        pickupPlace = findViewById(R.id.info_order_pickup_place);
        deliveryPlace = findViewById(R.id.info_order_delivery_place);
        pickUpdate = findViewById(R.id.prepare_order_date_pick_up);
        deliveryDate = findViewById(R.id.prepare_order_date_delivery);
        pickupTime = findViewById(R.id.prepare_order_time_pick_up);
        deliveryTime = findViewById(R.id.prepare_order_time_delivery);
        totalItem = findViewById(R.id.item_prepare_order_total_items);
        Intent intent = getIntent();
        if(intent.hasExtra(PICKUP_KEY)){
            pickupPlace.setText(intent.getStringExtra(PICKUP_KEY));
        }
        if(intent.hasExtra(DROPOFF_KEY)){
            deliveryPlace.setText(intent.getStringExtra(DROPOFF_KEY));
        }
        token = PreferenceUtil.getAuthToken(InfoOrderActivity.this);
        customer = PreferenceUtil.getCurrentUser(InfoOrderActivity.this);
        if(customer != null) {
            Picasso.get().load(Uri.parse(customer.postByCustomerAvatar().headerImageFile())).into(avatar);
            name.setText(customer.fullName());
            email.setText(customer.email());
            phone.setText(customer.phone());
        }
        else {
            GraphqlClient.getApolloClient(token, false)
                    .query(GetCustomerQuery.builder()
                            .id(String.valueOf(currentUser.id())).build())
                    .enqueue(new ApolloCall.Callback<GetCustomerQuery.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<GetCustomerQuery.Data> response) {
                            customer = response.data().customerById();
                            if ( customer != null){
                                InfoOrderActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        PreferenceUtil.setCurrentUser(InfoOrderActivity.this, customer);
                                        Picasso.get().load(Uri.parse(customer.postByCustomerAvatar().headerImageFile())).into(avatar);
                                        email.setText(customer.email());
                                        phone.setText(customer.phone());
                                        name.setText(customer.fullName());
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            Log.e("customer_err", e.getCause() +" - "+e);
                        }
                    });
        }


        listTime = new ArrayList<>();

        checkOut = findViewById(R.id.btn_check_out);
        listClothes = findViewById(R.id.list_prepare_order_clothes);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        listClothes.setLayoutManager(linearLayoutManager2);
        listClothes.setHasFixedSize(true);

        orderDetailList = new ArrayList<>();
        orderDetailList = PreferenceUtil.getListOrderDetail(InfoOrderActivity.this);
        prepareList();
        adapter = new ListClothesAdapter(this, orderDetailList);

        listClothes.setAdapter(adapter);
        totalItem.setText(adapter.sumCount() + " item");

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FinalOrderActivity.class));
            }
        });
        obBranch = new OBBranch("1", "CHI NHÁNH BÌNH THỦY 1", "10.0531254", "105.741299", "");

//        paymentValue = findViewById(R.id.item_prepare_order_payment);
        promotionValue = findViewById(R.id.item_prepare_order_promotion);
        noteValue = findViewById(R.id.item_prepare_order_note);

//        payment = findViewById(R.id.check_out_payment);
        promotion = findViewById(R.id.check_out_promotion);
        note = findViewById(R.id.check_out_note);

        initializePromotion();
//        paymentList.addAll(Arrays.asList(getResources().getStringArray(R.array.arr_payment)));
//        payment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ItemListDialogFragment bottomSheetDialogFragment = ItemListDialogFragment.newInstance(TYPE_LIST_PAYMENT,paymentList);
//                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
//            }
//        });


        note.setOnClickListener(new View.OnClickListener() {
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
                fetchData();
            }
        });


    }

    private void initializePromotion() {
        GraphqlClient.getApolloClient(token, false).query(GetPromotionBranchsQuery.builder().branchID(obBranch.getId()).build())
                .enqueue(new ApolloCall.Callback<GetPromotionBranchsQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GetPromotionBranchsQuery.Data> response) {
                        List<GetPromotionBranchsQuery.Node> nodes = response.data().allPromotionBranches().nodes();
                        for(GetPromotionBranchsQuery.Node node :nodes) {
                            promotionList.add(new OBPromotion(node.promotionByPromotionId().id(), node.promotionByPromotionId().promotionName(), null, node.promotionByPromotionId().promotionCode(),node.promotionByPromotionId().dateStart(),node.promotionByPromotionId().dateEnd() ,node.promotionByPromotionId().sale()));
                        }
                        InfoOrderActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                promotion.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ItemPromotionListDialogFragment promotionListDialogFragment = ItemPromotionListDialogFragment.newInstance(promotionList);
                                        promotionListDialogFragment.show(getSupportFragmentManager(),promotionListDialogFragment.getTag());
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e("branchPromotion_err", e.getCause() +" - "+e);
                    }
                });
    }

    private void fetchData() {
        GraphqlClient.getApolloClient(token, false).query(GetTimeSchedulesQuery.builder().build())
                .enqueue(new ApolloCall.Callback<GetTimeSchedulesQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GetTimeSchedulesQuery.Data> response) {
                        List<GetTimeSchedulesQuery.Node> list = response.data().allTimeSchedules().nodes();
                        listTime.clear();
                        for (GetTimeSchedulesQuery.Node node: list) {
                            listTime.add(new OBTimeSchedule(node.id(), node.timeStart(), node.timeEnd(), true));
                        }
                        InfoOrderActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(datePickupValue != null && dateDeliveryValue != null && TimePickupOB != null && TimeDeliveryOB != null) {
                                    PickupTimeDeliveryDialogFragment pickupTimeDeliveryDialogFragment = PickupTimeDeliveryDialogFragment.newInstance(listTime, TimePickupOB, TimeDeliveryOB, datePickupValue, dateDeliveryValue);
                                    pickupTimeDeliveryDialogFragment.show(getSupportFragmentManager(), pickupTimeDeliveryDialogFragment.getTag());
                                }
                                else {
                                    PickupTimeDeliveryDialogFragment pickupTimeDeliveryDialogFragment = PickupTimeDeliveryDialogFragment.newInstance(listTime, null, null, null, null);
                                    pickupTimeDeliveryDialogFragment.show(getSupportFragmentManager(), pickupTimeDeliveryDialogFragment.getTag());
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e("getTimeSchedule", e.getCause() +" - "+e);
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
        for(OBOrderDetail obOrderDetail : orderDetailList) {

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
    public void GetTime(OBTimeSchedule pickup, OBTimeSchedule delivery, String datePickup, String dateDelivery) {
        pickupTime.setText(pickup.getTimeStart() + " - " + pickup.getTimeEnd());
        deliveryTime.setText(delivery.getTimeStart() + " - " + delivery.getTimeEnd());
        pickUpdate.setText(datePickup);
        deliveryDate.setText(dateDelivery);
        TimePickupOB = pickup;
        TimeDeliveryOB = delivery;
        datePickupValue = datePickup;
        dateDeliveryValue = dateDelivery;
    }
}
