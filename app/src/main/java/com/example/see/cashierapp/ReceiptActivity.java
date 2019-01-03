package com.example.see.cashierapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import static com.example.see.cashierapp.DatabaseContract.TABLE_TRANSAKSI;

public class ReceiptActivity extends AppCompatActivity {
    private ArrayList<Kategori> listKategori;
    private TextView tvDateTime, tvNoOrder;
    private TableLayout tabelNotaMenu;
    private Button btnDone;
    private CheckBox cboxPrint;
    private long totalHarga = 0;
    private long totalUang = 0;
    private long totalKembalian = 0;
    private DatabaseHelper databaseHelper;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice bluetoothDevice;
    private OutputStream outputStream;
    private InputStream inputStream;
    private Thread workerThread;
    private byte[] readBuffer;
    private int readBufferPosition;
    volatile boolean stopWorker;
    private String msg, currentDate;
    private Date calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        listKategori = new ArrayList<>();
        tvDateTime = findViewById(R.id.tvDateTime);
        tvNoOrder = findViewById(R.id.tvNoOrder);
        tabelNotaMenu = findViewById(R.id.tabelNotaMenu);
        btnDone = findViewById(R.id.btnDone);
        cboxPrint = findViewById(R.id.cboxPrint);

        calendar = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = df.format(calendar);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Please Enable Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (bluetoothAdapter == null) {
                Toast.makeText(this, "No Bluetooth Available", Toast.LENGTH_SHORT).show();
            } else {
                Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
                for (BluetoothDevice device : pairedDevice) {
                    if (device.getName().equals("RPP02N")) {
                        bluetoothDevice = device;
                    } else {
                        Toast.makeText(this, "Printer Bluetooth Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        msg =   "      Warung Dayak Mahakam\n" +
                "  Jln.Sei Musi No.34, Samarinda\n" +
                "        Telp : 0541743884\n" +
                "-------------------------------\n";

        tabelNotaMenu.removeAllViews();
        databaseHelper = new DatabaseHelper(this);
        listKategori = (ArrayList<Kategori>)getIntent().getSerializableExtra("listKategori");
        totalUang = getIntent().getLongExtra("uang", 0);

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
                    if (b.getNamaBarang().length() > 18) {
                        int pivot = 99;
                        int countSpace = 0;
                        String tempMsg = "";
                        String tempTv = "";
                        if (b.getQty() < 10) {
                            tempMsg += "0" + String.valueOf(b.getQty());
                        } else {
                            tempMsg += String.valueOf(b.getQty());
                        }
                        tempMsg += " ";
                        for (int i = 0; i < b.getNamaBarang().length(); i++) {
                            if (b.getNamaBarang().charAt(i) == ' ') {
                                countSpace+=1;
                                if (countSpace == 2) {
                                    pivot = i;
                                    break;
                                }
                            }
                            tempMsg += b.namaBarang.charAt(i);
                            tempTv += b.namaBarang.charAt(i);
                        }
                        tempTv += "\n";
                        tempMsg += " ";
                        int spaceNeeded = 31 - tempMsg.length() - String.valueOf(b.getQty() * b.getHargaBarang()).length();
                        for (int i = 0; i < spaceNeeded; i++) {
                            tempMsg += " ";
                        }
                        tempMsg += String.valueOf(NumberFormat.getInstance(Locale.US).format(b.getQty() * b.getHargaBarang()));
                        tempMsg += "\n";
                        for (int i = pivot+1; i < b.getNamaBarang().length(); i++) {
                            tempMsg += b.getNamaBarang().charAt(i);
                            tempTv += b.getNamaBarang().charAt(i);
                        }
                        tempMsg += "\n\n";
                        msg += tempMsg;
                        textViewMenu.setText(String.valueOf(b.getQty()) + " " +tempTv);
                    } else {
                        String tempMsg = "";
                        if (b.getQty() < 10) {
                            tempMsg += "0" + String.valueOf(b.getQty());
                        } else {
                            tempMsg += String.valueOf(b.getQty());
                        }
                        tempMsg += " ";
                        tempMsg += b.getNamaBarang() + " ";
                        int spaceNeeded;
                        spaceNeeded = 30 - tempMsg.length() - String.valueOf(NumberFormat.getInstance(Locale.US).format(b.getQty() * b.getHargaBarang())).length();
                        for (int i = 0; i < spaceNeeded; i++) {
                            tempMsg += " ";
                        }
                        tempMsg += String.valueOf(NumberFormat.getInstance(Locale.US).format(b.getQty() * b.getHargaBarang())) + "\n\n";
                        msg += tempMsg;
                        textViewMenu.setText(String.valueOf(b.getQty()) + " " +b.getNamaBarang());
                    }
                    textViewTotal.setText(String.valueOf(NumberFormat.getInstance(Locale.US).format(b.getHargaBarang() * b.getQty())));
                    tableRow.addView(textViewMenu);
                    tableRow.addView(textViewTotal);
                    tabelNotaMenu.addView(tableRow);
                    totalHarga+=b.getHargaBarang() * b.getQty();
                }
            }
        }
        totalKembalian = totalUang - totalHarga;

        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 15, 0 , 0);
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
        row.setLayoutParams(params);
        tabelNotaMenu.addView(row);

        TableRow row1 = new TableRow(this);
        TextView tvUang = new TextView(this);
        TextView tvSpace1 = new TextView(this);
        tvUang.setTypeface(typeface);
        tvUang.setTextSize(12.0f);
        tvUang.setGravity(Gravity.END);
        tvUang.setText("Uang   : Rp. " + String.valueOf(NumberFormat.getInstance(Locale.US).format(totalUang)));
        tvSpace1.setText(" ");
        row1.addView(tvSpace1);
        row1.addView(tvUang);
        tabelNotaMenu.addView(row1);

        TableRow row2 = new TableRow(this);
        TextView tvKembalian = new TextView(this);
        TextView tvSpace2 = new TextView(this);
        tvKembalian.setTypeface(typeface);
        tvKembalian.setTextSize(12.0f);
        tvKembalian.setGravity(Gravity.END);
        tvKembalian.setText("Kembalian   : Rp. " + String.valueOf(NumberFormat.getInstance(Locale.US).format(totalKembalian)));
        tvSpace2.setText(" ");
        row2.addView(tvSpace2);
        row2.addView(tvKembalian);
        tabelNotaMenu.addView(row2);

        //      "   Jln.Sei Musi No.34, Samarinda\n"
        msg +=  "--------------------------------\n" +
                "Total       : " + String.valueOf(NumberFormat.getInstance(Locale.US).format(totalHarga)) + "\n" +
                "Uang        : " + String.valueOf(NumberFormat.getInstance(Locale.US).format(totalUang)) + "\n" +
                "Kembalian   : " + String.valueOf(NumberFormat.getInstance(Locale.US).format(totalKembalian)) + "\n\n" +
                "         Terima Kasih\n" +
                "      Atas Pembelian Anda\n\n\n\n";

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cboxPrint.isChecked()) {
                    try {
                        printReceipt();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Print Failed", Toast.LENGTH_SHORT).show();
                    }
                }
                Intent intent = new Intent(ReceiptActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void printReceipt() throws IOException {
        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();

            beginListenForData();
        } catch (IOException e) {
            Toast.makeText(this, "Bluetooth Error", Toast.LENGTH_SHORT).show();
        }
        outputStream.write(msg.getBytes());
        outputStream.write(msg.getBytes());
        Toast.makeText(getApplicationContext(), "Receipt Printed", Toast.LENGTH_SHORT).show();
    }

    private void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = inputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                inputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Data has been Send", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDateAndOrder() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor1 = database.rawQuery("SELECT * FROM " + TABLE_TRANSAKSI + " WHERE tanggal_transaksi BETWEEN'" + currentDate + "' AND '" + currentDate + "'", null);
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_TRANSAKSI + " ORDER BY _id DESC LIMIT 1", null);
        tvNoOrder.setText("No Order : " + String.valueOf(cursor1.getCount()));
        cursor.moveToFirst();
        tvDateTime.setText("Tanggal : " + cursor.getString(1) + "\nPukul : " + cursor.getString(2));
    }
}
