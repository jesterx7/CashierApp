package com.example.see.cashierapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.KategoriViewHolder> {
    Context context;
    ArrayList<Kategori> listKategori;

    public KategoriAdapter(Context context, ArrayList<Kategori> listKategori) {
        this.context = context;
        this.listKategori = listKategori;
    }

    @NonNull
    @Override
    public KategoriViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_kategori, viewGroup, false);
        return new KategoriViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final KategoriViewHolder kategoriViewHolder, int i) {
        final Animation slide_up = AnimationUtils.loadAnimation(context,
                R.anim.slide_up);
        kategoriViewHolder.tvNamaKategori.setText(listKategori.get(i).getNamaKategori());
        BarangAdapter adapter = new BarangAdapter(context, listKategori.get(i).getListBarang());
        kategoriViewHolder.rvListBarang.setLayoutManager(new LinearLayoutManager(context));
        kategoriViewHolder.rvListBarang.setAdapter(adapter);
        kategoriViewHolder.rvListBarang.setVisibility(View.GONE);
        listKategori.get(i).setClicked(false);
        kategoriViewHolder.llToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listKategori.get(kategoriViewHolder.getAdapterPosition()).getClicked()) {
                    kategoriViewHolder.imgIcon.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                    kategoriViewHolder.rvListBarang.setVisibility(View.GONE);
                    listKategori.get(kategoriViewHolder.getAdapterPosition()).setClicked(false);
                } else {
                    kategoriViewHolder.imgIcon.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                    kategoriViewHolder.rvListBarang.setAnimation(slide_up);
                    kategoriViewHolder.rvListBarang.getAnimation().start();
                    kategoriViewHolder.rvListBarang.setVisibility(View.VISIBLE);
                    listKategori.get(kategoriViewHolder.getAdapterPosition()).setClicked(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listKategori.size();
    }

    public class KategoriViewHolder extends RecyclerView.ViewHolder{
        LinearLayout llToggle;
        TextView tvNamaKategori;
        ImageView imgIcon;
        RecyclerView rvListBarang;
        public KategoriViewHolder(@NonNull View itemView) {
            super(itemView);
            llToggle = itemView.findViewById(R.id.llToggle);
            tvNamaKategori = itemView.findViewById(R.id.tvNamaKategori);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            rvListBarang = itemView.findViewById(R.id.rvListBarang);
        }
    }
}
