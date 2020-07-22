package com.example.krazynews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.krazynews.signin_siginup.SignIn;
import com.example.krazynews.signin_siginup.SignInSignUp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

public class Profile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //  <<,
    private Dialog dialog;
    private SharedPreferences preferences;
    private LinearLayout profileInfo, profileSignIn;
    private Button signIn, logOut, no, yes;
    private TextView userName, userEmail, userCity;
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

        preferences = getSharedPreferences("PREFERENCE",MODE_PRIVATE);
        String loggedIn = preferences.getString("Login","");

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
            SharedPreferences.Editor editor = preferences.edit();
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
                dialog.show();
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

        Spinner spinner = findViewById(R.id.language_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.language,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
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

//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Profile.this,DashBoard.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }
}
