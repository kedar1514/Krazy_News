package com.example.krazynews.signin_siginup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
    private ImageView main_image;
    private boolean flag , otp_correct;
    private String OTP;
    private String name, city, user_email;
    private String URL_OTP = "http://192.168.0.103/test/otp.php";
    private String URL_OTP_VALIDATE = "http://192.168.0.103/test/otp_validate.php";
    private String URL_EMAIL_CHECK = "http://192.168.0.103/test/emailCheck.php";
    private String URL_REGISTER = "http://192.168.0.103/test/index.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_email);
        // Taking city and name valur from previous activity
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        city = intent.getStringExtra("city");

        bold_text = findViewById(R.id.bold_text);
        long_text = findViewById(R.id.long_text);
        get_email = findViewById(R.id.edit_text);
        send_code = findViewById(R.id.sent_code);
        main_image = findViewById(R.id.imageView);
        empty_email = findViewById(R.id.empty_email);

        text_view1 = findViewById(R.id.text_view1);
        text_view2 = findViewById(R.id.text_view2);
        text_view3 = findViewById(R.id.text_view3);

        flag = false;
        send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!flag)
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
                        emailExist(get_email.getText().toString().trim());
                    }
                }
                else{
                    validateOTP(get_email.getText().toString().trim());
                }
            }
        });
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

                                Toast.makeText(GetEmail.this, "Verified Succesfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(GetEmail.this, SelectTopic.class);
                                intent.putExtra("name", name);
                                intent.putExtra("city", city);
                                intent.putExtra("email", user_email);
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(GetEmail.this,pairs);
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
                                empty_email.setText("Invalid Otp");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    public void sendOtp(final String email){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success").toString();
                            Toast.makeText(GetEmail.this, "Register Success!", Toast.LENGTH_LONG).show();
                            if(success.equals("-1")){
                                Toast.makeText(GetEmail.this, "Mail Error!", Toast.LENGTH_LONG).show();
                            }
                            else if(success.equals("1")){
                                Toast.makeText(GetEmail.this, "Otp Sent", Toast.LENGTH_LONG).show();
                                OTP = jsonObject.getString("message").toString();
                            }
                            else{
                                Toast.makeText(GetEmail.this, "Otp sent Failure", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GetEmail.this, "Register Error!" + e.toString(), Toast.LENGTH_LONG).show();
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
                params.put("email",email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void emailExist(final String email){
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
                                empty_email.setText("Email already registered");
                            }
                            else
                            {
                                user_email = get_email.getText().toString().trim();
                                text_view1.setText("We just sent you ");
                                text_view2.setText("Code");
                                text_view3.setText(" !");
                                send_code.setText("Next");
                                get_email.setText("");
                                empty_email.setText("");
                                get_email.setHint("Enter Code");
                                flag = true;
                                sendOtp(email);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GetEmail.this, "Register Error!" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GetEmail.this, "Register Error!" + error.toString(), Toast.LENGTH_LONG).show();
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


    @Override
    public void onBackPressed() {
        text_view1.setText("Tell us your ");
        text_view2.setText("Email");
        text_view3.setText(" !");
        send_code.setText("Next");
        get_email.setText("");
        empty_email.setText("");
        get_email.setHint("Enter Email");
        flag = false;
    }
}
