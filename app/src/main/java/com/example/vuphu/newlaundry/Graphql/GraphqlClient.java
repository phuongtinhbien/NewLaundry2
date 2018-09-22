package com.example.vuphu.newlaundry.Graphql;

import com.apollographql.apollo.ApolloClient;

import okhttp3.OkHttpClient;

public class GraphqlClient {

    private static final String BASE_URL = "http://localhost:3000/graphql";

    static OkHttpClient okHttpClient = new OkHttpClient.Builder().build();


    static ApolloClient apolloClient = ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttpClient)
            .build();

    public static ApolloClient getApolloClient() {
        return apolloClient;
    }



}
