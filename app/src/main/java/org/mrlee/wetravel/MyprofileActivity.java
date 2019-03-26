package org.mrlee.wetravel;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyprofileActivity extends AppCompatActivity {

    ImageView editprofile, profile;
    TextView email, location, language, name, gender;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference storageRef;
    int gen = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        setContentView(R.layout.activity_myprofile);
        ab.setDisplayHomeAsUpEnabled(true);

        email = findViewById(R.id.email);
        location = findViewById(R.id.location);
        name = findViewById(R.id.name);
        language = findViewById(R.id.language);
        gender = findViewById(R.id.gender);

        editprofile = findViewById(R.id.edit);
        profile = findViewById(R.id.profile);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyprofileActivity.this, EditprofileActivity.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    User get = postSnapshot.getValue(User.class);
                    if(key != null && get.getEmail() != null) {
                        if(get.getEmail().equals(mAuth.getCurrentUser().getEmail().toString())) {
                            email.setText(get.getEmail());
                            location.setText(get.getLocation());
                            name.setText(get.getName());
                            language.setText(get.getLanguage());
                            gen = get.getGender();
                            if(gen == 0) gender.setText("남자");
                            else gender.setText("여자");
                            storageRef = FirebaseStorage.getInstance()
                                    .getReference("profile/"+get.getEmail().split("@")[0]+"_"+get.getName()+".png");
                            Glide.with(MyprofileActivity.this).load(storageRef).into(profile);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
