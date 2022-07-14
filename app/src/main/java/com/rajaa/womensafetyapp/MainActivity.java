package com.rajaa.womensafetyapp;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    public static boolean isStarted = false, isHurt = true;
    float cur_acc = SensorManager.GRAVITY_EARTH, last_acc = SensorManager.GRAVITY_EARTH;
    final float THRESHOLD = 6 * SensorManager.GRAVITY_EARTH;
    public static SmsManager manager = SmsManager.getDefault();
    public static String myLocation;
    FusedLocationProviderClient fusedLocationClient;

    private final ActivityResultLauncher<String[]> multiplePermissions = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {

                    for (Map.Entry<String, Boolean> entry : result.entrySet())
                        if (!entry.getValue()) {
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Permission Must Be Granted!", Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction("Grant Permission", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    multiplePermissions.launch(new String[]{entry.getKey()});
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                        }


                }

            });
    Switch aSwitch;
    SensorManager sensorManager;
    Sensor accelerometer;
    Button start;
    public static String ENUM, ENUM2, ENUM3, ENUM4;

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
        if (isStarted) {
            sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        SharedPreferences sharedPreferences1 = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        ENUM = sharedPreferences1.getString("ENUM", "NONE");

        SharedPreferences sharedPreferences2 = getSharedPreferences("MySharedPref2", MODE_PRIVATE);
        ENUM2 = sharedPreferences2.getString("ENUM2", "NONE");

        SharedPreferences sharedPreferences3 = getSharedPreferences("MySharedPref3", MODE_PRIVATE);
        ENUM3 = sharedPreferences3.getString("ENUM3", "NONE");

        SharedPreferences sharedPreferences4 = getSharedPreferences("MySharedPref4", MODE_PRIVATE);
        ENUM4 = sharedPreferences4.getString("ENUM4", "NONE");

        if (ENUM.equalsIgnoreCase("NONE")) {
            startActivity(new Intent(this, RegisterNumberActivity.class));
        } else if (ENUM2.equalsIgnoreCase("NONE")) {
            startActivity(new Intent(this, RegisterNumberActivity.class));
        } else if (ENUM3.equalsIgnoreCase("NONE")) {
            startActivity(new Intent(this, RegisterNumberActivity.class));
        } else if (ENUM4.equalsIgnoreCase("NONE")) {
            startActivity(new Intent(this, RegisterNumberActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aSwitch = (Switch) findViewById(R.id.switch1);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER, true);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        Log.d("Accelerometer list ", "" + sensors.size());
        accelerometer = sensors.get(0);
        if (accelerometer != null) {
            Log.d("Accelerometer", "Not Null");
        } else {
            Toast.makeText(getApplicationContext(),
                    "There is no accelerometer on your device.You can't use the app.",
                    Toast.LENGTH_LONG).show();
        }


        multiplePermissions.launch(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE});

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Log.d("Switch", "Intent Passed");
                    sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                    isStarted = true;
                    Intent notificationIntent = new Intent(MainActivity.this, ServiceMine.class);
                    notificationIntent.setAction("RIDE_MODE_ON");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        getApplicationContext().startForegroundService(notificationIntent);
                    }
                    moveToAnimationActivity();
                } else {
                    cur_acc = SensorManager.GRAVITY_EARTH;
                    last_acc = SensorManager.GRAVITY_EARTH;
                    isStarted = false;
                    Log.d("Switch", "Closed");
                    sensorManager.unregisterListener(MainActivity.this);
                    deleteNotificationChannels(2);
                }
            }
        });

        //   start.setEnabled(true);

    }

    private void moveToAnimationActivity() {
        Intent intent = new Intent(this, transparentActivity.class);
        startActivity(intent);
    }


    // Created a function because it is reused
    private void stopRideModeNotification() {
        Intent notificationIntent = new Intent(MainActivity.this,ServiceMine.class);
        notificationIntent.setAction("RIDE_MODE_OFF");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(notificationIntent);
        }
    }

    public void stopService(View view) {

        Intent notificationIntent = new Intent(this, ServiceMine.class);
        notificationIntent.setAction("STOP");
        ServiceMine.sd.stop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(notificationIntent);
            if (ServiceMine.isRunning) {
                Snackbar.make(findViewById(android.R.id.content), "Service Stopped!", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Service Not Yet Started!", Snackbar.LENGTH_LONG).show();
            }
        }
    }


    public void startServiceV(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent notificationIntent = new Intent(this, ServiceMine.class);
            notificationIntent.setAction("START");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (aSwitch.isEnabled()) {
                    aSwitch.setChecked(false);
                }
                getApplicationContext().startForegroundService(notificationIntent);
                Snackbar.make(findViewById(android.R.id.content), "Service Started!", Snackbar.LENGTH_LONG).show();
            }
        } else {
            multiplePermissions.launch(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE});
        }

    }

    public void PopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.changeNum) {
                    startActivity(new Intent(MainActivity.this, UpdateActivity.class));
                }
                return true;
            }
        });
        popupMenu.show();
    }


    public void countDownTimer() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("SOS Alert")
                .setMessage("Cancel if you are you safe?")
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        //dialog.dismiss();
                    }

                })
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            private static final int AUTO_DISMISS_MILLIS = 10000;

            @Override
            public void onShow(final DialogInterface dialog) {
                final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                final CharSequence negativeButtonText = defaultButton.getText();
                new CountDownTimer(AUTO_DISMISS_MILLIS, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        defaultButton.setText(String.format(
                                Locale.getDefault(), "%s (%d)",
                                negativeButtonText,
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                        ));
                    }

                    @Override
                    public void onFinish() {
                        if (((AlertDialog) dialog).isShowing()) {
                            dialog.dismiss();
                            Intent intent = new Intent(MainActivity.this, AlertScreen.class);
                            startActivity(intent);
                        }
                    }
                }.start();
            }
        });
        dialog.show();
    }

    public void getLocation() {
        Log.d("Location", "found");
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


    public void deleteNotificationChannels(int i) {
        if(i==1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.deleteNotificationChannel("RIDE");
                nm.deleteNotificationChannel("MYID");
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.deleteNotificationChannel("RIDE");
            }
        }
    }

    @Override
    protected void onDestroy() {
        deleteNotificationChannels(1);
        ServiceMine.sd.stop();
        //ServiceMine.class.
        super.onDestroy();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //  Log.d("IN : ", "On sensor changed");
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        double relativeAcc = Math.sqrt(x * x + y * y + z * z);
        cur_acc = (float) relativeAcc;

        float del = Math.abs(cur_acc - last_acc);
        Log.d("g-force", "" + del);

        if (Math.abs(cur_acc - last_acc) > THRESHOLD && ServiceMine.isRideMode) {

            Log.d("Acc Change: ", "" + del);
            Log.d("cur acc: ", "" + cur_acc);
            Log.d("last acc: ", "" + last_acc);
            if (aSwitch.isEnabled()) {
                aSwitch.setChecked(false);
            }
            getLocation();
            stopRideModeNotification();
          /*  sc.manager.sendTextMessage(ENUM, null, "Im in Trouble!\nSending My Location :\n" + ServiceMine.myLocation, null, null);
            ServiceMine.manager.sendTextMessage(ENUM2, null, "Im in Trouble!\nSending My Location :\n" + ServiceMine.myLocation, null, null);
            ServiceMine.manager.sendTextMessage(ENUM3, null, "Im in Trouble!\nSending My Location :\n" + ServiceMine.myLocation, null, null);
            ServiceMine.manager.sendTextMessage(ENUM4, null, "Im in Trouble!\nSending My Location :\n" + ServiceMine.myLocation, null, null);
            Toast.makeText(this, "Message sent to your saved contacts", Toast.LENGTH_SHORT).show();*/
            countDownTimer();
//            if (isHurt) {
//                isHurt = false;
//
//            }
        }
        last_acc = cur_acc;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        getLocation();
        sensorManager.unregisterListener(this);
    }

}