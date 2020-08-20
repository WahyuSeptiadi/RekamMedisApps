package com.example.rekammedisapps.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekammedisapps.Activity.ListMonthCalendarActivity;
import com.example.rekammedisapps.Model.PasienModel;
import com.example.rekammedisapps.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListPasienAdapter extends RecyclerView.Adapter<ListPasienAdapter.ViewHolder> {

    private Activity mActivty;
    private ArrayList<PasienModel> pasienModelArrayList;

    public ListPasienAdapter (Activity activity, ArrayList<PasienModel> pasienModelArrayList){
        this.mActivty = activity;
        this.pasienModelArrayList = pasienModelArrayList;
    }

    @NonNull
    @Override
    public ListPasienAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivty).inflate(R.layout.item_listpasien, parent, false);
        return new ViewHolder(view);
    }


    public void onBindViewHolder(@NonNull ListPasienAdapter.ViewHolder holder, int position) {
        PasienModel pasienModel = pasienModelArrayList.get(position);

        if (pasienModel.getImageURL().substring(0, 4).equals("http")) {
            Picasso.get().load(pasienModel.getImageURL()).into(holder.civProfilePatient);
        } else {
            Picasso.get().load(R.drawable.icon_default_profile).into(holder.civProfilePatient);
        }

        holder.tvNamePatient.setText(pasienModel.getNama());

        holder.cv_listpasien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toListMonth = new Intent(mActivty, ListMonthCalendarActivity.class);
                toListMonth.putExtra("idPasien", pasienModel.getIdPasien());
                toListMonth.putExtra("namaPasien", pasienModel.getNama());
                toListMonth.putExtra("umurPasien", pasienModel.getUmur());
                toListMonth.putExtra("alamatPasien", pasienModel.getAlamat());
                mActivty.startActivity(toListMonth);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pasienModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_listpasien;
        private CircleImageView civProfilePatient;
        private TextView tvNamePatient;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cv_listpasien = itemView.findViewById(R.id.cv_listpasien);
            civProfilePatient = itemView.findViewById(R.id.civ_lp_fotopasien);
            tvNamePatient = itemView.findViewById(R.id.tv_lp_namapasien);
//            tvEstimationFinish = itemView.findViewById(R.id.tv_estimationfinish_patientlist);
//            tvQueueSort = itemView.findViewById(R.id.tv_queuesort_patientlist);
        }
    }
}
