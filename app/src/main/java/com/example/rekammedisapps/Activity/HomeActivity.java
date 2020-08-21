package com.example.rekammedisapps.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.rekammedisapps.R;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    CardView cv_daftarpasien, cv_rekammedis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cv_daftarpasien = findViewById(R.id.cv_daftarpasien_home);
        cv_rekammedis = findViewById(R.id.cv_rekammedis_home);

        cv_daftarpasien.setOnClickListener(this);
        cv_rekammedis.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cv_daftarpasien_home:
                startActivity(new Intent(this, DaftarPasienActivity.class));
                break;
            case R.id.cv_rekammedis_home:
                startActivity(new Intent(this, ListPasienActivity.class));
                break;
            default:
                break;
        }
    }
}