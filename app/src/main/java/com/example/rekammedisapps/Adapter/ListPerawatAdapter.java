package com.example.rekammedisapps.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekammedisapps.Model.UserModel;
import com.example.rekammedisapps.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListPerawatAdapter extends RecyclerView.Adapter<ListPerawatAdapter.ViewHolder> {
    private Activity mActivity;
    private ArrayList<UserModel> userModelArrayList;

    public ListPerawatAdapter(Activity mActivity, ArrayList<UserModel> userModelArrayList){
        this.mActivity = mActivity;
        this.userModelArrayList = userModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_list_perawat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserModel userModel = userModelArrayList.get(position);

        holder.tv_namaPerawat.setText(userModel.getUsername());
        if (userModel.getImageURL().substring(0, 4).equals("http")) {
            Picasso.get().load(userModel.getImageURL()).into(holder.civ_fotoPerawat);
        } else {
            Picasso.get().load(R.drawable.icon_default_profile).into(holder.civ_fotoPerawat);
        }

    }

    @Override
    public int getItemCount() {
        return userModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView civ_fotoPerawat;
        private TextView tv_namaPerawat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            civ_fotoPerawat = itemView.findViewById(R.id.civ_lperawat_fotoperawat);
            tv_namaPerawat = itemView.findViewById(R.id.tv_lperawat_namaperawat);
        }
    }
}
