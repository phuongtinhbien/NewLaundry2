package com.example.vuphu.newlaundry;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.vuphu.newlaundry.Utils.PreferenceUtil;

public class ConfigIPPort extends AppCompatActivity {
    private TextInputEditText txtIp, txtPort;
    private Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_ipport);
        txtIp = findViewById(R.id.ipconfig);
        txtPort = findViewById(R.id.portconfig);
        btnOK = findViewById(R.id.btn_OK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    String ip_port = txtIp.getText().toString() + ":" + txtPort.getText().toString();
                    PreferenceUtil.setIpconfig(getApplicationContext(), ip_port);
                }
            }
        });
    }

    private boolean validate() {
        return (!TextUtils.isEmpty(txtIp.getText().toString()) && (!TextUtils.isEmpty(txtPort.getText().toString())));
    }
}
