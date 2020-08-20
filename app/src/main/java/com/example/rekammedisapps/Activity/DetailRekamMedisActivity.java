package com.example.rekammedisapps.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.rekammedisapps.R;
import com.google.firebase.database.DatabaseReference;

public class DetailRekamMedisActivity extends AppCompatActivity {

    private ImageView iv_btnBack;
    private RecyclerView recyclerView;

    //Database
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rekam_medis);

        recyclerView = findViewById(R.id.rv_list_detailrekammedis);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.smoothScrollToPosition(0);
        iv_btnBack = findViewById(R.id.btnback_detailrekammedis);
    }

    private void getAllDetailRekam(){
//        reference
    }
}