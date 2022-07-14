package com.rajaa.womensafetyapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AlertScreen extends AppCompatActivity {
    MediaPlayer player;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_screen);
        if (!MainActivity.ENUM.equalsIgnoreCase("NONE") && !MainActivity.ENUM2.equalsIgnoreCase("NONE") && !MainActivity.ENUM3.equalsIgnoreCase("NONE") && !MainActivity.ENUM4.equalsIgnoreCase("NONE")) {
            Log.d("M here", "MSG");
            MainActivity.manager.sendTextMessage(MainActivity.ENUM, null, "I'm in Trouble!\nSending My Location :\n" + MainActivity.myLocation, null, null);
            MainActivity.manager.sendTextMessage(MainActivity.ENUM2, null, "I'm in Trouble!\nSending My Location :\n" + MainActivity.myLocation, null, null);
            MainActivity.manager.sendTextMessage(MainActivity.ENUM3, null, "I'm in Trouble!\nSending My Location :\n" + MainActivity.myLocation, null, null);
            MainActivity.manager.sendTextMessage(MainActivity.ENUM4, null, "I'm in Trouble!\nSending My Location :\n" + MainActivity.myLocation, null, null);
            Toast.makeText(this, "Message sent to your saved contacts", Toast.LENGTH_SHORT).show();

        } else {
            startActivity(new Intent(AlertScreen.this, UpdateActivity.class));
        }
        player = MediaPlayer.create(AlertScreen.this, R.raw.alert2);
        player.setLooping(true);
        player.start();
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.stop();
                finish();
            }
        });
    }
}