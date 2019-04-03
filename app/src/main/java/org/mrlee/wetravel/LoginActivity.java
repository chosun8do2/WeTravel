package org.mrlee.wetravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    // 이메일과 비밀번호
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button loginBtn;
    private Button loginRegBtn;
    private LoginButton loginButton;
    private Button find_passward_Button;

    private ProgressBar loginProgress;

    private String email = "";
    private String password = "";

    // 페이스북 콜백 매니저
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.hide();
        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.myprofile_car);
        editTextPassword = findViewById(R.id.passward);
        loginBtn = (Button) findViewById(R.id.login);
        loginRegBtn = (Button) findViewById(R.id.register);
        loginProgress = (ProgressBar) findViewById(R.id.login_progress);
        find_passward_Button = (Button) findViewById(R.id.find_passward);

        find_passward_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(LoginActivity.this);

                ad.setTitle("비밀번호 찾기");       // 제목 설정
                ad.setMessage("비밀번호를 찾을 이메일을 입력해주세요!");   // 내용 설정

                // EditText 삽입하기
                final EditText et = new EditText(LoginActivity.this);
                ad.setView(et);

                // 확인 버튼 설정
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Text 값 받기
                        String value = et.getText().toString();
                        mAuth.sendPasswordResetEmail(value)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this, "해당 메일로 비밀번호 재설정 메일을 보냈습니다! 메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(LoginActivity.this, "메일 형식이 잘못되었거나 가입되지 않은 메일입니다!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

                // 취소 버튼 설정
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });
                // 창 띄우기
                ad.show();
            }
        });

        // 페이스북 콜백 등록
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile", "user_location");
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("Success", String.valueOf(loginResult.getAccessToken()));
                Log.d("Success", String.valueOf(Profile.getCurrentProfile().getId()));
                Log.d("Success", String.valueOf(Profile.getCurrentProfile().getName()));
                Log.d("Success", String.valueOf(Profile.getCurrentProfile().getProfilePictureUri(200, 200)));
                requestUserProfile(loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(), "페이스북 로그인을 취소하셨습니다.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getBaseContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });




        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        };
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();

                if(isValidEmail() && isValidPasswd()) {
                    loginUser(email, password);
                }
            }
        });
        loginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            return false;
        } else {
            return true;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            sendToMain();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void sendToMain() {

        Intent mainIntent = new Intent(LoginActivity.this, SelectActivity.class);
        startActivity(mainIntent);
        finish();
    }

    // 로그인
    private void loginUser(String email, String password)
    {
        loginProgress.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            sendToMain();
                        } else {
                            // 로그인 실패
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(LoginActivity.this, "Error :" + errorMessage, Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                        loginProgress.setVisibility(View.INVISIBLE);
                    }
                });
    }

    public void requestUserProfile(LoginResult loginResult){
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String email = object.getString("email");
                            Log.e("object", object.toString());
                            Log.e("response", response.toString());
                            //JSONObject jsonobject_location = object.getJSONObject("location");
                            //String str_location = jsonobject_location.getString("name");
                            //String gender = response.getJSONObject().getString("gender");
                            //String locale = response.getJSONObject().getString("locale");
                            //String language = response.getJSONObject().getString("languages");
                            //String location = response.getJSONObject().getString("location");
                            Log.d("Result", email);
                            //Log.d("Result", gender);
                            //Log.d("Result", locale);
                            //Log.d("Result", language);
                            //Log.d("Result", str_location);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email");
        request.setParameters(parameters);
        request.executeAsync();
    }
    /****************************************************************************************************************
     * Facebook
     ****************************************************************************************************************/
    // 페이스북 로그인 이벤트
// 사용자가 정상적으로 로그인한 후 페이스북 로그인 버튼의 onSuccess 콜백 메소드에서 로그인한 사용자의
// 액세스 토큰을 가져와서 Firebase 사용자 인증 정보로 교환하고,
// Firebase 사용자 인증 정보를 사용해 Firebase에 인증.
    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                            sendToMain();
                        } else {
                            // 로그인 실패
                            Toast.makeText(LoginActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        // 페이스북 콜백 등록
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}
