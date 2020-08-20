package com.example.rekammedisapps.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekammedisapps.Adapter.ListPasienAdapter;
import com.example.rekammedisapps.Model.PasienModel;
import com.example.rekammedisapps.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListPasienActivity extends AppCompatActivity {

    //Database
    private DatabaseReference reference;

    //    private ProgressBar pb_listpasien;
    private RecyclerView rv_listpasien;
    private ImageView iv_btnback;
    private ArrayList<PasienModel> pasienModelArrayList;

    //Class
    private ListPasienAdapter listPasienAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pasien);

//        pb_listpasien = findViewById(R.id.pb_lp_progressBar);
        rv_listpasien = findViewById(R.id.rv_lp_listpasien);
        rv_listpasien.setLayoutManager(new LinearLayoutManager(this));
        rv_listpasien.setHasFixedSize(true);
        rv_listpasien.smoothScrollToPosition(0);

        //Button
        iv_btnback = findViewById(R.id.iv_lp_btnback);

        //InisialisasiDatabase
        reference = FirebaseDatabase.getInstance().getReference("Data Umum Pasien");

        getAllPasien();
    }

    private void getAllPasien() {
//        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Pasien");
//        ArrayList<String> idPasien = new ArrayList<>();
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                idPasien.add(snapshot.getKey());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        pasienModelArrayList = new ArrayList<>();
//        Log.d("ListPasienActivity", "getAllPasien: " + idPasien.get(0));
//        for (int i = 0; i < idPasien.size(); i++) {

        reference.addValueEventListener(new ValueEventListener() {
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
//                    pb_listpasien.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        }

    }
}