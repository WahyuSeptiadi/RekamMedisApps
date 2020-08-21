package com.example.rekammedisapps.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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

import java.util.ArrayList;

public class DetailRekamMedisActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_btnBack;
    private FloatingActionButton btnAdd;
    private RecyclerView recyclerView;
    private ArrayList<RekamMedisModel> rekamMedisModelArrayList;

    private ListRekamMedisAdapter listRekamMedisAdapter;

    //GetValueIntent
    private String idPasien, namaPasien, umurPasien, alamatPasien;
    //Database
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rekam_medis);

        getValueIntent();

        recyclerView = findViewById(R.id.rv_list_detailrekammedis);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.smoothScrollToPosition(0);


        reference = FirebaseDatabase.getInstance().getReference("Data RekamMedis Pasien");

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
//                RekamMedisModel rekamMedisModel = snapshot.getValue(RekamMedisModel.class);
                if (!snapshot.exists()){
//                    assert rekamMedisModel != null;
//                    if ()
                }else {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        RekamMedisModel rekamMedisModel = dataSnapshot.getValue(RekamMedisModel.class);
                        rekamMedisModelArrayList.add(rekamMedisModel);
                    }

                    listRekamMedisAdapter = new ListRekamMedisAdapter(DetailRekamMedisActivity.this, rekamMedisModelArrayList);
                    recyclerView.setAdapter(listRekamMedisAdapter);
                    listRekamMedisAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnback_detailrekammedis:
                Intent backToList = new Intent(DetailRekamMedisActivity.this, ListPasienActivity.class);
                startActivity(backToList);
                break;
            case R.id.add_drm_tambahrekammedis:
                Intent toForm = new Intent(DetailRekamMedisActivity.this, FormRekamMedisActivity.class);
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

    private void getValueIntent(){
        Intent getValueFromList = getIntent();

        idPasien = getValueFromList.getStringExtra("idPasien");
        namaPasien = getValueFromList.getStringExtra("namaPasien");
        umurPasien = getValueFromList.getStringExtra("umurPasien");
        alamatPasien = getValueFromList.getStringExtra("alamatPasien");
    }
}