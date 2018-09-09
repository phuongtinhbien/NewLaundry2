package com.example.vuphu.newlaundry.Order.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.vuphu.newlaundry.Main.MainActivity;
import com.example.vuphu.newlaundry.R;


public class FinalOrderActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_order);
        initToolbar();
    }
    private void initToolbar() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Your invoice");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void backToHome(View v){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }

}
