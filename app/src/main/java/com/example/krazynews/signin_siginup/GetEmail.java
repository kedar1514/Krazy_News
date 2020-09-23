package com.example.krazynews.signin_siginup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.krazynews.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//  <<,
public class GetEmail extends AppCompatActivity {
    private TextView bold_text, empty_email,text_view1,text_view2,text_view3;
    private LinearLayout long_text;
    private EditText get_email;
    private Button send_code;
    private ProgressBar progressBar;
    private ImageView main_image;
    private boolean flag , otp_correct;
    private boolean otpSentResult,emailExistVal;
    private String OTP;
    private String name, city, user_email;
    private String URL_OTP = "https://www.krazyfox.in/krazynews/app/otp.php";
    private String URL_OTP_VALIDATE = "https://www.krazyfox.in/krazynews/app/otp_validate.php";
    private String URL_EMAIL_CHECK = "https://www.krazyfox.in/krazynews/app/emailCheck.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_email);
        // Taking city and name value from previous activity
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        city = intent.getStringExtra("city");

        bold_text = findViewById(R.id.bold_text);
        long_text = findViewById(R.id.long_text);
        get_email = findViewById(R.id.edit_text);

        progressBar = findViewById(R.id.sent_code_progressbar);
        send_code = findViewById(R.id.sent_code);
        main_image = findViewById(R.id.imageView);
        empty_email = findViewById(R.id.empty_email);

        text_view1 = findViewById(R.id.text_view1);
        text_view2 = findViewById(R.id.text_view2);
        text_view3 = findViewById(R.id.text_view3);

        send_code.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        otpSentResult = false;
        emailExistVal = false;
        send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailPassTry();
            }
        });
    }



    public void sendOtp(final String email){
        final boolean tempFlag = true;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("sendOtp", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success").toString();
                            //Toast.makeText(GetEmail.this, "Register Success!", Toast.LENGTH_LONG).show();
                            if(success.equals("-1")){
                                send_code.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(GetEmail.this, "Mail Error!", Toast.LENGTH_LONG).show();
                                otpSentResult=false;
                            }
                            else if(success.equals("1")){
                                send_code.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(GetEmail.this, "Otp Sent", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(GetEmail.this, VerifyOtp.class);
                                i.putExtra("name",name);
                                i.putExtra("city",city);
                                i.putExtra("email",email);
                                startActivity(i);
                                finish();
                            }
                            else{
                                send_code.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(GetEmail.this, "Otp sent Failure", Toast.LENGTH_LONG).show();
                                otpSentResult = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            send_code.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            Log.d("sendOtp", "onResponse: "+ e.toString());
                            Toast.makeText(GetEmail.this, "JsonException Error!" + e.toString(), Toast.LENGTH_LONG).show();
                            otpSentResult = false;
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.d("jsonError",error.toString());
                        Toast.makeText(GetEmail.this, "Otp Error!" + error.toString(), Toast.LENGTH_LONG).show();
                        send_code.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        otpSentResult = false;
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                Log.d("emailValue", email.toString());
                params.put("email",email);
                Log.d("emailValue", params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void emailExist(final String email){
        final boolean tempFlag=true;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EMAIL_CHECK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            //Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1"))
                            {
                                send_code.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                empty_email.setText("Email already registered");
                                emailExistVal = tempFlag;
                            }
                            else
                            {
                                sendOtp(email);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Log.d("emailExist", e.toString());
                            Toast.makeText(GetEmail.this, "Json Error!" + e.toString(), Toast.LENGTH_LONG).show();
                            send_code.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.d("jsonError", error.toString());
                        Toast.makeText(GetEmail.this, "Register Error!" + error.toString(), Toast.LENGTH_LONG).show();
                        send_code.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",email);
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
                        emailPassTry();
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

    void emailPassTry()
    {
        if(checkConnection())
        {
            if(get_email.getText().toString().trim().isEmpty())
            {
                empty_email.setText("Please enter email");
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(get_email.getText().toString().trim()).matches()){
                empty_email.setText("Please enter valid Email");
            }
            else
            {
                user_email = get_email.getText().toString().trim();
                send_code.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                empty_email.setText("");
                emailExist(user_email);
            }
        }
    }
}
