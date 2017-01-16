package com.paul138.gattclient;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.util.List;
import java.util.HashMap;

/**
 * Created by 138 on 1/12/2017.
 */

public class GattProfile {
    private HashMap<String,BluetoothGattService> mGattServices;
    public GattProfile(){
        mGattServices = new HashMap<>();
    }
    public void addService(String id,BluetoothGattService service){
        mGattServices.put(id, service);
    }
    public List<BluetoothGattCharacteristic> readCharacteristics(String serviceId){
        BluetoothGattService service = mGattServices.get(serviceId);
        if(service != null){
            return service.getCharacteristics();
        }else{
            Log.i("", "ERROR Finding BluetoothGattService at ID : " + serviceId);
        }
        return null;
    }
}
