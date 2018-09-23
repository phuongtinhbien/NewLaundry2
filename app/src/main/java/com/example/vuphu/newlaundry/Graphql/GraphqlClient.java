package com.example.vuphu.newlaundry.Graphql;

import android.app.Activity;
import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.AuthenticateMutation;
import com.example.vuphu.newlaundry.CurrentUserQuery;
import com.example.vuphu.newlaundry.RegisterUserMutation;
import com.example.vuphu.newlaundry.Utils.Util;
import com.example.vuphu.newlaundry.type.RegisterUserInput;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;

public class GraphqlClient {

    private static final String BASE_URL = "http://192.168.16.130:5000/graphql";
    private static  final String USER_TYPE = "customer_type";
    private static final int TIME_OUT = 50000;
    private OkHttpClient okHttpClient;
    private String token;

    public GraphqlClient(String token) {
        this.token = token;

    }

    public ApolloClient getApolloClient() {
        okHttpClient = getOkHttpClient(token);
        return  ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();
    }

    private static OkHttpClient getOkHttpClient(String authToken) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // set the timeouts
        builder.connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        return builder.build();
    }


    public String authentication (String email, String pass){
        final String[] token = {""};
        getApolloClient().mutate(AuthenticateMutation.builder()
                .email(email)
                .password(pass)
                .userType(USER_TYPE)
                .build()).enqueue(new ApolloCall.Callback<AuthenticateMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<AuthenticateMutation.Data> response) {
                Log.i("authentication_token",response.data().authenticate().jwt().toString());
                token[0] = response.data().authenticate().jwt().toString();
            }
            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e("authentication_err", e.getCause() +" - "+e);
            }
        });
        return token[0];
    }

    public RegisterUserMutation.RegisterUser registerUser (String email, String pass, String firstName, String lastName){
        final RegisterUserInput input = RegisterUserInput.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(pass)
                .userType(USER_TYPE).build();

        final RegisterUserMutation.RegisterUser[] registerUser = new RegisterUserMutation.RegisterUser[1];

        getApolloClient().mutate(RegisterUserMutation.builder().registerUserInput(input)
                .build()).enqueue(new ApolloCall.Callback<RegisterUserMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<RegisterUserMutation.Data> response) {
                Log.e("register_user", response.data().registerUser().toString());
                registerUser[0] = response.data().registerUser();
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e("register_err", e.getCause() +" - "+e);
            }
        });
        return registerUser[0];
    }

    public CurrentUserQuery.CurrentUser currentUser(){
        final CurrentUserQuery.CurrentUser[] user = new CurrentUserQuery.CurrentUser[1];
        getApolloClient().query(CurrentUserQuery.builder().build()).enqueue(new ApolloCall.Callback<CurrentUserQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<CurrentUserQuery.Data> response) {
                user[0] = response.data().currentUser();
                Log.e("current_user", response.data().currentUser().toString());
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e("current_user_err", e.getCause() +" - "+e);

            }
        });

        return user[0];

    }








}
