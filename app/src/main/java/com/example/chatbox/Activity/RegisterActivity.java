package com.example.chatbox.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatbox.Helper.SelectImageHelper;
import com.example.chatbox.R;
import com.example.chatbox.pojo.UserPojo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    EditText name,emailaddress,phonenumber,password;
    Button register;
    boolean isNameValid,isEmailValid,isPhoneValid,isPasswordValid;
    String userid,name1,email,phoneno,paswd;
    CircleImageView circleImageView;
    SelectImageHelper selectImageHelper;
    boolean flag = true;
    StorageReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        emailaddress = findViewById(R.id.emailaddress);
        phonenumber = findViewById(R.id.phonenumber);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        circleImageView = findViewById(R.id.circleImageView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("userinfo");
        reference = FirebaseStorage.getInstance().getReference();
        selectImageHelper = new SelectImageHelper(this,circleImageView);

        SharedPreferences sharedPreferences = getSharedPreferences("shareddata",MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        if (getSharedPreferences("shareddata",MODE_PRIVATE).getBoolean("status",false)){
            startActivity(new Intent(RegisterActivity.this,dashboardActivity.class));
            finish();
        }
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageHelper.selectImageOption();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetValidation();

                userid = databaseReference.push().getKey();
                name1 = name.getText().toString();
                email = emailaddress.getText().toString();
                phoneno = phonenumber.getText().toString();
                paswd = password.getText().toString();

                Uri uri = selectImageHelper.getURI_FOR_SELECTED_IMAGE();
                if (uri == null) {
                    flag = true;
                    Toast.makeText(RegisterActivity.this, "Error getting Image!", Toast.LENGTH_SHORT).show();
                } else if (flag == true) {
                    final StorageReference storageReference = reference.child("/image/" + email);
                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserPojo userPojo = new UserPojo();
                                    userPojo.setName(name1);
                                    userPojo.setEmail(email);
                                    userPojo.setPhoneno(phoneno);
                                    userPojo.setPassword(paswd);
                                    userPojo.setUserid(userid);
                                    userPojo.setImage(uri.toString());

                                    editor.putString("name", name1);
                                    editor.putString("email", email);
                                    editor.putString("phonenumber", phoneno);
                                    editor.putString("password", paswd);
                                    editor.putString("userid", userid);
                                    editor.putBoolean("flag", true);
                                    editor.commit();

                                    databaseReference.child(userid).setValue(userPojo);

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });


                }

            }
        });
    }

    public void SetValidation() {
        // check for a valid name
        if (name.getText().toString().isEmpty()){
            name.setError("Please Enter your name!");
            isNameValid = false;
        }
        else {
            isNameValid = true;
        }

        // check for a valid Email
        if (emailaddress.getText().toString().isEmpty()) {
            emailaddress.setError("Enter EmailAddress!");
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailaddress.getText().toString()).matches()) {
            emailaddress.setError("Enter Valid EmailAddress!");
            isEmailValid = false;
        } else  {
            isEmailValid = true;
        }

        // check for a valid number
        if (phonenumber.getText().toString().isEmpty()){
            phonenumber.setError("Enter your Mobile Number!");
            isPhoneValid = false;
        }else {
            isPhoneValid = true;
        }
        // check for valid Password
        if (password.getText().toString().isEmpty()){
            password.setError("Enter Password!");
            isPasswordValid = false;
        }else if(password.getText().length()<6){
            password.setError("Length should be greater than 6");
            isPasswordValid = false;
        }else {
            isPasswordValid = true;
        }

        if (isNameValid && isEmailValid && isPhoneValid && isPasswordValid){
            Intent intent = new Intent(RegisterActivity.this,dashboardActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(RegisterActivity.this, "Successfully Registered.Now login", Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        selectImageHelper.handleResult(requestCode, resultCode, result);
    }
    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        selectImageHelper.handleGrantedPermisson(requestCode, grantResults);       }
}
