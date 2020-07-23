package com.example.krazynews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ForgotPasswordEmail extends AppCompatActivity {
    private Button sendMail;
    private EditText getEmail;
    private TextView emailWarrning;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_email);

        sendMail = findViewById(R.id.send_email);
        getEmail = findViewById(R.id.edit_text);
        emailWarrning = findViewById(R.id.empty_email);
        progressBar = findViewById(R.id.sent_email_progressbar);
        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailWarrning.setText("");
                if(checkDetails()){
                    sendMail.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public boolean checkDetails(){

        String email_input = getEmail.getText().toString().trim();

        boolean flag = false;
        if(email_input.isEmpty()){
            emailWarrning.setText("Email field can't be empty");
            flag = true;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email_input).matches()){
            emailWarrning.setText("Please enter valid Email");
            flag = true;
        }

        if(flag)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
