package com.example.vuphu.newlaundry.Graphql;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {
    private String authToken;

    public RequestInterceptor(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        requestBuilder.addHeader("Authorization", "Bearer " + authToken.trim());
        return chain.proceed(requestBuilder.build());
    }
}
