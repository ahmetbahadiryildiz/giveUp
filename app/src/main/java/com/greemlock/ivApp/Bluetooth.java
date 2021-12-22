package com.greemlock.ivApp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.greemlock.ivApp.Objects.Punishment;
import com.greemlock.ivApp.Objects.SaveSharedPreferences;
import com.greemlock.ivApp.Objects.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Bluetooth extends Service {

    User user           = null;
    String            address        = null;
    BluetoothAdapter  myBluetooth    = null;
    BluetoothSocket   btSocket       = null;
    private boolean   isBtConnected  = false;
    static final UUID myUUID         = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    FirebaseDatabase  database       = FirebaseDatabase.getInstance();
    DatabaseReference myRef          = database.getReference("users");
    int               cezaSayisi     = 0;
    int               not_using_time = 1;
    Handler           m_Handler      = new Handler();
    Runnable          mRunnable      = null;
    InputStream       mmInputStream;
    Thread            workerThread;
    byte[]            readBuffer;
    int               readBufferPosition;
    volatile boolean  stopWorker;



    private NotificationCompat.Builder builder;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class BTbaglan extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected Void doInBackground(Void... devices) {
            try {

                if (btSocket == null || !isBtConnected) {

                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice cihaz = myBluetooth.getRemoteDevice(address);
                    btSocket = cihaz.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mmInputStream = btSocket.getInputStream();
                    btSocket.connect();

                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (ConnectSuccess) {
                Toast.makeText(getApplicationContext(),"Bağlantı Başarılı",Toast.LENGTH_SHORT).show();


                if (user.getUser_device().equals("")){

                    user.setUser_device(address);
                    Map<String,Object> cihazDB = new HashMap<>();
                    cihazDB.put("user_device",user.getUser_device());
                    myRef.child(user.getUser_key()).updateChildren(cihazDB);

                }

                veriOku(mmInputStream);

                mRunnable = new Runnable(){
                    @Override
                    public void run() {

                        not_using_time++;
                        Toast.makeText(Bluetooth.this, String.valueOf(not_using_time), Toast.LENGTH_SHORT).show();
                        m_Handler.postDelayed(mRunnable, 5000);

                        if (not_using_time % 60 == 0){

                            Toast.makeText(Bluetooth.this, "Tebrikler 1 Saattir İçmiyorsun. Aynen Devam.", Toast.LENGTH_SHORT).show();
                            user.setUser_iv_para(user.getUser_iv_para()+50); ;
                            Map<String,Object> user_update = new HashMap<>();
                            user_update.put("user_iv_para",user.getUser_iv_para());
                            myRef.child(user.getUser_key()).updateChildren(user_update);
                            not_using_time = 1;

                        }
                    }};
                mRunnable.run();

                Intent mIntent = new Intent(Bluetooth.this, MainScreen.class);
                SaveSharedPreferences.setPrefIsBluetooth(Bluetooth.this,true);
                mIntent.putExtra("user",user);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);

            }else{
                Toast.makeText(getApplicationContext(),"Bağlantı Başarısız. Lütfen tekrar deneyiniz",Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(Bluetooth.this, MainScreen.class);
                mIntent.putExtra("user",user);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
            }
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        address = intent.getStringExtra("address");
        user    = (User) intent.getSerializableExtra("user");
        new BTbaglan().execute();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Disconnect(btSocket);
        super.onDestroy();
    }

    public void veriOku(final InputStream mmInputStream){
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            if(data.equals("1\r")){

                                                ArrayList<Punishment> cezaList;
                                                Date date = new Date();
                                                not_using_time = 1;

                                                if (user.getUser_punishment_list() != null){

                                                    cezaList = user.getUser_punishment_list();

                                                    for (Punishment p : user.getUser_punishment_list()) {
                                                        if (p.getPunishment_day() == date.getDate() && p.getPunishment_month() == date.getMonth() && p.getPunishment_year() == date.getYear() + 1900) {
                                                            cezaSayisi = p.getPunishment_number();
                                                            cezaList.remove(p);
                                                            break;
                                                        }
                                                    }
                                                }
                                                else{
                                                    cezaList = new ArrayList<>();
                                                }
                                                cezaSayisi++;
                                                Punishment new_punishment = new Punishment(date.getDate(),date.getMonth(),date.getYear() + 1900, cezaSayisi);
                                                cezaList.add(new_punishment);

                                                user.setUser_punishment_list(cezaList);

                                                user.setUser_iv_para(user.getUser_iv_para()-100); ;


                                                Map<String,Object> cezaListe = new HashMap<>();
                                                cezaListe.put("user_punishment_list",user.getUser_punishment_list());
                                                cezaListe.put("user_iv_para",user.getUser_iv_para());
                                                myRef.child(user.getUser_key()).updateChildren(cezaListe);

                                                Toast.makeText(getApplicationContext() , R.string.triggered, Toast.LENGTH_SHORT).show();

                                                bildirim();
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });
        workerThread.start();
    }

    private void Disconnect(BluetoothSocket btSocket){

        if(btSocket!=null){
            try {
                btSocket.close();
                SaveSharedPreferences.setPrefIsBluetooth(Bluetooth.this,false);
            } catch (IOException e){}
        }
    }

    public void bildirim(){


        NotificationManager bildirimYonetici = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(getApplicationContext(),getApplicationContext().getClass());
        intent.putExtra("user",user);
        intent.putExtra("tiklama",true);

        PendingIntent gidilecekIntent = PendingIntent.getActivity(getApplicationContext(),1,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String kanalID    = "kanalID";
            String kanalAd    = "kanalAd";
            String kanalTanım = "kanalTanım";
            int kanalOnceligi = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel kanal = bildirimYonetici.getNotificationChannel(kanalID);

            if (kanal == null){
                kanal = new NotificationChannel(kanalID,kanalAd,kanalOnceligi);
                kanal.setDescription(kanalTanım);
                bildirimYonetici.createNotificationChannel(kanal);

            }
            builder = new NotificationCompat.Builder(getApplicationContext(),kanalID);
            builder.setContentTitle("ivApp'tan mesajınız var");
            builder.setContentText("Sigara ya da Alkol kullanıldığı algılandı!");
            builder.setSmallIcon(R.mipmap.ic_launcher_round);
            builder.setAutoCancel(true);
            builder.setContentIntent(gidilecekIntent);


        }
        else {

            builder = new NotificationCompat.Builder(getApplicationContext());

            builder.setContentTitle("ivApp'tan mesajınız var");
            builder.setContentText("Sigara ya da Alkol kullanıldığı algılandı!");
            builder.setSmallIcon(R.mipmap.ic_launcher_round);
            builder.setAutoCancel(true);
            builder.setContentIntent(gidilecekIntent);
            builder.setPriority(Notification.PRIORITY_HIGH);
        }

        bildirimYonetici.notify(1,builder.build());
    }

}