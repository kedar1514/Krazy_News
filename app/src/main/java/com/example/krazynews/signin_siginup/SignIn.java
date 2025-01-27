package com.example.krazynews.signin_siginup;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.krazynews.Constants;
import com.example.krazynews.ForgotPasswordEmail;
import com.example.krazynews.MainActivity;
import com.example.krazynews.NewsLink;
import com.example.krazynews.R;
import com.google.android.material.textfield.TextInputEditText;

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
    private TextView email_warrning, password_warrning, forgotPassword, boldText;
    private EditText email;
    private TextInputEditText password;
    private Button signIn;
    private LinearLayout longText;
    private ProgressBar progressBar;
    private ImageView logo;
    private String URL_LOGIN = Constants.Base_Url+"/app/login.php";
    private String URL_PASS = Constants.Base_Url+"/forgot-password.php";
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
        boldText = findViewById(R.id.bold_text);
        longText = findViewById(R.id.long_text);
        logo = findViewById(R.id.logo);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(URL_PASS));
                startActivity(intent);
            }
        });

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
                    if(checkConnection())
                    {
                        login();
                    }
                }
            }
        });

//        forgotPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Pair[] pairs = new Pair[4];
//                pairs[0] = new Pair<View, String>(boldText,"bold");
//                pairs[1] = new Pair<View, String>(longText,"long_text");
//                pairs[2] = new Pair<View, String>(logo,"main_image");
//                pairs[3] = new Pair<View, String>(signIn,"sign_in_btn");
//
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignIn.this,pairs);
//                    startActivity(new Intent(SignIn.this, ForgotPasswordEmail.class), options.toBundle());
//                }
//                else{
//                    startActivity(new Intent(SignIn.this, ForgotPasswordEmail.class));
//                }
//                finish();
//            }
//        });
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

    public boolean checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        final Dialog networkDialog = new Dialog(this);
        networkDialog.setContentView(R.layout.network_dialog);
        networkDialog.setCanceledOnTouchOutside(false);
        networkDialog.setCancelable(false);
//        networkDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
        networkDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        networkDialog.getWindow().getAttributes().windowAnimations =
                android.R.style.Animation_Dialog;

        Button btnTryAgain = networkDialog.findViewById(R.id.try_again);

        if(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()){


            btnTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    networkDialog.hide();
                    if(checkConnection())
                    {
                        login();
                    }
                }
            });
            networkDialog.show();
            return false;
        }else{
            networkDialog.hide();
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignIn.this,SignInSignUp.class));
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
}


