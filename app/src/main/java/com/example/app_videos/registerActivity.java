package com.example.app_videos;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_videos.notificationPackage.APIService;
import com.example.app_videos.notificationPackage.Client;
import com.example.app_videos.notificationPackage.Data;
import com.example.app_videos.notificationPackage.MyResponse;
import com.example.app_videos.notificationPackage.NotificationSender;
import com.example.app_videos.notificationPackage.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class registerActivity extends AppCompatActivity {
    EditText editEmail, editPassword, editUser, editPhone;
    Button btnRegister;
    TextView txtLogin;
    FirebaseAuth fAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String message = "Registering process completed";

    private APIService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editUser = findViewById(R.id.editUser);
        editPhone = findViewById(R.id.editPhone);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            Toast.makeText(registerActivity.this, "User already logged in", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(this, MainActivity.class));
        }

        //Registro
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    editEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    editPassword.setError("Password is required");
                    return;
                }

                if(password.length() < 6){
                    editPassword.setError("Password must have 6 or more characters");
                    return;
                }

                //Registrar el usuario en firebase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(registerActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(registerActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                        String usertoken = dataSnapshot.getValue(String.class);
                        notifyRegister(usertoken, editUser.getText().toString().trim(), message);

                    }
                    public void onCancelled(@NonNull DatabaseError databaseError){

                    }
                });
                //UpdateToken();

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    String CHANNEL_ID="MESSAGE";
                    String CHANNEL_NAME="MESSAGE";
                    NotificationManagerCompat manager=NotificationManagerCompat.from(registerActivity.this);
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                        NotificationChannel channel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                        manager.createNotificationChannel(channel);
                    }
                    Notification notification = new NotificationCompat.Builder(registerActivity.this, CHANNEL_ID)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(editUser.getText().toString())
                            .setContentText(message)
                            .build();
                    manager.notify(getRandomNumber(),notification);
                }
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), loginActivity.class));
            }
        });
    }

    /*private void UpdateToken() {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(token);
    }*/

    public void notifyRegister(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, retrofit2.Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(registerActivity.this, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

    private static int getRandomNumber() {
        Date dd= new Date();
        SimpleDateFormat ft =new SimpleDateFormat ("mmssSS");
        String s=ft.format(dd);
        return Integer.parseInt(s);
    }
}
