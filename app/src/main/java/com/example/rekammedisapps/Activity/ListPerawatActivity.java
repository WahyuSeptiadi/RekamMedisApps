package com.example.rekammedisapps.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_perawat);
        iv_btnBack = findViewById(R.id.iv_lperawat_btnback);
        iv_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListPerawatActivity.this, HomeActivity.class));
            }
        });

        rv_listPerawat = findViewById(R.id.rv_lperawat_listperawat);
        rv_listPerawat.setLayoutManager(new LinearLayoutManager(this));
        rv_listPerawat.setHasFixedSize(true);
        rv_listPerawat.smoothScrollToPosition(0);
    }

    private void getAllData(){
        userModelArrayList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}