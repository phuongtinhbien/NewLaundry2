package com.example.vuphu.newlaundry.Graphql;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloMutationCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.apollographql.apollo.internal.interceptor.ApolloCacheInterceptor;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.example.vuphu.newlaundry.AuthenticateMutation;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;

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


public class GraphqlClient extends Activity {
    private static final String BASE_URL = "http://laundryserver.eastus.cloudapp.azure.com:5000/graphql";
//    private static final String BASE_URL = "http://" + PreferenceUtil.getIpconfig(this) + "/graphql";
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


}

