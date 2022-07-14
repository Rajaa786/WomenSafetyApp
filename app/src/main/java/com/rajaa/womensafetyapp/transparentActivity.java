package com.rajaa.womensafetyapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class transparentActivity extends AppCompatActivity {
    VideoView videoView;
    LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);
       lottie = findViewById(R.id.lottie);
       MediaPlayer mediaPlayer=MediaPlayer.create(this,R.raw.drive_start);
       mediaPlayer.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.stop();
//                startActivity(new Intent(transparentActivity.this, MainActivity.class));
                finish();
            }

        }, 5000);
    }

    @Override
    public void onBackPressed() {

    }
}