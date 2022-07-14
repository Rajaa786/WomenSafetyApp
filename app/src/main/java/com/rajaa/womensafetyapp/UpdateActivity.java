package com.rajaa.womensafetyapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class UpdateActivity extends AppCompatActivity {
    TextView text1, text2, text3, text4, textView1, textView2, textView3, textView4;
    TextInputEditText number1, number2, number3, number4;
    MaterialButton materialButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        text1=findViewById(R.id.text1);
        text2=findViewById((R.id.text2));
        text3=findViewById(R.id.text3);
        text4=findViewById(R.id.text4);
        textView1=findViewById(R.id.textView1);
        textView2=findViewById(R.id.textView2);
        textView3=findViewById(R.id.textView3);
        textView4=findViewById(R.id.textView4);
        number1=findViewById(R.id.numberEdit1);
        number2=findViewById(R.id.numberEdit2);
        number3=findViewById(R.id.numberEdit3);
        number4=findViewById(R.id.numberEdit4);
        materialButton=findViewById(R.id.materialButton);

        String ENUM1,ENUM2,ENUM3,ENUM4;

        SharedPreferences sp1= getSharedPreferences("MySharedPref",MODE_PRIVATE);
        ENUM1=sp1.getString("ENUM","NONE");
        if (!ENUM1.equalsIgnoreCase("NONE")) {
            textView1.setText(ENUM1);
        }

        SharedPreferences sp2= getSharedPreferences("MySharedPref2",MODE_PRIVATE);
        ENUM2=sp2.getString("ENUM2","NONE");
        if (!ENUM2.equalsIgnoreCase("NONE")) {
            textView2.setText(ENUM2);
        }

        SharedPreferences sp3= getSharedPreferences("MySharedPref3",MODE_PRIVATE);
        ENUM3=sp3.getString("ENUM3","NONE");
        if (!ENUM3.equalsIgnoreCase("NONE")) {
            textView3.setText(ENUM3);
        }

        SharedPreferences sp4= getSharedPreferences("MySharedPref4",MODE_PRIVATE);
        ENUM4=sp4.getString("ENUM4","NONE");
        if (!ENUM4.equalsIgnoreCase("NONE")) {
            textView4.setText(ENUM4);
        }

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numberString1 = number1.getText().toString().trim();
                String numberString2 = number2.getText().toString().trim();
                String numberString3 = number3.getText().toString().trim();
                String numberString4 = number4.getText().toString().trim();
                if(numberString1.length()==10){
                    SharedPreferences.Editor myEdit = sp1.edit();
                    myEdit.putString("ENUM", numberString1);
                    myEdit.apply();
                }

                if(numberString2.length()==10){
                    SharedPreferences.Editor myEdit = sp2.edit();
                    myEdit.putString("ENUM2", numberString2);
                    myEdit.apply();
                }

                if(numberString3.length()==10){
                    SharedPreferences.Editor myEdit = sp3.edit();
                    myEdit.putString("ENUM3", numberString3);
                    myEdit.apply();
                }

                if(numberString4.length()==10){
                    SharedPreferences.Editor myEdit = sp4.edit();
                    myEdit.putString("ENUM4", numberString4);
                    myEdit.apply();
                }
                UpdateActivity.this.finish();
            }
        });

    }
}