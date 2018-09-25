package com.example.vuphu.newlaundry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.example.vuphu.newlaundry.Graphql.Services;
import com.example.vuphu.newlaundry.Main.MainActivity;
import com.example.vuphu.newlaundry.Utils.Util;

import static com.example.vuphu.newlaundry.R.string.input_email_err;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private MaterialButton btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        String preToken = PreferenceUtil.getAuthToken(getApplicationContext());
        if (!Util.isEmptyorNull(preToken)) {
            final Services services = new Services(GraphqlClient.getApolloClient(PreferenceUtil.getAuthToken(getApplicationContext()).trim()));
            CurrentUserQuery.CurrentUser currentUser= services.currentUser();
            if (currentUser!= null){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Log.i("current_user", currentUser.toString());
                finish();
            }

        }
    }

    private  void init(){
        email = findViewById(R.id.login_email);
        Util.showHideCursor(email);
        password = findViewById(R.id.login_password);
        Util.showHideCursor(password);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (validate()){
                        String token = GraphqlClient
                                .authentication(
                                        email.getText().toString(),
                                        password.getText().toString());

                        if (!Util.isEmptyorNull(token)){
                            PreferenceUtil.setAuthToken(getApplicationContext(),token);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }

                    }
            }
        });

    }

    public boolean validate(){

        boolean validate = true;
        if (email.getText().length() == 0){
            TextInputLayout layout = findViewById(R.id.login_email_layout);
            layout.setError(getResources().getString(R.string.input_email_err));
            validate = false;
        }

        if (password.getText().length() == 0){
            TextInputLayout layout = findViewById(R.id.login_password_layout);
            layout.setError(getResources().getString(R.string.input_password_err));
            validate= false;
        }
        return validate;


    }
}
