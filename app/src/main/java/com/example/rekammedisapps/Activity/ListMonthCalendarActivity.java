package com.example.rekammedisapps.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rekammedisapps.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class ListMonthCalendarActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton btnTambahRekam;

    //ValueIntent
    private String idPasien, namaPasien, umurPasien, alamatPasien;

    //GetMonthCalendar
    private Calendar calendar;
    private int tahun, bulanInt, tanggal;
    private String bulan;

    //Database
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_month_calendar);
        getValueIntent();
        getMonthAndroid();

        btnTambahRekam = findViewById(R.id.btn_lmc_tambahrekammedis);
        btnTambahRekam.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_lmc_tambahrekammedis:
                Intent toForm = new Intent(ListMonthCalendarActivity.this, FormRekamMedisActivity.class);
                toForm.putExtra("idPasien", idPasien);
                toForm.putExtra("namaPasien", namaPasien);
                toForm.putExtra("umurPasien", umurPasien);
                toForm.putExtra("alamatPasien", alamatPasien);
                startActivity(toForm);
        }
    }

    private void getValueIntent(){
        Intent intent = getIntent();
        //FromListPasienAdapter.class
        idPasien = intent.getStringExtra("idPasien");
        namaPasien = intent.getStringExtra("namaPasien");
        umurPasien = intent.getStringExtra("umurPasien");
        alamatPasien = intent.getStringExtra("alamatPasien");
    }

    private void getMonthAndroid(){
        calendar = Calendar.getInstance();
        tahun = calendar.get(Calendar.YEAR);
        bulanInt = calendar.get(Calendar.MONTH);
        tanggal = calendar.get(Calendar.DAY_OF_MONTH);

        bulan = getMonthForInt(bulanInt);
    }

    private String getMonthForInt(int num){
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String [] months = dfs.getMonths();
        if (num >= 0 && num <= 11){
            month = months[num];
        }
        return month;
    }

    private void sendtoFirebase(){
        reference = FirebaseDatabase.getInstance().getReference("Data RekamMedis Pasien");

    }
}