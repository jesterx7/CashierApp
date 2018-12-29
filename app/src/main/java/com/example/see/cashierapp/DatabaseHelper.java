package com.example.see.cashierapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.MediaStore.Audio.Playlists.Members._ID;
import static com.example.see.cashierapp.DatabaseContract.DataBarangColoumns.PENDAPATAN;
import static com.example.see.cashierapp.DatabaseContract.DataBarangColoumns.TANGGAL_TRANSAKSI;
import static com.example.see.cashierapp.DatabaseContract.DataBarangColoumns.WAKTU_TRANSAKSI;
import static com.example.see.cashierapp.DatabaseContract.TABLE_TRANSAKSI;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "dbbarang";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_DB = String.format(
            "CREATE TABLE %s"
            + "(%s INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "%s DATE NOT NULL,"
            + "%s TIME NOT NULL,"
            + "%s INT NOT NULL)",
            TABLE_TRANSAKSI, _ID, TANGGAL_TRANSAKSI, WAKTU_TRANSAKSI, PENDAPATAN);

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSAKSI);
        onCreate(db);
    }
}
