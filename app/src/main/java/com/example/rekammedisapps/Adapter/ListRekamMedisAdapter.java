package com.example.rekammedisapps.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekammedisapps.Model.RekamMedisModel;
import com.example.rekammedisapps.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListRekamMedisAdapter extends RecyclerView.Adapter<ListRekamMedisAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<RekamMedisModel> rekamMedisModelArrayList;

    public ListRekamMedisAdapter(Activity mActivity, ArrayList<RekamMedisModel> rekamMedisModelArrayList) {
        this.mActivity = mActivity;
        this.rekamMedisModelArrayList = rekamMedisModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_list_rekammedis, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RekamMedisModel rekamMedisModel = rekamMedisModelArrayList.get(position);

        holder.bulan.setText(rekamMedisModel.getBulanPelayanan());
        holder.tanggal.setText(rekamMedisModel.getTanggalPelayanan());
        holder.tahun.setText(rekamMedisModel.getTahunPelayanan());
        holder.time.setText(rekamMedisModel.getTimePelayanan());

        holder.itemView.setOnClickListener(view -> {
            String time = rekamMedisModel.getTimePelayanan();
            String tanggal = rekamMedisModel.getTanggalPelayanan();
            String bulan = rekamMedisModel.getBulanPelayanan();
            String tahun = rekamMedisModel.getTahunPelayanan();
            String namaPasien = rekamMedisModel.getNamaPasien();
            String namaPerawat = rekamMedisModel.getNamaPerawat();
            String alamat = rekamMedisModel.getAlamatPasien();
        });
    }

    @Override
    public int getItemCount() {
        return rekamMedisModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tanggal, bulan, tahun, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tanggal = itemView.findViewById(R.id.tv_lrkm_tanggal);
            bulan = itemView.findViewById(R.id.tv_lrkm_bulan);
            tahun = itemView.findViewById(R.id.tv_lrkm_tahun);
            time = itemView.findViewById(R.id.tv_lrkm_time);
        }
    }
}
