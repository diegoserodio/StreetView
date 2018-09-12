package com.example.diego.rastreetview;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

public class DevicesList extends ListActivity {
    BluetoothAdapter sBluetoothAdapter = null;
    static String MACAddress = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> arrayBluetooth = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        sBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> bondedDevices = sBluetoothAdapter.getBondedDevices();

        if(bondedDevices.size() > 0){
            for(BluetoothDevice device: bondedDevices){
                String nameBT = device.getName();
                String addressBT = device.getAddress();
                arrayBluetooth.add(nameBT + "\n" + addressBT);
            }
        }
        setListAdapter(arrayBluetooth);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String data = ((TextView) v).getText().toString();
        String MACNumber = data.substring(data.length() - 17);

        Intent returnMAC = new Intent();
        returnMAC.putExtra(MACAddress, MACNumber);
        setResult(RESULT_OK, returnMAC);
        finish();
    }
}
