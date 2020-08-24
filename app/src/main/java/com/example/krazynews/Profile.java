package com.example.krazynews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krazynews.signin_siginup.CityInput;
import com.example.krazynews.signin_siginup.SignIn;
import com.example.krazynews.signin_siginup.SignInSignUp;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

public class Profile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //  <<,
    private Dialog dialog;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private LinearLayout profileInfo, profileSignIn;
    private Button signIn, logOut, no, yes;
    private TextView userName, userEmail, userCity;
    private Spinner languageSpinner;
    private ArrayList<String> languageList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.logout_warning_popup);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationFade;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        no = dialog.findViewById(R.id.no_button);
        yes = dialog.findViewById(R.id.yes_button);
        logOut = findViewById(R.id.log_out);
        signIn = findViewById(R.id.Sign_in);
        profileInfo = findViewById(R.id.profile_info);
        profileSignIn = findViewById(R.id.profile_sign_in);
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userCity = findViewById(R.id.user_city);
        languageSpinner = findViewById(R.id.language_spinner);

        preferences = getSharedPreferences("PREFERENCE",MODE_PRIVATE);
        String loggedIn = preferences.getString("Login","");
        String language = preferences.getString("lang","");
        languageList = new ArrayList<>();
        languageList.add("English");
        languageList.add("हिन्दी");
        languageSpinner.setAdapter(new ArrayAdapter<>(Profile.this,
                android.R.layout.simple_spinner_dropdown_item,languageList));

        if(language.equals("hindi"))
        {
            languageSpinner.setSelection(1);
        }
        else
        {
            languageSpinner.setSelection(0);
        }

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    editor = preferences.edit();
                    editor.putString("lang","english");
                    editor.apply();
                }
                else{
                    editor = preferences.edit();
                    editor.putString("lang","hindi");
                    editor.apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if(loggedIn.equals("YES"))
        {
            String name = preferences.getString("UserName","");
            String email = preferences.getString("UserEmail","");
            String city = preferences.getString("UserCity","");

            userName.setText(name);
            userEmail.setText(email);
            userCity.setText(city);

            profileSignIn.setVisibility(View.GONE);
            profileInfo.setVisibility(View.VISIBLE);
        }
        else
        {
            editor = preferences.edit();
            editor.putString("FirstTimeInstall","NO");
            editor.apply();
            profileSignIn.setVisibility(View.VISIBLE);
            profileInfo.setVisibility(View.GONE);
        }

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("UserName","");
                editor.putString("UserEmail","");
                editor.putString("UserCity","");
                editor.putString("Login","NO");
                editor.apply();
                profileSignIn.setVisibility(View.VISIBLE);
                profileInfo.setVisibility(View.GONE);
                dialog.hide();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkConnection())
                {
                    dialog.show();
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("FirstTimeInstall","NO");
                editor.apply();
                startActivity(new Intent(Profile.this, SignInSignUp.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

        ImageView imageView = findViewById(R.id.aero_icon_user_profile);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this,DashBoard.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                        dialog.show();
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
        startActivity(new Intent(Profile.this,DashBoard.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }
}
