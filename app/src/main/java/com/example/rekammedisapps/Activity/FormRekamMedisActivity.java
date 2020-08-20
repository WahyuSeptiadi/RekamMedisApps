package com.example.rekammedisapps.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.rekammedisapps.Model.UserModel;
import com.example.rekammedisapps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class FormRekamMedisActivity extends AppCompatActivity {

    private CardView btnAddRekamMedis;

    //Database
    private DatabaseReference reference, referenceDataPerawat;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    //Model
    private UserModel userModel;
    private String idPerawat;

    private EditText et_namaPasien, et_umurPasien, et_alamatPasien, et_keluhan, et_riwayatpenyakit,
            et_diagnosa, et_rencanaPenata, et_pengobatan;
    //Intent
    private String idPasien, namaPasien, umurPasien, alamatPasien;
    //GetMonthCalendar
    private Calendar calendar;
    private int tahun, bulanInt, tanggal;
    private String bulan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_rekam_medis);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        getValueInten();
        getMonthAndroid();
        getDataPerawat();

        et_namaPasien = findViewById(R.id.et_frm_nama);
        et_umurPasien = findViewById(R.id.et_frm_umur);
        et_alamatPasien = findViewById(R.id.et_frm_alamat);
        et_keluhan = findViewById(R.id.et_frm_keluhan);
        et_riwayatpenyakit = findViewById(R.id.et_frm_riwayat);
        et_diagnosa = findViewById(R.id.et_frm_diagnosa);
        et_rencanaPenata = findViewById(R.id.et_frm_rencanapenatalaksanaan);
        et_pengobatan = findViewById(R.id.et_frm_pengobatan);

        btnAddRekamMedis = findViewById(R.id.cv_frm_btnaddrekammedis);
        btnAddRekamMedis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToDatabase();
                Intent toDetailRekamMedis = new Intent(FormRekamMedisActivity.this, DetailRekamMedisActivity.class);
                toDetailRekamMedis.putExtra("idPasien", idPasien);
                toDetailRekamMedis.putExtra("namaPasien", namaPasien);
                toDetailRekamMedis.putExtra("umurPasien", umurPasien);
                toDetailRekamMedis.putExtra("alamatPasien", alamatPasien);
                startActivity(toDetailRekamMedis);
            }
        });

        et_namaPasien.setText(namaPasien);
        et_umurPasien.setText(umurPasien);
        et_alamatPasien.setText(alamatPasien);

    }

    private void getValueInten() {
        Intent getValue = getIntent();

        idPasien = getValue.getStringExtra("idPasien");
        namaPasien = getValue.getStringExtra("namaPasien");
        umurPasien = getValue.getStringExtra("umurPasien");
        alamatPasien = getValue.getStringExtra("alamatPasien");

    }

    private void getMonthAndroid() {
        calendar = Calendar.getInstance();
        tahun = calendar.get(Calendar.YEAR);
        bulanInt = calendar.get(Calendar.MONTH);
        tanggal = calendar.get(Calendar.DAY_OF_MONTH);

        bulan = getMonthForInt(bulanInt);
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

    private void getDataPerawat() {
        idPerawat = mUser.getUid();
        referenceDataPerawat = FirebaseDatabase.getInstance().getReference("Users");
        referenceDataPerawat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(FormRekamMedisActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                } else {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.getKey().equals(idPerawat)) {
                            userModel = dataSnapshot.getValue(UserModel.class);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendToDatabase() {
        reference = FirebaseDatabase.getInstance().getReference("Data RekamMedis Pasien");
        String key = reference.push().getKey();

        String nama_pasien = et_namaPasien.getText().toString();
        String umur_pasien = et_umurPasien.getText().toString();
        String alamat_pasien = et_alamatPasien.getText().toString();
        String keluhan_pasien = et_keluhan.getText().toString();

        HashMap<String, Object> dataRekamMedis = new HashMap<>();
        dataRekamMedis.put("namaPerawat", userModel.getUsername());
        dataRekamMedis.put("imageURLPerawat", userModel.getImageURL());
        dataRekamMedis.put("idPerawat", idPerawat);
        dataRekamMedis.put("idPasien", idPasien);
        dataRekamMedis.put("namaPasien", nama_pasien);
        dataRekamMedis.put("keluhanPasien", keluhan_pasien);
        dataRekamMedis.put("alamatPasien", alamat_pasien);
        dataRekamMedis.put("umurPasien", umur_pasien);
        dataRekamMedis.put("tanggalPelayanan", String.valueOf(tanggal));
        dataRekamMedis.put("bulanPelayanan", bulan);
        dataRekamMedis.put("tahunPelayanan", String.valueOf(tahun));
        dataRekamMedis.put("idRekamMedis", key);


        reference.child(idPasien).child(key).setValue(dataRekamMedis);
    }
}