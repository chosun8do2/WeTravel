package org.mrlee.wetravel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private EditText reg_email_field;
    private EditText reg_pass_field;
    private EditText reg_confirm_field;
    private EditText reg_name_field;
    private EditText reg_language_field;
    private EditText reg_location_field;
    private Button reg_btn;
    private ProgressBar reg_progress;
    private RadioButton btn_man;
    private RadioButton btn_woman;
    private RadioGroup rg;

    private ImageView profileImage;
    private TextView profileImageText;

    private FirebaseAuth mAuth;

    FirebaseDatabase database;
    DatabaseReference myRef;

    private Uri filePath;
    private String imageName;
    private int gender;

    private String str_Qtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.hide();
        mAuth = FirebaseAuth.getInstance();
        gender = 0;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        reg_email_field = (EditText) findViewById(R.id.emailText);
        reg_pass_field = (EditText) findViewById(R.id.passwardText);
        reg_confirm_field = (EditText) findViewById(R.id.checkpasswardText);
        reg_name_field = (EditText) findViewById(R.id.nameText);
        reg_language_field = (EditText) findViewById(R.id.languageText);
        reg_location_field = (EditText) findViewById(R.id.locationText);
        btn_man = (RadioButton)findViewById(R.id.man_button);
        btn_woman = (RadioButton)findViewById(R.id.woman_button);
        rg = (RadioGroup) findViewById(R.id.rdgroup);
        reg_btn = (Button) findViewById(R.id.reg_btn);
        reg_progress = (ProgressBar) findViewById(R.id.reg_progress);

        profileImage = (ImageView) findViewById(R.id.add_profileImage);
        profileImageText = (TextView) findViewById(R.id.myprofileImageTextView);



        btn_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rd = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
                str_Qtype = rd.getText().toString();
            }
        });

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("들어옴?-1");
                final String email = reg_email_field.getText().toString();
                String pass = reg_pass_field.getText().toString();
                String confirm_pass = reg_confirm_field.getText().toString();
                System.out.println("들어옴?0");
                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(confirm_pass)){
                    if(pass.equals(confirm_pass)){
                        System.out.println("들어옴?1");
                        createUser(email, pass);
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });

        RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.man_button){
                    gender = 0;
                }
                else if(checkedId == R.id.woman_button){
                    gender = 1;
                }
            }
        };
    }

    private void writeNewUser(String email, String nowDate, String name, String language, String location){
        User user = new User(email, nowDate, name, language,location, gender);
        myRef.child("users").child(email.split("@")[0]).setValue(user);
    }
    // 회원가입
    private void createUser(final String email, String password) {
        reg_progress.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        final String uid = task.getResult().getUser().getUid();
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            String username = reg_email_field.getText().toString().split("@")[0];
                            String imageUrl = username + "_" + reg_name_field.getText().toString() + ".png";
                            UserModel userModel = new UserModel();
                            userModel.userName = reg_name_field.getText().toString();
                            userModel.profileImageUrl = imageUrl;
                            userModel.uid = uid;

                            FirebaseDatabase.getInstance().getReference().child("userModel").child(uid).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //SignupActivity.this.finish();
                                }
                            });
                            upload(email);
                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다!", Toast.LENGTH_SHORT).show();
                        } else {
                            // 회원가입 실패
                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다!", Toast.LENGTH_SHORT).show();
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(RegisterActivity.this, "Error :" + errorMessage, Toast.LENGTH_LONG).show();
                        }
                        reg_progress.setVisibility(View.INVISIBLE);
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            sendToLogin();
        }
    }

    private void sendToLogin() {

        Intent mainIntent = new Intent(RegisterActivity.this, SurveyActivity.class);
        startActivity(mainIntent);
        finish();
    }

    //결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileImage.setImageBitmap(bitmap);
                profileImageText.setVisibility(View.INVISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void upload(final String email){
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            Date now = new Date();
            String username = reg_email_field.getText().toString().split("@")[0];
            String filename = username + "_" + reg_name_field.getText().toString() + ".png";
            imageName = filename;
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://wetravel-b6b10.appspot.com").child("profile/" + filename);
            //올라가거라...
            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            //Unique한 파일명을 만들자.
                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
                            String nowDate = sdfNow.format(date);
                            String name = reg_name_field.getText().toString();
                            String language = reg_language_field.getText().toString();
                            String location = reg_location_field.getText().toString();
                            writeNewUser(email, nowDate, name, language, location);
                            sendToLogin();
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();

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
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
