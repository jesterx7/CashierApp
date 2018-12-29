package com.example.see.cashierapp;

import java.io.Serializable;

public class Barang implements Serializable{
    String namaBarang;
    Integer hargaBarang;
    Integer qty = 0;

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public Integer getHargaBarang() {
        return hargaBarang;
    }

    public void setHargaBarang(Integer hargaBarang) {
        this.hargaBarang = hargaBarang;
    }
}
