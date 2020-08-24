package com.example.rekammedisapps.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekammedisapps.Model.UserModel;
import com.example.rekammedisapps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView civ_imageperawat;
    private TextView namaperawat, emailperawat;
    private ImageView iv_btnBack;

    //Database
    private DatabaseReference reference;
    private FirebaseUser mUser;

    //Model
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        civ_imageperawat = findViewById(R.id.civ_profile_fotoperawat);
        namaperawat = findViewById(R.id.tv_profile_namaperawat);
        emailperawat = findViewById(R.id.tv_profile_emailperawat);
        iv_btnBack = findViewById(R.id.btnback_profile);
        iv_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            }
        });

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        getDataUser();
//        Toast.makeText(this, userModel.getUsername(), Toast.LENGTH_SHORT).show();
    }

    private void getDataUser(){
        String idUser = mUser.getUid();
        reference.child(idUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModel = snapshot.getValue(UserModel.class);
                namaperawat.setText(userModel.getUsername());
                emailperawat.setText(userModel.getEmail());
                if (userModel.getImageURL().substring(0, 4).equals("http")) {
                    Picasso.get().load(userModel.getImageURL()).into(civ_imageperawat);
                } else {
                    Picasso.get().load(R.drawable.icon_default_profile).into(civ_imageperawat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}