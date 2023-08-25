package com.sample.gs_gram.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sample.gs_gram.R;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, passwordConfirmEditText, nameEditText;
    private TextView majorText, loginbtn;
    private Button signupButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordConfirmEditText = findViewById(R.id.passwordConfirmEditText);
        nameEditText = findViewById(R.id.nameEditText);
        majorText = findViewById(R.id.majorText);
        signupButton = findViewById(R.id.signupButton);
        loginbtn = findViewById(R.id.loginbtn);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = emailEditText.getText().toString();
                String strPassword = passwordEditText.getText().toString();
                String strPasswordConfirm = passwordConfirmEditText.getText().toString();
                String strName = nameEditText.getText().toString();
                String strMajor = majorText.getText().toString();

                if (strEmail.length() == 0) {
                    Toast.makeText(SignUpActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (strPassword.length() == 0) {
                    Toast.makeText(SignUpActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (strPasswordConfirm.length() == 0) {
                    Toast.makeText(SignUpActivity.this, "비밀번호를 한번 더 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (nameEditText.length() == 0) {
                    Toast.makeText(SignUpActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (strMajor.equals("학과")) {
                    Toast.makeText(SignUpActivity.this, "학과를 선택해주세요", Toast.LENGTH_SHORT).show();
                }else {
                    if (strPassword.equals(strPasswordConfirm)) {
                        String strPwd =passwordEditText.getText().toString();
                        mAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    DocumentReference documentReference = mStore.collection("Account").document(mAuth.getUid());
                                    Map<String,Object> users = new HashMap<>();
                                    users.put("email",strEmail);
                                    users.put("pw",strPwd);
                                    users.put("userName",strName);
                                    users.put("major",strMajor);
                                    documentReference.set(users);

                                    Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(SignUpActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        majorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 문자열 항목 배열을 생성합니다.
                String[] majorOptions = {"간호학과", "건축공학과","경호학과", "국제물류학과","디자인학과","미디어컨텐츠학과","사회복지학과","정보보안학과","호텔경영학과",};

                // 다이얼로그를 생성합니다.
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setTitle("학과를 선택해주세요.")
                        .setItems(majorOptions, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // 사용자가 선택한 항목을 가져옵니다.
                                String selectedMajor = majorOptions[which];

                                // 선택한 항목을 majorText 텍스트뷰에 설정합니다.
                                majorText.setText(selectedMajor);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                // 다이얼로그를 표시합니다.
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }
}