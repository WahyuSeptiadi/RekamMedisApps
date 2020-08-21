package com.example.rekammedisapps.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.rekammedisapps.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailRekamMedisActivity extends AppCompatActivity {

    private TextView tvNamaPasien, tvNamaPerawat, tvAlamatPasien, tvKeluhanPasien;
    private TextView tvTime, tvTanggal, tvBulan, tvTahun;
    private CircleImageView civProfilePasien;

    private String getTime, getTanggal, getBulan, getTahun;
    private String getNamaPasien, getNamaPerawat, getAlamat, getKeluhan, getImagePerawat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rekam_medis);

        civProfilePasien = findViewById(R.id.civ_foto_pasien_detail_rekam_medis);
        tvNamaPerawat = findViewById(R.id.tv_namaperawat_detail_rekam_medis);
        tvTime = findViewById(R.id.tv_time_detail_rekam_medis);
        tvTanggal = findViewById(R.id.tv_tanggal_detail_rekam_medis);
        tvBulan = findViewById(R.id.tv_bulan_detail_rekam_medis);
        tvTahun = findViewById(R.id.tv_tahun_detail_rekam_medis);
        tvNamaPasien = findViewById(R.id.tv_namapasien_detail_rekam_medis);
        tvAlamatPasien = findViewById(R.id.tv_alamat_detail_rekam_medis);
        tvKeluhanPasien = findViewById(R.id.tv_keluhan_detail_rekam_medis);

        getDataIntent();

        if (getNamaPasien != null) {
            tvTime.setText(getTime);
            tvTanggal.setText(getTanggal);
            tvBulan.setText(getBulan);
            tvTahun.setText(getTahun);
            tvNamaPasien.setText(getNamaPasien);
            tvKeluhanPasien.setText(getKeluhan);
            tvAlamatPasien.setText(getAlamat);
            tvNamaPerawat.setText(getNamaPerawat);

            if (getImagePerawat.substring(0, 4).equals("http")) {
                Picasso.get().load(getImagePerawat).into(civProfilePasien);
            } else {
                Picasso.get().load(R.drawable.icon_default_profile).into(civProfilePasien);
            }
        }
    }

    private void getDataIntent() {
        Intent getData = getIntent();
        getTime = getData.getStringExtra("time");
        getTanggal = getData.getStringExtra("tanggal");
        getBulan = getData.getStringExtra("bulan");
        getTahun = getData.getStringExtra("tahun");
        getNamaPasien = getData.getStringExtra("namapasien");
        getNamaPerawat = getData.getStringExtra("namaperawat");
        getKeluhan = getData.getStringExtra("keluhan");
        getAlamat = getData.getStringExtra("alamat");
        getImagePerawat = getData.getStringExtra("imageperawat");
    }
}