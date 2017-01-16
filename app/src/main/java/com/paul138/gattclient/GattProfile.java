package com.paul138.gattclient;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.ArrayList;

/**
 * Created by 138 on 1/12/2017.
 */

public class GattProfile {
    private ArrayList<BluetoothGattService> mGattServices;
    public GattProfile(){
        mGattServices = new ArrayList<>();
    }
    public void addService(BluetoothGattService service){
        mGattServices.add(service);
    }
}
