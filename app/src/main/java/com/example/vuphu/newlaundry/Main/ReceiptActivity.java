package com.example.vuphu.newlaundry.Main;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.GetCustomerQuery;
import com.example.vuphu.newlaundry.GetReceiptByOrderIdQuery;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Main.Adapter.AdapterListServiceWeight;
import com.example.vuphu.newlaundry.Order.Adapter.ListClothesAdapter;
import com.example.vuphu.newlaundry.Order.IFOBPrepareOrder;
import com.example.vuphu.newlaundry.Order.OBOrderDetail;
import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.Product.OBProduct;
import com.example.vuphu.newlaundry.Promotion.OBPromotion;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.vuphu.newlaundry.Utils.StringKey.ID_ORDER;
import static com.example.vuphu.newlaundry.Utils.StringKey.ITEM;
import static com.example.vuphu.newlaundry.Utils.StringKey.KG;
import static com.example.vuphu.newlaundry.Utils.StringKey.UNDEFINE;

public class ReceiptActivity extends AppCompatActivity implements IFOBPrepareOrder {
    private CircleImageView nav_img_avatar;
    private TextView nav_txt_name, nav_txt_email, nav_txt_phone, info_order_pickup_place,item_receipt_promotion,
            info_order_delivery_place, receipt_date_pick_up, receipt_time_pick_up, receipt_date_delivery,
            receipt_time_delivery, item_receipt_total, item_receipt_total_weight, item_receipt_total_items;
    private RecyclerView list_receipt_service_weight, list_clothes;
    private LinearLayout check_out_promotion;
    private ArrayList<OBService_Weight> listService_weight;
    private AdapterListServiceWeight adapterService;
    private ArrayList<OBOrderDetail> listOBOrderDetail;
    private ListClothesAdapter adapterClothes;
    private String token;
    private Toolbar toolbar;
    private String datePickupValue, dateDeliveryValue, promotionValue, pickupPlaceValue, deliveryPlaceValue, pickupTimeValue, deliveryTimeValue ;
    private Popup popup;
    private GetCustomerQuery.CustomerById customer;
    private String idOrder;
    private ArrayList<OBPromotion> promotionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        initToolbar();
        initialize();
    }

    private void initialize() {
        nav_txt_phone = findViewById(R.id.nav_txt_phone);
        nav_txt_email = findViewById(R.id.nav_txt_email);
        nav_txt_name = findViewById(R.id.nav_txt_name);
        nav_img_avatar = findViewById(R.id.nav_img_avatar);
        info_order_delivery_place = findViewById(R.id.info_order_delivery_place);
        info_order_pickup_place = findViewById(R.id.info_order_pickup_place);
        item_receipt_promotion = findViewById(R.id.item_receipt_promotion);
        receipt_date_pick_up = findViewById(R.id.receipt_date_pick_up);
        receipt_time_pick_up = findViewById(R.id.receipt_time_pick_up);
        receipt_date_delivery = findViewById(R.id.receipt_date_delivery);
        receipt_time_delivery = findViewById(R.id.receipt_time_delivery);
        item_receipt_total = findViewById(R.id.item_receipt_total);
        item_receipt_total_weight = findViewById(R.id.item_receipt_total_weight);
        item_receipt_total_items = findViewById(R.id.item_receipt_total_items);
        check_out_promotion = findViewById(R.id.check_out_promotion);
        check_out_promotion.setVisibility(View.GONE);
        list_clothes = findViewById(R.id.list_prepare_order_clothes);
        list_receipt_service_weight = findViewById(R.id.list_receipt_service_weight);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        list_clothes.setLayoutManager(linearLayoutManager);
        list_receipt_service_weight.setLayoutManager(linearLayoutManager1);

        listService_weight = new ArrayList<>();
        promotionList = new ArrayList<>();
        listOBOrderDetail = new ArrayList<>();

        Intent intent = getIntent();
        if(intent.hasExtra(ID_ORDER)) {
            idOrder = intent.getStringExtra(ID_ORDER);
        }
        token = PreferenceUtil.getAuthToken(ReceiptActivity.this);
        customer = PreferenceUtil.getCurrentUser(ReceiptActivity.this);
        if(customer != null) {
            Picasso.get().load(Uri.parse(customer.postByCustomerAvatar().headerImageFile())).into(nav_img_avatar);
            nav_txt_name.setText(customer.fullName());
            nav_txt_email.setText(customer.email());
            nav_txt_phone.setText(customer.phone());
        }
        getReceiptFromDB();

    }

    private void getReceiptFromDB() {
        GraphqlClient.getApolloClient(token, false).query(GetReceiptByOrderIdQuery.builder().idOrder(idOrder).build())
                .enqueue(new ApolloCall.Callback<GetReceiptByOrderIdQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GetReceiptByOrderIdQuery.Data> response) {
                        if(response.data().allReceipts().nodes().size() > 0){
                            GetReceiptByOrderIdQuery.Node node = response.data().allReceipts().nodes().get(0);
                            if(!TextUtils.isEmpty(node.deliveryDate())){
                                dateDeliveryValue = node.deliveryDate();
                            }
                            else {
                                dateDeliveryValue = node.customerOrderByOrderId().deliveryDate();
                            }
                            if(!TextUtils.isEmpty(node.deliveryPlace())) {
                                deliveryPlaceValue = node.deliveryPlace();
                            }
                            else {
                                deliveryPlaceValue = node.customerOrderByOrderId().deliveryPlace();
                            }
                            if(!TextUtils.isEmpty(node.deliveryTime())) {
                                deliveryTimeValue = node.deliveryTime();
                            }
                            else {
                                deliveryTimeValue = node.customerOrderByOrderId().timeScheduleByDeliveryTimeId().timeStart() + "-" + node.customerOrderByOrderId().timeScheduleByDeliveryTimeId().timeEnd();
                            }
                            if(!TextUtils.isEmpty(node.pickUpDate())) {
                                datePickupValue = node.pickUpDate();
                            }
                            else {
                                datePickupValue = node.customerOrderByOrderId().pickUpDate();
                            }
                            if(!TextUtils.isEmpty(node.pickUpPlace())) {
                                pickupPlaceValue = node.pickUpPlace();
                            }
                            else {
                                pickupPlaceValue = node.customerOrderByOrderId().pickUpPlace();
                            }
                            if(!TextUtils.isEmpty(node.pickUpTime())) {
                                pickupTimeValue = node.pickUpTime();
                            }
                            else {
                                pickupTimeValue = node.customerOrderByOrderId().timeScheduleByPickUpTimeId().timeStart() + "-" + node.customerOrderByOrderId().timeScheduleByPickUpTimeId().timeEnd();
                            }

                            List<GetReceiptByOrderIdQuery.Node1> list = node.receiptDetailsByReceiptId().nodes();
                            if(list.size() > 0) {
                                ArrayList<String> serviceIDs = new ArrayList<>();
                                for (GetReceiptByOrderIdQuery.Node1 node1 : list) {
                                    OBOrderDetail obOrderDetail = new OBOrderDetail();
                                    OBProduct obProduct = new OBProduct();
                                    if(node1.unitId().equals(ITEM)) {
                                        if(node1.recievedAmount() != null) {
                                            obOrderDetail.setCount(node1.recievedAmount().longValue());
                                        }
                                        else {
                                            obOrderDetail.setCount(node1.amount());
                                        }
                                        obOrderDetail.setUnitID(node1.unitId());
                                        obOrderDetail.setUnit(getResources().getString(R.string.item));
                                    }
                                    else if(node1.unitId().equals(KG)) {
                                        if(node1.recievedAmount() != null) {
                                            obOrderDetail.setCount(node1.recievedAmount().longValue());
                                        }
                                        obOrderDetail.setUnitID(node1.unitId());
                                        obOrderDetail.setUnit(getResources().getString(R.string.kg));
                                    }
                                    obProduct.setTitle(node1.productByProductId().productName());
                                    obProduct.setId(node1.productByProductId().id());
                                    obOrderDetail.setProduct(obProduct);
                                    obOrderDetail.setIdService(node1.serviceTypeByServiceTypeId().id());
                                    obOrderDetail.setServiceName(node1.serviceTypeByServiceTypeId().serviceTypeName());
                                    obOrderDetail.setPrice(node1.unitPriceByUnitPrice().price());
                                    if(node1.labelByLabelId() != null) {
                                        obOrderDetail.setLabel(node1.labelByLabelId().labelName());
                                    }
                                    if(node1.materialByMaterialId() != null) {
                                        obOrderDetail.setMaterial(node1.materialByMaterialId().materialName());
                                    }
                                    if(node1.colorByColorId() != null) {
                                        obOrderDetail.setColor(node1.colorByColorId().colorName());
                                    }
                                    listOBOrderDetail.add(obOrderDetail);
                                    if(obOrderDetail.getUnitID().equals(KG) && !serviceIDs.contains(obOrderDetail.getIdService())) {
                                        OBService_Weight obService_weight = new OBService_Weight();
                                        obService_weight.setIdService(obOrderDetail.getIdService());
                                        obService_weight.setPrice(obOrderDetail.getPrice());
                                        obService_weight.setServiceName(obOrderDetail.getServiceName());
                                        if(obOrderDetail.getCount() > 0) {
                                            obService_weight.setWeight(obOrderDetail.getCount());
                                        } else {
                                            obService_weight.setWeight(0.0d);
                                        }
                                        serviceIDs.add(obOrderDetail.getIdService());
                                        listService_weight.add(obService_weight);
                                    }
                                }
                                ReceiptActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        intializeView();
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


    private void intializeView() {
        info_order_pickup_place.setText(pickupPlaceValue);
        info_order_delivery_place.setText(deliveryPlaceValue);
        receipt_date_delivery.setText(parseDate(dateDeliveryValue));
        receipt_date_pick_up.setText(parseDate(datePickupValue));
        receipt_time_pick_up.setText(pickupTimeValue);
        receipt_time_delivery.setText(deliveryTimeValue);

        adapterClothes = new ListClothesAdapter(ReceiptActivity.this, listOBOrderDetail, this);
        adapterService = new AdapterListServiceWeight(listService_weight, ReceiptActivity.this);
        list_clothes.setAdapter(adapterClothes);
        list_receipt_service_weight.setAdapter(adapterService);
        if(adapterClothes.sumPrice() +  adapterService.sumPrice() > 0) {
            item_receipt_total.setText(adapterClothes.sumPrice() + adapterService.sumPrice() + " VND");
        }
        else {
            item_receipt_total.setText(getResources().getString(R.string.total_price));
        }
        if(adapterClothes.sumCount() > 0) {
            item_receipt_total_items.setText(adapterClothes.sumCount() + " " + getResources().getString(R.string.item));
        }
        else {
            item_receipt_total_items.setText(UNDEFINE + " " + getResources().getString(R.string.item));
        }
        if(adapterService.sumWeight() > 0) {
            item_receipt_total_weight.setText(adapterService.sumWeight() + " " + getResources().getString(R.string.kg));
        }
        else {
            item_receipt_total_weight.setText(UNDEFINE + " " + getResources().getString(R.string.kg));
        }

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
        setTitle(R.string.title_receipt);
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

    }

    @Override
    public void clickDel(int position) {

    }
}
