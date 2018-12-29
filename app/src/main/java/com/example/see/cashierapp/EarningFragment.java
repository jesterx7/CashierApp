package com.example.see.cashierapp;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.example.see.cashierapp.DatabaseContract.TABLE_TRANSAKSI;

public class EarningFragment extends Fragment {
    View myView;

    private EditText edtDateFrom, edtDateTo;
    private TableLayout tabelEarning;
    private TextView tvTotalEarning;
    private Button btnShow;
    private Calendar calendar;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.earning_fragment, container, false);

        edtDateFrom = myView.findViewById(R.id.edtDateFrom);
        edtDateTo = myView.findViewById(R.id.edtDateTo);
        tabelEarning = myView.findViewById(R.id.tabelEarning);
        btnShow = myView.findViewById(R.id.btnShow);
        tvTotalEarning = myView.findViewById(R.id.tvTotalEarning);

        calendar = Calendar.getInstance();
        databaseHelper = new DatabaseHelper(getContext());

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }
        };

        final DatePickerDialog.OnDateSetListener dateToListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateTo();
            }
        };

        edtDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateSetListener, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edtDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateToListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTable();
            }
        });


        return myView;
    }

    private void updateTable() {
        tabelEarning.removeAllViews();
        String dateFrom = edtDateFrom.getText().toString();
        String dateTo = edtDateTo.getText().toString();

        Typeface typefaceMedium = ResourcesCompat.getFont(getContext(), R.font.rubik_medium);
        Typeface typefaceRegular = ResourcesCompat.getFont(getContext(), R.font.rubik_regular);
        TableLayout.LayoutParams paramsRow = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        paramsRow.setMargins(0, 10, 0 ,10);
        TableRow tableRow = new TableRow(getContext());
        TextView textNo = new TextView(getContext());
        TextView textTgl = new TextView(getContext());
        TextView textEarn = new TextView(getContext());
        textNo.setTextSize(14.0f);
        textTgl.setTextSize(14.0f);
        textEarn.setTextSize(14.0f);
        textNo.setTypeface(typefaceMedium);
        textTgl.setTypeface(typefaceMedium);
        textEarn.setTypeface(typefaceMedium);
        textNo.setGravity(Gravity.CENTER);
        textTgl.setGravity(Gravity.CENTER);
        textEarn.setGravity(Gravity.CENTER);
        textNo.setText("No");
        textTgl.setText("Tanggal");
        textEarn.setText("Earning");
        tableRow.addView(textNo);
        tableRow.addView(textTgl);
        tableRow.addView(textEarn);
        tableRow.setLayoutParams(paramsRow);
        tabelEarning.addView(tableRow);

        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_TRANSAKSI + " WHERE tanggal_transaksi BETWEEN '" + dateFrom+"'" +
                " AND '" + dateTo +"'", null);
        ArrayList<String> listTanggal = new ArrayList<>();
        ArrayList<String> listEarning = new ArrayList<>();
        long totalEarning = 0;
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            listTanggal.add(cursor.getString(1).toString());
            listEarning.add(String.valueOf(NumberFormat.getInstance(Locale.US).format(cursor.getInt(3))));
            totalEarning += cursor.getInt(3);
        }
        for (int i = 0; i < cursor.getCount(); i++) {
            TableRow row = new TableRow(getContext());
            TextView tvNo = new TextView(getContext());
            TextView tvTgl = new TextView(getContext());
            TextView tvEarn = new TextView(getContext());
            tvNo.setTextSize(14.0f);
            tvTgl.setTextSize(14.0f);
            tvEarn.setTextSize(14.0f);
            tvNo.setTypeface(typefaceRegular);
            tvTgl.setTypeface(typefaceRegular);
            tvEarn.setTypeface(typefaceRegular);
            tvNo.setGravity(Gravity.CENTER);
            tvTgl.setGravity(Gravity.CENTER);
            tvEarn.setGravity(Gravity.CENTER);
            tvNo.setText(String.valueOf(i+1));
            tvTgl.setText(listTanggal.get(i));
            tvEarn.setText("Rp. " + listEarning.get(i));
            row.setLayoutParams(paramsRow);
            row.addView(tvNo);
            row.addView(tvTgl);
            row.addView(tvEarn);
            tabelEarning.addView(row);
        }
        tvTotalEarning.setText("Total     : Rp. " + String.valueOf(NumberFormat.getInstance(Locale.US).format(totalEarning)));
        tvTotalEarning.setVisibility(View.VISIBLE);
        tabelEarning.setVisibility(View.VISIBLE);
    }

    private void updateDateTo() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        edtDateTo.setText(df.format(calendar.getTime()));
    }

    private void updateDate() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        edtDateFrom.setText(df.format(calendar.getTime()));
    }
}
