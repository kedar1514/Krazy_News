package com.example.krazynews.signin_siginup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//  <<,
public class SignIn extends AppCompatActivity {
    private TextView email_warrning, password_warrning;
    private EditText email, password;
    private Button signIn;
    private String URL_LOGIN = "http://192.168.0.103/test/login.php";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        signIn = findViewById(R.id.sign_in_btn);
        email = findViewById(R.id.sign_in_email);
        email_warrning = findViewById(R.id.sign_in_email_warrning);
        password = findViewById(R.id.sign_in_password);
        password_warrning = findViewById(R.id.sign_in_password_warrning);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_warrning.setText("");
                password_warrning.setText("");
                if(checkDetails()){
                    login();
                }
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

                        if(response.contains("1")){
                            Toast.makeText(getApplicationContext(),"Signed up succesfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignIn.this, MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"Wrong email or Password", Toast.LENGTH_SHORT).show();
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
                params.put("email",email.getText().toString());
                params.put("password",password.getText().toString());
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


