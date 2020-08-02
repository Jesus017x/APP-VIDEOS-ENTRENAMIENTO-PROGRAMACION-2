package com.example.app_videos;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {
    EditText editEmail, editPassword;
    Button btnLogin;
    TextView txtRegister;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        fAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    editEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editPassword.setError("Password is required");
                    return;
                }
                if (password.length() < 6) {
                    editPassword.setError("Password must have 6 or more characters");
                    return;
                }

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(loginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(loginActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}
