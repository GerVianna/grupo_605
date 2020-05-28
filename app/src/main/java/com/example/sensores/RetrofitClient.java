package com.example.sensores;

import android.app.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String URL = "http://so-unlam.net.ar/api/api/";
    private static RetrofitClient instanceRetrofit;
    private Retrofit retrofit;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance(){
        if (instanceRetrofit == null) {
            instanceRetrofit = new RetrofitClient();
        }
        return instanceRetrofit;
    }

    public ServiceApi getService() {
        return retrofit.create(ServiceApi.class);
    }


}
