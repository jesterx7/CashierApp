package com.example.see.cashierapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.see.cashierapp.DatabaseContract.TABLE_TRANSAKSI;

public class ReceiptActivity extends AppCompatActivity {
    private ArrayList<Kategori> listKategori;
    private TextView tvDateTime, tvNoOrder;
    private TableLayout tabelNotaMenu;
    private Button btnDone;
    private long totalHarga = 0;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        listKategori = new ArrayList<>();
        tvDateTime = findViewById(R.id.tvDateTime);
        tvNoOrder = findViewById(R.id.tvNoOrder);
        tabelNotaMenu = findViewById(R.id.tabelNotaMenu);
        btnDone = findViewById(R.id.btnDone);

        tabelNotaMenu.removeAllViews();
        databaseHelper = new DatabaseHelper(this);
        listKategori = (ArrayList<Kategori>)getIntent().getSerializableExtra("listKategori");

        getDateAndOrder();

        for (Kategori k : listKategori) {
            for (Barang b : k.getListBarang()) {
                if (b.getQty() > 0) {
                    TableRow tableRow = new TableRow(this);
                    TextView textViewMenu = new TextView(this);
                    TextView textViewTotal = new TextView(this);
                    Typeface typeface = ResourcesCompat.getFont(this, R.font.rubik_regular);
                    textViewMenu.setTypeface(typeface);
                    textViewMenu.setTextSize(12.0f);
                    textViewTotal.setTypeface(typeface);
                    textViewTotal.setTextSize(12.0f);
                    textViewTotal.setGravity(Gravity.END);
                    textViewMenu.setText(String.valueOf(b.getQty()) + " " +b.getNamaBarang());
                    textViewTotal.setText(String.valueOf(NumberFormat.getInstance(Locale.US).format(b.getHargaBarang() * b.getQty())));
                    tableRow.addView(textViewMenu);
                    tableRow.addView(textViewTotal);
                    tabelNotaMenu.addView(tableRow);
                    totalHarga+=b.getHargaBarang() * b.getQty();
                }
            }
        }
        TableRow row = new TableRow(this);
        TextView tvSpace = new TextView(this);
        TextView tvTotal = new TextView(this);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.rubik_medium);
        tvTotal.setTypeface(typeface);
        tvTotal.setTextSize(12.0f);
        tvTotal.setGravity(Gravity.END);
        tvTotal.setText("Total   : Rp. " + String.valueOf(NumberFormat.getInstance(Locale.US).format(totalHarga)));
        tvSpace.setText(" ");
        row.addView(tvSpace);
        row.addView(tvTotal);
        tabelNotaMenu.addView(row);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiptActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDateAndOrder() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_TRANSAKSI + " ORDER BY _id DESC LIMIT 1", null);
        cursor.moveToFirst();
        tvNoOrder.setText("No Order : " + String.valueOf(cursor.getInt(0)));
        tvDateTime.setText("Tanggal : " + cursor.getString(1) + "\nPukul : " + cursor.getString(2));
    }
}
