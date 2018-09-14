package com.example.vuphu.newlaundry.Order.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.card.MaterialCardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vuphu.newlaundry.ItemListDialogFragment;
import com.example.vuphu.newlaundry.R;
import com.github.florent37.androidslidr.Slidr;

import java.util.ArrayList;
import java.util.Arrays;

public class DetailPrepareOrderClothesActivity extends AppCompatActivity implements ItemListDialogFragment.Listener {

    private static final String TYPE_LIST_PRODUCTION = "P";
    private static final String TYPE_LIST_COLOR = "C";
    private static final String TYPE_LIST_MATERIAL = "M";

    private ArrayList<String> productionList = new ArrayList<>();
    private ArrayList<String> materialList = new ArrayList<>();
    private ArrayList<String> colorList = new ArrayList<>();
    private Button addToBag;
    private Toolbar toolbar;
    private Slidr slidr;
    private LinearLayout totalPanel;

    private TextView productionValue, colorValue, materialValue;

    MaterialCardView production,color, material;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_prepare_order_clothes);
        initToolbar();
        init();
        initList();

    }

    @SuppressLint("ResourceType")
    private void initList() {
        productionList.addAll(Arrays.asList(getResources().getStringArray(R.array.arr_production)));
        colorList.addAll(Arrays.asList(getResources().getStringArray(R.array.arr_color)));
        materialList.addAll(Arrays.asList(getResources().getStringArray(R.array.arr_material)));
    }

    public void init(){
        addToBag = findViewById(R.id.see_your_bag);
        addToBag.setText(R.string.add_to_your_bag);
        slidr = findViewById(R.id.item_prepare_order_seek_count);
        countValue();
        totalPanel = findViewById(R.id.total_panel);
        totalPanel.setVisibility(View.GONE);
        productionValue = findViewById(R.id.item_prepare_order_txt_production);
        colorValue = findViewById(R.id.item_prepare_order_txt_color);
        materialValue = findViewById(R.id.item_prepare_order_txt_material);
        addToBag.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              startActivity(new Intent(getApplicationContext(), BagActivity.class));
                                          }
                                      }
        );
        production = findViewById(R.id.item_prepare_order_production);
        production.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemListDialogFragment bottomSheetDialogFragment = ItemListDialogFragment.newInstance(TYPE_LIST_PRODUCTION,productionList);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
        color = findViewById(R.id.item_prepare_order_color);
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemListDialogFragment bottomSheetDialogFragment = ItemListDialogFragment.newInstance(TYPE_LIST_COLOR,colorList);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        material = findViewById(R.id.item_prepare_order_material);
        material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemListDialogFragment bottomSheetDialogFragment = ItemListDialogFragment.newInstance(TYPE_LIST_MATERIAL,materialList);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });




    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Detail clothes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    public void countValue(){
        slidr.setMax(60);
        slidr.setMin(0);
        slidr.setCurrentValue(0);
        slidr.setTextFormatter(new Slidr.TextFormatter() {
            @Override
            public String format(float value) {

                return value>0?String.valueOf((int)value) + " items":String.valueOf((int)value) + " item";
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
        if (type.equals(TYPE_LIST_PRODUCTION)){
            productionValue.setText(productionList.get(position));
        }
        else if (type.equals(TYPE_LIST_COLOR)){
            colorValue.setText(colorList.get(position));
        }
        else if (type.equals(TYPE_LIST_MATERIAL)){
            materialValue.setText(materialList.get(position));
        }
    }

}
