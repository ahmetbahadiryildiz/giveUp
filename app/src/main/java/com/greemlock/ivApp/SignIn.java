package com.greemlock.ivApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button b_sign_in = findViewById(R.id.b_sign_in);
        b_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et_user_name = findViewById(R.id.et_username);
                EditText et_password  = findViewById(R.id.et_password);

                String t_user_name = et_user_name.getText().toString();
                String t_password  = et_password.getText().toString();

                FirebaseDatabase fb_database  = FirebaseDatabase.getInstance();
                DatabaseReference dr           = fb_database.getReference("users");

                Query q_find_user = dr.orderByChild("user_user_name").equalTo(t_user_name);
                q_find_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot ds_result) {

                        for (DataSnapshot ds : ds_result.getChildren()) {
                            User is_user = ds.getValue(User.class);
                            SaveSharedPreferences.setPrefUserId(SignIn.this, is_user.getUser_key());

                            if (t_password.equals(is_user.getUser_password())) {

                                Toast.makeText(SignIn.this, R.string.sign_in_successfully, Toast.LENGTH_SHORT).show();
                                SaveSharedPreferences.setPrefUserId(SignIn.this, is_user.getUser_key());

                                if (is_user.getUser_device().equals("")) {
                                    Intent intent_sign_in_to_device_list = new Intent(SignIn.this, deviceList.class);
                                    intent_sign_in_to_device_list.putExtra("user", is_user);
                                    startActivity(intent_sign_in_to_device_list);
                                } else {
                                    Intent serviceIntent = new Intent(SignIn.this, Bluetooth.class);
                                    serviceIntent.putExtra("user", is_user);
                                    serviceIntent.putExtra("address", is_user.getUser_device());
                                    serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startService(serviceIntent);
                                }
                                break;

                            } else {
                                Toast.makeText(SignIn.this, "Failed!", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("test",error.getDetails());
                    }
                });
            }
        });
    }
}