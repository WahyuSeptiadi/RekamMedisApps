package com.example.rekammedisapps.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rekammedisapps.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DaftarPasienActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_namapasien, et_alamatpasien, et_umurpasien, et_keluhan, et_riwayatpenyakit;
    private CardView cv_btnregisterdp;
    private Uri mImageUri;
    private ImageView iv_uploadimage, iv_deleteimage, iv_btnBack;
    private CircleImageView civ_imageprofilepasien;
//    private ProgressBar progressBar;

    private boolean isDeleteProfile = false;

    private StorageTask<UploadTask.TaskSnapshot> uploadTask;

    //Database
    private DatabaseReference reference;
    private StorageReference storageReference;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pasien);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
//        progressBar = findViewById(R.id.pb_dp_progressBar);
        et_namapasien =  findViewById(R.id.et_dp_namapasien);
        et_alamatpasien = findViewById(R.id.et_dp_alamat);
        et_umurpasien = findViewById(R.id.et_dp_umur);
        et_keluhan = findViewById(R.id.et_dp_keluhan);
        et_riwayatpenyakit = findViewById(R.id.et_dp_riwayatpenyakit);
        iv_uploadimage = findViewById(R.id.iv_dp_imageuploadprofilepasien);
        iv_uploadimage.setOnClickListener(this);
        iv_deleteimage = findViewById(R.id.iv_dp_imageDeleteProfilepasien);
        iv_deleteimage.setOnClickListener(this);
        civ_imageprofilepasien = findViewById(R.id.civ_dp_imagepasien);
        iv_btnBack = findViewById(R.id.btnback_registry);
        iv_btnBack.setOnClickListener(this);

        cv_btnregisterdp = findViewById(R.id.cv_dp_btnregister);
        cv_btnregisterdp.setOnClickListener(this);

        reference = FirebaseDatabase.getInstance().getReference("Data Umum Pasien");
        storageReference = FirebaseStorage.getInstance().getReference("Profile");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cv_dp_btnregister:
                String namapasien = et_namapasien.getText().toString();
                String alamatpasien = et_alamatpasien.getText().toString();
                String umurpasien = et_umurpasien.getText().toString();
                String keluhanPasien = et_keluhan.getText().toString();
                String riwayatPasien = et_riwayatpenyakit.getText().toString();
                saveDataPatient(namapasien, alamatpasien, umurpasien, keluhanPasien, riwayatPasien);
                break;
            case R.id.iv_dp_imageuploadprofilepasien:
                iv_uploadimage.setVisibility(View.GONE);
                iv_deleteimage.setVisibility(View.VISIBLE);
                isDeleteProfile = false;
                CropImage.activity().setAspectRatio(1, 1).start(this);
                break;
            case R.id.iv_dp_imageDeleteProfilepasien:
                iv_uploadimage.setVisibility(View.VISIBLE);
                iv_deleteimage.setVisibility(View.GONE);
                civ_imageprofilepasien.setImageResource(R.drawable.icon_cam_upload);
                mImageUri = null;
                break;
            case R.id.btnback_registry:
                startActivity(new Intent(DaftarPasienActivity.this, HomeActivity.class));
                break;
            default:
                break;
        }
    }
    private void saveDataPatient(String nama, String alamat, String umur, String keluhan, String riwayat){
        String idUser = mUser.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String keyPasien = database.getReference("Data Umum Pasien").push().getKey();
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Masih Proses...");
        pd.show();
        pd.setCancelable(false);

        HashMap<String, Object> saveDataPatient = new HashMap<>();
        saveDataPatient.put("idPasien", keyPasien);
        saveDataPatient.put("nama", nama);
        saveDataPatient.put("alamat", alamat);
        saveDataPatient.put("umur", umur);
        saveDataPatient.put("keluhan", keluhan);
        saveDataPatient.put("riwayat", riwayat);
        saveDataPatient.put("idUser", idUser);
        saveDataPatient.put("imageURL", "default");

        reference.child(keyPasien).setValue(saveDataPatient).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!isDeleteProfile){
                    if (mImageUri != null){
                        StorageReference fileReference = storageReference.child("img-pasien-" + nama.toLowerCase() + "-"
                                + System.currentTimeMillis() + ".jpg");

                        uploadTask = fileReference.putFile(mImageUri);
                        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task1) throws Exception {
                                if (!task1.isSuccessful()) {
                                    throw Objects.requireNonNull(task1.getException());
                                }
                                return fileReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()){
                                    Uri downloadUri = task.getResult();
                                    assert downloadUri != null;
                                    String mUri = downloadUri.toString();

                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("imageURL", mUri);
                                    reference.child(keyPasien).updateChildren(map);

                                    pd.dismiss();
                                    startActivity(new Intent(DaftarPasienActivity.this, HomeActivity.class));
                                    finish();

                                    Toast.makeText(DaftarPasienActivity.this, "Daftar Pasien Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DaftarPasienActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        pd.dismiss();
                        startActivity(new Intent(DaftarPasienActivity.this, HomeActivity.class));
                        finish();

                        Toast.makeText(DaftarPasienActivity.this, "Upload tanpa foto profile", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            assert result != null;
            mImageUri = result.getUri();
            civ_imageprofilepasien.setImageURI(mImageUri);
        } else {
            Toast.makeText(this, "Tambah foto dibatalkan", Toast.LENGTH_SHORT).show();
        }
    }

}