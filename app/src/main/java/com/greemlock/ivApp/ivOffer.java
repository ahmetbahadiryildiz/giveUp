package com.greemlock.ivApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.greemlock.ivApp.Objects.Offer;
import com.greemlock.ivApp.Objects.SaveSharedPreferences;
import com.greemlock.ivApp.Objects.User;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ivOffer extends AppCompatActivity {

    User user;
    Offer offer;
    ArrayList<String> offer_codes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iv_offer);

        user = (User) getIntent().getSerializableExtra("user");

        ImageView iv_bluetooth = findViewById(R.id.iv_bluetooth);
        if (SaveSharedPreferences.getPrefIsBluetooth(this)){
            iv_bluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_connected_24);
        }
        else{
            iv_bluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_24);
        }
        TextView tv_ivpara = findViewById(R.id.tv_user_ivpara);
        tv_ivpara.setText(String.valueOf(user.getUser_iv_para()));

        ImageButton ib_back = findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accounttomain = new Intent();
                accounttomain.putExtra("user",user);
                startActivity(accounttomain);
            }
        });
        FirebaseDatabase fb_database  = FirebaseDatabase.getInstance();
        DatabaseReference d_reference = fb_database.getReference("offers");

        for(int i = 1; i < 4; i++){

            String offerId = getOfferId(i);

            if (offerId == null){
                Random randomizer = new Random();
                ArrayList<Offer>  al_offers   = new ArrayList<>();

                d_reference.addListenerForSingleValueEvent(new ValueEventListener() {
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
                DatabaseReference myRef          = fb_database.getReference("users");

                setOfferId(i,al_offers.get(randomizer.nextInt(al_offers.size())).getOffer_id());
                Map<String,Object> update_list = new HashMap<>();
                update_list.put("user_offer_" + i,getOfferId(i));

                myRef.child(user.getUser_key()).updateChildren(update_list);
            }
            final int j = i - 1;

            String s_offer_company_photo = "iv_offer_company_photo_"  + i;
            String s_offer_company_name  = "tv_offer_company_name_"   + i;
            String s_offer_price         = "tv_offer_price_"          + i;
            String s_offer               = "tv_offer_"                + i;
            String s_offer_accept        = "b_offer_accept_"          + i;

            int id_offer_company_photo  = getResources().getIdentifier(s_offer_company_photo,"id",getApplicationContext().getPackageName());
            int id_offer_company_name   = getResources().getIdentifier(s_offer_company_name,"id",getApplicationContext().getPackageName());
            int id_offer_price          = getResources().getIdentifier(s_offer_price,"id",getApplicationContext().getPackageName());
            int id_offer                = getResources().getIdentifier(s_offer,"id",getApplicationContext().getPackageName());
            int id_offer_accept         = getResources().getIdentifier(s_offer_accept,"id",getApplicationContext().getPackageName());

            ImageView iv_offer_company_photo = findViewById(id_offer_company_photo);
            TextView  tv_offer_company_name  = findViewById(id_offer_company_name);
            TextView  tv_offer_price         = findViewById(id_offer_price);
            TextView  tv_offer               = findViewById(id_offer);
            Button    b_offer_accept         = findViewById(id_offer_accept);



            Query offer_query = d_reference.orderByChild("offer_id").equalTo(offerId);

            offer_query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds: snapshot.getChildren()){

                        offer = ds.getValue(Offer.class);

                        tv_offer_company_name.setText(offer.getOffer_company_name());
                        tv_offer.setText(offer.getOffer_description());
                        tv_offer_price.setText(String.valueOf(offer.getOffer_price()));
                        String offer_code = offer.getOffer_code();
                        b_offer_accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:

                                                if (user.getUser_iv_para() >= offer.getOffer_price() ){

                                                    user.setUser_iv_para(user.getUser_iv_para() - offer.getOffer_price());
                                                    Toast.makeText(ivOffer.this, R.string.get_offer_successfully, Toast.LENGTH_SHORT).show();

                                                    Query delete_offer = d_reference.child("offers").orderByChild("offer_id").equalTo(offer.getOffer_id());
                                                    delete_offer.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for (DataSnapshot ds: snapshot.getChildren()) {
                                                                ds.getRef().removeValue();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {}
                                                    });
                                                }
                                                else{
                                                    Toast.makeText(ivOffer.this, R.string.tv_offer_failed, Toast.LENGTH_SHORT).show();
                                                }
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                break;
                                        }
                                    }
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(ivOffer.this);
                                builder.setPositiveButtonIcon(getResources().getDrawable(R.drawable.background_iv_offer));
                                builder.setNegativeButtonIcon(getResources().getDrawable(R.drawable.background_iv_account));
                                builder.setMessage("Are you sure?").setPositiveButton("", dialogClickListener)
                                        .setNegativeButton("", dialogClickListener).show();


                            }

                        });

                        FirebaseStorage fb_storage = FirebaseStorage.getInstance();
                        StorageReference s_reference = fb_storage.getReference();
                        StorageReference sr_offer_company_photo = s_reference.child("offers/" + offer.getOffer_company_photo_name());

                        try {
                            File f_offer_company_photo = File.createTempFile("images","jpg");
                            sr_offer_company_photo.getFile(f_offer_company_photo).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    String s_file_path = f_offer_company_photo.getPath();
                                    Bitmap bm = BitmapFactory.decodeFile(s_file_path);
                                    iv_offer_company_photo.setImageBitmap(bm);
                                }
                            });
                        }
                        catch (Exception e){}
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
    private String getOfferId(int offerPosition){
        if (offerPosition == 1){
            return user.getUser_offer_1();
        }
        else if (offerPosition == 2){
            return user.getUser_offer_2();
        }
        else if (offerPosition == 3){
            return user.getUser_offer_3();
        }
        return null;
    }
    private String setOfferId(int offerPosition,String offerId){
        if (offerPosition == 1){
            user.setUser_offer_1(offerId);
        }
        else if (offerPosition == 2){
            user.setUser_offer_2(offerId);
        }
        else if (offerPosition == 3){
            user.setUser_offer_3(offerId);
        }
        return null;
    }

    public void notification(String offer_code){

        NotificationCompat.Builder builder;

        NotificationManager bildirimYonetici = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(getApplicationContext(),getApplicationContext().getClass());
        intent.putExtra("user",user);
        intent.putExtra("tiklama",true);

        PendingIntent gidilecekIntent = PendingIntent.getActivity(getApplicationContext(),1,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String kanalID ="kanalID";
            String kanalAd = "kanalAd";
            String kanalTanım = "kanalTanım";
            int kanalOnceligi = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel kanal = bildirimYonetici.getNotificationChannel(kanalID);

            if (kanal == null){
                kanal = new NotificationChannel(kanalID,kanalAd,kanalOnceligi);
                kanal.setDescription(kanalTanım);
                bildirimYonetici.createNotificationChannel(kanal);

            }
            builder = new NotificationCompat.Builder(getApplicationContext(),kanalID);
            builder.setContentTitle(getString(R.string.offer_notification_header));
            builder.setContentText(getString(R.string.offer_notification) + offer_code);
            builder.setSmallIcon(R.mipmap.ic_launcher_round);
            builder.setAutoCancel(true);
            builder.setContentIntent(gidilecekIntent);


        }
        else {

            builder = new NotificationCompat.Builder(getApplicationContext());

            builder.setContentTitle(getString(R.string.offer_notification_header));
            builder.setContentText(getString(R.string.offer_notification) + offer_code);
            builder.setSmallIcon(R.mipmap.ic_launcher_round);
            builder.setAutoCancel(true);
            builder.setContentIntent(gidilecekIntent);
            builder.setPriority(Notification.PRIORITY_HIGH);
        }

        bildirimYonetici.notify(1,builder.build());
    }
}