package com.greemlock.ivApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.greemlock.ivApp.Objects.SaveSharedPreferences;
import com.greemlock.ivApp.Objects.User;
import com.greemlock.ivApp.Objects.deviceList;

import java.io.File;

public class ivAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iv_account);

        User user = (User) getIntent().getSerializableExtra("user");

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

        ImageButton ib_back = findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accounttomain = new Intent(ivAccount.this,MainScreen.class);
                accounttomain.putExtra("user",user);
                startActivity(accounttomain);
            }
        });

        try {
            File f_profile_photo = File.createTempFile("images","jpg");
            sr_profile_photo.getFile(f_profile_photo).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    String s_file_path = f_profile_photo.getPath();
                    Bitmap bm = BitmapFactory.decodeFile(s_file_path);
                    iv_profile_photo.setImageBitmap(bm);
                }
            });

        }
        catch (Exception e){}

        TextView tv_name = findViewById(R.id.tv_name);
        TextView tv_surname = findViewById(R.id.tv_surname);
        TextView tv_age = findViewById(R.id.tv_age);



        tv_name.setText(user.getUser_name());
        tv_surname.setText(user.getUser_surname());
        tv_age.setText(String.valueOf(user.getUser_age()));



        Button b_add_device = findViewById(R.id.b_add_device);
        b_add_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accounttodevicelist = new Intent(ivAccount.this, deviceList.class);
                accounttodevicelist.putExtra("user",user);
                startActivity(accounttodevicelist);
            }
        });

        Button b_add_offer = findViewById(R.id.b_add_offer);

        if(!user.isUser_is_admin()){
            b_add_offer.setVisibility(View.GONE);
        }
        b_add_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accounttooffer = new Intent(ivAccount.this,AddOffer.class);
                accounttooffer.putExtra("user",user);
                startActivity(accounttooffer);
            }
        });

        ImageView iv_device_photo   = findViewById(R.id.iv_device_photo);
        TextView  tv_device_address = findViewById(R.id.tv_device_address);

        if(user.getUser_device() == ""){
            iv_device_photo.setVisibility(View.GONE);

        }
        else{
            iv_device_photo.setImageResource(R.drawable.ic_baseline_perm_device_information_24);
            tv_device_address.setText(user.getUser_device());
        }

    }
}