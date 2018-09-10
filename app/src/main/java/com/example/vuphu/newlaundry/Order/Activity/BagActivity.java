package com.example.vuphu.newlaundry.Order.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.vuphu.newlaundry.R;

public class BagActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button checkOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);
        initToolbar();
        init();
    }

    private void init() {
        checkOut = findViewById(R.id.see_your_bag);
        checkOut.setText("Check out");
        checkOut.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  startActivity(new Intent(getApplicationContext(), PrepareOrderAddressActivity.class));
              }
          }

        );
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Your bag");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_clear);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.home){
            onBackPressed();
            return true;
        }
        return false;
    }
}
