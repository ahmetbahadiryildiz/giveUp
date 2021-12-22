package com.greemlock.ivApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.greemlock.ivApp.Objects.Punishment;
import com.greemlock.ivApp.Objects.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ivCalendar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iv_calendar);

        Calendar takvim = Calendar.getInstance();

        TextView textViewAy = findViewById(R.id.tv_month);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM yyyy");
        String ay = simpleDateFormat.format(takvim.getTime());

        textViewAy.setText(ay);

        Calendar gunlerCalendar = Calendar.getInstance();

        gunlerCalendar.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        gunlerCalendar.clear(Calendar.MINUTE);
        gunlerCalendar.clear(Calendar.SECOND);
        gunlerCalendar.clear(Calendar.MILLISECOND);

        gunlerCalendar.set(Calendar.DAY_OF_MONTH, 1);

        ImageView imageViewBluetooth = findViewById(R.id.iv_bluetooth);
        boolean btConnected = getIntent().getBooleanExtra("btBaglanti", false);

        if (btConnected == true) {

            imageViewBluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_connected_24);

        } else {

            imageViewBluetooth.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_24);
        }


        int iMonth = takvim.getTime().getMonth();
        int iYear = takvim.getTime().getYear();
        int daysInMonth;
        //Toast.makeText(this, String.valueOf(iMonth) , Toast.LENGTH_SHORT).show();

        if (iMonth % 2 == 0) {

            daysInMonth = 31;
        } else {

            if (iMonth == 1) {

                if (iYear % 4 == 0) {
                    daysInMonth = 29;
                } else {
                    daysInMonth = 28;
                }
            } else {
                daysInMonth = 30;
            }
        }


        int takvimSayi = gunlerCalendar.getTime().getDay();

        for (int i = 0; i < daysInMonth; i++) {

            int gun = takvimSayi + i;
            String takvimTextView = "day" + gun;

            Resources res = getResources();
            int idGun = res.getIdentifier(takvimTextView, "id", getApplicationContext().getPackageName());

            TextView textViewGun = findViewById(idGun);

            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth() - 150;
            int a = i + 1;

            textViewGun.setWidth(width / 7);
            textViewGun.setText(String.valueOf(a));
        }

        Resources res = getResources();

        int gunBugun = takvimSayi + takvim.getTime().getDate() - 1;
        String bugun = "day" + gunBugun;
        int idBugun = res.getIdentifier(bugun, "id", getApplicationContext().getPackageName());
        TextView takvimBugun = findViewById(idBugun);
        takvimBugun.setBackgroundResource(R.drawable.background_calendar_today);
        takvimBugun.setTextColor(Color.BLACK);

        User aktifHesap = (User) getIntent().getSerializableExtra("user");
        ArrayList list = new ArrayList();
        if (!aktifHesap.getUser_punishment_list().isEmpty()) {

            for (Punishment ceza : aktifHesap.getUser_punishment_list()) {
                if (takvim.getTime().getMonth() == ceza.getPunishment_day()) {

                    String s1 = ceza.getPunishment_day() + "." + (ceza.getPunishment_month() + 1) + "." + ceza.getPunishment_year();
                    String s2 = "";
                    for (int i = 0; i < ceza.getPunishment_number(); i++) {

                        s2 = s2 + "X ";

                    }
                    list.add(s1 + "        " + s2);

                    int gunCeza = takvimSayi + ceza.getPunishment_day() - 1;
                    String cezaGun = "takvim" + gunCeza;
                    int idCeza = res.getIdentifier(cezaGun, "id", getApplicationContext().getPackageName());
                    TextView takvimceza = findViewById(idCeza);
                    takvimceza.setTextColor(Color.rgb(233, 30, 99));

                }

            }
        }
    }
}