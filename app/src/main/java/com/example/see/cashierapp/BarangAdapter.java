package com.example.see.cashierapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.BarangViewHolder> {
    Context context;
    ArrayList<Barang> listBarang;

    public BarangAdapter(Context context, ArrayList<Barang> listBarang) {
        this.context = context;
        this.listBarang = listBarang;
    }

    @NonNull
    @Override
    public BarangViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_barang, viewGroup, false);
        return new BarangViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final BarangViewHolder barangViewHolder, int i) {
        barangViewHolder.namaBarang.setText(listBarang.get(i).getNamaBarang());
        barangViewHolder.hargaBarang.setText("Rp. " + String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(listBarang.get(i).getHargaBarang())));
        barangViewHolder.cbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listBarang.get(barangViewHolder.getAdapterPosition()).setQty(listBarang.get(barangViewHolder.getAdapterPosition()).getQty()+1);
                barangViewHolder.edtQty.setText(String.valueOf(listBarang.get(barangViewHolder.getAdapterPosition()).getQty()));
            }
        });
        barangViewHolder.mbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listBarang.get(barangViewHolder.getAdapterPosition()).setQty(listBarang.get(barangViewHolder.getAdapterPosition()).getQty()-1);
                if (listBarang.get(barangViewHolder.getAdapterPosition()).getQty() < 0) {
                    listBarang.get(barangViewHolder.getAdapterPosition()).setQty(0);
                }
                barangViewHolder.edtQty.setText(String.valueOf(listBarang.get(barangViewHolder.getAdapterPosition()).getQty()));
            }
        });
        barangViewHolder.edtQty.setText(String.valueOf(listBarang.get(i).getQty()));
    }

    @Override
    public int getItemCount() {
        return listBarang.size();
    }

    public class BarangViewHolder extends RecyclerView.ViewHolder{
        TextView namaBarang, hargaBarang;
        ImageView cbox, mbox;
        EditText edtQty;
        public BarangViewHolder(@NonNull View itemView) {
            super(itemView);
            namaBarang = itemView.findViewById(R.id.tvNamaBarang);
            hargaBarang = itemView.findViewById(R.id.tvHargaBarang);
            cbox = itemView.findViewById(R.id.cbox);
            mbox = itemView.findViewById(R.id.mbox);
            edtQty = itemView.findViewById(R.id.edtQty);
        }
    }
}
