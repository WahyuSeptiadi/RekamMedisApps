package com.example.rekammedisapps.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cv_daftarpasien, cv_rekammedis, cv_profile, cv_listperawat;
    private ImageView iv_logout;

    private FirebaseUser mUser;
    private DatabaseReference reference;
    private String typeUser;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        getTypeUser();
        cv_daftarpasien = findViewById(R.id.cv_daftarpasien_home);
        cv_rekammedis = findViewById(R.id.cv_rekammedis_home);
        cv_profile = findViewById(R.id.cv_profleperawat_home);
        cv_listperawat = findViewById(R.id.cv_listperawat_home);
        iv_logout = findViewById(R.id.iv_logout_home);


        cv_daftarpasien.setOnClickListener(this);
        cv_rekammedis.setOnClickListener(this);
        cv_profile.setOnClickListener(this);
        cv_listperawat.setOnClickListener(this);
        iv_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_daftarpasien_home:
                startActivity(new Intent(this, DaftarPasienActivity.class));
                break;
            case R.id.cv_rekammedis_home:
                Intent toList = new Intent(this, ListPasienActivity.class);
                toList.putExtra("typeUser", typeUser);
                startActivity(toList);
                break;
            case R.id.cv_profleperawat_home:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.cv_listperawat_home:
                startActivity(new Intent(this, ListPerawatActivity.class));
                break;
            case R.id.iv_logout_home:
                showAlertDialogLogout();
                break;
            default:
                break;
        }
    }

    private void showAlertDialogLogout() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("KELUAR");
        alertDialogBuilder
                .setMessage("Apakah Anda yakin ingin keluar dari Aplikasi ?")
                .setCancelable(false)
                .setPositiveButton("Ya, tentu", (dialog, id) -> {

                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("Gak jadi", (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void getTypeUser(){
        String idPasien = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(idPasien).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModel = snapshot.getValue(UserModel.class);
                typeUser = userModel.getUserType();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}