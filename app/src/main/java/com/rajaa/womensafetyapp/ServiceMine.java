package com.rajaa.womensafetyapp;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.seismic.ShakeDetector;

import java.util.Random;

public class ServiceMine extends Service implements ShakeDetector.Listener {

    public static boolean isRunning = false;
    public static boolean isRideMode = false;
    FusedLocationProviderClient fusedLocationClient;
    Random random = new Random();
    String ENUM, ENUM2, ENUM3, ENUM4;
    public static ShakeDetector sd;
    long cur_time, last_time;
    SensorManager sensorManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public SmsManager manager = SmsManager.getDefault();
    public String myLocation;

    @Override
    public void onCreate() {
        super.onCreate();
//        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE};
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // ActivityCompat.requestPermissions( getApplicationContext(), permissions, 101);
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sd = new ShakeDetector(this);

        Log.d("ServiceMine", "sd present");

        SharedPreferences sharedPreferences1 = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        ENUM = sharedPreferences1.getString("ENUM", "NONE");
        SharedPreferences sharedPreferences2 = getSharedPreferences("MySharedPref2", MODE_PRIVATE);
        ENUM2 = sharedPreferences2.getString("ENUM2", "NONE");
        SharedPreferences sharedPreferences3 = getSharedPreferences("MySharedPref3", MODE_PRIVATE);
        ENUM3 = sharedPreferences3.getString("ENUM3", "NONE");
        SharedPreferences sharedPreferences4 = getSharedPreferences("MySharedPref4", MODE_PRIVATE);
        ENUM4 = sharedPreferences4.getString("ENUM4", "NONE");
        Log.d("ENUM", ENUM);
        Log.d("ENUM2", ENUM2);
        Log.d("ENUM3", ENUM3);
        Log.d("ENUM4", ENUM4);


    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Logic to handle location object
                            Log.d("location", "Got");
                            location.getAltitude();
                            location.getLongitude();
                            myLocation = "http://maps.google.com/maps?q=loc:" + location.getLatitude() + "," + location.getLongitude();
                        } else {
                            myLocation = "Unable to Find Location :(";
                        }
                    }
                });
    }


    public void sendAlert() {
        if (!ENUM.equalsIgnoreCase("NONE") && !ENUM2.equalsIgnoreCase("NONE") && !ENUM3.equalsIgnoreCase("NONE") && !ENUM4.equalsIgnoreCase("NONE")) {
            if (isRunning) {
                manager.sendTextMessage(ENUM, null, "I'm in Trouble!\nSending My Location :\n" + myLocation, null, null);
                manager.sendTextMessage(ENUM2, null, "I'm in Trouble!\nSending My Location :\n" + myLocation, null, null);
                manager.sendTextMessage(ENUM3, null, "I'm in Trouble!\nSending My Location :\n" + myLocation, null, null);
                manager.sendTextMessage(ENUM4, null, "I'm in Trouble!\nSending My Location :\n" + myLocation, null, null);
                Toast.makeText(this, "Message sent to your saved contacts", Toast.LENGTH_SHORT).show();
            }
        } else {
            startActivity(new Intent(ServiceMine.this, UpdateActivity.class));
            this.stopForeground(true);
        }
    }

    public void makePhoneCall(String phone_num) {
        String dial_action = "tel:" + phone_num;
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(dial_action));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equalsIgnoreCase("STOP")) {
            if (isRunning) {
                this.stopForeground(true);
                this.stopSelf();
                isRunning = false;
                int position = random.nextInt(4);
                if (position == 0 && !ENUM.equalsIgnoreCase("NONE")) {
                    makePhoneCall(ENUM);
                    return super.onStartCommand(intent, flags, startId);
                } else if (position == 1 && !ENUM2.equalsIgnoreCase("NONE")) {
                    makePhoneCall(ENUM2);
                    return super.onStartCommand(intent, flags, startId);
                } else if (position == 2 && !ENUM3.equalsIgnoreCase("NONE")) {
                    makePhoneCall(ENUM3);
                    return super.onStartCommand(intent, flags, startId);

                } else if (position == 3 && !ENUM4.equalsIgnoreCase("NONE")) {
                    makePhoneCall(ENUM4);
                    return super.onStartCommand(intent, flags, startId);
                }
            }


        } else if (intent.getAction().equalsIgnoreCase("START")) {
            sd.start(sensorManager);
            isRideMode = false;
            isRunning = true;
            this.stopForeground(true);
            Intent notificationIntent = new Intent(this, NotificationActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("MYID", "CHANNELFOREGROUND", NotificationManager.IMPORTANCE_DEFAULT);

                NotificationManager m = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                m.createNotificationChannel(channel);

                Notification notification = new Notification.Builder(this, "MYID")
                        .setContentTitle("Women Safety")
                        .setContentText("Shake Device to Send SOS")
                        .setSmallIcon(R.drawable.girl_vector)
                        .setContentIntent(pendingIntent)
                        .build();
                this.startForeground(115, notification);
                return START_NOT_STICKY;
            }
        } else if (intent.getAction().equalsIgnoreCase("RIDE_MODE_ON")) {
            isRideMode = true;
            isRunning = false;
            this.stopForeground(true);
            Intent notificationIntent = new Intent(this, NotificationActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("RIDE", "FORRIDE", NotificationManager.IMPORTANCE_DEFAULT);

                NotificationManager m = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                m.createNotificationChannel(channel);

                Notification notification = new Notification.Builder(this, "RIDE")
                        .setContentTitle("Accident Tracker")
                        .setContentText("Drive Safer")
                        .setSmallIcon(R.drawable.ride_icon_3)
                        .setContentIntent(pendingIntent)
                        .build();
                this.startForeground(116, notification);
                return START_NOT_STICKY;
            }

        } else {
            this.stopForeground(true);
            this.stopSelf();
            isRideMode = false;
        }

        return super.onStartCommand(intent, flags, startId);

    }


    @Override
    public void onDestroy() {
        //ShakeDetector.destroy();
        super.onDestroy();
    }

    @Override
    public void hearShake() {
        Log.d("Shake", "Detected");
        //Toast.makeText(this, "Don't shake me, bro!", Toast.LENGTH_SHORT).show();
        cur_time = System.currentTimeMillis();
        getLocation();
        if (cur_time - last_time >= 1000 || last_time == 0) {
            last_time = cur_time;
            if (isRunning && myLocation != null)
                sendAlert();
        }
    }
}
