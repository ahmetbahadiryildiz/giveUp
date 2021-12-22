package com.greemlock.ivApp.Objects;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.greemlock.ivApp.Bluetooth;
import com.greemlock.ivApp.MainScreen;
import com.greemlock.ivApp.R;

import java.util.ArrayList;
import java.util.Set;

public class deviceList extends AppCompatActivity {

    private Set<BluetoothDevice> paired_device;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        user = (User) getIntent().getSerializableExtra("user");
        ListView lv_device_list = findViewById(R.id.lv_device_list);

        BluetoothAdapter myBluetooth;
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        paired_device = myBluetooth.getBondedDevices();
        ArrayList paired_list = new ArrayList();

        ImageButton ib_back = findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent device_list_to_main = new Intent(deviceList.this, MainScreen.class
                );
                device_list_to_main.putExtra("user",user);
                startActivity(device_list_to_main);
            }
        });

        if (paired_device.size() > 0){
            for (BluetoothDevice bd: paired_device){
                Device pd = new Device();
                pd.setDevice_name(bd.getName());
                pd.setDevice_address(bd.getAddress());

                paired_list.add(pd);
            }
        }

        final deviceListAdapter a_device_list = new deviceListAdapter(this,R.layout.device_list_item,paired_list);
        lv_device_list.setAdapter(a_device_list);

        lv_device_list.setOnItemClickListener(selectDevice);
    }

    public AdapterView.OnItemClickListener selectDevice = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Toast.makeText(deviceList.this, "Bağlantı sağlanıyor...", Toast.LENGTH_SHORT).show();

            Device device = (Device) parent.getItemAtPosition(position);
            String address = device.getDevice_address();

            Intent bluetoothBaglanti = new Intent(deviceList.this, Bluetooth.class);
            bluetoothBaglanti.putExtra("user",user);
            bluetoothBaglanti.putExtra("address", address);
            startService(bluetoothBaglanti);

        }

    };
}