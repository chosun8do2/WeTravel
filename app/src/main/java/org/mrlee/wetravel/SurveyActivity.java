package org.mrlee.wetravel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SurveyActivity extends AppCompatActivity {

    private Button submitButton;
    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private int age = 0; //0: 20-24 1: 25-29 2: 30-34 3: 35-39 4:40대이상
    private int smoking=0; //0: 흡연 1: 비흡연
    private int noize = 0;
    private int type = 0;
    private int picture = 0;
    private int drinking=0; //0: 네 1: 아니요 2: 조금..
    private int drink_type = 0;
    private int hotel = 0;
    private int car = 0;

    private RadioGroup rg1, rg2, rg3, rg4, rg5, rg6, rg7, rg8, rg9;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        rg1 = (RadioGroup) findViewById(R.id.radioGroup1);
        rg2 = (RadioGroup) findViewById(R.id.radioGroup2);
        rg3 = (RadioGroup) findViewById(R.id.radioGroup3);
        rg4 = (RadioGroup) findViewById(R.id.radioGroup4);
        rg5 = (RadioGroup) findViewById(R.id.radioGroup5);
        rg6 = (RadioGroup) findViewById(R.id.radioGroup6);
        rg7 = (RadioGroup) findViewById(R.id.radioGroup7);
        rg8 = (RadioGroup) findViewById(R.id.radioGroup8);
        rg9 = (RadioGroup) findViewById(R.id.radioGroup9);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String uid = mAuth.getCurrentUser().getUid();
                survey Survey = new survey();
                Survey.age = age;
                Survey.drinking = drinking;
                Survey.smoking = smoking;
                Survey.noize = noize;
                Survey.type = type;
                Survey.picture = picture;
                Survey.drink_type = drink_type;
                Survey.hotel = hotel;
                Survey.car = car;
                FirebaseDatabase.getInstance().getReference().child("survey").child(uid).setValue(Survey).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "설문조사 등록에 성공했습니다!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SurveyActivity.this, SelectActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });

        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.rg1_bt1){
                    age = 0;
                }
                else if(i == R.id.rg1_bt2){
                    age = 1;
                }
                else if(i == R.id.rg1_bt3){
                    age = 2;
                }
                else if(i == R.id.rg1_bt4){
                    age = 3;
                }
                else if(i == R.id.rg1_bt5){
                    age = 4;
                }
            }
        });
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.rg2_bt1){
                    smoking = 0;
                }
                else if(i == R.id.rg2_bt2){
                    smoking = 1;
                }
                else if(i == R.id.rg2_bt3){
                    smoking = 2;
                }
            }
        });
        rg3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.rg3_bt1){
                    noize = 0;
                }
                else if(i == R.id.rg3_bt2){
                    noize = 1;
                }
                else if(i == R.id.rg3_bt3){
                    noize = 2;
                }
            }
        });
        rg4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.rg4_bt1){
                    type = 0;
                }
                else if(i == R.id.rg4_bt2){
                    type = 1;
                }
                else if(i == R.id.rg4_bt3){
                    type = 2;
                }
            }
        });
        rg5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.rg5_bt1){
                    picture = 0;
                }
                else if(i == R.id.rg5_bt2){
                    picture = 1;
                }
            }
        });
        rg6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.rg6_bt1){
                    drinking = 0;
                }
                else if(i == R.id.rg6_bt2){
                    drinking = 1;
                }
                else if(i == R.id.rg6_bt3){
                    drinking = 2;
                }
                else if(i == R.id.rg6_bt4){
                    drinking = 3;
                }
                else if(i == R.id.rg6_bt5){
                    drinking = 4;
                }
                else if(i == R.id.rg6_bt6){
                    drinking = 5;
                }
            }
        });
        rg7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.rg7_bt1){
                    drink_type = 0;
                }
                else if(i == R.id.rg7_bt2){
                    drink_type = 1;
                }
                else if(i == R.id.rg7_bt3){
                    drink_type = 2;
                }
                else if(i == R.id.rg7_bt4){
                    drink_type = 3;
                }
                else if(i == R.id.rg7_bt5){
                    drink_type = 4;
                }
            }
        });
        rg8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.rg8_bt1){
                    hotel = 0;
                }
                else if(i == R.id.rg8_bt2){
                    hotel = 1;
                }
                else if(i == R.id.rg8_bt3){
                    hotel = 2;
                }
                else if(i == R.id.rg8_bt4){
                    hotel = 3;
                }
            }
        });
        rg9.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.rg9_bt1){
                    car = 0;
                }
                else if(i == R.id.rg9_bt2){
                    car = 1;
                }
                else if(i == R.id.rg9_bt3){
                    car = 2;
                }
                else if(i == R.id.rg9_bt4){
                    car = 3;
                }
                else if(i == R.id.rg9_bt5){
                    car = 4;
                }
            }
        });
    }
}
