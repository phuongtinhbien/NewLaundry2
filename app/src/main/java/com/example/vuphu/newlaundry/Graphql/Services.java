package com.example.vuphu.newlaundry.Graphql;

import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.AuthenticateMutation;
import com.example.vuphu.newlaundry.CurrentUserQuery;
import com.example.vuphu.newlaundry.RegisterUserMutation;
import com.example.vuphu.newlaundry.type.RegisterUserInput;

import org.jetbrains.annotations.NotNull;

public class Services {

    public static  final String USER_TYPE = "customer_type";
    private ApolloClient apolloClient;
    private static CurrentUserQuery.CurrentUser currentUser;
    private static RegisterUserMutation.RegisterUser registerUser;
    public Services(ApolloClient apolloClient) {
        this.apolloClient = apolloClient;
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

        getApolloClient().mutate(RegisterUserMutation.builder().registerUserInput(input)
                .build()).enqueue(new ApolloCall.Callback<RegisterUserMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<RegisterUserMutation.Data> response) {
                Log.e("register_user", response.data().registerUser().toString());
                registerUser = response.data().registerUser();
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e("register_err", e.getCause() +" - "+e);
            }
        });
        return registerUser;
    }

    public CurrentUserQuery.CurrentUser currentUser(){
        getApolloClient().query(CurrentUserQuery.builder().build()).enqueue(new ApolloCall.Callback<CurrentUserQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<CurrentUserQuery.Data> response) {
                currentUser = response.data().currentUser();
                Log.i("current_user", response.data().currentUser().toString());
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e("current_user_err", e.getCause() +" - "+e);

            }
        });

        return currentUser;

    }

    public ApolloClient getApolloClient() {
        return apolloClient;
    }


}
