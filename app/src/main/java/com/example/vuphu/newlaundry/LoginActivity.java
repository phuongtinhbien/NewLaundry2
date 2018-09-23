package com.example.vuphu.newlaundry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Main.MainActivity;
import com.example.vuphu.newlaundry.Utils.Util;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private SharedPreferences token;
    private SharedPreferences.Editor editor;
    private MaterialButton btnLogin;
    private GraphqlClient graphqlClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private  void init(){
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        token = getSharedPreferences("customer", MODE_PRIVATE);
        editor = token.edit();
        btnLogin = findViewById(R.id.btn_login);
        graphqlClient = new GraphqlClient(token.getString("customer_token",""));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = graphqlClient.authentication(
                        email.getText().toString(),
                        password.getText().toString());
                editor.putString("customer_token", token);
                editor.commit();
                if (!Util.isEmptyorNull(token))
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                else
                    Toast.makeText(LoginActivity.this, "Cant authenticated", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void validate(){

    }
}
