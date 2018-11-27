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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.CreateOrderAndDetailMutation;
import com.example.vuphu.newlaundry.CurrentUserQuery;
import com.example.vuphu.newlaundry.GetCustomerQuery;
import com.example.vuphu.newlaundry.GetListUnitPriceMutation;
import com.example.vuphu.newlaundry.GetPromotionBranchsQuery;
import com.example.vuphu.newlaundry.GetTimeSchedulesQuery;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.InputDialogFragment;
import com.example.vuphu.newlaundry.ItemListDialogFragment;
import com.example.vuphu.newlaundry.Order.Adapter.ListClothesAdapter;
import com.example.vuphu.newlaundry.Order.IFOBPrepareOrder;
import com.example.vuphu.newlaundry.Order.OBBranch;
import com.example.vuphu.newlaundry.Order.OBOrderDetail;
import com.example.vuphu.newlaundry.Order.OBPrice;
import com.example.vuphu.newlaundry.Order.OBTimeSchedule;
import com.example.vuphu.newlaundry.Payment.OBPayment;
import com.example.vuphu.newlaundry.PickupTimeDeliveryDialogFragment;
import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.Promotion.ItemPromotionListDialogFragment;
import com.example.vuphu.newlaundry.Promotion.OBPromotion;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Service.ListServiceChooseAdapter;
import com.example.vuphu.newlaundry.Service.OBService;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.example.vuphu.newlaundry.Utils.Util;
import com.example.vuphu.newlaundry.type.CustomerOrderInput;
import com.example.vuphu.newlaundry.type.OrderDetailInput;
import com.example.vuphu.newlaundry.type.UnitPriceInput;
import com.github.florent37.androidslidr.Slidr;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.vuphu.newlaundry.Utils.PreferenceUtil.removeOrderList;
import static com.example.vuphu.newlaundry.Utils.StringKey.DROPOFF_KEY;
import static com.example.vuphu.newlaundry.Utils.StringKey.EDIT;
import static com.example.vuphu.newlaundry.Utils.StringKey.ID_BRANCH;
import static com.example.vuphu.newlaundry.Utils.StringKey.ID_ORDER;
import static com.example.vuphu.newlaundry.Utils.StringKey.ITEM;
import static com.example.vuphu.newlaundry.Utils.StringKey.KG;
import static com.example.vuphu.newlaundry.Utils.StringKey.LIST_SERVICE;
import static com.example.vuphu.newlaundry.Utils.StringKey.OB_ORDERDETAIL;
import static com.example.vuphu.newlaundry.Utils.StringKey.OB_UNIT_PRICE_ITEM;
import static com.example.vuphu.newlaundry.Utils.StringKey.OB_UNIT_PRICE_KG;
import static com.example.vuphu.newlaundry.Utils.StringKey.PICKUP_KEY;
import static com.example.vuphu.newlaundry.Utils.StringKey.TOTAL_PRICE;
import static com.example.vuphu.newlaundry.Utils.StringKey.TOTAL_WEIGHT;
import static com.example.vuphu.newlaundry.Utils.Util.checkDuplicateClothes;

public class InfoOrderActivity extends AppCompatActivity implements
        ItemPromotionListDialogFragment.Listener,
        PickupTimeDeliveryDialogFragment.GetPickupTimeDelivery,
        IFOBPrepareOrder {
    private String token;
    private static CurrentUserQuery.CurrentUser currentUser;
    private static GetCustomerQuery.CustomerById customer;
    private Toolbar toolbar;
    private RecyclerView listClothes;
    private CircleImageView avatar;
    private Button checkOut;
    private ArrayList<OBTimeSchedule> listTimePickup;
    private ArrayList<OBTimeSchedule> listTimeDelivery;
    final ArrayList<OBPromotion> promotionList = new ArrayList<>();
    private ListClothesAdapter adapter;
    private ArrayList<OBOrderDetail> orderDetailList;
    private String datePickupValue, dateDeliveryValue;
    private OBTimeSchedule TimePickupOB, TimeDeliveryOB;
    private TextView promotionValue, name, email, phone, pickupPlace, deliveryPlace, pickUpdate, deliveryDate, pickupTime, deliveryTime, totalItem, priceTotal;
    private LinearLayout promotion;
    private MaterialButton chooseSchedule;
    private String idBranch;
    private Popup popup;
    private ArrayList<String> listService;
    private String idOder;
    private String idPromotion = null;
    private DecimalFormat dec;
    private int position;


    private static final int REQUEST_CODE = 7;


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
        priceTotal = findViewById(R.id.item_prepare_order_total);
        popup = new Popup(InfoOrderActivity.this);
        dec = new DecimalFormat("##,###,###,###");

        Intent intent = getIntent();
        if(intent.hasExtra(PICKUP_KEY)){
            pickupPlace.setText(intent.getStringExtra(PICKUP_KEY));
        }
        if(intent.hasExtra(DROPOFF_KEY)){
            deliveryPlace.setText(intent.getStringExtra(DROPOFF_KEY));
        }

        idBranch = intent.getStringExtra(ID_BRANCH);
        listService = intent.getStringArrayListExtra(LIST_SERVICE);

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


        listTimePickup = new ArrayList<>();
        listTimeDelivery = new ArrayList<>();

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
        adapter = new ListClothesAdapter(this, orderDetailList, this);

        listClothes.setAdapter(adapter);
        totalItem.setText(adapter.sumCount() + " " + getResources().getString(R.string.item));
        if(adapter.sumPrice() != 0) {
            priceTotal.setText(dec.format(adapter.sumPrice()) + " VND");
        } else {
            priceTotal.setText(getResources().getString(R.string.total_price));
        }
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    popup.createLoadingDialog();
                    popup.show();
                    String delidate = dateDeliveryValue;
                    String pickdate = datePickupValue;
//                    String delidate = parseDate(dateDeliveryValue);
//                    String pickdate = parseDate(datePickupValue);
                    if(!TextUtils.isEmpty(delidate) && !TextUtils.isEmpty(pickdate)) {
                        CustomerOrderInput customerOrderInput = CustomerOrderInput.builder()
                                .branchId(idBranch)
                                .customerId(customer.id())
                                .deliveryDate(delidate)
                                .pickUpDate(pickdate)
                                .pickUpTimeId(TimePickupOB.getId())
                                .deliveryTimeId(TimeDeliveryOB.getId())
                                .deliveryPlace(deliveryPlace.getText().toString())
                                .pickUpPlace(pickupPlace.getText().toString())
                                .promotionId(idPromotion)
                                .status("PENDING")
                                .createBy(customer.id())
                                .build();


                        List<OrderDetailInput> list = new ArrayList<>();
                        for(OBOrderDetail obOrderDetail : orderDetailList){
                            OrderDetailInput orderDetailInput = OrderDetailInput.builder()
                                    .amount((double) obOrderDetail.getCount())
                                    .colorId(obOrderDetail.getColorID())
                                    .materialId(obOrderDetail.getMaterialID())
                                    .labelId(obOrderDetail.getLabelID())
                                    .productId(obOrderDetail.getProduct().getId())
                                    .unitPrice(obOrderDetail.getPriceID())
                                    .unitId(obOrderDetail.getUnitID())
                                    .serviceTypeId(obOrderDetail.getIdService())
                                    .note(obOrderDetail.getNote())
                                    .build();
                            list.add(orderDetailInput);
                        }


                        GraphqlClient.getApolloClient(token, false).mutate(CreateOrderAndDetailMutation.builder()
                                .order(customerOrderInput)
                                .detailOrder(list)
                                .build()
                        ).enqueue(new ApolloCall.Callback<CreateOrderAndDetailMutation.Data>() {
                            @Override
                            public void onResponse(@NotNull Response<CreateOrderAndDetailMutation.Data> response) {
                                idOder = response.data().createOrderAndDetail().customerOrder().id();
                                Log.i("customer_order", idOder);
                                clearPreference();
                                popup.hide();
                                startActivity(new Intent(getApplicationContext(), FinalOrderActivity.class).putExtra(ID_ORDER, idOder));
                                finish();
                            }

                            @Override
                            public void onFailure(@NotNull ApolloException e) {
                                Log.e("createOrderDetail_err", e.getCause() +" - "+e);
                                popup.createFailDialog(getResources().getString(R.string.order_fail), "OK");

                            }
                        });
                    }
                }
            }
        });

        promotionValue = findViewById(R.id.item_prepare_order_promotion);


        promotion = findViewById(R.id.check_out_promotion);

        initializePromotion();

        chooseSchedule = findViewById(R.id.item_prepare_order_btn_schedule);
        chooseSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData();
            }
        });


    }

    private String parseDate(String str) {
        String result = "";
        SimpleDateFormat sdfOld = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfNew = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date d = sdfOld.parse(str);
            result = sdfNew.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void clearPreference() {
        removeOrderList(InfoOrderActivity.this);
    }

    private boolean validate() {
        if(TimeDeliveryOB == null) {
            Toast.makeText(InfoOrderActivity.this, getResources().getString(R.string.please_choose_time_delivery), Toast.LENGTH_LONG).show();
            return false;
        } else if(TimePickupOB == null) {
            Toast.makeText(InfoOrderActivity.this, getResources().getString(R.string.please_choose_time_pickup), Toast.LENGTH_LONG).show();
            return false;
        } else if(TextUtils.isEmpty(datePickupValue)) {
            Toast.makeText(InfoOrderActivity.this, getResources().getString(R.string.please_choose_date_pickup), Toast.LENGTH_LONG).show();
            return false;
        } else if(TextUtils.isEmpty(datePickupValue)) {
            Toast.makeText(InfoOrderActivity.this, getResources().getString(R.string.please_choose_date_delivery), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void initializePromotion() {
        GraphqlClient.getApolloClient(token, false).query(GetPromotionBranchsQuery.builder().branchID(idBranch).build())
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
                        listTimePickup.clear();
                        for (GetTimeSchedulesQuery.Node node: list) {
                            OBTimeSchedule ob1 = new OBTimeSchedule(node.id(), node.timeStart(), node.timeEnd(), true);
                            OBTimeSchedule ob2 = new OBTimeSchedule(node.id(), node.timeStart(), node.timeEnd(), true);
                            listTimePickup.add(ob1);
                            listTimeDelivery.add(ob2);
                        }
                        InfoOrderActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(datePickupValue != null && dateDeliveryValue != null && TimePickupOB != null && TimeDeliveryOB != null) {
                                    PickupTimeDeliveryDialogFragment pickupTimeDeliveryDialogFragment = PickupTimeDeliveryDialogFragment.newInstance(listTimePickup, listTimeDelivery, TimePickupOB, TimeDeliveryOB, datePickupValue, dateDeliveryValue, idBranch);
                                    pickupTimeDeliveryDialogFragment.show(getSupportFragmentManager(), pickupTimeDeliveryDialogFragment.getTag());
                                }
                                else {
                                    PickupTimeDeliveryDialogFragment pickupTimeDeliveryDialogFragment = PickupTimeDeliveryDialogFragment.newInstance(listTimePickup, listTimeDelivery, null, null, null, null, idBranch);
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
        setTitle(R.string.info_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onItemPromotionClicked(int position) {
        promotionValue.setText(promotionList.get(position).getCode());
        idPromotion = promotionList.get(position).getId();
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

    @Override
    public void clickClothes(OBOrderDetail obOrderDetail) {
        if(obOrderDetail != null){
            position = orderDetailList.indexOf(obOrderDetail);
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
                                             Intent intent = new Intent(InfoOrderActivity.this, DetailPrepareOrderClothesActivity.class);
                                             intent.putExtra(OB_ORDERDETAIL, obOrderDetail);
                                             intent.putExtra(EDIT, true);
                                             intent.putExtra(OB_UNIT_PRICE_ITEM, obPriceItem);
                                             intent.putExtra(OB_UNIT_PRICE_KG, obPriceKg);
                                             startActivityForResult(intent, REQUEST_CODE);
                                         } else if(obOrderDetail.getUnitID().equals(KG)) {
                                             OBPrice obPriceItem = new OBPrice(unitPrices.get(0).id(), unitPrices.get(0).price());
                                             OBPrice obPriceKg = new OBPrice(obOrderDetail.getPriceID(), obOrderDetail.getPrice());
                                             Intent intent = new Intent(InfoOrderActivity.this, DetailPrepareOrderClothesActivity.class);
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
            for(OBOrderDetail ob : orderDetailList) {
                if(checkDuplicateClothes(ob.getColorID(), obOrderDetailResult.getColorID())
                        && checkDuplicateClothes(ob.getLabelID(), obOrderDetailResult.getLabelID())
                        && checkDuplicateClothes(ob.getMaterialID(), obOrderDetailResult.getMaterialID())
                        && checkDuplicateClothes(ob.getProduct().getId(), obOrderDetailResult.getProduct().getId())
                        && checkDuplicateClothes(ob.getIdService(), obOrderDetailResult.getIdService())
                        && checkDuplicateClothes(ob.getUnitID(), obOrderDetailResult.getUnitID())) {
                    pos = orderDetailList.indexOf(ob);
                    orderDetailList.set(pos, obOrderDetailResult);
                    flag = false;
                    break;
                }
            }
            if(flag) {
                orderDetailList.add(obOrderDetailResult);
            }
            if(position != pos) {
                orderDetailList.remove(position);
            }
            PreferenceUtil.setListOrderDetail(orderDetailList, this);
            Log.i("ListOrderDetail", "Size: " + orderDetailList.size());
            adapter.notifyDataSetChanged();
            if(adapter.sumPrice() != 0) {
                priceTotal.setText(dec.format(adapter.sumPrice()) + " VND");
            } else {
                priceTotal.setText(getResources().getString(R.string.total_price));
            }
            totalItem.setText(adapter.sumCount() + " " + getResources().getString(R.string.item));
        }
    }

    @Override
    public void clickDel(int position) {

    }
}
