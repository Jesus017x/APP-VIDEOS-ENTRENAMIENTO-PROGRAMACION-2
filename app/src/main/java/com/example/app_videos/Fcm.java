package com.example.app_videos;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class Fcm extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        Log.e("token", "My token is:"+s);
        savetoken(s);

    }

    private void savetoken(String s) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("token");
        ref.child("user").setValue(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from = remoteMessage.getFrom();

        if(remoteMessage.getData().size() > 0 ) {
            String title = remoteMessage.getData().get("title");
            String detail = remoteMessage.getData().get("detail");

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                greaterThanOreo(title, detail);
            }if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                lessThanOreo(title, detail);
            }
        }
    }

    private void lessThanOreo(String title, String detail) {
        String id = "message";

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id);

        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(detail)
                .setContentIntent(clickNotif())
                .setContentInfo("new");

        Random rnd = new Random();
        int idNotify = rnd.nextInt(8000);

        assert nm != null;
        nm.notify(idNotify, builder.build());
    }

    private void greaterThanOreo(String title, String detail) {
        String id = "message";

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel nc = new NotificationChannel(id, "new", NotificationManager.IMPORTANCE_HIGH);
            nc.setShowBadge(true);
            assert nm != null;
            nm.createNotificationChannel(nc);
        }
        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(detail)
                .setContentIntent(clickNotif())
                .setContentInfo("new");

        Random rnd = new Random();
        int idNotify = rnd.nextInt(8000);

        assert nm != null;
        nm.notify(idNotify, builder.build());
    }

    public PendingIntent clickNotif(){
        Intent nf = new Intent(getApplicationContext(), MainActivity.class);
        nf.putExtra("colour", "purple");
        nf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this, 0, nf, 0);
    }


}
