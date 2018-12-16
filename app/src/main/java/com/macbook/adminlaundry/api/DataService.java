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

    @GET("/transaksi/customer/{idUser}")
    Call<ArrayList<Transaksi>> getListTransaksi(@Header("Authorization") String token, @Path("idUser") String idUser);

    @POST("/transaksi/auto-set-cabang/{latitude}/{longitude}")
    Call<ResponseBody> postTransaksi(@Header("Authorization") String token,
                                     @Path("latitude") String latitude,
                                     @Path("longitude") String longitude,
                                     @Body Transaksi transaksi);

    @POST("/transaksi")
    Call<ResponseBody> postCancelTransaksi(@Header("Authorization") String token, @Body Transaksi transaksi);
}