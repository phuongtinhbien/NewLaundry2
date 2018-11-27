package com.example.vuphu.newlaundry.Graphql;

import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloMutationCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.apollographql.apollo.internal.interceptor.ApolloCacheInterceptor;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.example.vuphu.newlaundry.AuthenticateMutation;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class GraphqlClient {

    private static final String BASE_URL = "http://192.168.1.12:5000/graphql";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
    private static final int TIME_OUT = 30000;
    private static OkHttpClient okHttpClient;

    public static ApolloClient getApolloClient(String authToken, boolean noHeader) {

        if (okHttpClient != null){
            okHttpClient = null;
        }

        okHttpClient = getOkHttpClient(authToken, noHeader);
        return ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();
    }

    private static OkHttpClient getOkHttpClient(final String authToken, boolean noHeader) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // set the timeouts
        builder.connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.interceptors().clear();
        if (!noHeader) {
            builder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request.Builder builder = request.newBuilder();
                    builder.addHeader("Authorization","BEARER " + authToken);
                    builder.method(request.method(), request.body());
                    return chain.proceed(builder.build());
                }
            });
        }


        return builder.build();
    }


    public static ApolloClient uploadImage(final File image, final String imageName, final String authToken){

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // set the timeouts
        builder.connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.interceptors().clear();
        builder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();
                RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("headerImageFile", imageName, RequestBody.create(MEDIA_TYPE_PNG, image))
                        .addPart(request.body())
                        .build();
                builder.addHeader("Authorization", "BEARER " + authToken);
                builder.method(request.method(), request.body());
                builder.post(requestBody);
                return chain.proceed(builder.build());
            }
        });
        return ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(builder.build())
                .build();

    }



}

