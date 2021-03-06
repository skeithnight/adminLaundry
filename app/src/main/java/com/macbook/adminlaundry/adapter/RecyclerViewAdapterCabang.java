package com.macbook.adminlaundry.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.gson.Gson;
import com.macbook.adminlaundry.R;
import com.macbook.adminlaundry.activities.DetailDataActivity;
import com.macbook.adminlaundry.models.Cabang;

import java.util.ArrayList;

public class RecyclerViewAdapterCabang extends RecyclerView.Adapter<RecyclerViewAdapterCabang.MyViewHolder> {
    private ArrayList<Cabang> cabangArrayList;
    private View itemView;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaCabang, alamatCabang;
        public CardView cardView;
        public View mView;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            cardView = (CardView) view.findViewById(R.id.cd_cabang);
            namaCabang = (TextView) view.findViewById(R.id.tv_nama_toko);
            alamatCabang = (TextView) view.findViewById(R.id.tv_alamat);
        }
    }

    public RecyclerViewAdapterCabang(ArrayList<Cabang> cabangArrayList) {
        this.cabangArrayList = cabangArrayList;
    }

    @Override
    public RecyclerViewAdapterCabang.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_cabang, parent, false);

        return new RecyclerViewAdapterCabang.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapterCabang.MyViewHolder holder, int position) {
        final Cabang cabang = cabangArrayList.get(position);

        holder.namaCabang.setText(cabang.getNama());
        holder.alamatCabang.setText(cabang.getAlamat());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.mView.getContext(), DetailDataActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("menu","Toko");
                Gson gson = new Gson();
                intent.putExtra("data",gson.toJson(cabang));
                intent.putExtra("typeDetail","detail");
                holder.mView.getContext().startActivity(intent);
//                // Create a Uri from an intent string. Use the result to create an Intent.
//                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + String.valueOf(cabang.getLatitude()) + ", " + String.valueOf(cabang.getLongitude()) + "(Oxy+Laundry)");
//
//// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//// Make the Intent explicit by setting the Google Maps package
//                mapIntent.setPackage("com.google.android.apps.maps");
//
//// Attempt to start an activity that can handle the Intent
//                itemView.getContext().startActivity(mapIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cabangArrayList.size();
    }
}

