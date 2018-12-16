package com.macbook.adminlaundry.adapter;

import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import com.macbook.adminlaundry.R;
        import com.macbook.adminlaundry.models.Kurir;

        import java.util.ArrayList;

public class RecyclerViewAdapterKurir extends RecyclerView.Adapter<RecyclerViewAdapterKurir.MyViewHolder> {
    private ArrayList<Kurir> KurirArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaKurir, usernameKurir;

        public MyViewHolder(View view) {
            super(view);
            namaKurir = (TextView) view.findViewById(R.id.tv_nama_kurir);
            usernameKurir = (TextView) view.findViewById(R.id.tv_username_kurir);
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
    public void onBindViewHolder(RecyclerViewAdapterKurir.MyViewHolder holder, int position) {
        Kurir Kurir = KurirArrayList.get(position);

        holder.namaKurir.setText(Kurir.getNama());
        holder.usernameKurir.setText(Kurir.getUsername());
    }

    @Override
    public int getItemCount() {
        return KurirArrayList.size();
    }
}