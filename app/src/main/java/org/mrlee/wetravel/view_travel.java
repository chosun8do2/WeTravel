package org.mrlee.wetravel;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class view_travel extends AppCompatActivity {
    private String TAG = "view_travel";
    private Button start_day, end_day;
    private ImageButton back_btn;
    private TextView title, content, country;

    private ImageView add_image;

    private Uri filePath;
    private String boardTitle = "";
    private String boardContent = "";

    private FirebaseAuth mAuth;
    private String imageName;

    FirebaseDatabase database;
    DatabaseReference myRef;

    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_travel);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.hide();
        init();
        add_image = (ImageView) findViewById(R.id.add_image);

        Intent intent = getIntent();

        String ti = intent.getExtras().getString("title");
        String con = intent.getExtras().getString("content");
        String img = intent.getExtras().getString("image");
        String stday = intent.getExtras().getString("startday");
        String enday = intent.getExtras().getString("endday");
        String ctry = intent.getExtras().getString("country");

        title.setText(ti);
        content.setText(con);
        country.setText(ctry);
        start_day.setText(stday);
        end_day.setText(enday);
        storageRef = FirebaseStorage.getInstance().getReference("board/"+img);
        Glide.with(this).load(storageRef).into(add_image);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void init(){
        //Calendar를 이용하여 년, 월, 일, 시간, 분을 PICKER에 넣어준다.
        final Calendar cal = Calendar.getInstance();

        Log.e(TAG, cal.get(Calendar.YEAR)+"");
        Log.e(TAG, cal.get(Calendar.MONTH)+1+"");
        Log.e(TAG, cal.get(Calendar.DATE)+"");

        title = findViewById(R.id.Board_title);
        content = findViewById(R.id.Board_Context);
        country = findViewById(R.id.country);

        start_day = findViewById(R.id.start_day_btn);
        end_day = findViewById(R.id.end_day_btn);
        back_btn = findViewById(R.id.back_btn);
    }
}
