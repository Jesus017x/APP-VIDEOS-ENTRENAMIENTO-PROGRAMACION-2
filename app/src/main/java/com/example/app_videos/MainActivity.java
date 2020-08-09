package com.example.app_videos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    private VideoView mainVideoView, mainVideoView2;
    private ImageView playBtn, playBtn2;
    private TextView currentTimer, currentTimer2;
    private TextView durationTimer, durationTimer2;
    private ProgressBar progressBar, progressBar2;

    private boolean isPlaying;

    private Uri videoUri;
    private Uri videoUri2;

    private int current = 0;
    private int current2 = 0;
    private int duration = 0;
    private int duration2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isPlaying = false;

        mainVideoView = (VideoView) findViewById(R.id.mainVideoView);
        mainVideoView2 = (VideoView) findViewById(R.id.mainVideoView2);
        playBtn = (ImageView) findViewById(R.id.playBtn);
        playBtn2 = (ImageView) findViewById(R.id.playBtn2);
        currentTimer = (TextView) findViewById(R.id.currentTimer);
        currentTimer2 = (TextView) findViewById(R.id.currentTimer2);
        durationTimer = (TextView) findViewById(R.id.durationTimer);
        durationTimer2 = (TextView) findViewById(R.id.durationTimer2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setMax(100);
        progressBar2.setMax(100);

        videoUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/appvideos-b3b88.appspot.com/o/Why%20Programming%20Is%20Important.mp4?alt=media&token=c588f403-f562-4c52-a064-b2e221d8ebff");
        videoUri2 = Uri.parse("https://firebasestorage.googleapis.com/v0/b/appvideos-b3b88.appspot.com/o/Introduction%20to%20Cybersecurity.mp4?alt=media&token=f7100b05-34a3-40ff-8f47-fc649d4fcf0d");

        mainVideoView.setVideoURI(videoUri);
        mainVideoView2.setVideoURI(videoUri2);
        mainVideoView.requestFocus();
        mainVideoView2.requestFocus();


        mainVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer mp){
                duration = mp.getDuration()/1000;
                String durationString = String.format("%02d:%02d", duration/60, duration %60);
                durationTimer.setText(durationString);
            }
        });
        mainVideoView2.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer mp){
                duration = mp.getDuration()/1000;
                String durationString = String.format("%02d:%02d", duration/60, duration %60);
                durationTimer2.setText(durationString);
            }
        });


        mainVideoView.start();
        mainVideoView2.start();
        isPlaying = true;
        playBtn.setImageResource(R.drawable.pausebtn);
        playBtn2.setImageResource(R.drawable.pausebtn);

        new VideoProgress().execute();

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying){
                    mainVideoView.pause();
                    isPlaying = false;
                    playBtn.setImageResource(R.drawable.playbtn);
                } else {
                    mainVideoView.start();
                    isPlaying = true;
                    playBtn.setImageResource(R.drawable.pausebtn);
                }
            }
        });
        playBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying){
                    mainVideoView2.pause();
                    isPlaying = false;
                    playBtn2.setImageResource(R.drawable.playbtn);
                } else {
                    mainVideoView2.start();
                    isPlaying = true;
                    playBtn2.setImageResource(R.drawable.pausebtn);
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        isPlaying = false;
    }

    public class VideoProgress extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            do {

                if(isPlaying){
                    current = mainVideoView.getCurrentPosition() / 1000;
                    current2 = mainVideoView2.getCurrentPosition() / 1000;
                    publishProgress(current);
                    publishProgress(current2);
                }

            } while (progressBar.getProgress() <= 100 || progressBar2.getProgress() <= 100);

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            try{
                int currentPercent = values[0] * 100/duration;
                progressBar.setProgress(currentPercent);
                progressBar2.setProgress(currentPercent);

                String currentString = String.format("%02d:%02d", values[0] / 60, values[0] % 60);

                currentTimer.setText(currentString);
                currentTimer2.setText(currentString);

            } catch(Exception e) {

            }
        }
    }
}
