package com.greemlock.ivApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.greemlock.ivApp.Objects.Offer;
import com.greemlock.ivApp.Objects.Punishment;
import com.greemlock.ivApp.Objects.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class SignUp extends AppCompatActivity {

    private static final int gallery_pick = 31;
    Uri profile_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button b_add_photo = findViewById(R.id.b_add_photo);
        b_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent select_photo = new Intent();
                select_photo.setAction(Intent.ACTION_GET_CONTENT);
                select_photo.setType("image/*");
                startActivityForResult(select_photo,gallery_pick);

            }
        });

        Button b_sign_up = findViewById(R.id.b_sign_up);
        b_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et_name      = findViewById(R.id.et_name);
                EditText et_surname   = findViewById(R.id.et_surname);
                EditText et_age       = findViewById(R.id.et_age);
                EditText et_user_name = findViewById(R.id.et_username);
                EditText et_password  = findViewById(R.id.et_password);

                String u_name      = et_name.getText().toString();
                String u_surname   = et_surname.getText().toString();
                int    u_age       = Integer.parseInt(et_age.getText().toString());
                String u_user_name = et_user_name.getText().toString();
                String u_password  = et_password.getText().toString();

                if (u_user_name != null && u_password != null){

                    String                u_pp_name    = UUID.randomUUID().toString();
                    ArrayList<Punishment> u_punishment = new ArrayList<>();
                    Punishment p = new Punishment(0,0,0,0);
                    u_punishment.add(p);

                    FirebaseDatabase  fb_database = FirebaseDatabase.getInstance();
                    DatabaseReference dr_offers   = fb_database.getReference("offers");
                    ArrayList<Offer>  al_offers   = new ArrayList<>();

                    dr_offers.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()){

                                Offer offer = ds.getValue(Offer.class);
                                al_offers.add(offer);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    if (profile_photo != null){

                        FirebaseStorage fb_storage   = FirebaseStorage.getInstance();
                        StorageReference s_reference = fb_storage.getReference();
                        StorageReference s_profile   = s_reference.child("users/" + u_pp_name);
                        s_profile.putFile(profile_photo);

                    }


                    DatabaseReference dr_users   = fb_database.getReference("users");
                    Random            randomizer = new Random();

                    User new_user = new User("",u_name,u_surname,u_age,u_user_name,u_password,u_pp_name,0,u_punishment,al_offers.get(randomizer.nextInt(al_offers.size())).getOffer_id(),al_offers.get(randomizer.nextInt(al_offers.size())).getOffer_id(),al_offers.get(randomizer.nextInt(al_offers.size())).getOffer_id(),"",false);
                    dr_users.push().setValue(new_user);

                    Query q_set_user_key = dr_users.orderByChild("user_user_name").equalTo(u_user_name);

                    q_set_user_key.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot ds_result) {

                            for(DataSnapshot ds:ds_result.getChildren()){

                                String             user_key  = ds.getKey();
                                Map<String,Object> m_set_key = new HashMap<>();

                                m_set_key.put("user_key",user_key);
                                dr_users.child(user_key).updateChildren(m_set_key);

                                Toast.makeText(SignUp.this, R.string.sign_up_successfully, Toast.LENGTH_SHORT).show();

                                break;
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });


                }
                else{
                    Toast.makeText(SignUp.this, R.string.sign_up_error, Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {


        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == gallery_pick && resultCode == RESULT_OK && data != null){

            Uri photo_uri = data.getData();

            CropImage.activity(photo_uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(result != null){

                profile_photo = result.getUri();
                ImageView iv_profile_photo = findViewById(R.id.iv_profile_photo);
                iv_profile_photo.setImageURI(profile_photo);

            }
        }
    }
}