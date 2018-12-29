package com.example.see.cashierapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class KasirFragment extends Fragment {
    View myView;

    private RecyclerView rvPembayaran;
    private Button btnPay;
    private ArrayList<Barang> listBarang1;
    private ArrayList<Barang> listBarang2;
    private ArrayList<Barang> listBarang3;
    private ArrayList<Barang> listBarang4;
    private ArrayList<Barang> listBarang5;
    private ArrayList<Barang> listBarang6;
    private ArrayList<Barang> listBarang7;
    private ArrayList<Barang> listBarang8;
    private ArrayList<Barang> listBarang9;
    private ArrayList<Kategori> listKategori;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.kasir_fragment, container, false);
        rvPembayaran = myView.findViewById(R.id.rvPembayaran);
        btnPay = myView.findViewById(R.id.btnPay);

        listBarang1 = new ArrayList<>();
        listBarang2 = new ArrayList<>();
        listBarang3 = new ArrayList<>();
        listBarang4 = new ArrayList<>();
        listBarang5 = new ArrayList<>();
        listBarang6 = new ArrayList<>();
        listBarang7 = new ArrayList<>();
        listBarang8 = new ArrayList<>();
        listBarang9 = new ArrayList<>();
        listKategori = new ArrayList<>();

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PaymentActivity.class);
                intent.putExtra("listKategori", listKategori);
                startActivity(intent);
            }
        });

        Kategori kategoriMakananBabi = new Kategori();
        Kategori kategoriMinuman = new Kategori();
        Kategori kategoriSayur = new Kategori();
        Kategori kategoriMakananKuah = new Kategori();
        Kategori kategoriMakananIkan = new Kategori();
        Kategori kategoriMakananAyam = new Kategori();
        Kategori kategoriMakananUdang = new Kategori();
        Kategori kategoriMakananCumi = new Kategori();
        Kategori kategoriNasi = new Kategori();

        Barang babi1 = new Barang();
        babi1.setNamaBarang("Babi Goreng");
        babi1.setHargaBarang(30000);

        Barang babi2 = new Barang();
        babi2.setNamaBarang("Babi Kecap");
        babi2.setHargaBarang(30000);

        Barang babi3 = new Barang();
        babi3.setNamaBarang("Babi Rica");
        babi3.setHargaBarang(30000);

        Barang babi4 = new Barang();
        babi4.setNamaBarang("Babi Panggang");
        babi4.setHargaBarang(30000);

        Barang babi5 = new Barang();
        babi5.setNamaBarang("Babi Panggang Oseng Pedas");
        babi5.setHargaBarang(30000);

        Barang babi6 = new Barang();
        babi6.setNamaBarang("RW Rica");
        babi6.setHargaBarang(30000);

        Barang babi7 = new Barang();
        babi7.setNamaBarang("Babi Oseng Mercon");
        babi7.setHargaBarang(27500);

        Barang babi8 = new Barang();
        babi8.setNamaBarang("Sate Babi (10 Ucuk)");
        babi8.setHargaBarang(60000);

        Barang babi9 = new Barang();
        babi9.setNamaBarang("Jeroan B2");
        babi9.setHargaBarang(20000);

        Barang babi10 = new Barang();
        babi10.setNamaBarang("Babi Lada Hitam");
        babi10.setHargaBarang(30000);

        Barang babi11 = new Barang();
        babi11.setNamaBarang("Nasi Campur B2");
        babi11.setHargaBarang(30000);

        Barang babi12 = new Barang();
        babi12.setNamaBarang("Nasi Goreng B2");
        babi12.setHargaBarang(30000);

        Barang babi13 = new Barang();
        babi13.setNamaBarang("Babi Goreng Tepung");
        babi13.setHargaBarang(27500);

        Barang babi14 = new Barang();
        babi14.setNamaBarang("Babi Asam Manis");
        babi14.setHargaBarang(30000);

        listBarang1.add(babi1);
        listBarang1.add(babi2);
        listBarang1.add(babi3);
        listBarang1.add(babi4);
        listBarang1.add(babi5);
        listBarang1.add(babi6);
        listBarang1.add(babi7);
        listBarang1.add(babi8);
        listBarang1.add(babi9);
        listBarang1.add(babi10);
        listBarang1.add(babi11);
        listBarang1.add(babi12);
        listBarang1.add(babi13);
        listBarang1.add(babi14);
        kategoriMakananBabi.setListBarang(listBarang1);
        kategoriMakananBabi.setNamaKategori("Menu Babi");

        Barang kuah1 = new Barang();
        kuah1.setNamaBarang("Kuah Tulangan B2 + Daun Kiit");
        kuah1.setHargaBarang(30000);

        Barang kuah2 = new Barang();
        kuah2.setNamaBarang("Kuah Tulangan B2 + Umbut Rotan(Pahit)");
        kuah2.setHargaBarang(30000);

        Barang kuah3 = new Barang();
        kuah3.setNamaBarang("Kuah Tulangan B2 + Jaung/Kecombang");
        kuah3.setHargaBarang(30000);

        Barang kuah4 = new Barang();
        kuah4.setNamaBarang("Kuah Tulangan B2 + Paikut(Sawi Asin)");
        kuah4.setHargaBarang(30000);

        Barang kuah5 = new Barang();
        kuah5.setNamaBarang("Bubur B2");
        kuah5.setHargaBarang(20000);

        Barang kuah6 = new Barang();
        kuah6.setNamaBarang("Sop Ikan Nila");
        kuah6.setHargaBarang(20000);

        listBarang2.add(kuah1);
        listBarang2.add(kuah2);
        listBarang2.add(kuah3);
        listBarang2.add(kuah4);
        listBarang2.add(kuah5);
        listBarang2.add(kuah6);
        kategoriMakananKuah.setListBarang(listBarang2);
        kategoriMakananKuah.setNamaKategori("Menu Kuah");

        Barang ikan1 = new Barang();
        ikan1.setNamaBarang("Nila Goreng + Nasi + Lalapan");
        ikan1.setHargaBarang(25000);

        Barang ikan2 = new Barang();
        ikan2.setNamaBarang("Nila Bakar + Nasi + Lalapan");
        ikan2.setHargaBarang(25000);

        Barang ikan3 = new Barang();
        ikan3.setNamaBarang("Nila Asam Manis + Nasi");
        ikan3.setHargaBarang(30000);

        Barang ikan4 = new Barang();
        ikan4.setNamaBarang("Patin Goreng + Nasi + Lalapan");
        ikan4.setHargaBarang(25000);

        Barang ikan5 = new Barang();
        ikan5.setNamaBarang("Patin Bakar + Nasi + Lalapan");
        ikan5.setHargaBarang(25000);

        Barang ikan6 = new Barang();
        ikan6.setNamaBarang("Patin Asam Manis + Nasi");
        ikan6.setHargaBarang(30000);

        listBarang3.add(ikan1);
        listBarang3.add(ikan2);
        listBarang3.add(ikan3);
        listBarang3.add(ikan4);
        listBarang3.add(ikan5);
        listBarang3.add(ikan6);
        kategoriMakananIkan.setListBarang(listBarang3);
        kategoriMakananIkan.setNamaKategori("Menu Ikan");

        Barang ayam = new Barang();
        ayam.setNamaBarang("Goreng + Nasi + Lalapan");
        ayam.setHargaBarang(25000);

        Barang ayam1 = new Barang();
        ayam1.setNamaBarang("Bakar + Nasi + Lalapan");
        ayam1.setHargaBarang(25000);

        Barang ayam2 = new Barang();
        ayam2.setNamaBarang("Ayam Goreng Renyah Polos");
        ayam2.setHargaBarang(30000);

        Barang ayam3 = new Barang();
        ayam3.setNamaBarang("Ayam Grg Renyah Asam Manis");
        ayam3.setHargaBarang(30000);

        listBarang4.add(ayam);
        listBarang4.add(ayam1);
        listBarang4.add(ayam2);
        listBarang4.add(ayam3);
        kategoriMakananAyam.setListBarang(listBarang4);
        kategoriMakananAyam.setNamaKategori("Menu Ayam");

        Barang udang = new Barang();
        udang.setNamaBarang("Udang Goreng Tepung");
        udang.setHargaBarang(30000);

        Barang udang1 = new Barang();
        udang1.setNamaBarang("Udang Grg Tepung Asam Manis");
        udang1.setHargaBarang(30000);

        listBarang5.add(udang);
        listBarang5.add(udang1);
        kategoriMakananUdang.setListBarang(listBarang5);
        kategoriMakananUdang.setNamaKategori("Menu Udang");

        Barang sayur = new Barang();
        sayur.setNamaBarang("Singkong Jaung + Keluhing");
        sayur.setHargaBarang(10000);

        Barang sayur1 = new Barang();
        sayur1.setNamaBarang("Singkong Terong Pipit + Keluhing");
        sayur1.setHargaBarang(10000);

        Barang sayur2 = new Barang();
        sayur2.setNamaBarang("Singkong Biasa (Polos)");
        sayur2.setHargaBarang(10000);

        Barang sayur3 = new Barang();
        sayur3.setNamaBarang("Singkong Jantung Pisang + Keluhing");
        sayur3.setHargaBarang(10000);

        Barang sayur4 = new Barang();
        sayur4.setNamaBarang("Capcay B2");
        sayur4.setHargaBarang(25000);

        Barang sayur5 = new Barang();
        sayur5.setNamaBarang("Capcay Seafood");
        sayur5.setHargaBarang(25000);

        Barang sayur6 = new Barang();
        sayur6.setNamaBarang("Oseng Pakis Biasa");
        sayur6.setHargaBarang(10000);

        Barang sayur7 = new Barang();
        sayur7.setNamaBarang("Oseng Pakis Pahit");
        sayur7.setHargaBarang(10000);

        Barang sayur8 = new Barang();
        sayur8.setNamaBarang("Oseng Pakis Udang");
        sayur8.setHargaBarang(15000);

        Barang sayur9 = new Barang();
        sayur9.setNamaBarang("Oseng Kangkung Biasa");
        sayur9.setHargaBarang(10000);

        Barang sayur10 = new Barang();
        sayur10.setNamaBarang("Oseng Kangkung Pahit");
        sayur10.setHargaBarang(10000);

        Barang sayur11 = new Barang();
        sayur11.setNamaBarang("Oseng Kangkung Udang");
        sayur11.setHargaBarang(15000);

        Barang sayur12 = new Barang();
        sayur12.setNamaBarang("Oseng Daun Kates");
        sayur12.setHargaBarang(10000);

        Barang sayur13 = new Barang();
        sayur13.setNamaBarang("Oseng Umbut Rotan (Pahit)");
        sayur13.setHargaBarang(15000);

        listBarang6.add(sayur);
        listBarang6.add(sayur1);
        listBarang6.add(sayur2);
        listBarang6.add(sayur3);
        listBarang6.add(sayur4);
        listBarang6.add(sayur5);
        listBarang6.add(sayur6);
        listBarang6.add(sayur7);
        listBarang6.add(sayur8);
        listBarang6.add(sayur9);
        listBarang6.add(sayur10);
        listBarang6.add(sayur11);
        listBarang6.add(sayur12);
        listBarang6.add(sayur13);
        kategoriSayur.setListBarang(listBarang6);
        kategoriSayur.setNamaKategori("Menu Sayur");

        Barang cumi = new Barang();
        cumi.setNamaBarang("Cumi Goreng Tepung");
        cumi.setHargaBarang(30000);

        Barang cumi1 = new Barang();
        cumi1.setNamaBarang("Cumi Goreng Asam Manis");
        cumi1.setHargaBarang(30000);

        listBarang7.add(cumi);
        listBarang7.add(cumi1);
        kategoriMakananCumi.setListBarang(listBarang7);
        kategoriMakananCumi.setNamaKategori("Menu Cumi");

        Barang nasi = new Barang();
        nasi.setNamaBarang("Nasi Putih");
        nasi.setHargaBarang(5000);

        listBarang8.add(nasi);
        kategoriNasi.setListBarang(listBarang8);
        kategoriNasi.setNamaKategori("Menu Tambahan");

        Barang minum = new Barang();
        minum.setNamaBarang("Jerus Es/Hangat");
        minum.setHargaBarang(7000);

        Barang minum1 = new Barang();
        minum1.setNamaBarang("Teh Es/Hangat");
        minum1.setHargaBarang(5000);

        Barang minum2 = new Barang();
        minum2.setNamaBarang("Kopi Hitam");
        minum2.setHargaBarang(5000);

        Barang minum3 = new Barang();
        minum3.setNamaBarang("Kopi Susu");
        minum3.setHargaBarang(5000);

        Barang minum4 = new Barang();
        minum4.setNamaBarang("Teh Tarik");
        minum4.setHargaBarang(7000);

        Barang minum5 = new Barang();
        minum5.setNamaBarang("Indogafe Coffemix");
        minum5.setHargaBarang(5000);

        Barang minum6 = new Barang();
        minum6.setNamaBarang("Luwak White Coffe");
        minum6.setHargaBarang(5000);

        Barang minum7 = new Barang();
        minum7.setNamaBarang("Soda Gembira");
        minum7.setHargaBarang(12000);

        Barang minum8 = new Barang();
        minum8.setNamaBarang("Jus Alpukat");
        minum8.setHargaBarang(12000);

        Barang minum9 = new Barang();
        minum9.setNamaBarang("Jus Mangga");
        minum9.setHargaBarang(12000);

        Barang minum10 = new Barang();
        minum10.setNamaBarang("Jus Buah Naga");
        minum10.setHargaBarang(12000);

        Barang minum11 = new Barang();
        minum11.setNamaBarang("Jus Sirsak");
        minum11.setHargaBarang(12000);

        Barang minum12 = new Barang();
        minum12.setNamaBarang("Jus Tomat");
        minum12.setHargaBarang(12000);

        Barang minum13 = new Barang();
        minum13.setNamaBarang("Jus Wortel");
        minum13.setHargaBarang(12000);

        Barang minum14 = new Barang();
        minum14.setNamaBarang("Jus Timun");
        minum14.setHargaBarang(12000);

        Barang minum15 = new Barang();
        minum15.setNamaBarang("Aqua Biasa");
        minum15.setHargaBarang(5000);

        Barang minum16 = new Barang();
        minum16.setNamaBarang("Aqua Dingin");
        minum16.setHargaBarang(9000);

        Barang minum17 = new Barang();
        minum17.setNamaBarang("Green Tea Panas/Dingin");
        minum17.setHargaBarang(10000);

        Barang minum18 = new Barang();
        minum18.setNamaBarang("Cappuccino Panas/Dingin");
        minum18.setHargaBarang(10000);

        Barang minum19 = new Barang();
        minum19.setNamaBarang("Salad Buah");
        minum19.setHargaBarang(15000);

        listBarang9.add(minum);
        listBarang9.add(minum1);
        listBarang9.add(minum2);
        listBarang9.add(minum3);
        listBarang9.add(minum4);
        listBarang9.add(minum5);
        listBarang9.add(minum6);
        listBarang9.add(minum7);
        listBarang9.add(minum8);
        listBarang9.add(minum9);
        listBarang9.add(minum10);
        listBarang9.add(minum11);
        listBarang9.add(minum12);
        listBarang9.add(minum13);
        listBarang9.add(minum14);
        listBarang9.add(minum15);
        listBarang9.add(minum16);
        listBarang9.add(minum17);
        listBarang9.add(minum18);
        listBarang9.add(minum19);
        kategoriMinuman.setListBarang(listBarang9);
        kategoriMinuman.setNamaKategori("Menu Minuman");

        listKategori.add(kategoriMakananBabi);
        listKategori.add(kategoriMakananIkan);
        listKategori.add(kategoriMakananKuah);
        listKategori.add(kategoriMakananAyam);
        listKategori.add(kategoriMakananCumi);
        listKategori.add(kategoriMakananUdang);
        listKategori.add(kategoriSayur);
        listKategori.add(kategoriNasi);
        listKategori.add(kategoriMinuman);

        rvPembayaran.setLayoutManager(new LinearLayoutManager(getContext()));
        KategoriAdapter adapter = new KategoriAdapter(getContext(), listKategori);
        rvPembayaran.setAdapter(adapter);

        return myView;
    }
}
