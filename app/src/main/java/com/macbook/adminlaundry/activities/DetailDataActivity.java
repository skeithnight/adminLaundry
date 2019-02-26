package com.macbook.adminlaundry.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.macbook.adminlaundry.R;
import com.macbook.adminlaundry.TampilDialog;
import com.macbook.adminlaundry.adapter.RecyclerViewAdapterMenuLaundry;
import com.macbook.adminlaundry.api.APIClient;
import com.macbook.adminlaundry.api.DataService;
import com.macbook.adminlaundry.models.Cabang;
import com.macbook.adminlaundry.models.Kurir;
import com.macbook.adminlaundry.models.MenuLaundry;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class DetailDataActivity extends AppCompatActivity {

    private static String TAG = "Testing";
    String token, menu, typeDetail;
    //    SharedPreferences
    SharedPreferences mSPLogin;
    @BindView(R.id.VS_detail_data)
    ViewStub stub;
    Bundle extras;
    // data laundry
    EditText namaLayanan;
    EditText hargaLayanan;
    RadioGroup rgJenis;
    RadioButton radioButton;
    MenuLaundry menuLaundry;
    // data Kurir
    EditText namaKurir;
    EditText usernameKurir;
    EditText passwordKurir;
    Kurir kurir;
    // data Cabang
    EditText namaCabang;
    EditText alamatCabang;
    Button btn_pilih_lokasi;
    Cabang cabang;
    double latitude;
    double longitude;
    private TampilDialog tampilDialog;
    //    Data Cabang
    private int PLACE_PICKER_REQUEST = 1;
    private Place place;

    @BindView(R.id.btn_hapus_detail_data)
    Button btnHapus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data);
        ButterKnife.bind(this);
        tampilDialog = new TampilDialog(this);
        tampilDialog.showLoading();

        extras = getIntent().getExtras();
        if (extras == null) {
            menu = null;
            typeDetail = null;
        } else {
            menu = extras.getString("menu");
            typeDetail = extras.getString("typeDetail");
//            Log.i(TAG, "onCreate: "+menu);
        }
        setDetailDataCardView();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                alamatCabang.setText(place.getAddress());
            }
        }
    }

    private void setDetailDataCardView() {
        Gson gson = new Gson();
        if (stub != null) {
            setTitle(menu);
            Log.i(TAG, "onCreate: " + menu);

            if (typeDetail.equals("detail")){
                btnHapus.setVisibility(View.VISIBLE);
            }

            if (menu.equals("Layanan")) {
                stub.setLayoutResource(R.layout.detail_data_laundry);
                View inflated = stub.inflate();
                menuLaundry = gson.fromJson(extras.getString("data"), MenuLaundry.class);
                contentDataLaundry();
            } else if (menu.equals("Toko")) {
                stub.setLayoutResource(R.layout.detail_data_cabang);
                View inflated = stub.inflate();
                cabang = gson.fromJson(extras.getString("data"), Cabang.class);
                contentDataCabang();
            } else if (menu.equals("Kurir")) {
                stub.setLayoutResource(R.layout.detail_data_kurir);
                View inflated = stub.inflate();
                kurir = gson.fromJson(extras.getString("data"), Kurir.class);
                contentDataKurir();
            } else {
                Toast.makeText(this, "Failed to render", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Failed to render", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_detail_data)
    public void submitData() {
        if (menu.equals("Layanan")) {
            inputDataLaundry();
        } else if (menu.equals("Toko")) {
            inputDataCabang();
        } else if (menu.equals("Kurir")) {
            inputDataKurir();
        } else {
            Toast.makeText(this, "Failed to render", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_hapus_detail_data)
    public void hapusData(){
        Toast.makeText(this, "Failed to render", Toast.LENGTH_SHORT).show();
        if (menu.equals("Layanan")) {
            hapusDataLaundry();
        } else if (menu.equals("Toko")) {
            hapusDataCabang();
        } else if (menu.equals("Kurir")) {
            hapusDataKurir();
        } else {
            Toast.makeText(this, "Failed to render", Toast.LENGTH_SHORT).show();
        }
    }

    //    Data Laundry
    public void contentDataLaundry() {
        namaLayanan = (EditText) findViewById(R.id.nama_layanan_detail_data);
        hargaLayanan = (EditText) findViewById(R.id.harga_detail_data);
        rgJenis = (RadioGroup) findViewById(R.id.radioGrupJenis);

        if (typeDetail.equals("detail")) {
            namaLayanan.setText(menuLaundry.getJenis());
            hargaLayanan.setText(String.valueOf(menuLaundry.getHarga()));
        }

        tampilDialog.dismissLoading();
    }

    public boolean validasiDataLaundry() {
        if (!menuLaundry.getJenis().equals("") || menuLaundry.getHarga() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public void inputDataLaundry() {
        int selectID = rgJenis.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectID);
        String jenis = namaLayanan.getText().toString();
        int harga = Integer.parseInt(hargaLayanan.getText().toString().equals("") ? "0" : hargaLayanan.getText().toString());
        String satuan = radioButton.getText().toString();

        if (!typeDetail.equals("add")) {
            menuLaundry = new MenuLaundry(menuLaundry.getId(), jenis, harga, satuan);
        } else {
            menuLaundry = new MenuLaundry(jenis, harga, satuan);
        }
        if (validasiDataLaundry()) {
            Gson gson = new Gson();
//            Log.i(TAG, "inputDataLaundry: " + gson.toJson(menuLaundry));
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
        } else {
            tampilDialog.showDialog("Failed", "Data tidak boleh kosong!", "");
        }
    }
    public void hapusDataLaundry() {

        if (validasiDataLaundry()) {
            Gson gson = new Gson();
//            Log.i(TAG, "inputDataLaundry: " + gson.toJson(menuLaundry));
            mSPLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
            token = mSPLogin.getString("token", null);
            // show loading
            tampilDialog.showLoading();
            if (token != null) {

                DataService dataService = APIClient.getClient().create(DataService.class);
                dataService.deleteMenuLaundry("Bearer " + token, menuLaundry.getId()).enqueue(new Callback<ResponseBody>() {
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
        } else {
            tampilDialog.showDialog("Failed", "Data tidak boleh kosong!", "");
        }
    }

    //    Data Kurir
    public void contentDataKurir() {
        namaKurir = (EditText) findViewById(R.id.nama_kurir_detail_data);
        usernameKurir = (EditText) findViewById(R.id.username_kurir_detail_data);
        passwordKurir = (EditText) findViewById(R.id.password_kurir_detail_data);

        if (typeDetail.equals("detail")) {
            namaKurir.setText(kurir.getNama());
            usernameKurir.setText(kurir.getUsername());
        }

        tampilDialog.dismissLoading();
    }

    public boolean validasiDataKurir() {
        if (!kurir.getNama().equals("") || !kurir.getUsername().equals("") || !kurir.getPassword().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public void inputDataKurir() {
        String nama = namaKurir.getText().toString();
        String username = usernameKurir.getText().toString();
        String password = passwordKurir.getText().toString();

        if (!typeDetail.equals("add")) {
            kurir = new Kurir(kurir.getId(), username, password, nama, true);
        } else {
            kurir = new Kurir(username, password, nama, true);
        }
        if (validasiDataKurir()) {
            Gson gson = new Gson();
//            Log.i(TAG, "inputDataLaundry: " + gson.toJson(kurir));
            mSPLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
            token = mSPLogin.getString("token", null);
            // show loading
            tampilDialog.showLoading();
            if (token != null) {

                DataService dataService = APIClient.getClient().create(DataService.class);
                dataService.postMenuKurir("Bearer " + token, kurir).enqueue(new Callback<ResponseBody>() {
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
        } else {
            tampilDialog.showDialog("Failed", "Data tidak boleh kosong!", "");
        }
    }
    public void hapusDataKurir() {

        if (validasiDataKurir()) {
            Gson gson = new Gson();
//            Log.i(TAG, "inputDataLaundry: " + gson.toJson(menuLaundry));
            mSPLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
            token = mSPLogin.getString("token", null);
            // show loading
            tampilDialog.showLoading();
            if (token != null) {

                DataService dataService = APIClient.getClient().create(DataService.class);
                dataService.deleteMenuKurir("Bearer " + token, kurir.getId()).enqueue(new Callback<ResponseBody>() {
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
        } else {
            tampilDialog.showDialog("Failed", "Data tidak boleh kosong!", "");
        }
    }

    public void contentDataCabang() {
        namaCabang = (EditText) findViewById(R.id.nama_cabang_detail_data);
        btn_pilih_lokasi = (Button) findViewById(R.id.pilih_lokasi_detail_data);
        alamatCabang = (EditText) findViewById(R.id.alamat_cabang_detail_data);

        if (typeDetail.equals("detail")) {
            namaCabang.setText(cabang.getNama());
            alamatCabang.setText(cabang.getAlamat());
            latitude = cabang.getLatitude();
            longitude = cabang.getLongitude();
        }

        btn_pilih_lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(DetailDataActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });

        tampilDialog.dismissLoading();
    }

    public boolean validasiDataCabang() {
        if (!cabang.getNama().equals("") || !cabang.getAlamat().equals("") || cabang.getLatitude() != null || cabang.getLatitude() == null) {
            return true;
        } else {
            return false;
        }
    }

    public void inputDataCabang() {
        String nama = namaCabang.getText().toString();
        String alamat = alamatCabang.getText().toString();

        if (!typeDetail.equals("add")) {
            cabang = new Cabang(cabang.getId(), nama, latitude, longitude, alamat);
        } else {
            cabang = new Cabang(nama, latitude, longitude, alamat);
        }
        if (validasiDataCabang()) {
            Gson gson = new Gson();
//            Log.i(TAG, "inputDataLaundry: " + gson.toJson(cabang));
            mSPLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
            token = mSPLogin.getString("token", null);
            // show loading
            tampilDialog.showLoading();
            if (token != null) {

                DataService dataService = APIClient.getClient().create(DataService.class);
                dataService.postMenuCabang("Bearer " + token, cabang).enqueue(new Callback<ResponseBody>() {
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
        } else {
            tampilDialog.showDialog("Failed", "Data tidak boleh kosong!", "");
        }
    }
    public void hapusDataCabang() {

        if (validasiDataCabang()) {
            Gson gson = new Gson();
//            Log.i(TAG, "inputDataLaundry: " + gson.toJson(menuLaundry));
            mSPLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
            token = mSPLogin.getString("token", null);
            // show loading
            tampilDialog.showLoading();
            if (token != null) {

                DataService dataService = APIClient.getClient().create(DataService.class);
                dataService.deleteMenuCabang("Bearer " + token, cabang.getId()).enqueue(new Callback<ResponseBody>() {
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
        } else {
            tampilDialog.showDialog("Failed", "Data tidak boleh kosong!", "");
        }
    }
}
