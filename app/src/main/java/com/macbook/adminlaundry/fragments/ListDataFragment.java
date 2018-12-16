package com.macbook.adminlaundry.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import com.macbook.adminlaundry.R;
import com.macbook.adminlaundry.activities.ListDataActivity;
import com.macbook.adminlaundry.controller.Authorization;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListDataFragment extends Fragment {

    View mview;

    public ListDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_list_data, container, false);
//        Initiate Butterknife
        ButterKnife.bind(this,mview);

        return mview;
    }
    @OnClick({R.id.cd_layanan_list_data,R.id.cd_toko_list_data,R.id.cd_kurir_list_data,R.id.btn_logout})
    public void click(View view){
        switch (view.getId()) {
            case R.id.cd_layanan_list_data:
                goToListData("Layanan");
                break;
            case R.id.cd_toko_list_data:
                goToListData("Toko");
                break;
            case R.id.cd_kurir_list_data:
                goToListData("Kurir");
                break;
            case R.id.btn_logout:
                Authorization authorization = new Authorization(mview.getContext());
                authorization.logout();
                break;
        }
    }

    private void goToListData(String menu) {
        Intent intent = new Intent(mview.getContext(), ListDataActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("menu",menu);
        startActivity(intent);
    }

}
