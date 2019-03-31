package org.mrlee.wetravel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;

public class EditprofileActivity extends AppCompatActivity {

    static final int REQ_ADD_CONTACT = 1;

    private Button submitButton, find_location_button, cancelButton;
    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private RadioGroup rg1, rg2, rg3, rg4, rg5, rg6, rg7, rg8, rg9;
    private EditText nameText, locationText;
    private Spinner languageText;

    private int age = 0; //0: 20-24 1: 25-29 2: 30-34 3: 35-39 4:40대이상
    private int smoking=0; //0: 흡연 1: 비흡연
    private int noize = 0;
    private int type = 0;
    private int picture = 0;
    private int drinking=0; //0: 네 1: 아니요 2: 조금..
    private int drink_type = 0;
    private int hotel = 0;
    private int car = 0;

    private String name;
    private int language;
    private String location;

    String[] item;
    String temp;

    //결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ADD_CONTACT) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra("location");
                if(str != null) locationText.setText(str);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        item = new String[]{"선택하세요","한국어","영어","일본어","중국어", "독일어", "프랑스어", "아랍어"};

        rg1 = (RadioGroup) findViewById(R.id.radioGroup1);
        rg2 = (RadioGroup) findViewById(R.id.radioGroup2);
        rg3 = (RadioGroup) findViewById(R.id.radioGroup3);
        rg4 = (RadioGroup) findViewById(R.id.radioGroup4);
        rg5 = (RadioGroup) findViewById(R.id.radioGroup5);
        rg6 = (RadioGroup) findViewById(R.id.radioGroup6);
        rg7 = (RadioGroup) findViewById(R.id.radioGroup7);
        rg8 = (RadioGroup) findViewById(R.id.radioGroup8);
        rg9 = (RadioGroup) findViewById(R.id.radioGroup9);
        nameText = (EditText) findViewById(R.id.nameText);
        languageText = (Spinner) findViewById(R.id.languageText);
        locationText = (EditText) findViewById(R.id.locationText);

        find_location_button = (Button) findViewById(R.id.find_location);
        find_location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditprofileActivity.this, DaumWebViewActivity.class);
                startActivityForResult(intent, REQ_ADD_CONTACT);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageText.setAdapter(adapter);
        languageText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(EditprofileActivity.this, item[i], Toast.LENGTH_LONG).show();
                temp = item[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    User get = postSnapshot.getValue(User.class);
                    if(key != null) {
                        if(key.equals(mAuth.getCurrentUser().getEmail().split("@")[0])) {
                            name = get.getName();
                            temp = get.getLanguage();
                            location = get.getLocation();
                            nameText.setText(name);
                            locationText.setText(location);
                            int i=0;
                            for(String s : item){
                                if(s.equals(temp)){
                                    language = i;
                                    break;
                                }
                                i++;
                            }
                            languageText.setSelection(language);
                        }
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.child("survey").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    survey get = postSnapshot.getValue(survey.class);
                    if(key != null) {
                        if(key.equals(mAuth.getCurrentUser().getUid())) {
                            age = get.getAge(); //0: 20-24 1: 25-29 2: 30-34 3: 35-39 4:40대이상
                            smoking=get.getSmoking(); //0: 흡연 1: 비흡연
                            noize = get.getNoize();
                            type = get.getType();
                            picture = get.getPicture();
                            drinking=get.getDrinking(); //0: 네 1: 아니요 2: 조금..
                            drink_type = get.getDrink_type();
                            hotel = get.getHotel();
                            car = get.getCar();

                            rg1.check(((RadioButton)rg1.getChildAt(age)).getId());
                            rg2.check(((RadioButton)rg2.getChildAt(smoking)).getId());
                            rg3.check(((RadioButton)rg3.getChildAt(noize)).getId());
                            rg4.check(((RadioButton)rg4.getChildAt(type)).getId());
                            rg5.check(((RadioButton)rg5.getChildAt(picture)).getId());
                            rg6.check(((RadioButton)rg6.getChildAt(drinking)).getId());
                            //rg7.check(((RadioButton)rg7.getChildAt(drink_type)).getId());
                            //rg8.check(((RadioButton)rg8.getChildAt(hotel)).getId());
                            //rg9.check(((RadioButton)rg9.getChildAt(car)).getId());
                        }
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
                myRef.child("survey").child(uid).setValue(Survey);
                myRef.child("users").child(mAuth.getCurrentUser().getEmail().split("@")[0]).child("name").setValue(nameText.getText().toString());
                myRef.child("users").child(mAuth.getCurrentUser().getEmail().split("@")[0]).child("location").setValue(locationText.getText().toString());
                myRef.child("users").child(mAuth.getCurrentUser().getEmail().split("@")[0]).child("language").setValue(temp);
                Intent intent = new Intent(EditprofileActivity.this, MyprofileActivity.class);
                startActivity(intent);
                finish();
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
