package com.example.krazynews.signin_siginup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.krazynews.R;

import org.w3c.dom.Text;

public class RegistrationName extends AppCompatActivity {
    //  <<,
    private Button next;
    private ImageView imageView;
    private TextView bold_text,name_warning;
    private LinearLayout long_text;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_name);

        next = findViewById(R.id.next);
        imageView = findViewById(R.id.imageView);
        bold_text = findViewById(R.id.bold_text);
        long_text = findViewById(R.id.long_text);
        editText = findViewById(R.id.edit_text);
        name_warning = findViewById(R.id.empty_name);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View, String>(bold_text,"bold");
                pairs[1] = new Pair<View, String>(next,"fixed_button");
                pairs[2] = new Pair<View, String>(editText,"edit_text");
                pairs[3] = new Pair<View, String>(long_text,"long_text");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                    if(editText.getText().toString().trim().isEmpty())
                    {
                        name_warning.setText("Name field can't be empty");
                    }
                    else
                    {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegistrationName.this,pairs);
                        Intent intent = new Intent(RegistrationName.this, CityInput.class);
                        intent.putExtra("name", editText.getText().toString().trim());
                        startActivity(intent,options.toBundle());
                        finish();
                    }
                }
                else{

                    if(editText.getText().toString().trim().isEmpty())
                    {
                        name_warning.setText("Name field can't be empty");
                    }
                    else
                    {
                        startActivity(new Intent(RegistrationName.this, CityInput.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                    }
                }
            }
        });
    }
}
