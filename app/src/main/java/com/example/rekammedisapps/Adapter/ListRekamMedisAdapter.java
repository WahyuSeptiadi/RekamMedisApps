package com.example.rekammedisapps.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekammedisapps.Activity.DetailRekamMedisActivity;
import com.example.rekammedisapps.Model.RekamMedisModel;
import com.example.rekammedisapps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

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
            String umurPasien = rekamMedisModel.getUmurPasien();
            String namaPerawat = rekamMedisModel.getNamaPerawat();
            String keluhan = rekamMedisModel.getKeluhanPasien();
            String alamat = rekamMedisModel.getAlamatPasien();
            String imagePerawat = rekamMedisModel.getImageURLPerawat();
            String riwayat = rekamMedisModel.getRiwayatPasien();
            String diagnosa = rekamMedisModel.getDiagnosaPasien();
            String rencana = rekamMedisModel.getRencanaPenataPasien();
            String pengobatan = rekamMedisModel.getPengobatanPasien();

            //put to Detail Activity
            Intent sendData = new Intent(mActivity, DetailRekamMedisActivity.class);
            sendData.putExtra("time", time);
            sendData.putExtra("tanggal", tanggal);
            sendData.putExtra("bulan", bulan);
            sendData.putExtra("tahun", tahun);
            sendData.putExtra("namapasien", namaPasien);
            sendData.putExtra("umurpasien", umurPasien);
            sendData.putExtra("namaperawat", namaPerawat);
            sendData.putExtra("keluhan", keluhan);
            sendData.putExtra("alamat", alamat);
            sendData.putExtra("imageperawat", imagePerawat);
            sendData.putExtra("riwayat", riwayat);
            sendData.putExtra("diagnosa", diagnosa);
            sendData.putExtra("rencana", rencana);
            sendData.putExtra("pengobatan", pengobatan);
            sendData.putExtra("idpasien", rekamMedisModel.getIdPasien());
            sendData.putExtra("idperawat", rekamMedisModel.getIdPerawat());
            sendData.putExtra("idrekam", rekamMedisModel.getIdRekamMedis());

            mActivity.startActivity(sendData);
            mActivity.finish();
        });

        holder.itemView.setOnLongClickListener(view -> {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            assert firebaseUser != null;
            final Dialog dialog = new Dialog(mActivity);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_edit_delete);
            dialog.show();

            TextView choice = dialog.findViewById(R.id.tv_choice_action);
            Button editButton = dialog.findViewById(R.id.btnEdit);
            Button deleteButton = dialog.findViewById(R.id.btnDelete);

            //sementara btn Edit jadi Hapus
            editButton.setText(R.string.str_delete);
            choice.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);

            //apabila tombol edit diklik
            editButton.setOnClickListener(view1 -> {
                        dialog.dismiss();
                        //termasuk menghapus antrian pada user
                        showDialogAlertDelete(rekamMedisModel.getIdPasien(), rekamMedisModel.getIdRekamMedis());
                    }
            );

//                //apabila tombol delete diklik
//                deleteButton.setOnClickListener(view2 -> {
//                            dialog.dismiss();
//                            showDialogAlertDelete(patientModel.getIdDokter(), patientModel.getIdPasien());
//                        }
//                );

            return true;
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

    private void showDialogAlertDelete(String pasienId, String rekamId) {
        DatabaseReference rootRekamMedis = FirebaseDatabase.getInstance().getReference("Data RekamMedis Pasien").child(pasienId);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setTitle("HAPUS DATA");
        alertDialogBuilder
                .setMessage("Apakah Anda yakin ingin menghapus data rekam medis ini ?")
                .setCancelable(false)
                .setPositiveButton("Ya, tentu", (dialog, id) -> {

                    rootRekamMedis.child(rekamId).removeValue();

                    mActivity.overridePendingTransition(0, 0);
                    mActivity.startActivity(mActivity.getIntent());
                    mActivity.finish();
                    mActivity.overridePendingTransition(0, 0);

                    Toast.makeText(mActivity, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Gak jadi", (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
