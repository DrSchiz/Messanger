package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthorizationActivity extends AppCompatActivity {

    EditText email, password;
    Button enter, registration;
    TextView forgetPassword;
    String log, pass;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        log = myPrefs.getString("email","");
        pass = myPrefs.getString("password","");

        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        enter = findViewById(R.id.btEnter);
        registration = findViewById(R.id.btRegistration);
        forgetPassword = findViewById(R.id.tvForgetPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        if (log != "" && pass != "") {
            firebaseAuthorization(log.trim(), pass.trim());
        }

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validData(email.getText().toString().trim(), password.getText().toString().trim());
            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthorizationActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthorizationActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
    private void validData(String log, String pass) {
        if (log.isEmpty()) { email.setError("Введите адрес электронной почты!"); }
        else if (!Patterns.EMAIL_ADDRESS.matcher(log).matches()) { email.setError("Некорректная почта!"); }
        else if (pass.isEmpty()) { password.setError("Введите пароль!"); }
        else { firebaseAuthorization(log, pass); }
    }

    private void firebaseAuthorization(String log, String pass) {
        SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        firebaseAuth.signInWithEmailAndPassword(log, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                myPrefs.edit().putString("email",log).putString("password",pass).putString("uid", firebaseAuth.getCurrentUser().getUid()).apply();
                Intent intent = new Intent(AuthorizationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AuthorizationActivity.this, "Ошибка авторизации!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}