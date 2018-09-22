package com.example.vuphu.newlaundry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.vuphu.newlaundry.Main.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private SharedPreferences token;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private  void init(){
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        token = getSharedPreferences("user_token", MODE_PRIVATE);
        editor = token.edit();
    }

    public void login(View view) {

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void validate(){

    }
}
