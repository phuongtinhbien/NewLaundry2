package com.example.vuphu.newlaundry;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vuphu.newlaundry.Order.Activity.PrepareOrderActivity;
import com.uniquestudio.library.CircleCheckBox;

import static com.example.vuphu.newlaundry.Utils.StringKey.ITEM;
import static com.example.vuphu.newlaundry.Utils.StringKey.KG;

public class ChooseUnitActivity extends AppCompatActivity {

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
                if(kg.isChecked()) {
                    if(!TextUtils.isEmpty(weight.getText().toString())){
                        weight_kg = weight.getText().toString();
                        intent1.putExtra("weight", weight_kg);
                    }
                    else {
                        Toast.makeText(ChooseUnitActivity.this, R.string.weight_null, Toast.LENGTH_LONG).show();
                    }
                }
                if((kg.isChecked() && !TextUtils.isEmpty(weight.getText().toString())) || item.isChecked()) {
                    intent1.putExtra("unit", unit);
                    intent1.putExtra("idServiceChoose", idService);
                    intent1.putExtra("nameServiceChoose", serviceName);
                    startActivity(intent1);
                    finish();
                }
                else {

                }
            }
        });
    }

    private void initToolbar() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.choose_unit_of_clothes);
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
