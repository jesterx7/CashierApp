package com.example.see.cashierapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Kategori implements Serializable{
    String namaKategori;
    ArrayList<Barang> listBarang;
    Boolean clicked;

    public Boolean getClicked() {
        return clicked;
    }

    public void setClicked(Boolean clicked) {
        this.clicked = clicked;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public void setNamaKategori(String namaKategori) {
        this.namaKategori = namaKategori;
    }

    public ArrayList<Barang> getListBarang() {
        return listBarang;
    }

    public void setListBarang(ArrayList<Barang> listBarang) {
        this.listBarang = listBarang;
    }
}
