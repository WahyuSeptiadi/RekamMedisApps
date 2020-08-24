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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekammedisapps.Activity.ListRekamMedisActivity;
import com.example.rekammedisapps.Model.PasienModel;
import com.example.rekammedisapps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListPasienAdapter extends RecyclerView.Adapter<ListPasienAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<PasienModel> pasienModelArrayList;

    public ListPasienAdapter(Activity activity, ArrayList<PasienModel> pasienModelArrayList) {
        this.mActivity = activity;
        this.pasienModelArrayList = pasienModelArrayList;
    }

    @NonNull
    @Override
    public ListPasienAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_listpasien, parent, false);
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

        holder.cv_listpasien.setOnClickListener(view -> {
            Intent toListMonth = new Intent(mActivity, ListRekamMedisActivity.class);
            toListMonth.putExtra("idPasien", pasienModel.getIdPasien());
            toListMonth.putExtra("namaPasien", pasienModel.getNama());
            toListMonth.putExtra("umurPasien", pasienModel.getUmur());
            toListMonth.putExtra("alamatPasien", pasienModel.getAlamat());
            toListMonth.putExtra("keluhanPasien", pasienModel.getKeluhan());
            toListMonth.putExtra("riwayatPasien", pasienModel.getRiwayat());
            mActivity.startActivity(toListMonth);
        });

        holder.cv_listpasien.setOnLongClickListener(view -> {
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
                        showDialogAlertDelete(pasienModel.getIdPasien());
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

    private void showDialogAlertDelete(String pasienId) {
        DatabaseReference rootRekamMedis = FirebaseDatabase.getInstance().getReference("Data RekamMedis Pasien");
        DatabaseReference rootPasien = FirebaseDatabase.getInstance().getReference("Data Umum Pasien");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setTitle("HAPUS DATA");
        alertDialogBuilder
                .setMessage("Apakah Anda yakin ingin menghapus data pasien ini ?")
                .setCancelable(false)
                .setPositiveButton("Ya, tentu", (dialog, id) -> {

                    rootRekamMedis.child(pasienId).removeValue();
                    rootPasien.child(pasienId).removeValue();

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
