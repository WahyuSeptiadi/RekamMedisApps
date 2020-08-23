package com.example.rekammedisapps.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class FormRekamMedisActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
    private Spinner spinnerPoli;

    //intent from detail rekam medis
    private String getTime, getTanggal, getBulan, getTahun;
    private String getNamaPasien, getUmurPasien, getNamaPerawat, getAlamat, getKeluhan, getImagePerawat, getRiwayat, getDiagnosa, getRencana, getPengobatan;
    private String getIdPasien, getIdPerawat, getIdRekam, getRujukanpoli, getNomerRekam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_rekam_medis);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        calendar = Calendar.getInstance();

        getValueInten();
        getMonthAndroid();
        getDataPerawat();
        getDataIntent();

        et_namaPasien = findViewById(R.id.et_frm_nama);
        et_umurPasien = findViewById(R.id.et_frm_umur);
        et_alamatPasien = findViewById(R.id.et_frm_alamat);
        et_keluhan = findViewById(R.id.et_frm_keluhan);
        et_riwayatpenyakit = findViewById(R.id.et_frm_riwayat);
        et_diagnosa = findViewById(R.id.et_frm_diagnosa);
        et_rencanaPenata = findViewById(R.id.et_frm_rencanapenatalaksanaan);
        et_pengobatan = findViewById(R.id.et_frm_pengobatan);
        btnAddRekamMedis = findViewById(R.id.cv_frm_btnaddrekammedis);
        spinnerPoli = findViewById(R.id.spinner_poli);
        spinnerPoli.setOnItemSelectedListener(this);
        TextView titlebar = findViewById(R.id.titlebar_profile);
        ImageView btnBack = findViewById(R.id.btnback_form);

        if (getIdPasien != null) {
            et_namaPasien.setText(getNamaPasien);
            et_alamatPasien.setText(getAlamat);
            et_diagnosa.setText(getDiagnosa);
            et_keluhan.setText(getKeluhan);
            et_rencanaPenata.setText(getRencana);
            et_riwayatpenyakit.setText(getRiwayat);
            et_pengobatan.setText(getPengobatan);
            et_umurPasien.setText(getUmurPasien);
            titlebar.setText(R.string.str_update_rekammedis);
        } else {
            et_namaPasien.setText(namaPasien);
            et_umurPasien.setText(umurPasien);
            et_alamatPasien.setText(alamatPasien);
        }

        btnBack.setOnClickListener(view -> {
            startActivity(new Intent(FormRekamMedisActivity.this, HomeActivity.class));
            finish();
        });

        btnAddRekamMedis.setOnClickListener(view -> {
            if (getIdPasien != null && getIdRekam != null) {
                updateToDatabase();
                Intent toListRekamMedis = new Intent(FormRekamMedisActivity.this, ListRekamMedisActivity.class);
                toListRekamMedis.putExtra("idPasien", getIdPasien);
                startActivity(toListRekamMedis);
            } else {
                sendToDatabase();
                Intent toListRekamMedis = new Intent(FormRekamMedisActivity.this, ListRekamMedisActivity.class);
                toListRekamMedis.putExtra("idPasien", idPasien);
                startActivity(toListRekamMedis);
            }
        });
    }

    private void getDataIntent() {
        Intent getData = getIntent();
//        getTime = getData.getStringExtra("time");
//        getTanggal = getData.getStringExtra("tanggal");
//        getBulan = getData.getStringExtra("bulan");
//        getTahun = getData.getStringExtra("tahun");
//        getIdPerawat = getData.getStringExtra("idperawat");
//        getNamaPerawat = getData.getStringExtra("namaperawat");
//        getImagePerawat = getData.getStringExtra("imageperawat");
        getIdRekam = getData.getStringExtra("idrekam");
        getIdPasien = getData.getStringExtra("idpasien");
        getNamaPasien = getData.getStringExtra("namapasien");
        getUmurPasien = getData.getStringExtra("umurpasien");
        getKeluhan = getData.getStringExtra("keluhan");
        getAlamat = getData.getStringExtra("alamat");
        getRiwayat = getData.getStringExtra("riwayat");
        getDiagnosa = getData.getStringExtra("diagnosa");
        getRencana = getData.getStringExtra("rencana");
        getPengobatan = getData.getStringExtra("pengobatan");
        getNomerRekam = getData.getStringExtra("nomerRekam");

    }

    private void getValueInten() {
        Intent getValue = getIntent();

        idPasien = getValue.getStringExtra("idPasien");
        namaPasien = getValue.getStringExtra("namaPasien");
        umurPasien = getValue.getStringExtra("umurPasien");
        alamatPasien = getValue.getStringExtra("alamatPasien");

    }

    private void getMonthAndroid() {
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

    public String getCurrentLocalTimeStamp(int plus) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        calendar.add(Calendar.MINUTE, plus);
        return currentTime.format(calendar.getTime());
    }

    private void sendToDatabase() {
        reference = FirebaseDatabase.getInstance().getReference("Data RekamMedis Pasien");
        String key = reference.push().getKey();

        String nama_pasien = et_namaPasien.getText().toString();
        String umur_pasien = et_umurPasien.getText().toString();
        String alamat_pasien = et_alamatPasien.getText().toString();
        String keluhan_pasien = et_keluhan.getText().toString();
        String riwayat = et_riwayatpenyakit.getText().toString();
        String diagnosa = et_diagnosa.getText().toString();
        String rencana = et_rencanaPenata.getText().toString();
        String pengobatan = et_pengobatan.getText().toString();

        //GENERATE NOMER REKAM MEDIS
        String nomerRekam = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> dataRekamMedis = new HashMap<>();
        dataRekamMedis.put("namaPerawat", userModel.getUsername());
        dataRekamMedis.put("imageURLPerawat", userModel.getImageURL());
        dataRekamMedis.put("idPerawat", idPerawat);
        dataRekamMedis.put("idPasien", idPasien);
        dataRekamMedis.put("namaPasien", nama_pasien);
        dataRekamMedis.put("keluhanPasien", keluhan_pasien);
        dataRekamMedis.put("alamatPasien", alamat_pasien);
        dataRekamMedis.put("umurPasien", umur_pasien);
        dataRekamMedis.put("riwayatPasien", riwayat);
        dataRekamMedis.put("diagnosaPasien", diagnosa);
        dataRekamMedis.put("rencanaPenataPasien", rencana);
        dataRekamMedis.put("pengobatanPasien", pengobatan);
        dataRekamMedis.put("tanggalPelayanan", String.valueOf(tanggal));
        dataRekamMedis.put("bulanPelayanan", bulan);
        dataRekamMedis.put("tahunPelayanan", String.valueOf(tahun));
        dataRekamMedis.put("idRekamMedis", key);
        dataRekamMedis.put("timePelayanan", getCurrentLocalTimeStamp(0));
        dataRekamMedis.put("rujukanPoli", getRujukanpoli);
        dataRekamMedis.put("nomerRekam", nomerRekam);

        assert key != null;
        reference.child(idPasien).child(key).setValue(dataRekamMedis);
    }

    private void updateToDatabase() {
        reference = FirebaseDatabase.getInstance().getReference("Data RekamMedis Pasien");

        String nama_pasien = et_namaPasien.getText().toString();
        String umur_pasien = et_umurPasien.getText().toString();
        String alamat_pasien = et_alamatPasien.getText().toString();
        String keluhan_pasien = et_keluhan.getText().toString();
        String riwayat = et_riwayatpenyakit.getText().toString();
        String diagnosa = et_diagnosa.getText().toString();
        String rencana = et_rencanaPenata.getText().toString();
        String pengobatan = et_pengobatan.getText().toString();

        HashMap<String, Object> dataRekamMedis = new HashMap<>();
        dataRekamMedis.put("namaPerawat", userModel.getUsername()); //jika yang update beda perawat
        dataRekamMedis.put("imageURLPerawat", userModel.getImageURL()); //jika yang update beda perawat
        dataRekamMedis.put("idPerawat", idPerawat); //jika yang update beda perawat
        dataRekamMedis.put("idPasien", getIdPasien);
        dataRekamMedis.put("idRekamMedis", getIdRekam);
        dataRekamMedis.put("namaPasien", nama_pasien);
        dataRekamMedis.put("keluhanPasien", keluhan_pasien);
        dataRekamMedis.put("alamatPasien", alamat_pasien);
        dataRekamMedis.put("umurPasien", umur_pasien);
        dataRekamMedis.put("riwayatPasien", riwayat);
        dataRekamMedis.put("diagnosaPasien", diagnosa);
        dataRekamMedis.put("rencanaPenataPasien", rencana);
        dataRekamMedis.put("pengobatanPasien", pengobatan);
        dataRekamMedis.put("timePelayanan", getCurrentLocalTimeStamp(0)); //get current time aja
        dataRekamMedis.put("tanggalPelayanan", String.valueOf(tanggal)); //get current time aja
        dataRekamMedis.put("bulanPelayanan", bulan); //get current time aja
        dataRekamMedis.put("tahunPelayanan", String.valueOf(tahun)); //get current time aja
        dataRekamMedis.put("rujukanPoli", getRujukanpoli);
        dataRekamMedis.put("nomerRekam", getNomerRekam);

        reference.child(getIdPasien).child(getIdRekam).setValue(dataRekamMedis);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        getRujukanpoli = adapterView.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}