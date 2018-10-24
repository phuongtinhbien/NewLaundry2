package com.example.vuphu.newlaundry;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.vuphu.newlaundry.Order.Activity.PrepareOrderActivity;
import com.uniquestudio.library.CircleCheckBox;

public class ChooseUnitActivity extends AppCompatActivity {

    private static String KG = "4";
    private static String ITEM = "1";
    private Toolbar toolbar;
    private String idService;
    private String serviceName;
    private CircleCheckBox kg;
    private CircleCheckBox item;
    private MaterialButton submit;
    private EditText weight;
    private String unit;
    private String weight_kg;
    private TextInputLayout inputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_unit);
        initToolbar();
        init();
    }

    private void init() {
        Intent intent = getIntent();
        idService = intent.getStringExtra("idService");
        serviceName = intent.getStringExtra("NameService");
        inputLayout = findViewById(R.id.edt_kilogram);
        submit = findViewById(R.id.btn_submit);
        weight = findViewById(R.id.choose_unit_kg);
        item = findViewById(R.id.item_choose_check_box);
        kg = findViewById(R.id.item_choose_check_box1);
        kg.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if(isChecked) {
                    inputLayout.setVisibility(View.VISIBLE);
                    item.setChecked(false);
                    Log.i("onCheckedChanged", "kg: " + isChecked);
                    unit = KG;
                }
                else {
                    inputLayout.setVisibility(View.GONE);
                }
            }
        });

        item.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if(isChecked) {
                    inputLayout.setVisibility(View.GONE);
                    kg.setChecked(false);
                    Log.i("onCheckedChanged", "item: " + isChecked);
                    unit = ITEM;
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ChooseUnitActivity.this, PrepareOrderActivity.class);
                if(weight.getText().toString().length() > 0 && kg.isChecked()) {
                    weight_kg = weight.getText().toString();
                    intent1.putExtra("weight", weight_kg);
                }
                if(kg.isChecked() || item.isChecked()) {
                    intent1.putExtra("unit", unit);
                    intent1.putExtra("idServiceChoose", idService);
                    intent1.putExtra("nameServiceChoose", serviceName);
                    startActivity(intent1);
                    finish();
                }
            }
        });
    }

    private void initToolbar() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Choose unit of clothes");
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
