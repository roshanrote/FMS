package com.acquiscent.myapplication.Activity;

import com.acquiscent.myapplication.Model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceInterface {

    @POST("IsLogin")
    Call<LoginResponse> _login(@Query("stringLoginDetails") String data );


}
