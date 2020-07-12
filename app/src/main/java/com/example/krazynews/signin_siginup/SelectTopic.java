package com.example.krazynews.signin_siginup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.krazynews.MainActivity;
import com.example.krazynews.R;
//  <<,
public class SelectTopic extends AppCompatActivity {
    private Button next;
    private String name, city, user_email;
    private boolean l1=false,l2=false,l3=false,l4=false,l5=false,l6=false,l7=false,l8=false,l9=false;
    private ImageView check;
    private LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearLayout5, linearLayout6, linearLayout7, linearLayout8, linearLayout9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_topic);
        Intent i = getIntent();
        name = i.getStringExtra("name");
        city = i.getStringExtra("city");
        user_email = i.getStringExtra("email");
        next = findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectTopic.this, MainActivity.class));
                finish();
            }
        });

        linearLayout1 = findViewById(R.id.linear_layout1);
        linearLayout2 = findViewById(R.id.linear_layout2);
        linearLayout3 = findViewById(R.id.linear_layout3);
        linearLayout4 = findViewById(R.id.linear_layout4);
        linearLayout5 = findViewById(R.id.linear_layout5);
        linearLayout6 = findViewById(R.id.linear_layout6);
        linearLayout7 = findViewById(R.id.linear_layout7);
        linearLayout8 = findViewById(R.id.linear_layout8);
        linearLayout9 = findViewById(R.id.linear_layout9);

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = findViewById(R.id.check1);
                if(!l1)
                {
                    check.setVisibility(View.VISIBLE);
                    l1=true;
                }
                else
                {
                    check.setVisibility(View.GONE);
                    l1=false;
                }
            }
        });

        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = findViewById(R.id.check2);
                if(!l2)
                {
                    check.setVisibility(View.VISIBLE);
                    l2=true;
                }
                else
                {
                    check.setVisibility(View.GONE);
                    l2=false;
                }
            }
        });

        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = findViewById(R.id.check3);
                if(!l3)
                {
                    check.setVisibility(View.VISIBLE);
                    l3=true;
                }
                else
                {
                    check.setVisibility(View.GONE);
                    l3=false;
                }
            }
        });

        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = findViewById(R.id.check4);
                if(!l4)
                {
                    check.setVisibility(View.VISIBLE);
                    l4=true;
                }
                else
                {
                    check.setVisibility(View.GONE);
                    l4=false;
                }
            }
        });

        linearLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = findViewById(R.id.check5);
                if(!l5)
                {
                    check.setVisibility(View.VISIBLE);
                    l5=true;
                }
                else
                {
                    check.setVisibility(View.GONE);
                    l5=false;
                }
            }
        });

        linearLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = findViewById(R.id.check6);
                if(!l6)
                {
                    check.setVisibility(View.VISIBLE);
                    l6=true;
                }
                else
                {
                    check.setVisibility(View.GONE);
                    l6=false;
                }
            }
        });

        linearLayout7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = findViewById(R.id.check7);
                if(!l7)
                {
                    check.setVisibility(View.VISIBLE);
                    l7=true;
                }
                else
                {
                    check.setVisibility(View.GONE);
                    l7=false;
                }
            }
        });

        linearLayout8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = findViewById(R.id.check8);
                if(!l8)
                {
                    check.setVisibility(View.VISIBLE);
                    l8=true;
                }
                else
                {
                    check.setVisibility(View.GONE);
                    l8=false;
                }
            }
        });

        linearLayout9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = findViewById(R.id.check9);
                if(!l9)
                {
                    check.setVisibility(View.VISIBLE);
                    l9=true;
                }
                else
                {
                    check.setVisibility(View.GONE);
                    l9=false;
                }
            }
        });
    }
}
