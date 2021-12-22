package com.greemlock.ivApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.greemlock.ivApp.Objects.SaveSharedPreferences;
import com.greemlock.ivApp.Objects.User;
import com.greemlock.ivApp.Objects.deviceList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabase fb_database   = FirebaseDatabase.getInstance();
        DatabaseReference db_reference  = fb_database.getReference("users");

        if (SaveSharedPreferences.getPrefUserId(MainActivity.this) != ""){

            setContentView(R.layout.activity_please_wait);

            Toast.makeText(this,R.string.sign_in_automatically , Toast.LENGTH_SHORT).show();
            Query q_find_user = db_reference.orderByChild("user_key").equalTo(SaveSharedPreferences.getPrefUserId(MainActivity.this));
            q_find_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot ds_result) {
                    for(DataSnapshot ds:ds_result.getChildren()){
                        User user = ds.getValue(User.class);
                        if(user.getUser_device().equals("")) {
                            Intent intent_first_to_device_list = new Intent(MainActivity.this, deviceList.class);
                            intent_first_to_device_list.putExtra("user", user);
                            startActivity(intent_first_to_device_list);
                            break;
                        }
                        else{
                            Intent intent_first_to_main = new Intent(MainActivity.this, Bluetooth.class);
                            intent_first_to_main.putExtra("user",user);
                            intent_first_to_main.putExtra("address", user.getUser_device());
                            intent_first_to_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startService(intent_first_to_main);
                            break;
                        }

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
        else{
            setContentView(R.layout.activity_main);

            Button b_sign_in = findViewById(R.id.b_sign_in);
            b_sign_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent maintosignin = new Intent(MainActivity.this,SignIn.class);
                    startActivity(maintosignin);
                }
            });
            Button b_sign_up = findViewById(R.id.b_sign_up);
            b_sign_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent maintosignup = new Intent(MainActivity.this,SignUp.class);
                    startActivity(maintosignup);
                }
            });
        }
    }
}