package com.rajaa.womensafetyapp;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class NotificationActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If this activity is the root activity of the task, the app is not running
//        if (isTaskRoot())
//        {
//            // Start the app before finishing
//            Intent startAppIntent = new Intent(getApplicationContext(), MainActivity.class);
//            startAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(startAppIntent);
//        }

        finish();
    }
}
