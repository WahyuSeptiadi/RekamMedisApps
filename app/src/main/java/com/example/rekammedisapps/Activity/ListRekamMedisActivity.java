package com.example.rekammedisapps.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekammedisapps.Adapter.ListRekamMedisAdapter;
import com.example.rekammedisapps.Model.RekamMedisModel;
import com.example.rekammedisapps.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class ListRekamMedisActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_btnBack;
    private FloatingActionButton btnAdd;
    private RecyclerView recyclerView;
    private ArrayList<RekamMedisModel> rekamMedisModelArrayList;

    private ListRekamMedisAdapter listRekamMedisAdapter;

    //GetValueIntent
    private String idPasien, namaPasien, umurPasien, alamatPasien;
    //Database
    private DatabaseReference reference;
    private Calendar calendar;
    private int tahun, bulanInt, tanggal;
    private String bulan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rekam_medis);

        getValueIntent();

        recyclerView = findViewById(R.id.rv_list_detailrekammedis);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.smoothScrollToPosition(0);

        reference = FirebaseDatabase.getInstance().getReference("Data RekamMedis Pasien");
        calendar = Calendar.getInstance();

        iv_btnBack = findViewById(R.id.btnback_detailrekammedis);
        btnAdd = findViewById(R.id.add_drm_tambahrekammedis);
        iv_btnBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        getAllDetailRekam();
    }

    private void getAllDetailRekam() {
        rekamMedisModelArrayList = new ArrayList<>();

        reference.child(idPasien).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child(idPasien).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rekamMedisModelArrayList.clear();
                if (!snapshot.exists()) {
                    Toast.makeText(ListRekamMedisActivity.this, "Maaf, rekam medis masih kosong kosong", Toast.LENGTH_SHORT).show();
                } else {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        RekamMedisModel rekamMedisModel = dataSnapshot.getValue(RekamMedisModel.class);
                        assert rekamMedisModel != null;
//                        if (rekamMedisModel.getBulanPelayanan().equals(getCurrentMonth()) &&
//                                rekamMedisModel.getTahunPelayanan().equals(String.valueOf(getCurrentYear()))) {
                            rekamMedisModelArrayList.add(rekamMedisModel);
//                        }
                    }

                    listRekamMedisAdapter = new ListRekamMedisAdapter(ListRekamMedisActivity.this, rekamMedisModelArrayList);
                    recyclerView.setAdapter(listRekamMedisAdapter);
                    listRekamMedisAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private int getCurrentDate() {
        tanggal = calendar.get(Calendar.DAY_OF_MONTH);
        return tanggal;
    }

    private String getCurrentMonth() {
        bulanInt = calendar.get(Calendar.MONTH);
        bulan = getMonthForInt(bulanInt);
        return bulan;
    }

    private int getCurrentYear() {
        tahun = calendar.get(Calendar.YEAR);
        return tahun;
    }

    private String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnback_detailrekammedis:
                Intent backToList = new Intent(ListRekamMedisActivity.this, ListPasienActivity.class);
                startActivity(backToList);
                break;
            case R.id.add_drm_tambahrekammedis:
                Intent toForm = new Intent(ListRekamMedisActivity.this, FormRekamMedisActivity.class);
                toForm.putExtra("idPasien", idPasien);
                toForm.putExtra("namaPasien", namaPasien);
                toForm.putExtra("umurPasien", umurPasien);
                toForm.putExtra("alamatPasien", alamatPasien);

                startActivity(toForm);
                break;
            default:
                break;
        }
    }

    private void getValueIntent() {
        Intent getValueFromList = getIntent();

        idPasien = getValueFromList.getStringExtra("idPasien");
        namaPasien = getValueFromList.getStringExtra("namaPasien");
        umurPasien = getValueFromList.getStringExtra("umurPasien");
        alamatPasien = getValueFromList.getStringExtra("alamatPasien");
    }
}