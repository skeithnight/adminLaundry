package com.macbook.adminlaundry.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.macbook.adminlaundry.R;
import com.macbook.adminlaundry.TampilDialog;
import com.macbook.adminlaundry.adapter.RecyclerViewAdapterMenuLaundry;
import com.macbook.adminlaundry.api.APIClient;
import com.macbook.adminlaundry.api.DataService;
import com.macbook.adminlaundry.models.MenuLaundry;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class DetailDataActivity extends AppCompatActivity {

    private static String TAG = "Testing";
    String token;
    //    SharedPreferences
    SharedPreferences mSPLogin;
    @BindView(R.id.nama_layanan_detail_data)
    EditText namaLayanan;
    @BindView(R.id.harga_detail_data)
    EditText hargaLayanan;
    @BindView(R.id.radioGrupJenis)
    RadioGroup rgJenis;
    @BindView(R.id.btn_detail_data)
    Button btnDetailData;
    RadioButton radioButton;
    private TampilDialog tampilDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data);
        ButterKnife.bind(this);
        tampilDialog = new TampilDialog(this);
    }

    @OnClick(R.id.btn_detail_data)
    public void submitData() {
        int selectID = rgJenis.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectID);
        String jenis = namaLayanan.getText().toString();
        int harga = Integer.parseInt(hargaLayanan.getText().toString());
        String satuan = radioButton.getText().toString();

        MenuLaundry menuLaundry = new MenuLaundry(jenis, harga, satuan);

        mSPLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = mSPLogin.getString("token", null);
        // show loading
        tampilDialog.showLoading();
        if (token != null) {

            DataService dataService = APIClient.getClient().create(DataService.class);
            dataService.postMenuLaundry("Bearer " + token, menuLaundry).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    tampilDialog.dismissLoading();
                    tampilDialog.showDialog("Information", "Sukses input data", "main-activity");

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    tampilDialog.dismissLoading();
                    tampilDialog.showDialog("Failed", t.getMessage(), "");
                }
            });
        }
    }
}
