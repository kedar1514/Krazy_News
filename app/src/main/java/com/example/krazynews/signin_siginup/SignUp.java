package com.example.krazynews.signin_siginup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.krazynews.MainActivity;
import com.example.krazynews.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {
    //  <<,
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
    private Button  signUp;
    private EditText name, email, password, re_enter;
    private TextView skip, name_warning, email_warning, password_warning, re_enter_password_warning;
    private ProgressBar progressBar;
    private String URL_SIGNUP = "http://192.168.0.103/test/index.php";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        skip = findViewById(R.id.skip);
        name = findViewById(R.id.name);
        name_warning = findViewById(R.id.name_warning);
        email = findViewById(R.id.email);
        email_warning = findViewById(R.id.email_warning);
        password = findViewById(R.id.password);
        password_warning = findViewById(R.id.password_warning);
        re_enter = findViewById(R.id.re_enter_password);
        re_enter_password_warning = findViewById(R.id.re_enter_password_warning);
        signUp = findViewById(R.id.sign_up_btn);
        progressBar = findViewById(R.id.loading);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_warning.setText("");
                email_warning.setText("");
                password_warning.setText("");
                re_enter_password_warning.setText("");

                if(checkDetails()){
                    singUpProcess();
                    startActivity(new Intent(SignUp.this, MainActivity.class));
                    finish();
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    signUp.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public boolean checkDetails(){
        progressBar.setVisibility(View.VISIBLE);
        signUp.setVisibility(View.GONE);
        String email_input = email.getText().toString().trim();
        String password_input = password.getText().toString().trim();
        String name_input = name.getText().toString().trim();
        String re_enter_input = re_enter.getText().toString().trim();
        boolean flag = false;
        if(name_input.isEmpty()){
            name_warning.setText("Name field Can't be empty");
            flag = true;
        }
        if(email_input.isEmpty()){
            email_warning.setText("Email field can't be empty");
            flag = true;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email_input).matches()){
            email_warning.setText("Please enter valid Email");
            flag = true;
        }
        if(password_input.isEmpty()){
            password_warning.setText("Password field can't be empty");
            flag = true;
        }
        else if(!PASSWORD_PATTERN.matcher(password_input).matches()){
            password_warning.setText("Please use correct rules for password. Password must contain one uppercase letter, one lower case letter, one number and a special symbol (@#$%^&+=)");
            flag = true;
        }
        if(!password.getText().toString().equals(re_enter_input)){
            re_enter_password_warning.setText("Password is not macthing");
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
    //  <<,
    public void singUpProcess(){
        progressBar.setVisibility(View.VISIBLE);
        signUp.setVisibility(View.GONE);
        final String name = this.name.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String password = this.password.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SIGNUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success").toString();

                            if(success.equals("1")){
                                Toast.makeText(SignUp.this, "Register Success!", Toast.LENGTH_LONG).show();
                            }
                            else if(success.equals("-1")){
                                Toast.makeText(SignUp.this, "Email already registered!", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SignUp.this, "Register Error!" + e.toString(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            signUp.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUp.this, "Register Error!" + error.toString(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        signUp.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_name",name);
                params.put("email",email);
                params.put("password",password);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {

    }
}
