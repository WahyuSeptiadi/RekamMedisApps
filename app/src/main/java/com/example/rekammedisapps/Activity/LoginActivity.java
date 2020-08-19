package com.example.rekammedisapps.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.rekammedisapps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView tvRegistry = findViewById(R.id.tv_registryfirst);
        etEmail = findViewById(R.id.et_email_login);
        etPassword = findViewById(R.id.et_password_login);
        CardView btnLogin = findViewById(R.id.cv_btnlogin);

        //inisialisasi
        auth = FirebaseAuth.getInstance();

        tvRegistry.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        btnLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                if (password.length() < 6) {
                    Toast.makeText(LoginActivity.this, "Password harus lebih dari 6 digit", Toast.LENGTH_SHORT).show();
                } else {
                    Login(email, password);
                }
            } else {
                Toast.makeText(this, "Tolong lengkapi semua field ya", Toast.LENGTH_SHORT).show();
            }
        });

        //hide keyboard first
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //cek jika sudah pernah login
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Intent goHome = new Intent(this, HomeActivity.class);
            startActivity(goHome);
            finish();
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void Login(String email, String password) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Tunggu sebentar ya :)");
        pd.show();
        pd.setCancelable(false);

        if (isEmailValid(email)) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent toStart = new Intent(LoginActivity.this, HomeActivity.class);
                            toStart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(toStart);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Email belum terdaftar / password anda salah", Toast.LENGTH_SHORT).show();
                        }
                        pd.dismiss();
                    });
        } else {
            Toast.makeText(this, "Maaf, Format Email salah !", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }
    }
}