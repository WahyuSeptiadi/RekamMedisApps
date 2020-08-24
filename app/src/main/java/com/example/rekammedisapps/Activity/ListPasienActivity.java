package com.example.rekammedisapps.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekammedisapps.Adapter.ListPasienAdapter;
import com.example.rekammedisapps.Model.PasienModel;
import com.example.rekammedisapps.Model.UserModel;
import com.example.rekammedisapps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListPasienActivity extends AppCompatActivity {

    //Database
    private DatabaseReference reference, reference2;

    private String typeUser, idPasien;

    private ProgressBar pb_listpasien;
    private RecyclerView rv_listpasien;
    private ArrayList<PasienModel> pasienModelArrayList;

    //Class
    private ListPasienAdapter listPasienAdapter;

    //Database
    private FirebaseUser mUser;

    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pasien);
        reference = FirebaseDatabase.getInstance().getReference("Data Umum Pasien");
        reference2 = FirebaseDatabase.getInstance().getReference("Users");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        idPasien = mUser.getUid();

        getTypeUser();
        if (typeUser.equals("admin")) {
            getAllPasien();
        } else {
            getOnePasien();
        }
        pb_listpasien = findViewById(R.id.progressBar_listPasien);
        rv_listpasien = findViewById(R.id.rv_lp_listpasien);
        rv_listpasien.setLayoutManager(new LinearLayoutManager(this));
        rv_listpasien.setHasFixedSize(true);
        rv_listpasien.smoothScrollToPosition(0);

        //Button
        ImageView iv_btnback = findViewById(R.id.iv_lp_btnback);
        iv_btnback.setOnClickListener(view -> {
            startActivity(new Intent(ListPasienActivity.this, HomeActivity.class));
            finish();
        });

        //InisialisasiDatabase

    }

    private void getAllPasien() {
        pasienModelArrayList = new ArrayList<>();
        Query query = reference.orderByChild("nama");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pasienModelArrayList.clear();
                if (!snapshot.exists()) {
                    Toast.makeText(ListPasienActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                } else {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        PasienModel pasienModel = dataSnapshot.getValue(PasienModel.class);
                        Log.d("DataPasien", pasienModel.getNama());
                        pasienModelArrayList.add(pasienModel);
                    }
                    listPasienAdapter = new ListPasienAdapter(ListPasienActivity.this, pasienModelArrayList);
                    rv_listpasien.setAdapter(listPasienAdapter);
                    listPasienAdapter.notifyDataSetChanged();
                }
                pb_listpasien.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getOnePasien() {
//        String idUser = mUser.getUid();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(ListPasienActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                } else {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        PasienModel pasienModel = dataSnapshot.getValue(PasienModel.class);
                        assert pasienModel != null;
                        Log.d("DataPasien", pasienModel.getNama());
                        if (idPasien.equals(pasienModel.getIdPasien())) {
                            pasienModelArrayList.add(pasienModel);
                        }
                    }
                    listPasienAdapter = new ListPasienAdapter(ListPasienActivity.this, pasienModelArrayList);
                    rv_listpasien.setAdapter(listPasienAdapter);
                    listPasienAdapter.notifyDataSetChanged();
                }
                pb_listpasien.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTypeUser() {
        reference2.child(idPasien).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModel = snapshot.getValue(UserModel.class);
                assert userModel != null;
                typeUser = "user";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}