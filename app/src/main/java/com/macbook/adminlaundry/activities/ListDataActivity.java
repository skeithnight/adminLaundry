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
import butterknife.ButterKnife;
import com.google.gson.Gson;
import com.macbook.adminlaundry.R;
import com.macbook.adminlaundry.TampilDialog;
import com.macbook.adminlaundry.adapter.RecyclerViewAdapterCabang;
import com.macbook.adminlaundry.adapter.RecyclerViewAdapterKurir;
import com.macbook.adminlaundry.adapter.RecyclerViewAdapterMenuLaundry;
import com.macbook.adminlaundry.api.APIClient;
import com.macbook.adminlaundry.api.DataService;
import com.macbook.adminlaundry.models.Cabang;
import com.macbook.adminlaundry.models.Kurir;
import com.macbook.adminlaundry.models.MenuLaundry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class ListDataActivity extends AppCompatActivity {
    String menu, token;

    static String TAG = "Testing";
    private TampilDialog tampilDialog;
    //    SharedPreferences
    SharedPreferences mSPLogin;
    // RecyclerView
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);
        ButterKnife.bind(this);
        tampilDialog = new TampilDialog(this);

        // show loading
        tampilDialog.showLoading();

        getData();
    }
    private void getData() {
        mSPLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = mSPLogin.getString("token",null);
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            menu= null;
        } else {
            menu= extras.getString("menu");
            if (menu.equals("Layanan")){
                setTitle(menu);
                getDataLayanan();
            }else if (menu.equals("Toko")){
                setTitle(menu);
                getDataCabang();
            }else if (menu.equals("Kurir")){
                setTitle(menu);
                getDataKurir();
            }
        }

        tampilDialog.dismissLoading();
    }
    //    Data Menu Laundry
    ArrayList<MenuLaundry> listDataMenuLaundry = new ArrayList<MenuLaundry>();
    private RecyclerViewAdapterMenuLaundry mAdapter;
    private void getDataLayanan() {
        Log.i(TAG, "getDataLayanan: "+token);
        if (token != null){

            DataService dataService = APIClient.getClient().create(DataService.class);
            dataService.getListMenuLaundry("Bearer "+token).enqueue(new Callback<ArrayList<MenuLaundry>>() {
                @Override
                public void onResponse(Call<ArrayList<MenuLaundry>> call, Response<ArrayList<MenuLaundry>> response) {
                    Gson gson = new Gson();
                    Log.i(TAG, "onResponseResponse: "+gson.toJson(response.body()));
                    listDataMenuLaundry = response.body();

                    tampilDialog.dismissLoading();
                    // initiate RecyclerView
                    recyclerView = (RecyclerView) findViewById(R.id.rv_list_data);
                    recyclerView.setVisibility(View.VISIBLE);

                    mAdapter = new RecyclerViewAdapterMenuLaundry(listDataMenuLaundry);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ListDataActivity.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                }

                @Override
                public void onFailure(Call<ArrayList<MenuLaundry>> call, Throwable t) {
                    tampilDialog.dismissLoading();
                    tampilDialog.showDialog("Failed",t.getMessage(),"");
                }
            });
        }
    }
    //    Data Cabang
    ArrayList<Cabang> cabangArrayList = new ArrayList<Cabang>();
    private RecyclerViewAdapterCabang mAdapterCabang;
    private void getDataCabang() {
        if (token != null){

            DataService dataService = APIClient.getClient().create(DataService.class);
            dataService.getListCabang("Bearer "+token).enqueue(new Callback<ArrayList<Cabang>>() {
                @Override
                public void onResponse(Call<ArrayList<Cabang>> call, Response<ArrayList<Cabang>> response) {
                    Gson gson = new Gson();
                    Log.i(TAG, "onResponse: "+gson.toJson(response.body()));
                    cabangArrayList = response.body();

                    tampilDialog.dismissLoading();
                    // initiate RecyclerView
                    recyclerView = (RecyclerView) findViewById(R.id.rv_list_data);
                    recyclerView.setVisibility(View.VISIBLE);

                    mAdapterCabang = new RecyclerViewAdapterCabang(cabangArrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ListDataActivity.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapterCabang);
                    mAdapterCabang.notifyDataSetChanged();

                }

                @Override
                public void onFailure(Call<ArrayList<Cabang>> call, Throwable t) {
                    tampilDialog.dismissLoading();
                    tampilDialog.showDialog("Failed",t.getMessage(),"");
                }
            });
        }
    }
    //    Data Menu Laundry
    ArrayList<Kurir> listDataKurir = new ArrayList<Kurir>();
    private RecyclerViewAdapterKurir mAdapterKurir;
    private void getDataKurir() {
        Log.i(TAG, "getDataLayanan: "+token);
        if (token != null){

            DataService dataService = APIClient.getClient().create(DataService.class);
            dataService.getListKurir("Bearer "+token).enqueue(new Callback<ArrayList<Kurir>>() {
                @Override
                public void onResponse(Call<ArrayList<Kurir>> call, Response<ArrayList<Kurir>> response) {
                    Gson gson = new Gson();
                    Log.i(TAG, "onResponseResponse: "+gson.toJson(response.body()));
                    listDataKurir = response.body();

                    tampilDialog.dismissLoading();
                    // initiate RecyclerView
                    recyclerView = (RecyclerView) findViewById(R.id.rv_list_data);
                    recyclerView.setVisibility(View.VISIBLE);

                    mAdapterKurir = new RecyclerViewAdapterKurir(listDataKurir);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ListDataActivity.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapterKurir);
                    mAdapterKurir.notifyDataSetChanged();

                }

                @Override
                public void onFailure(Call<ArrayList<Kurir>> call, Throwable t) {
                    tampilDialog.dismissLoading();
                    tampilDialog.showDialog("Failed",t.getMessage(),"");
                }
            });
        }
    }
}
