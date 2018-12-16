package com.macbook.adminlaundry.api;

import com.macbook.adminlaundry.models.Administrator;
import com.macbook.adminlaundry.models.ResponseLogin;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthService {

    @POST("administrator/login")
    Call<ResponseLogin> postLogin(@Body Administrator administratorLogin);
//
    @GET("administrator/check-session")
    Call<Administrator> getCheckLogin(@Header("Authorization") String token);
}
