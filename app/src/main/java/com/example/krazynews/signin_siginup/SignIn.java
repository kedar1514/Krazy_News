package com.example.krazynews.signin_siginup;

import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//  <<,
public class SignIn extends AppCompatActivity {
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
    private TextView email_warrning, password_warrning, forgotPassword;
    private EditText email, password;
    private Button signIn;
    private ProgressBar progressBar;
    private String URL_LOGIN = "https://krazynews.000webhostapp.com/app/login.php";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        progressBar = findViewById(R.id.sign_in_progress_bar);
        signIn = findViewById(R.id.sign_in_btn);
        email = findViewById(R.id.sign_in_email);
        email_warrning = findViewById(R.id.sign_in_email_warrning);
        password = findViewById(R.id.sign_in_password);
        password_warrning = findViewById(R.id.sign_in_password_warrning);
        forgotPassword = findViewById(R.id.forgot_password);

        signIn.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_warrning.setText("");
                password_warrning.setText("");
                if(checkDetails()){
                    signIn.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    login();
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public boolean checkDetails(){

        String email_input = email.getText().toString().trim();
        String password_input = password.getText().toString().trim();

        boolean flag = false;
        if(email_input.isEmpty()){
            email_warrning.setText("Email field can't be empty");
            flag = true;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email_input).matches()){
            email_warrning.setText("Please enter valid Email");
            flag = true;
        }
        if(password_input.isEmpty()){
            password_warrning.setText("Password field can't be empty");
            flag = true;
        }
        else if(!PASSWORD_PATTERN.matcher(password_input).matches()){
            password_warrning.setText("Please use correct rules for password. Password must contain one uppercase letter, one lower case letter, one number and a special symbol (@#$%^&+=)");
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
    public void login(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("tagconvertstr", "["+response+"]");
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success").toString();
//                            Log.i("tagconvertstr", "["+success+"]");
                            if(success.equals("1"))
                            {
                                // saving login information in shared preferance
                                String userName = jsonObject.getString("username").toString();
                                String city = jsonObject.getString("city").toString();
                                SharedPreferences preferences = getSharedPreferences("PREFERENCE",MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("Login","YES");
                                editor.putString("UserName",userName);
                                editor.putString("UserEmail",email.getText().toString().trim());
                                editor.putString("UserCity",city);
                                editor.apply();

                                Toast.makeText(getApplicationContext(),"Signed up succesfull", Toast.LENGTH_SHORT).show();
                                signIn.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(SignIn.this, ChooseTopicSignIn.class));
                                finish();
                            }
                            else if(success.equals("0")){
                                Toast.makeText(getApplicationContext(),"Wrong email or Password", Toast.LENGTH_SHORT).show();
                                signIn.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            signIn.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignIn.this, "Login Error!" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",email.getText().toString().trim());
                params.put("password",password.getText().toString().trim());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignIn.this,SignInSignUp.class));
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
}


