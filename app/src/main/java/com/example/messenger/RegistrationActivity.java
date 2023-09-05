package com.example.messenger;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.messenger.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity {
    EditText firstname, name, nickname, email, password;
    Button registration, authorization;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef = db.collection("users");
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);

        firstname = findViewById(R.id.etFirstname);
        name = findViewById(R.id.etName);
        nickname = findViewById(R.id.etNickname);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        registration = findViewById(R.id.btRegistration);
        authorization = findViewById(R.id.btAuthorization);

        firebaseAuth = FirebaseAuth.getInstance();



        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validData();
            }
        });

        authorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, AuthorizationActivity.class);
                startActivity(intent);
            }
        });
    }



    private void validData() {
        String log = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if (!nickname.getText().toString().trim().matches("(?=\\S+$).{3,}")) { nickname.setError("Никнейм должен состоять не менее чем из 3 символов, а также не должен содержать пробелов!"); }
        else if (!Patterns.EMAIL_ADDRESS.matcher(log).matches()) { email.setError("Некорректная почта!"); }
        else if (!pass.matches("(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}")) { password.setError("Пароль должен состоять не менее чем из 6 символов, включать в себя символы верхнего и нижнего регистра, не содержать пробелов, а также должен содержать в себе только буквы латинского алфавита!"); }
        else { firebaseRegistration(log, pass); }
    }

    private void firebaseRegistration(String log, String pass) {
        SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        firebaseAuth.createUserWithEmailAndPassword(log, pass).addOnSuccessListener((new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                User newUser = new User(firebaseUser.getEmail(), firebaseUser.getUid(), firstname.getText().toString().trim(), name.getText().toString().trim(), nickname.getText().toString().trim(), null);

                usersRef.add(newUser).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        myPrefs.edit().putString("email",log).putString("password",pass).putString("uid", firebaseAuth.getCurrentUser().getUid()).apply();
                        Intent intent = new Intent(RegistrationActivity.this, AvatarActivity.class);
                        startActivity(intent);
                    }
                });
            }
        })).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistrationActivity.this, "Аккаунт не был зарегистрирован!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
