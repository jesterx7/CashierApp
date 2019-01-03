package com.example.see.cashierapp;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URLConnection;
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
    private TextView tvTotalEarning, tvDateTimeEarning;
    private Button btnShow, btnExport;
    private Calendar calendar;
    private DatabaseHelper databaseHelper;
    private String dateFrom, dateTo;
    private long totalEarning = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.earning_fragment, container, false);

        edtDateFrom = myView.findViewById(R.id.edtDateFrom);
        edtDateTo = myView.findViewById(R.id.edtDateTo);
        tabelEarning = myView.findViewById(R.id.tabelEarning);
        btnShow = myView.findViewById(R.id.btnShow);
        tvTotalEarning = myView.findViewById(R.id.tvTotalEarning);
        tvDateTimeEarning = myView.findViewById(R.id.tvDateTimeEarning);
        btnExport = myView.findViewById(R.id.btnExport);

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
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportToExcel();
                File file = new File(Environment.getExternalStorageDirectory(), "earning.xls");
                Uri path = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", file);
                if (Build.VERSION.SDK_INT >= 24) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/vnd.ms-excel");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    PackageManager pm = getActivity().getPackageManager();
                    if (intent.resolveActivity(pm) != null) {
                        getActivity().getApplicationContext().startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.ms-excel").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().getApplicationContext().startActivity(intent);
                }
            }
        });


        return myView;
    }

    private void exportToExcel() {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Toast.makeText(getContext(), "Storage is not available or read only", Toast.LENGTH_SHORT).show();
            return;
        }

        Workbook wb = new HSSFWorkbook();
        Cell c = null;

        CellStyle cs = wb.createCellStyle();
        cs.setAlignment(CellStyle.ALIGN_CENTER);

        Sheet sheet1 = null;
        sheet1 = wb.createSheet("Earning");

        Row rowHead = sheet1.createRow(0);
        c = rowHead.createCell(0);
        c.setCellValue("No Order");
        c.setCellStyle(cs);
        c = rowHead.createCell(1);
        c.setCellValue("Tanggal");
        c.setCellStyle(cs);
        c = rowHead.createCell(2);
        c.setCellValue("Waktu");
        c.setCellStyle(cs);
        c = rowHead.createCell(3);
        c.setCellValue("Earning");
        c.setCellStyle(cs);

        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_TRANSAKSI + " WHERE tanggal_transaksi BETWEEN '" + dateFrom+"'" +
                " AND '" + dateTo +"'", null);
        cursor.moveToFirst();
        int countRow = 1;
        for (int i = 1; i <= cursor.getCount(); i++) {
            cursor.moveToPosition(i-1);
            Row row = sheet1.createRow(i);
            c = row.createCell(0);
            c.setCellValue(cursor.getInt(0));
            c.setCellStyle(cs);
            c = row.createCell(1);
            c.setCellValue(cursor.getString(1));
            c.setCellStyle(cs);
            c = row.createCell(2);
            c.setCellValue(cursor.getString(2));
            c.setCellStyle(cs);
            c = row.createCell(3);
            c.setCellValue(cursor.getInt(3));
            c.setCellStyle(cs);
            countRow++;
        }
        Row rows = sheet1.createRow(countRow+1);
        c = rows.createCell(2);
        c.setCellValue("Total");
        c.setCellStyle(cs);

        c = rows.createCell(3);
        c.setCellValue(totalEarning);
        c.setCellStyle(cs);

        Row rowsTgl = sheet1.createRow(countRow+2);
        c = rowsTgl.createCell(2);
        c.setCellValue("Tanggal");
        c.setCellStyle(cs);

        c = rowsTgl.createCell(3);
        c.setCellValue(dateFrom + " - " + dateTo);
        c.setCellStyle(cs);

        sheet1.setColumnWidth(0, (15 * 150));
        sheet1.setColumnWidth(1, (15 * 300));
        sheet1.setColumnWidth(2, (15 * 300));
        sheet1.setColumnWidth(3, (15 * 500));

        File file = new File(getContext().getExternalFilesDir(null), "earning.xls");
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Toast.makeText(getContext(), "Export Success", Toast.LENGTH_SHORT).show();
            System.out.println("PATH : " + file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateTable() {
        tabelEarning.removeAllViews();
        tvTotalEarning.setText("");
        dateFrom = edtDateFrom.getText().toString();
        dateTo = edtDateTo.getText().toString();

        tvDateTimeEarning.setText("Data Tanggal : " + dateFrom + " - " + dateTo);

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
        System.out.println("CURSOR HASIL : " +cursor);
        ArrayList<String> listTanggal = new ArrayList<>();
        ArrayList<String> listEarning = new ArrayList<>();
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
        btnExport.setEnabled(true);
    }

    private void updateDateTo() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        edtDateTo.setText(df.format(calendar.getTime()));
    }

    private void updateDate() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        edtDateFrom.setText(df.format(calendar.getTime()));
    }

    private Boolean isExternalStorageReadOnly(){
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private Boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}
