package com.example.krazynews.signin_siginup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.krazynews.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

//  <<,
public class GetPassword extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile(("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$"));

    private String name,email,city,password;
    private Button next;
    private ProgressBar progressBar;
    private TextInputEditText getPassword;
    private TextView passwordError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_password);
        Intent i = getIntent();
        name = i.getStringExtra("name");
        city = i.getStringExtra("city");
        email = i.getStringExtra("email");

        progressBar = findViewById(R.id.next_progressbar);
        next = findViewById(R.id.next);
        getPassword = findViewById(R.id.edit_password);
        passwordError = findViewById(R.id.password_error);

        next.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password_input = getPassword.getText().toString().trim();
                password = getPassword.getText().toString().trim();
                if(password_input.isEmpty()){
                    passwordError.setText("Password field can't be empty");
                }
                else if(!PASSWORD_PATTERN.matcher(password_input).matches()){
                    passwordError.setText("Please use correct rules for password. Password must contain one uppercase letter, one lower case letter, one number and a special symbol (@#$%^&+=)");
                }
                else{
                    Intent intent = new Intent(GetPassword.this, SelectTopic.class);
                    intent.putExtra("name", name);
                    intent.putExtra("city", city);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    next.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                }
            }
        });
    }
}
