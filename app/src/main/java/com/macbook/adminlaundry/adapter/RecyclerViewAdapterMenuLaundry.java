package com.macbook.adminlaundry.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.gson.Gson;
import com.macbook.adminlaundry.R;
import com.macbook.adminlaundry.activities.DetailDataActivity;
import com.macbook.adminlaundry.activities.ListDataActivity;
import com.macbook.adminlaundry.models.MenuLaundry;

import java.util.ArrayList;

public class RecyclerViewAdapterMenuLaundry extends RecyclerView.Adapter<RecyclerViewAdapterMenuLaundry.MyViewHolder> {
    private ArrayList<MenuLaundry> menuLaundryArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaLaundry, hargaLaundry;
        public CardView cdData;
        public View mView;
        public MyViewHolder(View view) {
            super(view);
            mView = view;
            namaLaundry = (TextView) view.findViewById(R.id.tv_nama_layanan);
            hargaLaundry = (TextView) view.findViewById(R.id.tv_harga);
            cdData = (CardView) view.findViewById(R.id.cd_menu_laundry);
        }
    }

    public RecyclerViewAdapterMenuLaundry(ArrayList<MenuLaundry> menuLaundryArrayList) {
        this.menuLaundryArrayList = menuLaundryArrayList;
    }

    @Override
    public RecyclerViewAdapterMenuLaundry.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_menu_laundry, parent, false);

        return new RecyclerViewAdapterMenuLaundry.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapterMenuLaundry.MyViewHolder holder, int position) {
        final MenuLaundry menuLaundry = menuLaundryArrayList.get(position);

        holder.namaLaundry.setText(menuLaundry.getJenis());
        holder.hargaLaundry.setText("Rp. "+String.valueOf(menuLaundry.getHarga())+ " /"+menuLaundry.getSatuan().toUpperCase());

        holder.cdData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(holder.mView.getContext(), DetailDataActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("menu","Layanan");
                Gson gson = new Gson();
                intent.putExtra("data",gson.toJson(menuLaundry));
                intent.putExtra("typeDetail","detail");
                holder.mView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuLaundryArrayList.size();
    }
}
