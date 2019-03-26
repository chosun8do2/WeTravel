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

    private RadioGroup rg1, rg2, rg3, rg4;

    private int age = 0; //0: 20-24 1: 25-29 2: 30-34 3: 35-39 4:40대이상
    private int smoking=0; //0: 흡연 1: 비흡연
    private int drinking=0; //0: 네 1: 아니요 2: 조금..
    private int together=0; //0: 1명 1: 2명 2: 3명 3: 4명이상 4: 상관없다.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
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
                Survey.together =together;
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

        rg1 = (RadioGroup) findViewById(R.id.radioGroup1);
        rg2 = (RadioGroup) findViewById(R.id.radioGroup2);
        rg3 = (RadioGroup) findViewById(R.id.radioGroup3);
        rg4 = (RadioGroup) findViewById(R.id.radioGroup4);

        RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rg1_bt1){
                    age = 0;
                }
                else if(checkedId == R.id.rg1_bt2){
                    age = 1;
                }
                else if(checkedId == R.id.rg1_bt3){
                    age = 2;
                }
                else if(checkedId == R.id.rg1_bt4){
                    age = 3;
                }
                else if(checkedId == R.id.rg1_bt5){
                    age = 4;
                }
                else if(checkedId == R.id.rg2_bt1){
                    smoking = 0;
                }
                else if(checkedId == R.id.rg2_bt2){
                    smoking = 1;
                }
                else if(checkedId == R.id.rg3_bt1){
                    drinking = 0;
                }
                else if(checkedId == R.id.rg3_bt2){
                    drinking = 1;
                }
                else if(checkedId == R.id.rg3_bt3){
                    drinking = 2;
                }
                else if(checkedId == R.id.rg4_bt1){
                    together = 0;
                }
                else if(checkedId == R.id.rg4_bt2){
                    together = 1;
                }
                else if(checkedId == R.id.rg4_bt3){
                    together = 2;
                }
                else if(checkedId == R.id.rg4_bt4){
                    together = 3;
                }
                else if(checkedId == R.id.rg4_bt5){
                    together = 4;
                }
            }
        };
    }
}
