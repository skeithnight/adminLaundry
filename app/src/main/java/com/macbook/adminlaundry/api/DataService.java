package com.macbook.adminlaundry.api;

import com.macbook.adminlaundry.models.Cabang;
import com.macbook.adminlaundry.models.Kurir;
import com.macbook.adminlaundry.models.MenuLaundry;
import com.macbook.adminlaundry.models.Transaksi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.ArrayList;

public interface DataService {
    @GET("/menu-laundry")
    Call<ArrayList<MenuLaundry>> getListMenuLaundry(@Header("Authorization") String token);

    @GET("/cabang")
    Call<ArrayList<Cabang>> getListCabang(@Header("Authorization") String token);

    @GET("/kurir")
    Call<ArrayList<Kurir>> getListKurir(@Header("Authorization") String token);

    @GET("/transaksi")
    Call<ArrayList<Transaksi>> getListTransaksi(@Header("Authorization") String token);

    @POST("/transaksi")
    Call<ResponseBody> postTransaksi(@Header("Authorization") String token,
                                     @Body Transaksi transaksi);

    @POST("/transaksi")
    Call<ResponseBody> postCancelTransaksi(@Header("Authorization") String token, @Body Transaksi transaksi);
}
