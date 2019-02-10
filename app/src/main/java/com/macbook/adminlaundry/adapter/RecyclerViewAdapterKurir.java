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
import com.macbook.adminlaundry.models.Kurir;

        import java.util.ArrayList;

public class RecyclerViewAdapterKurir extends RecyclerView.Adapter<RecyclerViewAdapterKurir.MyViewHolder> {
    private ArrayList<Kurir> KurirArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaKurir, usernameKurir;
        public CardView cdData;
        public View mView;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            namaKurir = (TextView) view.findViewById(R.id.tv_nama_kurir);
            usernameKurir = (TextView) view.findViewById(R.id.tv_username_kurir);
            cdData = (CardView) view.findViewById(R.id.cd_kurir);
        }
    }

    public RecyclerViewAdapterKurir(ArrayList<Kurir> KurirArrayList) {
        this.KurirArrayList = KurirArrayList;
    }

    @Override
    public RecyclerViewAdapterKurir.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_kurir, parent, false);

        return new RecyclerViewAdapterKurir.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapterKurir.MyViewHolder holder, int position) {
        final Kurir Kurir = KurirArrayList.get(position);

        holder.namaKurir.setText(Kurir.getNama());
        holder.usernameKurir.setText(Kurir.getUsername());
        holder.cdData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(holder.mView.getContext(), DetailDataActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("menu","Kurir");
                Gson gson = new Gson();
                intent.putExtra("data",gson.toJson(Kurir));
                intent.putExtra("typeDetail","detail");
                holder.mView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return KurirArrayList.size();
    }
}