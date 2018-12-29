package com.example.see.cashierapp;

import android.provider.BaseColumns;

public class DatabaseContract {
    public static String TABLE_TRANSAKSI = "data_transaksi";
    public static final class DataBarangColoumns implements BaseColumns {
        public static String TANGGAL_TRANSAKSI = "tanggal_transaksi";
        public static String WAKTU_TRANSAKSI = "waktu_transaksi";
        public static String PENDAPATAN = "pendapatan";
    }
}
