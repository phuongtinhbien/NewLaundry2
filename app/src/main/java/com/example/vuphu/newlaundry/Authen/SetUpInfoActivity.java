package com.example.vuphu.newlaundry.Authen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.example.vuphu.newlaundry.ItemListDialogFragment;
import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetUpInfoActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private List<String> genderList;

    private Popup popup;
    private EditText gender, phone, address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_info);
        initToolbar();
        init();
    }

    private void init() {

        popup = new Popup(this);
        genderList = Arrays.asList(getResources().getStringArray(R.array.arr_gender));
        gender = findViewById(R.id.set_up_gender);
        phone = findViewById(R.id.set_up_phone);
        address = findViewById(R.id.set_up_address);
        Util.showHideCursor(gender);
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               popup.createListPopup(genderList, getResources().getString(R.string.input_gender), gender);
               popup.show();
            }
        });
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Set up information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public boolean validate(){
        boolean validate = true;


        return validate;
    }
}
