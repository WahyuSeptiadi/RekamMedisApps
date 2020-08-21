package com.example.rekammedisapps.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.rekammedisapps.Adapter.ListPerawatAdapter;
import com.example.rekammedisapps.Model.UserModel;
import com.example.rekammedisapps.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListPerawatActivity extends AppCompatActivity {

    private RecyclerView rv_listPerawat;

    private ImageView iv_btnBack;
    private ArrayList<UserModel> userModelArrayList;
    private ListPerawatAdapter listPerawatAdapter;

    //Database
    private DatabaseReference reference;

    private ProgressBar pb_listperawat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_perawat);
        iv_btnBack = findViewById(R.id.iv_lperawat_btnback);
        pb_listperawat = findViewById(R.id.progressBar_listPerawat);
        iv_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListPerawatActivity.this, HomeActivity.class));
                finish();
            }
        });

        rv_listPerawat = findViewById(R.id.rv_lperawat_listperawat);
        rv_listPerawat.setLayoutManager(new LinearLayoutManager(this));
        rv_listPerawat.setHasFixedSize(true);
        rv_listPerawat.smoothScrollToPosition(0);

        reference = FirebaseDatabase.getInstance().getReference("Users");
        getAllData();
    }

    private void getAllData(){
        userModelArrayList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModelArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    userModelArrayList.add(userModel);
                }
                listPerawatAdapter = new ListPerawatAdapter(ListPerawatActivity.this, userModelArrayList);
                rv_listPerawat.setAdapter(listPerawatAdapter);
                listPerawatAdapter.notifyDataSetChanged();
                pb_listperawat.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}