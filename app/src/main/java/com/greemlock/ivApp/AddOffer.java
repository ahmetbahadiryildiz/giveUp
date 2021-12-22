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
import com.greemlock.ivApp.Objects.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddOffer extends AppCompatActivity {

    private static final int gallery_pick = 31;
    Uri offer_company_photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);

        User user = (User) getIntent().getSerializableExtra("user");

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

        Button b_add_offer = findViewById(R.id.b_add_offer);
        b_add_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et_offer_company_name = findViewById(R.id.et_offer_company_name);
                EditText et_offer              = findViewById(R.id.et_offer);
                EditText et_offer_price        = findViewById(R.id.et_offer_price);
                EditText et_offer_code         = findViewById(R.id.et_offer_code);

                String o_company_name       = et_offer_company_name.getText().toString();
                String o_description        = et_offer.getText().toString();
                String o_code               = et_offer_code.getText().toString();
                int    o_price              = Integer.parseInt(et_offer_price.getText().toString());
                String o_company_photo_name = UUID.randomUUID().toString();


                Offer new_offer = new Offer("",o_company_name,o_description,o_price,o_code,o_company_photo_name);

                if (offer_company_photo != null){

                    FirebaseStorage fb_storage   = FirebaseStorage.getInstance();
                    StorageReference s_reference = fb_storage.getReference();
                    StorageReference s_profile   = s_reference.child("offers/" + o_company_photo_name);
                    s_profile.putFile(offer_company_photo);

                }

                FirebaseDatabase fb_database  = FirebaseDatabase.getInstance();
                DatabaseReference dr           = fb_database.getReference("offers");

                dr.push().setValue(new_offer);

                Query q_set_user_key = dr.orderByChild("offer_description").equalTo(o_description);

                q_set_user_key.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot ds_result) {

                        for(DataSnapshot ds:ds_result.getChildren()){

                            String             offer_id  = ds.getKey();
                            Map<String,Object> m_set_key = new HashMap<>();

                            m_set_key.put("offer_id",offer_id);
                            dr.child(offer_id).updateChildren(m_set_key);
                            break;

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
                Toast.makeText(AddOffer.this, "Teklif Eklendi!", Toast.LENGTH_SHORT).show();

                Intent add_offer_to_account = new Intent(AddOffer.this,ivAccount.class);
                add_offer_to_account.putExtra("user",user);
                startActivity(add_offer_to_account);

            }
        });
    }
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

                offer_company_photo = result.getUri();
                ImageView iv_offer_company_photo = findViewById(R.id.iv_offer_company_photo);
                iv_offer_company_photo.setImageURI(offer_company_photo);

            }
        }
    }

}