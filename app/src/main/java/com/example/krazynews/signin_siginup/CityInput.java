package com.example.krazynews.signin_siginup;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.krazynews.MainActivity;
import com.example.krazynews.R;
//  <<,
public class CityInput extends AppCompatActivity {
    private Button next;
    private EditText editText;
    private LinearLayout pune,mumbai,delhi, chennai,lucknow,jaipur,bangalore,agra,chandigarh, long_text;
    private TextView pune_text,mumbai_text,delhi_text, chennai_text,lucknow_text,jaipur_text,bangalore_text,agra_text,chandigarh_text, empty_city;
    private TextView bold_text;
    private GridLayout main_image;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_input);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        //transition elemnts
        next = findViewById(R.id.next);
        bold_text = findViewById(R.id.bold);
        editText = findViewById(R.id.city_name);
        long_text = findViewById(R.id.longText);
        main_image = findViewById(R.id.category_block);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim().isEmpty())
                {
                    empty_city.setText("City field can't be empty.");
                }
                else{
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                        Pair[] pairs = new Pair[5];
                        pairs[0] = new Pair<View, String>(bold_text,"bold");
                        pairs[1] = new Pair<View, String>(next,"fixed_button");
                        pairs[2] = new Pair<View, String>(editText,"edit_text");
                        pairs[3] = new Pair<View, String>(long_text,"long_text");
                        pairs[4] = new Pair<View, String>(main_image,"main_image");

                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CityInput.this,pairs);
                        Intent i = new Intent(CityInput.this, GetEmail.class);
                        i.putExtra("name",name);
                        i.putExtra("city",editText.getText().toString().trim());
                        startActivity(i,options.toBundle());
                        finish();
                    }
                    else{
                        startActivity(new Intent(CityInput.this, GetEmail.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                    }
                }
            }
        });

        pune = findViewById(R.id.pune);
        mumbai = findViewById(R.id.mumbai);
        delhi = findViewById(R.id.delhi);
        chennai = findViewById(R.id.chennai);
        lucknow = findViewById(R.id.lucknow);
        jaipur = findViewById(R.id.jaipur);
        bangalore = findViewById(R.id.banglore);
        agra = findViewById(R.id.agra);
        chandigarh = findViewById(R.id.chandigarh);

        empty_city =findViewById(R.id.empty_city);
        pune_text = findViewById(R.id.pune_text);
        mumbai_text = findViewById(R.id.mumbai_text);
        delhi_text = findViewById(R.id.delhi_text);
        chennai_text = findViewById(R.id.chennai_text);
        lucknow_text = findViewById(R.id.lucknow_text);
        jaipur_text = findViewById(R.id.jaipur_text);
        bangalore_text = findViewById(R.id.banglore_text);
        agra_text = findViewById(R.id.agra_text);
        chandigarh_text = findViewById(R.id.chandigarh_text);

        pune.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pune.setBackgroundResource(R.drawable.buttons);
                pune_text.setTextColor(0xFFFFFFFF);

                mumbai.setBackgroundResource(R.drawable.city_background);
                mumbai_text.setTextColor(0xFFFD676C);

                delhi.setBackgroundResource(R.drawable.city_background);
                delhi_text.setTextColor(0xFFFD676C);

                chennai.setBackgroundResource(R.drawable.city_background);
                chennai_text.setTextColor(0xFFFD676C);

                lucknow.setBackgroundResource(R.drawable.city_background);
                lucknow_text.setTextColor(0xFFFD676C);

                jaipur.setBackgroundResource(R.drawable.city_background);
                jaipur_text.setTextColor(0xFFFD676C);

                bangalore.setBackgroundResource(R.drawable.city_background);
                bangalore_text.setTextColor(0xFFFD676C);

                agra.setBackgroundResource(R.drawable.city_background);
                agra_text.setTextColor(0xFFFD676C);

                chandigarh.setBackgroundResource(R.drawable.city_background);
                chandigarh_text.setTextColor(0xFFFD676C);

                editText.setText("pune");
            }
        });

        mumbai.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pune.setBackgroundResource(R.drawable.city_background);
                pune_text.setTextColor(0xFFFD676C);

                mumbai.setBackgroundResource(R.drawable.buttons);
                mumbai_text.setTextColor(0xFFFFFFFF);

                delhi.setBackgroundResource(R.drawable.city_background);
                delhi_text.setTextColor(0xFFFD676C);

                chennai.setBackgroundResource(R.drawable.city_background);
                chennai_text.setTextColor(0xFFFD676C);

                lucknow.setBackgroundResource(R.drawable.city_background);
                lucknow_text.setTextColor(0xFFFD676C);

                jaipur.setBackgroundResource(R.drawable.city_background);
                jaipur_text.setTextColor(0xFFFD676C);

                bangalore.setBackgroundResource(R.drawable.city_background);
                bangalore_text.setTextColor(0xFFFD676C);

                agra.setBackgroundResource(R.drawable.city_background);
                agra_text.setTextColor(0xFFFD676C);

                chandigarh.setBackgroundResource(R.drawable.city_background);
                chandigarh_text.setTextColor(0xFFFD676C);
                editText.setText("mumbai");
            }
        });

        delhi.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pune.setBackgroundResource(R.drawable.city_background);
                pune_text.setTextColor(0xFFFD676C);

                mumbai.setBackgroundResource(R.drawable.city_background);
                mumbai_text.setTextColor(0xFFFD676C);

                delhi.setBackgroundResource(R.drawable.buttons);
                delhi_text.setTextColor(0XFFFFFFFF);

                chennai.setBackgroundResource(R.drawable.city_background);
                chennai_text.setTextColor(0xFFFD676C);

                lucknow.setBackgroundResource(R.drawable.city_background);
                lucknow_text.setTextColor(0xFFFD676C);

                jaipur.setBackgroundResource(R.drawable.city_background);
                jaipur_text.setTextColor(0xFFFD676C);

                bangalore.setBackgroundResource(R.drawable.city_background);
                bangalore_text.setTextColor(0xFFFD676C);

                agra.setBackgroundResource(R.drawable.city_background);
                agra_text.setTextColor(0xFFFD676C);

                chandigarh.setBackgroundResource(R.drawable.city_background);
                chandigarh_text.setTextColor(0xFFFD676C);
                editText.setText("delhi");
            }
        });

        chennai.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pune.setBackgroundResource(R.drawable.city_background);
                pune_text.setTextColor(0xFFFD676C);

                mumbai.setBackgroundResource(R.drawable.city_background);
                mumbai_text.setTextColor(0xFFFD676C);

                delhi.setBackgroundResource(R.drawable.city_background);
                delhi_text.setTextColor(0xFFFD676C);

                chennai.setBackgroundResource(R.drawable.buttons);
                chennai_text.setTextColor(0xFFFFFFFF);

                lucknow.setBackgroundResource(R.drawable.city_background);
                lucknow_text.setTextColor(0xFFFD676C);

                jaipur.setBackgroundResource(R.drawable.city_background);
                jaipur_text.setTextColor(0xFFFD676C);

                bangalore.setBackgroundResource(R.drawable.city_background);
                bangalore_text.setTextColor(0xFFFD676C);

                agra.setBackgroundResource(R.drawable.city_background);
                agra_text.setTextColor(0xFFFD676C);

                chandigarh.setBackgroundResource(R.drawable.city_background);
                chandigarh_text.setTextColor(0xFFFD676C);

                editText.setText("chennai");
            }
        });

        lucknow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pune.setBackgroundResource(R.drawable.city_background);
                pune_text.setTextColor(0xFFFD676C);

                mumbai.setBackgroundResource(R.drawable.city_background);
                mumbai_text.setTextColor(0xFFFD676C);

                delhi.setBackgroundResource(R.drawable.city_background);
                delhi_text.setTextColor(0xFFFD676C);

                chennai.setBackgroundResource(R.drawable.city_background);
                chennai_text.setTextColor(0xFFFD676C);

                lucknow.setBackgroundResource(R.drawable.buttons);
                lucknow_text.setTextColor(0xFFFFFFFF);

                jaipur.setBackgroundResource(R.drawable.city_background);
                jaipur_text.setTextColor(0xFFFD676C);

                bangalore.setBackgroundResource(R.drawable.city_background);
                bangalore_text.setTextColor(0xFFFD676C);

                agra.setBackgroundResource(R.drawable.city_background);
                agra_text.setTextColor(0xFFFD676C);

                chandigarh.setBackgroundResource(R.drawable.city_background);
                chandigarh_text.setTextColor(0xFFFD676C);

                editText.setText("lucknow");
            }
        });

        jaipur.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pune.setBackgroundResource(R.drawable.city_background);
                pune_text.setTextColor(0xFFFD676C);

                mumbai.setBackgroundResource(R.drawable.city_background);
                mumbai_text.setTextColor(0xFFFD676C);

                delhi.setBackgroundResource(R.drawable.city_background);
                delhi_text.setTextColor(0xFFFD676C);

                chennai.setBackgroundResource(R.drawable.city_background);
                chennai_text.setTextColor(0xFFFD676C);

                lucknow.setBackgroundResource(R.drawable.city_background);
                lucknow_text.setTextColor(0xFFFD676C);

                jaipur.setBackgroundResource(R.drawable.buttons);
                jaipur_text.setTextColor(0xFFFFFFFF);

                bangalore.setBackgroundResource(R.drawable.city_background);
                bangalore_text.setTextColor(0xFFFD676C);

                agra.setBackgroundResource(R.drawable.city_background);
                agra_text.setTextColor(0xFFFD676C);

                chandigarh.setBackgroundResource(R.drawable.city_background);
                chandigarh_text.setTextColor(0xFFFD676C);

                editText.setText("jaipur");
            }
        });

        bangalore.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pune.setBackgroundResource(R.drawable.city_background);
                pune_text.setTextColor(0xFFFD676C);

                mumbai.setBackgroundResource(R.drawable.city_background);
                mumbai_text.setTextColor(0xFFFD676C);

                delhi.setBackgroundResource(R.drawable.city_background);
                delhi_text.setTextColor(0xFFFD676C);

                chennai.setBackgroundResource(R.drawable.city_background);
                chennai_text.setTextColor(0xFFFD676C);

                lucknow.setBackgroundResource(R.drawable.city_background);
                lucknow_text.setTextColor(0xFFFD676C);

                jaipur.setBackgroundResource(R.drawable.city_background);
                jaipur_text.setTextColor(0xFFFD676C);

                bangalore.setBackgroundResource(R.drawable.buttons);
                bangalore_text.setTextColor(0xFFFFFFFF);

                agra.setBackgroundResource(R.drawable.city_background);
                agra_text.setTextColor(0xFFFD676C);

                chandigarh.setBackgroundResource(R.drawable.city_background);
                chandigarh_text.setTextColor(0xFFFD676C);

                editText.setText("bangalore");
            }
        });

        agra.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pune.setBackgroundResource(R.drawable.city_background);
                pune_text.setTextColor(0xFFFD676C);

                mumbai.setBackgroundResource(R.drawable.city_background);
                mumbai_text.setTextColor(0xFFFD676C);

                delhi.setBackgroundResource(R.drawable.city_background);
                delhi_text.setTextColor(0xFFFD676C);

                chennai.setBackgroundResource(R.drawable.city_background);
                chennai_text.setTextColor(0xFFFD676C);

                lucknow.setBackgroundResource(R.drawable.city_background);
                lucknow_text.setTextColor(0xFFFD676C);

                jaipur.setBackgroundResource(R.drawable.city_background);
                jaipur_text.setTextColor(0xFFFD676C);

                bangalore.setBackgroundResource(R.drawable.city_background);
                bangalore_text.setTextColor(0xFFFD676C);

                agra.setBackgroundResource(R.drawable.buttons);
                agra_text.setTextColor(0xFFFFFFFF);

                chandigarh.setBackgroundResource(R.drawable.city_background);
                chandigarh_text.setTextColor(0xFFFD676C);

                editText.setText("agra");
            }
        });

        chandigarh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pune.setBackgroundResource(R.drawable.city_background);
                pune_text.setTextColor(0xFFFD676C);

                mumbai.setBackgroundResource(R.drawable.city_background);
                mumbai_text.setTextColor(0xFFFD676C);

                delhi.setBackgroundResource(R.drawable.city_background);
                delhi_text.setTextColor(0xFFFD676C);

                chennai.setBackgroundResource(R.drawable.city_background);
                chennai_text.setTextColor(0xFFFD676C);

                lucknow.setBackgroundResource(R.drawable.city_background);
                lucknow_text.setTextColor(0xFFFD676C);

                jaipur.setBackgroundResource(R.drawable.city_background);
                jaipur_text.setTextColor(0xFFFD676C);

                bangalore.setBackgroundResource(R.drawable.city_background);
                bangalore_text.setTextColor(0xFFFD676C);

                agra.setBackgroundResource(R.drawable.city_background);
                agra_text.setTextColor(0xFFFD676C);

                chandigarh.setBackgroundResource(R.drawable.buttons);
                chandigarh_text.setTextColor(0xFFFFFFFF);

                editText.setText("chandigarh");
            }
        });
    }
}
