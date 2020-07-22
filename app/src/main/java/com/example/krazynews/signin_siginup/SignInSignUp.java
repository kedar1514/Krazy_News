package com.example.krazynews.signin_siginup;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.krazynews.MainActivity;
import com.example.krazynews.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignInSignUp extends AppCompatActivity {
    private Button sign_in;
    private Button register;
    private ImageView imageView;
    private TextView textView, skip;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_signin);

        SharedPreferences preferences = getSharedPreferences("PREFERENCE",MODE_PRIVATE);
        String firstTime = preferences.getString("FirstTimeInstall","");

        if(firstTime.equals("Yes"))
        {
            startActivity(new Intent(SignInSignUp.this,MainActivity.class));
            finish();
        }
        else{
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("FirstTimeInstall","Yes");
            editor.apply();
        }

        skip = findViewById(R.id.skip);
        sign_in = findViewById(R.id.sign_in_page);
        register = findViewById(R.id.sign_up_page);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.info_text);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInSignUp.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View, String>(imageView,"main_image");
                pairs[1] = new Pair<View, String>(textView,"random_text");
                pairs[2] = new Pair<View, String>(register,"fixed_button");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignInSignUp.this,pairs);
                    startActivity(new Intent(SignInSignUp.this, SignIn.class),options.toBundle());
                }
                else{
                    startActivity(new Intent(SignInSignUp.this, SignIn.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                }
            }
        });
//  <<,
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View, String>(imageView,"main_image");
                pairs[1] = new Pair<View, String>(textView,"random_text");
                pairs[2] = new Pair<View, String>(register,"fixed_button");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignInSignUp.this,pairs);
                    startActivity(new Intent(SignInSignUp.this, RegistrationName.class),options.toBundle());
                }
                else{
                    startActivity(new Intent(SignInSignUp.this, RegistrationName.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                }
            }
        });
    }
}
