package com.example.vuphu.newlaundry.Main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.GetCustomerQuery;
import com.example.vuphu.newlaundry.GetListUnitPriceMutation;
import com.example.vuphu.newlaundry.GetOrdetailByOrderidQuery;
import com.example.vuphu.newlaundry.GetPromotionBranchsQuery;
import com.example.vuphu.newlaundry.GetTimeSchedulesQuery;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Order.Activity.DetailPrepareOrderClothesActivity;
import com.example.vuphu.newlaundry.Order.Activity.InfoOrderActivity;
import com.example.vuphu.newlaundry.Order.Adapter.ListClothesAdapter;
import com.example.vuphu.newlaundry.Order.IFOBPrepareOrder;
import com.example.vuphu.newlaundry.Order.OBOrderDetail;

import com.example.vuphu.newlaundry.Order.OBPrice;
import com.example.vuphu.newlaundry.Order.OBTimeSchedule;
import com.example.vuphu.newlaundry.PickupTimeDeliveryDialogFragment;
import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.Product.OBProduct;

import com.example.vuphu.newlaundry.Promotion.ItemPromotionListDialogFragment;
import com.example.vuphu.newlaundry.Promotion.OBPromotion;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.UpdateOrderAndDetailMutation;
import com.example.vuphu.newlaundry.UpdateStatusMutation;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.example.vuphu.newlaundry.Utils.Util;
import com.example.vuphu.newlaundry.type.CustomerOrderInput;
import com.example.vuphu.newlaundry.type.OrderDetailInput;
import com.example.vuphu.newlaundry.type.UnitPriceInput;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


import static com.example.vuphu.newlaundry.Utils.StringKey.DECLINED;
import static com.example.vuphu.newlaundry.Utils.StringKey.DRAFT;
import static com.example.vuphu.newlaundry.Utils.StringKey.EDIT;
import static com.example.vuphu.newlaundry.Utils.StringKey.ID_BRANCH;
import static com.example.vuphu.newlaundry.Utils.StringKey.ID_ORDER;
import static com.example.vuphu.newlaundry.Utils.StringKey.ITEM;
import static com.example.vuphu.newlaundry.Utils.StringKey.KG;
import static com.example.vuphu.newlaundry.Utils.StringKey.OB_ORDERDETAIL;
import static com.example.vuphu.newlaundry.Utils.StringKey.OB_UNIT_PRICE_ITEM;
import static com.example.vuphu.newlaundry.Utils.StringKey.OB_UNIT_PRICE_KG;
import static com.example.vuphu.newlaundry.Utils.StringKey.PENDING;
import static com.example.vuphu.newlaundry.Utils.StringKey.STATUS;
import static com.example.vuphu.newlaundry.Utils.StringKey.UNIT_NAME_ITEM;
import static com.example.vuphu.newlaundry.Utils.StringKey.UNIT_NAME_KG;
import static com.example.vuphu.newlaundry.Utils.Util.checkDuplicateClothes;
import static com.example.vuphu.newlaundry.Utils.Util.parseDate;

public class InfoOrderDetailActivity extends AppCompatActivity implements IFOBPrepareOrder,
        ItemPromotionListDialogFragment.Listener,
        PickupTimeDeliveryDialogFragment.GetPickupTimeDelivery{

    private String token;
    private Toolbar toolbar;
    private RecyclerView listClothes;
    private CircleImageView avatar;
    private ListClothesAdapter adapter;
    private ArrayList<OBOrderDetail> orderDetailList;
    private String datePickupValue, dateDeliveryValue, promotionValue, pickupPlaceValue, deliveryPlaceValue, pickupTimeValue, deliveryTimeValue ;
    private TextView promotion, name, email, phone, pickupPlace, deliveryPlace, pickUpdate, deliveryDate, pickupTime, deliveryTime, countTotal, totalPrice;
    private Popup popup;
    private String idOder, status, idBranch;
    private GetCustomerQuery.CustomerById customer;
    private DecimalFormat dec;
    private ImageView img_qrcode;
    private MaterialButton btnSave_Edit, btnCancelOrder, btnChooseSchedule;
    private LinearLayout promotionLayout;
    private ArrayList<OBTimeSchedule> listTimePickup;
    private ArrayList<OBTimeSchedule> listTimeDelivery;
    private OBTimeSchedule TimePickupOB, TimeDeliveryOB;
    private boolean isClickClothesItem = false;
    private int position;
    private static final int REQUEST_CODE = 7;
    private ArrayList<OBPromotion> promotionList = new ArrayList<>();
    private String idPromotion;
    private int salePercent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_info_order_detail);
        initToolbar();
        init();
    }

    private void init() {
        orderDetailList = new ArrayList<>();
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
        countTotal = findViewById(R.id.item_prepare_order_total_items);
        totalPrice = findViewById(R.id.item_prepare_order_total);
        img_qrcode = findViewById(R.id.info_detail_qrCode);
        btnSave_Edit = findViewById(R.id.edit_save_order);
        btnCancelOrder = findViewById(R.id.cancel_order);
        btnChooseSchedule = findViewById(R.id.item_prepare_order_btn_schedule);
        promotionLayout = findViewById(R.id.promotion_layout);
        promotion = findViewById(R.id.item_prepare_order_promotion);

        popup = new Popup(InfoOrderDetailActivity.this);
        dec = new DecimalFormat("##,###,###,###");
        listTimeDelivery = new ArrayList<>();
        listTimePickup = new ArrayList<>();
        TimePickupOB = new OBTimeSchedule();
        TimeDeliveryOB = new OBTimeSchedule();

        Intent intent = getIntent();
        if(intent.hasExtra(ID_ORDER)){
            idOder = intent.getStringExtra(ID_ORDER);
        }
        if(intent.hasExtra(ID_BRANCH)) {
            idBranch = intent.getStringExtra(ID_BRANCH);
        }

        try {
            generateQRCodeImage(idOder, 700, 700);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        token = PreferenceUtil.getAuthToken(InfoOrderDetailActivity.this);
        customer = PreferenceUtil.getCurrentUser(InfoOrderDetailActivity.this);
        if(customer != null) {
            Picasso.get().load(Uri.parse(customer.postByCustomerAvatar().headerImageFile())).into(avatar);
            name.setText(customer.fullName());
            email.setText(customer.email());
            phone.setText(customer.phone());
        }

        listClothes = findViewById(R.id.list_prepare_order_clothes);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        listClothes.setLayoutManager(linearLayoutManager2);
        listClothes.setHasFixedSize(true);
        getOrderDetailFromServer();
        if(intent.hasExtra(STATUS)) {
            status = intent.getStringExtra(STATUS);
            if(status.equals(PENDING)){
                initializeEditAndCacel();
            }
            else {
                btnChooseSchedule.setVisibility(View.GONE);
                btnSave_Edit.setVisibility(View.GONE);
                btnCancelOrder.setVisibility(View.GONE);
            }
        }


    }

    private void initializeEditAndCacel() {
        btnSave_Edit.setVisibility(View.VISIBLE);
        btnSave_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnSave_Edit.getText().toString().equals(getResources().getString(R.string.edit))) {
                    editOrder();
                }
                else if(btnSave_Edit.getText().toString().equals(getResources().getString(R.string.save))) {
                    saveOrder();
                }
            }
        });
        btnCancelOrder.setVisibility(View.VISIBLE);
        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: check (time pickup - current time) > 2h
                if(checkTime()){
                    setStatusOrder(DECLINED);
                }
                else {
                    popup.createFailDialog(getResources().getString(R.string.can_not_cancel_order), getResources().getString(R.string.btn_fail), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popup.hide();
                        }
                    });
                    popup.show();
                }
            }
        });
    }

    private boolean checkTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(datePickupValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(pickupTimeValue.substring(0,2)));
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        long timeCondition = calendar1.getTimeInMillis() - calendar.getTimeInMillis();
        Log.i("timeCondition", timeCondition + " ms");
        if(timeCondition > 2*3600000) {
            return true;
        }
        return false;
    }

    private void saveOrder() {
        // TODO: Update Customer_order and order detail
//        String delidate = dateDeliveryValue;
//        String pickdate = datePickupValue;
        String delidate = parseDate(dateDeliveryValue, "dd/MM/yyyy", "yyyy/MM/dd");
        String pickdate = parseDate(datePickupValue, "dd/MM/yyyy", "yyyy/MM/dd");
        if(!TextUtils.isEmpty(delidate) && !TextUtils.isEmpty(pickdate)) {
            CustomerOrderInput customerOrderInput = CustomerOrderInput.builder()
                    .customerId(customer.id())
                    .id(idOder)
                    .deliveryDate(delidate)
                    .pickUpDate(pickdate)
                    .pickUpTimeId(TimePickupOB.getId())
                    .deliveryTimeId(TimeDeliveryOB.getId())
                    .status(status)
                    .promotionId(idPromotion)
                    .updateBy(customer.id())
                    .updateDate(Util.getDate().toString())
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
                Log.i("UpdateOrderAndetail", obOrderDetail.toString());
                list.add(orderDetailInput);
            }

            GraphqlClient.getApolloClient(token, false).mutate(UpdateOrderAndDetailMutation.builder()
                    .cus(customerOrderInput)
                    .list(list)
                    .build()
            ).enqueue(new ApolloCall.Callback<UpdateOrderAndDetailMutation.Data>() {
                @Override
                public void onResponse(@NotNull Response<UpdateOrderAndDetailMutation.Data> response) {
                    Log.i("UpdateOrderAndetail", response.errors().toString());
                    if(response.data().updateOrderAndDetail().customerOrder() != null) {
                        if(!TextUtils.isEmpty(response.data().updateOrderAndDetail().customerOrder().id())) {
                            InfoOrderDetailActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnSave_Edit.setText(R.string.edit);
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Update_order_detail_success), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull ApolloException e) {

                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Update_order_detail_fail), Toast.LENGTH_LONG).show();
        }
    }

    private void editOrder() {
        setStatusOrder(DRAFT);
        btnSave_Edit.setText(getResources().getString(R.string.save));
        isClickClothesItem = true;
        initializeSchedule();
        initializePromotion();
    }

    private void initializePromotion() {
        promotionLayout.setVisibility(View.VISIBLE);
        promotionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickPromotion();
            }
        });
    }

    private void clickPromotion() {
        GraphqlClient.getApolloClient(token, false).query(GetPromotionBranchsQuery.builder().branchID(idBranch).build())
                .enqueue(new ApolloCall.Callback<GetPromotionBranchsQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GetPromotionBranchsQuery.Data> response) {
                        promotionList.clear();
                        List<GetPromotionBranchsQuery.Node> nodes = response.data().allPromotionBranches().nodes();
                        for(GetPromotionBranchsQuery.Node node : nodes) {
                            promotionList.add(new OBPromotion(node.promotionByPromotionId().id(), node.promotionByPromotionId().promotionName(), null, node.promotionByPromotionId().promotionCode(),node.promotionByPromotionId().dateStart(),node.promotionByPromotionId().dateEnd() ,node.promotionByPromotionId().sale()));
                        }
                        InfoOrderDetailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ItemPromotionListDialogFragment promotionListDialogFragment = ItemPromotionListDialogFragment.newInstance(promotionList);
                                promotionListDialogFragment.show(getSupportFragmentManager(),promotionListDialogFragment.getTag());
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e("branchPromotion_err", e.getCause() +" - "+e);
                    }
                });
    }

    private void setStatusOrder(String status) {
        GraphqlClient.getApolloClient(token, false).mutate(UpdateStatusMutation.builder()
                .idOrder(idOder)
                .idcus(customer.id())
                .status(status)
                .build())
                .enqueue(new ApolloCall.Callback<UpdateStatusMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<UpdateStatusMutation.Data> response) {
                        if(response.data().updateCustomerOrderById().customerOrder().status().equals(DECLINED)){
                            InfoOrderDetailActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    popup.createSuccessDialog(R.string.cancel_order_result, R.string.btn_ok, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            // TODO route parent
                                            finish();
                                            popup.hide();
                                        }
                                    });
                                    popup.show();
                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });
    }

    private void initializeSchedule() {
        btnChooseSchedule.setVisibility(View.VISIBLE);
        btnChooseSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScheduleClick();
            }
        });

    }

    private void ScheduleClick() {
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
                        InfoOrderDetailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!TextUtils.isEmpty(idBranch)) {
                                    if(datePickupValue != null && dateDeliveryValue != null && TimePickupOB != null && TimeDeliveryOB != null) {
                                        PickupTimeDeliveryDialogFragment pickupTimeDeliveryDialogFragment = PickupTimeDeliveryDialogFragment.newInstance(listTimePickup, listTimeDelivery, TimePickupOB, TimeDeliveryOB, datePickupValue, dateDeliveryValue, idBranch);
                                        pickupTimeDeliveryDialogFragment.show(getSupportFragmentManager(), pickupTimeDeliveryDialogFragment.getTag());
                                    }
                                    else {
                                        PickupTimeDeliveryDialogFragment pickupTimeDeliveryDialogFragment = PickupTimeDeliveryDialogFragment.newInstance(listTimePickup, listTimeDelivery, null, null, null, null, idBranch);
                                        pickupTimeDeliveryDialogFragment.show(getSupportFragmentManager(), pickupTimeDeliveryDialogFragment.getTag());
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.err_not_found_branch), Toast.LENGTH_LONG).show();
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

    private void generateQRCodeImage(String text, int width, int height)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        height = bitMatrix.getHeight();
        width = bitMatrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        img_qrcode.setImageBitmap(bmp);
    }

    private void getOrderDetailFromServer() {
        popup.createLoadingDialog();
        popup.show();
        GraphqlClient.getApolloClient(token, false).query(GetOrdetailByOrderidQuery.builder()
                .idOrder(idOder)
                .build()
        ).enqueue(new ApolloCall.Callback<GetOrdetailByOrderidQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetOrdetailByOrderidQuery.Data> response) {
                if(response.data().customerOrderById() != null){
                    Log.i("responsedata", response.data().toString());
                    datePickupValue = parseDate(response.data().customerOrderById().pickUpDate(), "yyyy-MM-dd", "dd/MM/yyyy");
                    dateDeliveryValue = parseDate(response.data().customerOrderById().deliveryDate(), "yyyy-MM-dd", "dd/MM/yyyy");
                    deliveryPlaceValue = response.data().customerOrderById().deliveryPlace();
                    pickupPlaceValue = response.data().customerOrderById().pickUpPlace();

                    TimePickupOB.setId(response.data().customerOrderById().timeScheduleByPickUpTimeId().id());
                    TimePickupOB.setTimeStart(response.data().customerOrderById().timeScheduleByPickUpTimeId().timeStart());
                    TimePickupOB.setTimeEnd(response.data().customerOrderById().timeScheduleByPickUpTimeId().timeEnd());
                    TimePickupOB.setDisplay(true);

                    TimeDeliveryOB.setId(response.data().customerOrderById().timeScheduleByDeliveryTimeId().id());
                    TimeDeliveryOB.setTimeStart(response.data().customerOrderById().timeScheduleByDeliveryTimeId().timeStart());
                    TimeDeliveryOB.setTimeEnd(response.data().customerOrderById().timeScheduleByDeliveryTimeId().timeEnd());
                    TimeDeliveryOB.setDisplay(true);

                    pickupTimeValue = response.data().customerOrderById().timeScheduleByPickUpTimeId().timeStart() + "-" + response.data().customerOrderById().timeScheduleByPickUpTimeId().timeEnd();
                    deliveryTimeValue = response.data().customerOrderById().timeScheduleByDeliveryTimeId().timeStart() + "-" + response.data().customerOrderById().timeScheduleByDeliveryTimeId().timeEnd();
                    if(response.data().customerOrderById().promotionByPromotionId() != null){
                        promotionValue = response.data().customerOrderById().promotionByPromotionId().sale();
                        idPromotion = response.data().customerOrderById().promotionByPromotionId().id();
                    }
                    List<GetOrdetailByOrderidQuery.Node> nodes = response.data().customerOrderById().orderDetailsByOrderId().nodes();
                    if(nodes.size() > 0) {
                        for(GetOrdetailByOrderidQuery.Node node : nodes) {
                            OBProduct obProduct = new OBProduct();
                            OBOrderDetail obOrderDetail = new OBOrderDetail();
                            obProduct.setTitle(node.productByProductId().productName());
                            obProduct.setId(node.productByProductId().id());
                            obProduct.setAvatar(node.productByProductId().postByProductAvatar().headerImageFile());
                            obOrderDetail.setProduct(obProduct);
                            if(node.unitId().equals(KG)){
                                obOrderDetail.setUnit(UNIT_NAME_KG);
                            } else {
                                obOrderDetail.setUnit(UNIT_NAME_ITEM);
                                obOrderDetail.setCount(node.amount().longValue());
                            }
                            obOrderDetail.setUnitID(node.unitId());
                            obOrderDetail.setPrice(node.unitPriceByUnitPrice().price());
                            obOrderDetail.setServiceName(node.serviceTypeByServiceTypeId().serviceTypeName());
                            obOrderDetail.setIdService(node.serviceTypeByServiceTypeId().id());
                            if(node.colorByColorId() != null) {
                                obOrderDetail.setColorID(node.colorByColorId().id());
                                obOrderDetail.setColor(node.colorByColorId().colorName());
                            }
                            if(node.materialByMaterialId() != null) {
                                obOrderDetail.setMaterialID(node.materialByMaterialId().id());
                                obOrderDetail.setMaterial(node.materialByMaterialId().materialName());
                            }
                            if(node.labelByLabelId() != null) {
                                obOrderDetail.setLabelID(node.labelByLabelId().id());
                                obOrderDetail.setLabel(node.labelByLabelId().labelName());
                            }
                            orderDetailList.add(obOrderDetail);
                        }
                    }
                    InfoOrderDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initializeView();
                            popup.hide();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });
    }

    private void initializeView() {
        if(TextUtils.isEmpty(promotionValue)) {
            promotionLayout.setVisibility(View.GONE);
        } else {
            promotionLayout.setVisibility(View.VISIBLE);
            salePercent = Integer.parseInt(promotionValue);
            promotion.setText(promotionValue + "%");
        }
        if(!TextUtils.isEmpty(dateDeliveryValue)){
            deliveryDate.setText(dateDeliveryValue);
        }
        if(!TextUtils.isEmpty(datePickupValue)){
            pickUpdate.setText(datePickupValue);
        }
        if(!TextUtils.isEmpty(deliveryTimeValue)){
            deliveryTime.setText(deliveryTimeValue);
        }
        if(!TextUtils.isEmpty(pickupTimeValue)){
            pickupTime.setText(pickupTimeValue);
        }
        if(!TextUtils.isEmpty(deliveryPlaceValue)){
            deliveryPlace.setText(deliveryPlaceValue);
        }
        if(!TextUtils.isEmpty(pickupPlaceValue)){
            pickupPlace.setText(pickupPlaceValue);
        }

        adapter = new ListClothesAdapter(InfoOrderDetailActivity.this , orderDetailList, this);
        listClothes.setAdapter(adapter);
        countTotal.setText(adapter.sumCount() + " " + getResources().getString(R.string.item));
        if(adapter.sumPrice(salePercent) != 0) {
            totalPrice.setText(dec.format(adapter.sumPrice(salePercent)) + " VND");
        } else {
            totalPrice.setText(getResources().getString(R.string.total_price));
        }
    }


    private void initToolbar() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.info_order);
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

    @Override
    public void clickClothes(OBOrderDetail obOrderDetail) {
        if(isClickClothesItem) {
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
                                                 Intent intent = new Intent(InfoOrderDetailActivity.this, DetailPrepareOrderClothesActivity.class);
                                                 intent.putExtra(OB_ORDERDETAIL, obOrderDetail);
                                                 intent.putExtra(EDIT, true);
                                                 intent.putExtra(OB_UNIT_PRICE_ITEM, obPriceItem);
                                                 intent.putExtra(OB_UNIT_PRICE_KG, obPriceKg);
                                                 startActivityForResult(intent, REQUEST_CODE);
                                             } else if(obOrderDetail.getUnitID().equals(KG)) {
                                                 OBPrice obPriceItem = new OBPrice(unitPrices.get(0).id(), unitPrices.get(0).price());
                                                 OBPrice obPriceKg = new OBPrice(obOrderDetail.getPriceID(), obOrderDetail.getPrice());
                                                 Intent intent = new Intent(InfoOrderDetailActivity.this, DetailPrepareOrderClothesActivity.class);
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
    }

    @Override
    public void clickDel(int position) {

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
    public void onItemPromotionClicked(int position) {
        promotion.setText(promotionList.get(position).getSale() + "%");
        salePercent = Integer.parseInt(promotionList.get(position).getSale());
        idPromotion = promotionList.get(position).getId();
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
            if(adapter.sumPrice(salePercent) != 0) {
                totalPrice.setText(dec.format(adapter.sumPrice(salePercent)) + " VND");
            } else {
                totalPrice.setText(getResources().getString(R.string.total_price));
            }
            countTotal.setText(adapter.sumCount() + " " + getResources().getString(R.string.item));
        }
    }
}
