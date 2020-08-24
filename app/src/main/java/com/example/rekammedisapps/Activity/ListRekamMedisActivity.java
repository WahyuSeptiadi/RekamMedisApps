package com.example.rekammedisapps.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekammedisapps.Adapter.ListRekamMedisAdapter;
import com.example.rekammedisapps.Model.RekamMedisModel;
import com.example.rekammedisapps.Model.UserModel;
import com.example.rekammedisapps.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListRekamMedisActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ArrayList<RekamMedisModel> rekamMedisModelArrayList;
    private ProgressBar progressBar;
    private ListRekamMedisAdapter listRekamMedisAdapter;

    //GetValueIntent
    private String idPasien, namaPasien, umurPasien, alamatPasien, keluhanPasien, riwayatPasien;
    //Database
    private DatabaseReference reference;
    private FirebaseUser mUser;

    private UserModel userModel;
    private String typeUser;

//    private Calendar calendar;
//    private int tahun, bulanInt, tanggal;
//    private String bulan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rekam_medis);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        getValueIntent();
        getTypeUser();

        progressBar = findViewById(R.id.progressBar_listRekamMedis);
        recyclerView = findViewById(R.id.rv_list_detailrekammedis);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.smoothScrollToPosition(0);

        reference = FirebaseDatabase.getInstance().getReference("Data RekamMedis Pasien");
//        calendar = Calendar.getInstance();

        ImageView iv_btnBack = findViewById(R.id.btnback_detailrekammedis);
        FloatingActionButton btnAdd = findViewById(R.id.add_drm_tambahrekammedis);
        iv_btnBack.setOnClickListener(this);
        if (typeUser.equals("admin")) {
            btnAdd.setVisibility(View.VISIBLE);
        } else {
            btnAdd.setVisibility(View.GONE);
        }
        btnAdd.setOnClickListener(this);

        getAllDetailRekam();
    }

    private void getAllDetailRekam() {
        rekamMedisModelArrayList = new ArrayList<>();

        reference.child(idPasien).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rekamMedisModelArrayList.clear();
                if (!snapshot.exists()) {
                    Toast.makeText(ListRekamMedisActivity.this, "Maaf, rekam medis masih kosong", Toast.LENGTH_SHORT).show();
                } else {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        RekamMedisModel rekamMedisModel = dataSnapshot.getValue(RekamMedisModel.class);
                        assert rekamMedisModel != null;
                        rekamMedisModelArrayList.add(rekamMedisModel);
                    }

                    listRekamMedisAdapter = new ListRekamMedisAdapter(ListRekamMedisActivity.this, rekamMedisModelArrayList);
                    recyclerView.setAdapter(listRekamMedisAdapter);
                    listRekamMedisAdapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private int getCurrentDate() {
//        tanggal = calendar.get(Calendar.DAY_OF_MONTH);
//        return tanggal;
//    }
//
//    private String getCurrentMonth() {
//        bulanInt = calendar.get(Calendar.MONTH);
//        bulan = getMonthForInt(bulanInt);
//        return bulan;
//    }
//
//    private int getCurrentYear() {
//        tahun = calendar.get(Calendar.YEAR);
//        return tahun;
//    }

//    private String getMonthForInt(int num) {
//        String month = "wrong";
//        DateFormatSymbols dfs = new DateFormatSymbols();
//        String[] months = dfs.getMonths();
//        if (num >= 0 && num <= 11) {
//            month = months[num];
//        }
//        return month;
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnback_detailrekammedis:
                Intent backToList = new Intent(ListRekamMedisActivity.this, HomeActivity.class);
                startActivity(backToList);
                finish();
                break;
            case R.id.add_drm_tambahrekammedis:
                Intent toForm = new Intent(ListRekamMedisActivity.this, FormRekamMedisActivity.class);
                toForm.putExtra("idPasien", idPasien);
                toForm.putExtra("namaPasien", namaPasien);
                toForm.putExtra("umurPasien", umurPasien);
                toForm.putExtra("alamatPasien", alamatPasien);
                toForm.putExtra("keluhanPasien", keluhanPasien);
                toForm.putExtra("riwayatPasien", riwayatPasien);

                startActivity(toForm);
                finish();
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
        keluhanPasien = getValueFromList.getStringExtra("keluhanPasien");
        riwayatPasien = getValueFromList.getStringExtra("riwayatPasien");
    }

    private void getTypeUser() {
        String idPasien = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(idPasien).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModel = snapshot.getValue(UserModel.class);
                assert userModel != null;
                typeUser = userModel.getTypeUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}