package com.macbook.adminlaundry.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.macbook.adminlaundry.R;
import com.macbook.adminlaundry.TampilDialog;
import com.macbook.adminlaundry.api.APIClient;
import com.macbook.adminlaundry.api.DataService;
import com.macbook.adminlaundry.models.MenuLaundry;
import com.macbook.adminlaundry.models.Service;
import com.macbook.adminlaundry.models.Transaksi;
import com.microblink.MicroblinkSDK;
import com.microblink.activity.BarcodeScanActivity;
import com.microblink.entities.recognizers.Recognizer;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkbarcode.barcode.BarcodeRecognizer;
import com.microblink.entities.recognizers.blinkbarcode.pdf417.Pdf417Recognizer;
import com.microblink.uisettings.ActivityRunner;
import com.microblink.uisettings.BarcodeUISettings;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DetailPesananActivity extends AppCompatActivity {

    private static int Request_Code_Scanning = 1;
    private static String TAG = "Testing";
    Transaksi transaksi;
    //    SharedPreferences
    SharedPreferences mSPLogin;
    @BindView(R.id.id_pesanan)
    EditText idPesanan;
    @BindView(R.id.nama_pemesan)
    EditText namaPemesan;
    @BindView(R.id.waktu_pemesanan)
    EditText waktuPemesanan;
    @BindView(R.id.listLayanan__detail_pesan)
    ListView lvLayananPesan;
    @BindView(R.id.btn_detail_pesanan)
    Button btnDetailPesan;
    //  Blink ID
    private RecognizerBundle mRecognizerBundle;
    private Pdf417Recognizer pdf417Recognizer;
    private String token, idUser;
    private TampilDialog tampilDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);
        ButterKnife.bind(this);
        tampilDialog = new TampilDialog(this);

        //        Blink ID
        try {
            MicroblinkSDK.setLicenseFile("MB_com.macbook.adminlaundry_Pdf417Mobi_Android_2019-02-25.mblic", this);
            // setup views, as you would normally do in onCreate callback

            // create Pdf417Recognizer
            pdf417Recognizer = new Pdf417Recognizer();

            // bundle recognizers into RecognizerBundle
            mRecognizerBundle = new RecognizerBundle(pdf417Recognizer);
        } catch (Exception e) {
            tampilDialog.showDialog("Error", e.getMessage(), "");
        }
        getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Request_Code_Scanning) {
            if (resultCode == BarcodeScanActivity.RESULT_OK && data != null) {
                // load the data into all recognizers bundled within your RecognizerBundle

                mRecognizerBundle.loadFromIntent(data);

                // now every recognizer object that was bundled within RecognizerBundle
                // has been updated with results obtained during scanning session

                // you can get the result by invoking getResult on recognizer
                Pdf417Recognizer.Result result = pdf417Recognizer.getResult();
                if (result.getResultState() == Recognizer.Result.State.Valid) {
                    // result is valid, you can use it however you wish
                    Log.i(TAG, "onActivityResult: " + result.getStringData());
                } else {
                    Log.i(TAG, "onActivityResult: gagal");
                }
            }
        }
    }

    // Blink ID Start activity scanning
    public void startScanning() {
        // Settings for BarcodeScanActivity Activity
        BarcodeUISettings settings = new BarcodeUISettings(mRecognizerBundle);

        // tweak settings as you wish

        // Start activity
        ActivityRunner.startActivityForResult(this, Request_Code_Scanning, settings);
    }

    private void getData() {
        transaksi = new Transaksi();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            transaksi = null;
        } else {
            Log.i(TAG, "onCreate: " + extras.getString("transaksi", null));
            Gson gson = new Gson();
            transaksi = gson.fromJson(extras.getString("transaksi", null), Transaksi.class);
            Log.i(TAG, "onCreate: " + gson.toJson(transaksi));
            setData();
        }
    }

    private void setData() {
        idPesanan.setText(transaksi.getId() == null ? "" : transaksi.getId());
        namaPemesan.setText(transaksi.getCustomer().getNama() == null ? "" : transaksi.getCustomer().getNama());
        waktuPemesanan.setText(getDate(transaksi.getWaktuPesan() == null ? 0 : transaksi.getWaktuPesan()));

        ArrayList<String> list = new ArrayList<>();
        double totalTagihan = 0;
        for (Service item : transaksi.getMenuLaundry()
                ) {
            if(item.getService() != null) {
                list.add(item.getService().getJenis() + " | Rp. " + item.getService().getHarga() + "/" + item.getService().getSatuan());
            }
            totalTagihan += (item.getService() == null) ? 0 : item.getService().getHarga();
        }

        String[] arrayPilihan = list.toArray(new String[list.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1, arrayPilihan
        );

        // set data
        lvLayananPesan.setAdapter(adapter);

        if (transaksi.getStatus().equals("sampai-laundry")) {
            btnDetailPesan.setVisibility(View.VISIBLE);
            btnDetailPesan.setText("Cuci");
        } else if (transaksi.getStatus().equals("Cuci-laundry")) {
            btnDetailPesan.setVisibility(View.VISIBLE);
            btnDetailPesan.setText("Kering");
        } else if (transaksi.getStatus().equals("Kering-laundry")) {
            btnDetailPesan.setVisibility(View.VISIBLE);
            btnDetailPesan.setText("Setrika");
        } else if (transaksi.getStatus().equals("Setrika-laundry")) {
            btnDetailPesan.setVisibility(View.VISIBLE);
            btnDetailPesan.setText("Selesai");
        }

    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    @OnClick(R.id.btn_detail_pesanan)
    public void processPesanan() {
        tampilDialog.showLoading();
        mSPLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = mSPLogin.getString("token", null);

        Log.i(TAG, "cancelPesanan: " + token + " : " + transaksi.getId());

        // set status
        if (transaksi.getStatus().equals("sampai-laundry")) {
            transaksi.setStatus("Cuci-laundry");
        } else if (transaksi.getStatus().equals("Cuci-laundry")) {
            transaksi.setStatus("Kering-laundry");
        } else if (transaksi.getStatus().equals("Kering-laundry")) {
            transaksi.setStatus("Setrika-laundry");
        } else if (transaksi.getStatus().equals("Setrika-laundry")) {
            transaksi.setStatus("Selesai-laundry");
        }

        DataService dataService = APIClient.getClient().create(DataService.class);
        dataService.postCancelTransaksi("Bearer " + token, transaksi).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                tampilDialog.dismissLoading();
                if (response.isSuccessful()) {
                    tampilDialog.showDialog("Information", "Transaksi di batalkan", "main-activity");
                } else {
                    tampilDialog.showDialog("Failed", response.message(), "");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                tampilDialog.dismissLoading();
                tampilDialog.showDialog("Failed", t.getMessage(), "");
            }
        });
    }

    @OnClick(R.id.btn_scan_barcode)
    public void scanBarcode() {
        startScanning();
    }

}
