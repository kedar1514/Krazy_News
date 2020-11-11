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
import com.example.krazynews.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerifyOtp extends AppCompatActivity {

    private TextView bold_text, wrongOtp;
    private LinearLayout long_text;
    private EditText getOtp;
    private Button verifyOtp;
    private ProgressBar progressBar;
    private String OTP;
    private String name, city, user_email;
    private String URL_OTP_VALIDATE = Constants.Base_Url+"/app/otp_validate.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_otp);
        // Taking city and name value from previous activity
        bold_text = findViewById(R.id.bold_text);
        long_text = findViewById(R.id.long_text);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        city = intent.getStringExtra("city");
        user_email = intent.getStringExtra("email");

        progressBar = findViewById(R.id.verify_otp_progressbar);
        verifyOtp = findViewById(R.id.verify_otp);
        wrongOtp = findViewById(R.id.wrong_otp);
        getOtp = findViewById(R.id.edit_text);

        verifyOtp.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OTP = getOtp.getText().toString().trim();
                wrongOtp.setText("");
                if(OTP.isEmpty()){
                    wrongOtp.setText("Please Enter Code");
                }
                else{
                    tryOtp();
                }
            }
        });
    }

    public void tryOtp() {
        if(checkConnection())
        {
            validateOTP(OTP);
        }
    }

    public void validateOTP(final String OTP_VAL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_OTP_VALIDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success").toString();

                            if(success.equals("1")){
                                Pair[] pairs = new Pair[2];
                                pairs[0] = new Pair<View, String>(bold_text,"bold");
                                pairs[1] = new Pair<View, String>(long_text,"long_text");

                                verifyOtp.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(VerifyOtp.this, "Verified Succesfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(VerifyOtp.this, GetPassword.class);
                                intent.putExtra("name", name);
                                intent.putExtra("city", city);
                                intent.putExtra("email", user_email);
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(VerifyOtp.this,pairs);
                                    startActivity(intent,options.toBundle());
                                    finish();
                                }
                                else
                                {
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            else{
                                verifyOtp.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                wrongOtp.setText("Invalid Code");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            verifyOtp.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(VerifyOtp.this, "Verification Error", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("otp",OTP_VAL);
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
                        tryOtp();
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
}
