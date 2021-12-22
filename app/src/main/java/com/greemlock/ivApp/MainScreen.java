package com.greemlock.ivApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.greemlock.ivApp.Objects.SaveSharedPreferences;
import com.greemlock.ivApp.Objects.User;

import java.io.File;

public class MainScreen extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        User user = (User) getIntent().getSerializableExtra("user");

        TextView tv_ivpara  = findViewById(R.id.tv_ivpara);
        tv_ivpara.setText(String.valueOf(user.getUser_iv_para()));

        ImageView iv_profile_photo = findViewById(R.id.iv_profile_photo);

        StorageReference sr_firebase = FirebaseStorage.getInstance().getReference();
        StorageReference sr_profile_photo = sr_firebase.child("users/" + user.getUser_profile_photo_name());


        ImageView iv_bluetooth = findViewById(R.id.iv_bluetooth);
        if (SaveSharedPreferences.getPrefIsBluetooth(this)){
            iv_bluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_connected_24);
        }
        else{
            iv_bluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_24);
        }



        Button b_sign_out = findViewById(R.id.b_sign_out);
        b_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveSharedPreferences.setPrefUserId(MainScreen.this,"");
                SaveSharedPreferences.setPrefIsBluetooth(MainScreen.this,false);
                stopService(new Intent(MainScreen.this, Bluetooth.class));

                Intent intent_main_to_first = new Intent(MainScreen.this,MainActivity.class);
                startActivity(intent_main_to_first);
                finish();

            }
        });

        Button b_iv_calendar = findViewById(R.id.b_iv_calender);
        b_iv_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent maintocalendar = new Intent(MainScreen.this,ivCalendar.class);
                maintocalendar.putExtra("user",user);
                startActivity(maintocalendar);

            }
        });

        Button b_iv_offer = findViewById(R.id.b_iv_offer);
        b_iv_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maintooffer = new Intent(MainScreen.this,ivOffer.class);
                maintooffer.putExtra("user",user);
                startActivity(maintooffer);
            }
        });

        Button b_iv_account = findViewById(R.id.b_iv_account);
        b_iv_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maintoaccount = new Intent(MainScreen.this,ivAccount.class);
                maintoaccount.putExtra("user",user);
                startActivity(maintoaccount);
            }
        });

    }
}