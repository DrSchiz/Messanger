package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText email, code;
    Button send, _continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        email = findViewById(R.id.etEmail);
        code = findViewById(R.id.etCode);
        send = findViewById(R.id.btSendCode);
        _continue = findViewById(R.id.btContinue);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validData();
            }
        });
    }

    private void validData() {
        if (email.toString().trim().isEmpty()) { email.setError("Введите адрес электронной почты!"); }
        else {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of email");
            intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
            intent.setData(Uri.parse("mailto:" + email.toString().trim()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}