package com.rajaa.womensafetyapp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterNumberActivity extends AppCompatActivity {

    private int check = 0;
    private boolean lessDigit = false;
    TextInputEditText number1;
    TextInputEditText number2;
    TextInputEditText number3;
    TextInputEditText number4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_number);

        number1 = findViewById(R.id.numberEdit1);
        number2 = findViewById(R.id.numberEdit2);
        number3 = findViewById(R.id.numberEdit3);
        number4 = findViewById(R.id.numberEdit4);
    }

    public void saveNumber(View view) {

        String numberString1 = number1.getText().toString().trim();
        String numberString2 = number2.getText().toString().trim();
        String numberString3 = number3.getText().toString().trim();
        String numberString4 = number4.getText().toString().trim();

        if (numberString1.length() == 10) {
            check += 1;
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("ENUM", numberString1);
            myEdit.apply();
        } else if (numberString1.length() > 0) {
            lessDigit = true;
        }


        if (numberString2.length() == 10) {
            check += 1;
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref2", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("ENUM2", numberString2);
            myEdit.apply();
        } else if (numberString2.length() > 0) {
            lessDigit = true;
        }

        if (numberString3.length() == 10) {
            check += 1;
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref3", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("ENUM3", numberString3);
            myEdit.apply();
        } else if (numberString3.length() > 0) {
            lessDigit = true;
        }

        if (numberString4.length() == 10) {
            check += 1;
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref4", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("ENUM4", numberString4);
            myEdit.apply();
        } else if (numberString4.length() > 0) {
            lessDigit = true;
        }

        if (check == 4) {
            Log.d("Check",""+check);
            finish();
        } else {
            check=0;
            Log.d("Check",""+check);
            if (lessDigit) {
                Toast.makeText(this, "Phone Number should be 10 digits long!", Toast.LENGTH_SHORT).show();
            }
            lessDigit=false;

            Toast.makeText(this, "Enter atleast 4 contacts!", Toast.LENGTH_SHORT).show();
        }
    }

}