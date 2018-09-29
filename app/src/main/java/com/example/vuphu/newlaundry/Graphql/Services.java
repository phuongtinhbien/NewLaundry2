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

public class Services{

    public static  final String USER_TYPE = "customer_type";
    private ApolloClient apolloClient;
    private static CurrentUserQuery.CurrentUser currentUser;

    public Services(ApolloClient apolloClient) {
        this.apolloClient = apolloClient;
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


    public CurrentUserQuery.CurrentUser getCurrentUser() {
        return currentUser;
    }
}
