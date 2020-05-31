package com.example.services;

import com.example.models.LoginRequest;
import com.example.models.ResponseLogin;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ServiceApi {
    @POST("login")
    @FormUrlEncoded
    Call<ResponseLogin> loginUser(
            @Field("env") String env,
            @Field("name") String name,
            @Field("lastname") String lastName,
            @Field("dni") int dni,
            @Field("email") String email,
            @Field("password") String password,
            @Field("commission") int commission,
            @Field("group") int group);

    @POST("register")
    @FormUrlEncoded
    Call<LoginRequest> regUser(
            @Field("env") String env,
            @Field("name") String name,
            @Field("lastname") String lastName,
            @Field("dni") int dni,
            @Field("email") String email,
            @Field("password") String password,
            @Field("commission") int commission,
            @Field("group") int group);
}
