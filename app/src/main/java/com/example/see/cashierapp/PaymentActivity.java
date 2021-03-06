package com.example.see.cashierapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.see.cashierapp.DatabaseContract.DataBarangColoumns.PENDAPATAN;
import static com.example.see.cashierapp.DatabaseContract.DataBarangColoumns.TANGGAL_TRANSAKSI;
import static com.example.see.cashierapp.DatabaseContract.DataBarangColoumns.WAKTU_TRANSAKSI;
import static com.example.see.cashierapp.DatabaseContract.TABLE_TRANSAKSI;

public class PaymentActivity extends AppCompatActivity {

    private ArrayList<Kategori> listKategori ;
    private TableLayout tabelPesan;
    private EditText edtUang;
    private TextView tvTotalPembelian;
    private TextView tvTotalKembalian;
    private Button btnPay;
    private long totalHarga;
    private DatabaseHelper databaseHelper;
    private Boolean bayar = false;
    private Long uangBayar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tabelPesan = findViewById(R.id.tabelPesan);
        edtUang = findViewById(R.id.edtUang);
        tvTotalPembelian = findViewById(R.id.tvTotalPembelian);
        tvTotalKembalian = findViewById(R.id.tvKembalian);
        btnPay = findViewById(R.id.btnBayar);

        listKategori = new ArrayList<>();
        listKategori = (ArrayList<Kategori>)getIntent().getSerializableExtra("listKategori");
        databaseHelper = new DatabaseHelper(this);

        edtUang.addTextChangedListener(onTextChangedListener());

        for (Kategori k : listKategori) {
            for (Barang b : k.getListBarang()) {
                if (b.getQty() > 0) {
                    TableRow tableRow = new TableRow(this);
                    TextView textViewMenu = new TextView(this);
                    TextView textViewQty = new TextView(this);
                    Typeface typeface = ResourcesCompat.getFont(this, R.font.rubik_regular);
                    textViewMenu.setTypeface(typeface);
                    textViewMenu.setTextSize(14.0f);
                    textViewQty.setTypeface(typeface);
                    textViewQty.setTextSize(14.0f);
                    textViewMenu.setText(b.getNamaBarang());
                    textViewQty.setText(String.valueOf(b.getQty()));
                    tableRow.addView(textViewMenu);
                    tableRow.addView(textViewQty);
                    tabelPesan.addView(tableRow);
                    totalHarga+=b.getHargaBarang() * b.getQty();
                }
            }
        }

        tvTotalPembelian.setText("Rp. " + String.valueOf(NumberFormat.getInstance(Locale.US).format(totalHarga)));
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bayar) {
                    Intent intent = new Intent(PaymentActivity.this, ReceiptActivity.class);
                    intent.putExtra("listKategori", listKategori);
                    intent.putExtra("uang", uangBayar);
                    startActivity(intent);
                    bayar = false;
                } else {
                    String uang = edtUang.getText().toString().replace(",","");
                    uangBayar = Long.parseLong(uang);
                    if (totalHarga > uangBayar) {
                        Toast.makeText(getApplicationContext(), "Uang Anda Kurang", Toast.LENGTH_SHORT).show();
                    } else {
                        btnPay.setText("Nota");
                        tvTotalKembalian.setText("Rp. " + String.valueOf(NumberFormat.getInstance(Locale.US).format(uangBayar - totalHarga)));
                        Toast.makeText(getApplicationContext(), "Pembayaran Berhasil", Toast.LENGTH_SHORT).show();
                        bayar = true;
                        Date date = Calendar.getInstance().getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                        String currentDate = dateFormat.format(date);
                        String currentTime = timeFormat.format(date);
                        SQLiteDatabase database = databaseHelper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(TANGGAL_TRANSAKSI, currentDate);
                        contentValues.put(WAKTU_TRANSAKSI, currentTime);
                        contentValues.put(PENDAPATAN, totalHarga);
                        database.insert(TABLE_TRANSAKSI, null, contentValues);
                    }
                }
            }
        });
    }

    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtUang.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    edtUang.setText(formattedString);
                    edtUang.setSelection(edtUang.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                edtUang.addTextChangedListener(this);
            }
        };
    }
}
