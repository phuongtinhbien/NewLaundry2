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
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.GetCustomerQuery;
import com.example.vuphu.newlaundry.GetOrdetailByOrderidQuery;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Order.Adapter.ListClothesAdapter;
import com.example.vuphu.newlaundry.Order.OBOrderDetail;

import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.Product.OBProduct;

import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
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
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


import static com.example.vuphu.newlaundry.Utils.StringKey.ID_ORDER;
import static com.example.vuphu.newlaundry.Utils.StringKey.KG;

public class InfoOrderDetailActivity extends AppCompatActivity {

    private String token;
    private Toolbar toolbar;
    private RecyclerView listClothes;
    private CircleImageView avatar;
    private ListClothesAdapter adapter;
    private List<OBOrderDetail> orderDetailList;
    private String datePickupValue, dateDeliveryValue, promotionValue, pickupPlaceValue, deliveryPlaceValue, pickupTimeValue, deliveryTimeValue ;
    private TextView promotion, name, email, phone, pickupPlace, deliveryPlace, pickUpdate, deliveryDate, pickupTime, deliveryTime, countTotal, totalPrice, totalWeight;;
    private Popup popup;
    private String idOder;
    private GetCustomerQuery.CustomerById customer;
    private DecimalFormat dec;
    private ImageView img_qrcode;


    public InfoOrderDetailActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_order_detail);
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
        totalWeight = findViewById(R.id.item_info_order_total_weight);
        totalPrice = findViewById(R.id.item_prepare_order_total);
        img_qrcode = findViewById(R.id.info_detail_qrCode);
        popup = new Popup(InfoOrderDetailActivity.this);
        dec = new DecimalFormat("##,###,###,###");

        Intent intent = getIntent();
        if(intent.hasExtra(ID_ORDER)){
            idOder = intent.getStringExtra(ID_ORDER);
        }
        try {
            generateQRCodeImage(idOder, 500, 500);
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

        promotion = findViewById(R.id.item_prepare_order_promotion);

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
                    datePickupValue = response.data().customerOrderById().pickUpDate();
                    dateDeliveryValue = response.data().customerOrderById().deliveryDate();
                    deliveryPlaceValue = response.data().customerOrderById().deliveryPlace();
                    pickupPlaceValue = response.data().customerOrderById().pickUpPlace();
                    pickupTimeValue = response.data().customerOrderById().timeScheduleByPickUpTimeId().timeStart() + "-" + response.data().customerOrderById().timeScheduleByPickUpTimeId().timeEnd();
                    deliveryTimeValue = response.data().customerOrderById().timeScheduleByDeliveryTimeId().timeStart() + "-" + response.data().customerOrderById().timeScheduleByDeliveryTimeId().timeEnd();
                    promotionValue = response.data().customerOrderById().promotionByPromotionId().promotionCode();
                    List<GetOrdetailByOrderidQuery.Node> nodes = response.data().customerOrderById().orderDetailsByOrderId().nodes();
                    if(nodes.size() > 0) {
                        for(GetOrdetailByOrderidQuery.Node node : nodes) {
                            OBProduct obProduct = new OBProduct();
                            OBOrderDetail obOrderDetail = new OBOrderDetail();
                            obProduct.setTitle(node.productByProductId().productName());
                            obOrderDetail.setProduct(obProduct);
                            obOrderDetail.setCount(node.amount());
                            if(node.unitId().equals(KG)){
                                obOrderDetail.setUnit("Kg");
                            } else {
                                obOrderDetail.setUnit("Item");
                            }
                            obOrderDetail.setUnitID(node.unitId());
                            obOrderDetail.setPrice(node.unitPriceByUnitPrice().price());
                            obOrderDetail.setServiceName(node.serviceTypeByServiceTypeId().serviceTypeName());
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
        if(!TextUtils.isEmpty(promotionValue)){
            promotion.setText(promotionValue);
        }
        if(!TextUtils.isEmpty(dateDeliveryValue)){
            deliveryDate.setText(parseDate(dateDeliveryValue));
        }
        if(!TextUtils.isEmpty(datePickupValue)){
            pickUpdate.setText(parseDate(datePickupValue));
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
        adapter = new ListClothesAdapter(InfoOrderDetailActivity.this , orderDetailList);
        listClothes.setAdapter(adapter);
        countTotal.setText(adapter.sumCount() + " " + getResources().getString(R.string.item));
        totalPrice.setText(dec.format(adapter.sumPrice()) + " VND");
        totalWeight.setText(adapter.sumWeight() + " " + getResources().getString(R.string.kg));
    }

    private String parseDate(String date) {
        SimpleDateFormat sdfOld = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfNew = new SimpleDateFormat("dd/MM/yyyy");
        String result = "";
        try {
            Date d = sdfOld.parse(date);
            result = sdfNew.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }


    private void initToolbar() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Information Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return false;
    }

}
