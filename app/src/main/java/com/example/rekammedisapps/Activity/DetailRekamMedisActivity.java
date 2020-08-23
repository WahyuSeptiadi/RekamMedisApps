package com.example.rekammedisapps.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rekammedisapps.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailRekamMedisActivity extends AppCompatActivity {

    private TextView tvNamaPasien, tvUmurPasien, tvNamaPerawat, tvAlamatPasien, tvKeluhanPasien, tvRiwayat, tvDiagnosa, tvRencana, tvPengobatan;
    private TextView tvTime, tvTanggal, tvBulan, tvTahun, tvRujukanpoli;
    private CircleImageView civProfilePasien;
    private ImageView iv_btnBack;
    private ImageView iv_EditRekamMedis;

    private String getTime, getTanggal, getBulan, getTahun;
    private String getNamaPasien, getUmurPasien, getNamaPerawat, getAlamat, getKeluhan, getImagePerawat, getRiwayat, getDiagnosa, getRencana, getPengobatan;
    private String getIdPasien, getIdPerawat, getIdRekam, getRujukanpoli;

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
        tvUmurPasien = findViewById(R.id.tv_umurpasien_detail_rekam_medis);
        tvAlamatPasien = findViewById(R.id.tv_alamat_detail_rekam_medis);
        tvKeluhanPasien = findViewById(R.id.tv_keluhan_detail_rekam_medis);
        tvRiwayat = findViewById(R.id.tv_riwayat_detail_rekam_medis);
        tvDiagnosa = findViewById(R.id.tv_diagnosa_detail_rekam_medis);
        tvRencana = findViewById(R.id.tv_rencanapenata_detail_rekam_medis);
        tvPengobatan = findViewById(R.id.tv_pengobatanatautindakan_detail_rekam_medis);
        iv_btnBack = findViewById(R.id.btnback_hasil_rekam_medis);
        iv_EditRekamMedis = findViewById(R.id.iv_edit_rekammedis);
        tvRujukanpoli = findViewById(R.id.tv_rujukanpoli_detail_rekam_medis);

        iv_btnBack.setOnClickListener(view -> {
            startActivity(new Intent(DetailRekamMedisActivity.this, HomeActivity.class));
            finish();
        });

        iv_EditRekamMedis.setOnClickListener(view -> {
            Intent toUpdate = new Intent(DetailRekamMedisActivity.this, FormRekamMedisActivity.class);
            toUpdate.putExtra("time", getTime);
            toUpdate.putExtra("tanggal", getTanggal);
            toUpdate.putExtra("bulan", getBulan);
            toUpdate.putExtra("tahun", getTahun);
            toUpdate.putExtra("namapasien", getNamaPasien);
            toUpdate.putExtra("umurpasien", getUmurPasien);
            toUpdate.putExtra("namaperawat", getNamaPerawat);
            toUpdate.putExtra("keluhan", getKeluhan);
            toUpdate.putExtra("alamat", getAlamat);
            toUpdate.putExtra("imageperawat", getImagePerawat);
            toUpdate.putExtra("riwayat", getRiwayat);
            toUpdate.putExtra("diagnosa", getDiagnosa);
            toUpdate.putExtra("rencana", getRencana);
            toUpdate.putExtra("pengobatan", getPengobatan);
            toUpdate.putExtra("idpasien", getIdPasien);
            toUpdate.putExtra("idperawat", getIdPerawat);
            toUpdate.putExtra("idrekam", getIdRekam);
            toUpdate.putExtra("rujukanPoli", getRujukanpoli);

            startActivity(toUpdate);
            finish();
        });

        getDataIntent();

        //set detail
        if (getNamaPasien != null) {
            tvTime.setText(getTime);
            tvTanggal.setText(getTanggal);
            tvBulan.setText(getBulan);
            tvTahun.setText(getTahun);
            tvNamaPasien.setText(getNamaPasien);
            tvUmurPasien.setText(getUmurPasien);
            tvKeluhanPasien.setText(getKeluhan);
            tvAlamatPasien.setText(getAlamat);
            tvNamaPerawat.setText(getNamaPerawat);
            tvRiwayat.setText(getRiwayat);
            tvDiagnosa.setText(getDiagnosa);
            tvRencana.setText(getRencana);
            tvPengobatan.setText(getPengobatan);
            tvRujukanpoli.setText(getRujukanpoli);

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
        getUmurPasien = getData.getStringExtra("umurpasien");
        getNamaPerawat = getData.getStringExtra("namaperawat");
        getKeluhan = getData.getStringExtra("keluhan");
        getAlamat = getData.getStringExtra("alamat");
        getImagePerawat = getData.getStringExtra("imageperawat");
        getRiwayat = getData.getStringExtra("riwayat");
        getDiagnosa = getData.getStringExtra("diagnosa");
        getRencana = getData.getStringExtra("rencana");
        getPengobatan = getData.getStringExtra("pengobatan");
        getIdPasien = getData.getStringExtra("idpasien");
        getIdPerawat = getData.getStringExtra("idperawat");
        getIdRekam = getData.getStringExtra("idrekam");
        getRujukanpoli = getData.getStringExtra("rujukanPoli");
    }
}