package com.example.rekammedisapps.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.rekammedisapps.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUsername, etEmail, etPassword;
    private ImageView imgUpload, imgDelete;
    private CircleImageView civProfile;
    private TextView tv_login;

    private Uri mImageUri;

    private FirebaseAuth auth;
    private StorageReference storageReference;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.et_username_registry);
        etEmail = findViewById(R.id.et_email_registry);
        etPassword = findViewById(R.id.et_password_registry);
        CardView btnRegistry = findViewById(R.id.cv_btnregister);
        imgUpload = findViewById(R.id.iv_imageUploadProfile);
        imgDelete = findViewById(R.id.iv_imageDeleteProfile);
        civProfile = findViewById(R.id.civ_imageProfile);
        tv_login = findViewById(R.id.tv_loginfirst);
        tv_login.setOnClickListener(this);

        //inisialisasi
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //SET ONCLICK LISTENER
        imgUpload.setOnClickListener(this);
        imgDelete.setOnClickListener(this);
        btnRegistry.setOnClickListener(this);

        //hide keyboard first
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_loginfirst:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.cv_btnregister:
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    if (password.length() < 6) {
                        Toast.makeText(this, "Password harus lebih dari 6 digit", Toast.LENGTH_SHORT).show();
                    } else {
                        Registry(username, email, password);
                    }
                } else {
                    Toast.makeText(this, "Tolong lengkapi semua field ya", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.iv_imageUploadProfile:
                imgUpload.setVisibility(View.GONE);
                imgDelete.setVisibility(View.VISIBLE);
                CropImage.activity().setAspectRatio(1, 1).start(this);
                break;
            case R.id.iv_imageDeleteProfile:
                imgUpload.setVisibility(View.VISIBLE);
                imgDelete.setVisibility(View.GONE);
                civProfile.setImageResource(R.drawable.icon_cam_upload);
                mImageUri = null;
                break;
            default:
                break;
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void Registry(final String username, String email, String password) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Tunggu sebentar ya..");
        pd.show();
        pd.setCancelable(false);

        if (isEmailValid(email)) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                assert firebaseUser != null;
                                String userid = firebaseUser.getUid();

                                reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("email", email);
                                hashMap.put("id", userid);
                                hashMap.put("username", username);
                                hashMap.put("search", username.toLowerCase());
                                hashMap.put("imageURL", "default");
//                                hashMap.put("type", "user");

                                reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                                    if (mImageUri != null) {
                                        final StorageReference fileReference = storageReference.child("Profile")
                                                .child("img-" + username.toLowerCase().concat("-") + System.currentTimeMillis() + ".jpg");

                                        uploadTask = fileReference.putFile(mImageUri);
                                        uploadTask.continueWithTask(task2 -> {
                                            if (!task2.isSuccessful()) {
                                                throw Objects.requireNonNull(task2.getException());
                                            }
                                            return fileReference.getDownloadUrl();
                                        }).addOnCompleteListener(task3 -> {
                                            if (task3.isSuccessful()) {
                                                Uri downloadUri = task3.getResult();
                                                assert downloadUri != null;
                                                String mUri = downloadUri.toString();

                                                HashMap<String, Object> map = new HashMap<>();
                                                map.put("imageURL", mUri);
                                                reference.updateChildren(map);

                                                FirebaseAuth.getInstance().signOut();

                                                Intent toLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                                                RegisterActivity.this.startActivity(toLogin);
                                                RegisterActivity.this.finish();

                                                Toast.makeText(RegisterActivity.this, "Registrasi berhasil, silahkan login", Toast.LENGTH_SHORT).show();

                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Maaf, Foto tidak bisa di unggah !", Toast.LENGTH_SHORT).show();
                                            }
                                            pd.dismiss();
                                        }).addOnFailureListener(e -> {
                                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            pd.dismiss();
                                        });
                                    } else {
                                        FirebaseAuth.getInstance().signOut();

                                        Intent toLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                                        RegisterActivity.this.startActivity(toLogin);
                                        RegisterActivity.this.finish();
                                        Toast.makeText(RegisterActivity.this, "Berhasil registrasi tanpa foto profile", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }
                                });
                            } else {
                                Toast.makeText(RegisterActivity.this, "Email tersebut telah digunakan user lain",
                                        Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Maaf, Format Email salah !", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            assert result != null;
            mImageUri = result.getUri();
            civProfile.setImageURI(mImageUri);
        } else {
            Toast.makeText(this, "Tambah foto dibatalkan", Toast.LENGTH_SHORT).show();
        }
    }
}