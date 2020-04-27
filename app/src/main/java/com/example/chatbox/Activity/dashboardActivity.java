package com.example.chatbox.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.chatbox.Adapter.CustomAdapter;
import com.example.chatbox.R;
import com.example.chatbox.pojo.UserPojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
public class dashboardActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = findViewById(R.id.toolbardashboard);

        final RecyclerView recyclerView;
        FirebaseDatabase database;
        DatabaseReference databaseReference;
        final ArrayList<UserPojo> arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("userinfo");

        final SharedPreferences sharedPreferences = getSharedPreferences("shareddata",MODE_PRIVATE);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    UserPojo userPojo = dataSnapshot1.getValue(UserPojo.class);
                    if (!userPojo.getEmail().equals(sharedPreferences.getString("email",null))){
                        arrayList.add(userPojo);
                    }
                }
                CustomAdapter customAdapter = new CustomAdapter(arrayList,dashboardActivity.this);
                recyclerView.setAdapter(customAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(dashboardActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_activity_menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.viewProfileMenu:
                Intent intent = new Intent(dashboardActivity.this,ViewProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.changePasswordMenu:
                Intent intent1 = new Intent(dashboardActivity.this,ChangePasswordActivity.class);
                startActivity(intent1);
                break;
            case R.id.logoutMenu:
                Intent intent2 = new Intent(dashboardActivity.this,LoginActivity.class);
                SharedPreferences sharedPreferences = getSharedPreferences("shareddata",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                startActivity(intent2);
                finish();
                break;
        }
        return true;

    }


}
