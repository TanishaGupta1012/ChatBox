package com.example.chatbox.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatbox.Adapter.MessageAdapter;
import com.example.chatbox.R;
import com.example.chatbox.pojo.MessagePojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomActivity extends AppCompatActivity {
    CircleImageView imageProfile;
    TextView textViewName;
    EditText editTextMessage;
    ImageView imageSend;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    String senderId,receiverId,name,imageUrl;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    ArrayList<MessagePojo> arrayList;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        imageProfile = findViewById(R.id.imageProfile);
        textViewName = findViewById(R.id.textViewName);
        editTextMessage = findViewById(R.id.editTextMessage);
        imageSend = findViewById(R.id.imageSend);
        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("messageinfo");
        sharedPreferences = getSharedPreferences("shareddata",MODE_PRIVATE);

        senderId = sharedPreferences.getString("id",null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        imageUrl = intent.getStringExtra("imageUrl");
        receiverId = intent.getStringExtra("id");

        textViewName.setText(name);
        Glide.with(this).load(imageUrl).into(imageProfile);
        getFirebaseData();

        imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString();
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);

                String time = hour+":"+min;
                MessagePojo messagePojo = new MessagePojo();
                messagePojo.setMessage(message);
                messagePojo.setSenderId(senderId);
                messagePojo.setReceiverId(receiverId);
                messagePojo.setTime(time);
                databaseReference.push().setValue(messagePojo);
                editTextMessage.setText(null);
            }
        });
    }
    public void getFirebaseData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    MessagePojo messagePojo = dataSnapshot1.getValue(MessagePojo.class);
                    if ((senderId.equals(messagePojo.getSenderId()) && receiverId.equals(messagePojo.getReceiverId())) || (senderId.equals(messagePojo.getReceiverId()) && receiverId.equals(messagePojo.getSenderId()))){
                        arrayList.add(messagePojo);
                    }
                }
                MessageAdapter adapter = new MessageAdapter(arrayList,ChatRoomActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatRoomActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.home){

        }
return true;
    }
}
