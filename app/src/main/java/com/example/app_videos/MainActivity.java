package com.example.app_videos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    VideoView mainVideoView;
    Button uploadBtn;
    ProgressBar mainProgressBar;
    EditText videoName;
    MediaController mediaController;
    private Uri videoUri;
    private ImageView playBtn;
    private ProgressBar progressBar;

    //private boolean isPlaying;

    private static final int PICK_VIDEO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Reproductor de video

        //isPlaying = false;

        mainVideoView = (VideoView) findViewById(R.id.mainVideoView);
        uploadBtn = (Button) findViewById(R.id.btn_upload);
        mainProgressBar = (ProgressBar) findViewById(R.id.progressBar_main);
        videoName = (EditText) findViewById(R.id.video_name);
        playBtn = (ImageView) findViewById(R.id.playBtn);
        mediaController = new MediaController(this);
        mainVideoView.setMediaController(mediaController);
        mainVideoView.start();

        //videoUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/appvideos-b3b88.appspot.com/o/Why%20Programming%20Is%20Important.mp4?alt=media&token=c588f403-f562-4c52-a064-b2e221d8ebff");

        /*mainVideoView.setVideoURI(videoUri);
        mainVideoView.requestFocus();*/


        /*mainVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer mp){
                duration = mp.getDuration()/1000;
                String durationString = String.format("%02d:%02d", duration/60, duration %60);
                durationTimer.setText(durationString);
            }
        });*/

        //mainVideoView.start();
        //isPlaying = true;
        //playBtn.setImageResource(R.drawable.pausebtn);

        /*new VideoProgress().execute();

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
        });*/

    }
    /*
    @Override
    protected void onStop() {
        super.onStop();

        isPlaying = false;
    }*/

    public void ChooseVideo(View view) {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO);
    }

    public void ShowVideos(View view) {
    }
    /*
    public class VideoProgress extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            do {

                if(isPlaying){
                    current = mainVideoView.getCurrentPosition() / 1000;
                    publishProgress(current);
                }

            } while (progressBar.getProgress() <= 100);

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            try{
                int currentPercent = values[0] * 100/duration;
                progressBar.setProgress(currentPercent);

                String currentString = String.format("%02d:%02d", values[0] / 60, values[0] % 60);

                currentTimer.setText(currentString);

            } catch(Exception e) {

            }
        }
    }*/
}
