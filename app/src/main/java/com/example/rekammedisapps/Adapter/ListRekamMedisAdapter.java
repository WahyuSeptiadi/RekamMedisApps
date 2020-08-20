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

    public void ListRekamMedisAdapter(Activity mActivity, ArrayList<RekamMedisModel> rekamMedisModelArrayList){
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
        if (rekamMedisModel.getImageURLPerawat().substring(0, 4).equals("http")) {
            Picasso.get().load(rekamMedisModel.getImageURLPerawat()).into(holder.civImagePerawat);
        } else {
            Picasso.get().load(R.drawable.icon_default_profile).into(holder.civImagePerawat);
        }
        holder.tv_namaPerawat.setText(rekamMedisModel.getNamaPerawat());
        holder.tv_keluhan.setText(rekamMedisModel.getKeluhanPasien());
    }

    @Override
    public int getItemCount() {
        return rekamMedisModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView civImagePerawat;
        private TextView tv_namaPerawat, tv_keluhan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            civImagePerawat = itemView.findViewById(R.id.civ_lrkm_fotodokter);
            tv_namaPerawat = itemView.findViewById(R.id.tv_lrkm_namadokter);
            tv_keluhan = itemView.findViewById(R.id.tv_lrkm_keluhan);
        }
    }
}
