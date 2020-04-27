package com.example.chatbox.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatbox.R;
import com.example.chatbox.pojo.UserPojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText loginemail, loginpassword;
    Button loginbutton;
    TextView forgetpassword, textviewregister;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginemail = findViewById(R.id.loginemail);
        loginpassword = findViewById(R.id.loginpassword);
        loginbutton = findViewById(R.id.loginbutton);
        forgetpassword = findViewById(R.id.forgetpassword);
        textviewregister = findViewById(R.id.textviewregister);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("userinfo");

        final Intent intent = new Intent(LoginActivity.this, dashboardActivity.class);

        final SharedPreferences sharedPreferences = getSharedPreferences("shareddata", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("status",false)==true){
            startActivity(intent);
            finish();
        }
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UserPojo userPojo = new UserPojo();
                userPojo.setEmail(loginemail.getText().toString());
                userPojo.setPassword(loginpassword.getText().toString());

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot DS) {
                        int flag = 0;
                        for (DataSnapshot data : DS.getChildren()) {
                            UserPojo datapojo = data.getValue(UserPojo.class);
                            if (userPojo.getEmail().equals(datapojo.getEmail()) && userPojo.getPassword().equals(datapojo.getPassword())) {

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("name", datapojo.getName());
                                editor.putString("number", datapojo.getPhoneno());
                                editor.putString("email", datapojo.getEmail());
                                editor.putString("password", datapojo.getPassword());
                                editor.putString("userid",datapojo.getUserid());
                                editor.putBoolean("status", true);
                                editor.apply();
                                flag = 1;
                                break;
                            }
                            Log.d("dataSnapshot", DS.toString());
                        }
                        if (flag == 1){
                            Toast.makeText(LoginActivity.this, "WELCOME USER", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        }
                        else
                            loginpassword.setError("Wrong data Entered!");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
    });
        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LoginActivity.this,forgetPasswordActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        textviewregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent2);
                finish();
            }
        });


}}