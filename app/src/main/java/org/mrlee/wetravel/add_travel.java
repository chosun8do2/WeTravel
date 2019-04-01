package org.mrlee.wetravel;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class add_travel extends AppCompatActivity {

    private String TAG = "add_travel";
    private Button start_day, end_day;
    private String startDay, endDay;
    private EditText title, content;

    private ImageView add_image;
    private ImageButton pre_btn, save_btn, del_btn, back_btn;;

    private Uri filePath;
    private String boardTitle = "";
    private String boardContent = "";

    private FirebaseAuth mAuth;
    private String imageName;

    private Spinner spinner;

    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    String countryName;
    String userName;

    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.hide();
        init();
        add_image = (ImageView) findViewById(R.id.add_image);
        pre_btn = (ImageButton) findViewById(R.id.view_btn);
        save_btn = (ImageButton) findViewById(R.id.save_btn);
        del_btn = (ImageButton) findViewById(R.id.del_btn);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        arrayList = new ArrayList<>();

        Locale[] availableLocales = Locale.getAvailableLocales();
        for(Locale locale : availableLocales){
            String code = locale.getCountry();
            String name = locale.getDisplayCountry();
            String ename = locale.getDisplayCountry(Locale.KOREA);
            if(ename != "" && !arrayList.contains(ename)) arrayList.add(ename);
        }
        Collections.sort(arrayList);
        arrayList.add(0, "여행할 국가를 선택하세요!");
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayList);

        spinner = (Spinner)findViewById(R.id.country);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(),arrayList.get(i)+"가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                if(i != 0) countryName = arrayList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner.setSelection(0);
        countryName = "선택안함";
        userName = "게스트";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_HH");
        startDay = formatter.format(new Date());
        endDay = formatter.format(new Date());

        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    User get = postSnapshot.getValue(User.class);
                    if(key != null && get.getEmail() != null) {
                        if(get.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                            userName = get.getName();
                            break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            Log.d(TAG, "uri:" + String.valueOf(filePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                add_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void upload(){
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            Date now = new Date();
            String username = mAuth.getCurrentUser().getEmail().split("@")[0];
            String filename = username + "_" + title.getText().toString() + ".png";
            imageName = filename;
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://wetravel-b6b10.appspot.com").child("board/" + filename);
            //올라가거라...
            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            //Unique한 파일명을 만들자.
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
                            String filename = mAuth.getCurrentUser().getEmail().split("@")[0]+formatter.format(new Date());
                            myRef.child("board").child(filename).child("user").setValue(mAuth.getCurrentUser().getEmail());
                            myRef.child("board").child(filename).child("image").setValue(imageName);
                            myRef.child("board").child(filename).child("title").setValue(title.getText().toString());
                            myRef.child("board").child(filename).child("content").setValue(content.getText().toString());
                            myRef.child("board").child(filename).child("startday").setValue(startDay);
                            myRef.child("board").child(filename).child("endday").setValue(endDay);
                            myRef.child("board").child(filename).child("country").setValue(countryName);
                            myRef.child("board").child(filename).child("name").setValue(userName);
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
            boardTitle = title.getText().toString();
            boardContent = content.getText().toString();
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private void init(){
        //Calendar를 이용하여 년, 월, 일, 시간, 분을 PICKER에 넣어준다.
        final Calendar cal = Calendar.getInstance();

        Log.e(TAG, cal.get(Calendar.YEAR)+"");
        Log.e(TAG, cal.get(Calendar.MONTH)+1+"");
        Log.e(TAG, cal.get(Calendar.DATE)+"");

        title = findViewById(R.id.Board_title);
        content = findViewById(R.id.Board_Context);

        start_day = findViewById(R.id.start_day_btn);
        end_day = findViewById(R.id.end_day_btn);

        start_day.setText(String.format("%d 년 %d 월 %d 일", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE)));
        end_day.setText(String.format("%d 년 %d 월 %d 일", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE)));
        start_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(add_travel.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        String msg = String.format("%d 년 %d 월 %d 일", year, month+1, date);
                        startDay = String.format("%d-%d-%d", year, month+1, date);
                        Toast.makeText(add_travel.this, msg, Toast.LENGTH_SHORT).show();
                        start_day.setText(msg);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.getDatePicker().setMinDate(new Date().getTime()-1);    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        end_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    DatePickerDialog dialog = new DatePickerDialog(add_travel.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                            String msg = String.format("%d 년 %d 월 %d 일", year, month + 1, date);
                            endDay = String.format("%d-%d-%d", year, month+1, date);
                            Toast.makeText(add_travel.this, msg, Toast.LENGTH_SHORT).show();
                            end_day.setText(msg);
                        }
                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                    dialog.getDatePicker().setMinDate(new Date().getTime()-1);    //입력한 날짜 이후로 클릭 안되게 옵션
                    dialog.show();
                }
        });
    }
}

class AscendingString implements Comparator<String> { @Override public int compare(String a, String b) { return b.compareTo(a); } }