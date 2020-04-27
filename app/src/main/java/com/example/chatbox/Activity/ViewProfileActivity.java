package com.example.chatbox.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.chatbox.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfileActivity extends AppCompatActivity {
    CircleImageView imageView;
    TextView nameView,emailView,numberView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        imageView = findViewById(R.id.imageView);
        nameView = findViewById(R.id.nameView);
        emailView = findViewById(R.id.emailView);
        numberView = findViewById(R.id.numberView);

        SharedPreferences sharedPreferences = getSharedPreferences("shareddata",MODE_PRIVATE);
        String name = sharedPreferences.getString("name",null),
                email = sharedPreferences.getString("email",null),
                phoneno = sharedPreferences.getString("phonenumber",null);

        nameView.setText("Name : "+name);
        emailView.setText("EmailAddress : "+email);
        numberView.setText("Phone : "+phoneno);
    }
}
