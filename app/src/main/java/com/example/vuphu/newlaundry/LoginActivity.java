package com.example.vuphu.newlaundry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.example.vuphu.newlaundry.Graphql.Services;
import com.example.vuphu.newlaundry.Main.MainActivity;
import com.example.vuphu.newlaundry.Utils.Util;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private MaterialButton btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private  void init(){
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.btn_login);

        final Services services = new Services(GraphqlClient.getApolloClient(PreferenceUtil.getAuthToken(getApplicationContext())));
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String preToken = PreferenceUtil.getAuthToken(getApplicationContext());
                if (!Util.isEmptyorNull(preToken)) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                else {
                    String token = GraphqlClient
                            .authentication(
                                    email.getText().toString(),
                                    password.getText().toString());

                    PreferenceUtil.setAuthToken(getApplicationContext(),token);

                }
            }
        });

    }

    public void validate(){

    }
}
