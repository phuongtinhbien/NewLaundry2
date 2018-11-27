package com.example.vuphu.newlaundry.Order.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.design.card.MaterialCardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.GetColorsQuery;
import com.example.vuphu.newlaundry.GetLabelQuery;
import com.example.vuphu.newlaundry.GetMaterialsQuery;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.ItemListDialogFragment;
import com.example.vuphu.newlaundry.Order.OBOrderDetail;
import com.example.vuphu.newlaundry.Order.OBPrice;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.github.florent37.androidslidr.Slidr;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.vuphu.newlaundry.Utils.StringKey.EDIT;
import static com.example.vuphu.newlaundry.Utils.StringKey.ITEM;
import static com.example.vuphu.newlaundry.Utils.StringKey.KG;
import static com.example.vuphu.newlaundry.Utils.StringKey.OB_ORDERDETAIL;
import static com.example.vuphu.newlaundry.Utils.StringKey.OB_UNIT_PRICE_ITEM;
import static com.example.vuphu.newlaundry.Utils.StringKey.OB_UNIT_PRICE_KG;
import static com.example.vuphu.newlaundry.Utils.StringKey.TYPE_LIST_COLOR;
import static com.example.vuphu.newlaundry.Utils.StringKey.TYPE_LIST_MATERIAL;
import static com.example.vuphu.newlaundry.Utils.StringKey.TYPE_LIST_PRODUCTION;
import static com.example.vuphu.newlaundry.Utils.StringKey.TYPE_LIST_UNIT;
import static com.example.vuphu.newlaundry.Utils.Util.checkDuplicateClothes;

public class DetailPrepareOrderClothesActivity extends AppCompatActivity implements ItemListDialogFragment.Listener {

    private ArrayList<String> productionIDList = new ArrayList<>();
    private ArrayList<String> materialIDList = new ArrayList<>();
    private ArrayList<String> colorIDList = new ArrayList<>();
    private ArrayList<String> unitIDList = new ArrayList<>();

    private ArrayList<String> unitList = new ArrayList<>();
    private ArrayList<String> productionList = new ArrayList<>();
    private ArrayList<String> materialList = new ArrayList<>();
    private ArrayList<String> colorList = new ArrayList<>();
    private static List<GetColorsQuery.Node> listColor = new ArrayList<>();
    private Button addToBag;
    private Toolbar toolbar;
    private Slidr slidr;
    private long count = 1;
    private LinearLayout totalPanel;
    private boolean edit = false;

    private TextView productionValue, colorValue, materialValue, title, unitValue;
    private EditText note;
    private MaterialCardView production, color, material, unit;
    private ImageView imgClothes;
//    private Chip price;
    private TextView price;
    private String token;

    private OBOrderDetail obOrderDetail = new OBOrderDetail();
    private OBPrice obPriceItem = new OBPrice();
    private OBPrice obPriceKG = new OBPrice();
    private Intent intent;
    private DecimalFormat dec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_prepare_order_clothes);
        initToolbar();
        init();

    }

    @SuppressLint("ResourceType")
    private void initList() {
        clickUnit();

        GraphqlClient.getApolloClient(token, false).query(GetLabelQuery.builder().build())
                .enqueue(new ApolloCall.Callback<GetLabelQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GetLabelQuery.Data> response) {
                        List<GetLabelQuery.Node> labelList = response.data().allLabels().nodes();
                        if(!labelList.isEmpty()) {
                            for(GetLabelQuery.Node node: labelList){
                                productionList.add(node.labelName());
                                productionIDList.add(node.id());
                            }
                            DetailPrepareOrderClothesActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    clickLabel();
                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e("getLabel", e.getCause() +" - "+e);
                    }
                });
        GraphqlClient.getApolloClient(token, false).query(GetColorsQuery.builder().build())
                .enqueue(new ApolloCall.Callback<GetColorsQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GetColorsQuery.Data> response) {
                        listColor = response.data().allColors().nodes();
                        DetailPrepareOrderClothesActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!listColor.isEmpty()) {
                                    for(GetColorsQuery.Node node: listColor){
                                        colorList.add(node.colorName());
                                        colorIDList.add(node.id());
                                    }
                                    DetailPrepareOrderClothesActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            clickColor();
                                        }
                                    });
                                }
                            }
                        });

                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e("getColors", e.getCause() +" - "+e);
                    }
                });
        GraphqlClient.getApolloClient(token, false).query(GetMaterialsQuery.builder().build())
                .enqueue(new ApolloCall.Callback<GetMaterialsQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GetMaterialsQuery.Data> response) {
                        List<GetMaterialsQuery.Node> listMaterial = response.data().allMaterials().nodes();

                        if(!listMaterial.isEmpty()) {
                            for(GetMaterialsQuery.Node node : listMaterial){
                                materialList.add(node.materialName());
                                materialIDList.add(node.materialName());
                            }
                            DetailPrepareOrderClothesActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    clickMaterial();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e("getMaterials", e.getCause() +" - "+e);
                    }
                });
    }

    private void clickUnit() {
        unitIDList.add("1");
        unitIDList.add("4");
        unitList.add(getResources().getString(R.string.item));
        unitList.add(getResources().getString(R.string.kg));
        unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemListDialogFragment bottomSheetDialogFragment = ItemListDialogFragment.newInstance(TYPE_LIST_UNIT, unitList);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
    }

    private void clickMaterial() {

        material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemListDialogFragment bottomSheetDialogFragment = ItemListDialogFragment.newInstance(TYPE_LIST_MATERIAL, materialList);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
    }

    private void clickColor() {
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemListDialogFragment bottomSheetDialogFragment = ItemListDialogFragment.newInstance(TYPE_LIST_COLOR, colorList);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
    }

    private void clickLabel() {
        production.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemListDialogFragment bottomSheetDialogFragment = ItemListDialogFragment.newInstance(TYPE_LIST_PRODUCTION, productionList);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
    }

    public void init(){
        dec = new DecimalFormat("##,###,###,###");
        imgClothes = findViewById(R.id.cloth_image);
        materialValue = findViewById(R.id.item_prepare_order_txt_material);
        material = findViewById(R.id.item_prepare_order_material);
        colorValue = findViewById(R.id.item_prepare_order_txt_color);
        color = findViewById(R.id.item_prepare_order_color);
        productionValue = findViewById(R.id.item_prepare_order_txt_production);
        production = findViewById(R.id.item_prepare_order_production);
        unitValue = findViewById(R.id.item_prepare_order_txt_unit);
        unit = findViewById(R.id.item_prepare_order_unit);
        addToBag = findViewById(R.id.see_your_bag);
        title = findViewById(R.id.item_prepare_order_txt_title);
        note = findViewById(R.id.item_prepare_order_txt_note);
        totalPanel = findViewById(R.id.total_panel_spml);
        slidr = findViewById(R.id.item_prepare_order_seek_count);
        price = findViewById(R.id.chip_pricing);

        token = PreferenceUtil.getAuthToken(getApplicationContext());
        intent = getIntent();
        obOrderDetail = (OBOrderDetail) intent.getSerializableExtra(OB_ORDERDETAIL);
        obPriceItem = (OBPrice) intent.getSerializableExtra(OB_UNIT_PRICE_ITEM);
        obPriceKG = (OBPrice) intent.getSerializableExtra(OB_UNIT_PRICE_KG);

        Picasso.get().load(Uri.parse(obOrderDetail.getProduct().getAvatar())).into(imgClothes);
        if(intent.hasExtra(EDIT)) {
            edit = intent.getBooleanExtra(EDIT, true);
            addToBag.setText(R.string.save);

            note.setText(obOrderDetail.getNote());
            if (obOrderDetail.getLabel() != null) {
                productionValue.setText(obOrderDetail.getLabel());
            } else {
                productionValue.setText(R.string.Undefine);
            }
            if (obOrderDetail.getLabel() != null) {
                colorValue.setText(obOrderDetail.getColor());
            } else {
                colorValue.setText(R.string.Undefine);
            }
            if(obOrderDetail.getMaterial() != null) {
                materialValue.setText(obOrderDetail.getMaterial());
            } else {
                materialValue.setText(R.string.Undefine);
            }
            if(obOrderDetail.getUnitID().equals(KG)) {
                slidr.setVisibility(View.GONE);
                price.setText(dec.format(obOrderDetail.getPrice()) + " VND/" + getResources().getString(R.string.kg));
            } else if(obOrderDetail.getUnitID().equals(ITEM)) {
                slidr.setVisibility(View.VISIBLE);
                countValue(obOrderDetail.getCount());
                price.setText(dec.format(obOrderDetail.getPrice()) + " VND/" + getResources().getString(R.string.item));
            }
            unitValue.setText(obOrderDetail.getUnit());
//
//            production.setEnabled(false);
//            color.setEnabled(false);
//            material.setEnabled(false);

        } else {
            addToBag.setText(R.string.add_to_your_bag);
        }

        title.setText(obOrderDetail.getProduct().getTitle());
        initList();

        totalPanel.setVisibility(View.GONE);

        addToBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    if(edit){
                        if(TextUtils.isEmpty(note.getText().toString())) {
                            obOrderDetail.setNote(note.getText().toString());
                        }
                        if(obOrderDetail.getUnitID().equals(ITEM)) {
                            count = (long) slidr.getCurrentValue();
                            obOrderDetail.setCount(count);
                        } else {
                            obOrderDetail.setCount(0);
                        }
                        Intent intentResult = new Intent();
                        intentResult.putExtra(OB_ORDERDETAIL, obOrderDetail);
                        setResult(RESULT_OK, intentResult);
                        finish();
                    }
                    else if(!TextUtils.isEmpty(obOrderDetail.getUnitID())) {
                        if(!TextUtils.isEmpty(note.getText().toString())){
                            obOrderDetail.setNote(note.getText().toString());
                        }
                        if(obOrderDetail.getUnitID().equals(ITEM)) {
                            if(Float.toString(slidr.getCurrentValue()) != null) {
                                count = (long) slidr.getCurrentValue();
                            } else {
                                count = 1;
                            }
                            obOrderDetail.setCount(count);
                        }

                        ArrayList<OBOrderDetail> list = PreferenceUtil.getListOrderDetail(DetailPrepareOrderClothesActivity.this);
                        boolean flag = false;
                        for (OBOrderDetail orderDetail: list) {
                            if (checkDuplicateClothes(orderDetail.getColorID(), obOrderDetail.getColorID())
                                    && checkDuplicateClothes(orderDetail.getLabelID(), obOrderDetail.getLabelID())
                                    && checkDuplicateClothes(orderDetail.getMaterialID(), obOrderDetail.getMaterialID())
                                    && checkDuplicateClothes(orderDetail.getProduct().getId(), obOrderDetail.getProduct().getId())
                                    && checkDuplicateClothes(orderDetail.getIdService(), obOrderDetail.getIdService())
                                    && checkDuplicateClothes(orderDetail.getUnitID(), obOrderDetail.getUnitID())) {
                                if (orderDetail.getUnitID().equals(ITEM)) {
                                    long count = orderDetail.getCount();
                                    orderDetail.setCount(count + obOrderDetail.getCount());
                                    list.set(list.indexOf(orderDetail), orderDetail);
                                    flag = true;
                                    break;
                                } else if (orderDetail.getUnitID().equals(KG)) {
                                    flag = true;
                                    break;
                                }
                            }
                        }
                        if(!flag) {
                            list.add(obOrderDetail);
                        }
                        PreferenceUtil.setListOrderDetail(list, DetailPrepareOrderClothesActivity.this);
                        Intent intent = new Intent(DetailPrepareOrderClothesActivity.this, BagActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(DetailPrepareOrderClothesActivity.this, getResources().getString(R.string.validate_unit), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.detail_clothes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    public Boolean validate() {
//        if( obOrderDetail.getColor() == null || obOrderDetail.getLabel() == null || obOrderDetail.getMaterial() == null) {
//            Toast.makeText(DetailPrepareOrderClothesActivity.this, "Vui lòng chọn đủ thông tin!", Toast.LENGTH_LONG).show();
//            return false;
//        }
//        else {
//            return true;
//        }
        return true;
    }

    public void countValue(long currentValue){
        if(currentValue > 60) {
            slidr.setMax(currentValue);
        }
        else {
            slidr.setMax(60);
        }
        slidr.setMin(1);
        slidr.setCurrentValue(currentValue);
        slidr.setTextFormatter(new Slidr.TextFormatter() {
            @Override
            public String format(float value) {

                return value>0? String.valueOf((int)value) + " " + getResources().getString(R.string.item) : String.valueOf((int)value) + " " + getResources().getString(R.string.item);
            }
        });
        slidr.setListener(new Slidr.Listener() {
            @Override
            public void valueChanged(Slidr slidr, float currentValue) {
                slidr.setCurrentValue(currentValue);
            }

            @Override
            public void bubbleClicked(Slidr slidr) {

            }
        });
    }

    @Override
    public void onItemClicked(String type, int position) {
        switch (type) {
            case TYPE_LIST_PRODUCTION: {
                productionValue.setText(productionList.get(position));
                obOrderDetail.setLabel(productionList.get(position));
                obOrderDetail.setLabelID(productionIDList.get(position));
                break;
            }
            case TYPE_LIST_COLOR: {
                colorValue.setText(colorList.get(position));
                obOrderDetail.setColor(colorList.get(position));
                obOrderDetail.setColorID(colorIDList.get(position));
                break;
            }
            case TYPE_LIST_MATERIAL: {
                materialValue.setText(materialList.get(position));
                obOrderDetail.setMaterial(materialList.get(position));
                obOrderDetail.setMaterialID(materialIDList.get(position));
                break;
            }
            case TYPE_LIST_UNIT: {
                unitValue.setText(unitList.get(position));
                obOrderDetail.setUnitID(unitIDList.get(position));
                obOrderDetail.setUnit(unitList.get(position));
                if(unitIDList.get(position).equals(KG)) {
                    slidr.setVisibility(View.GONE);
                    obOrderDetail.setPriceID(obPriceKG.getId());
                    obOrderDetail.setPrice(obPriceKG.getUnitPrice());
//                    price.setChipText(dec.format(obPriceKG.getUnitPrice()) + " VND/" + getResources().getString(R.string.kg));
                    price.setText(dec.format(obPriceKG.getUnitPrice()) + " VND/" + getResources().getString(R.string.kg));
//                    Log.i("PriceText", price.getChipText());
                    Log.i("PriceText", price.getText().toString());
                }
                else {
                    slidr.setVisibility(View.VISIBLE);
                    obOrderDetail.setPriceID(obPriceItem.getId());
                    obOrderDetail.setPrice(obPriceItem.getUnitPrice());
                    DetailPrepareOrderClothesActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            price.setChipText(dec.format(obPriceItem.getUnitPrice()) + " VND/" + getResources().getString(R.string.item));
                            price.setText(dec.format(obPriceItem.getUnitPrice()) + " VND/" + getResources().getString(R.string.item));
//                            Log.i("PriceText", price.getChipText());
                            Log.i("PriceText", price.getText().toString());
                            countValue(count);
                        }
                    });
                }
                break;
            }
        }
    }
}
