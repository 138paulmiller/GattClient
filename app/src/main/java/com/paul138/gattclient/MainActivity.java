package com.paul138.gattclient;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    ArrayList<String> listItems = new ArrayList<>(); //device address list
    ListView listView; //device list view
    TextView textViewLog;
    Button scanButton,clearButton; //buttons
    ArrayAdapter<String> adapter; //handles data in list view as strings
    private GattClientService mGattClientService = null;
    private boolean mBound = false;
    private GattClientService.GattCallbackInterface mBLEInterface = new GattClientService.GattCallbackInterface() {
        @Override
        public void onGattDeviceFound(final BluetoothDevice device, final String id) {
            Log.i("MainActivity:", "Found Device:\n\tADDR : " + device.getAddress());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onDeviceFound(device, id);
                }
            });
        }
        @Override
        public void onGattServerConnected(final BluetoothGatt bluetoothGatt){
            Log.i("MainActivity:", "Connected to Device:\n\tADDR : " + bluetoothGatt.getDevice().getAddress());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onDeviceConnected(bluetoothGatt);
                }
            });
    }
        @Override
        public void onGattServerDisconnected(final BluetoothGatt bluetoothGatt){
            Log.i("MainActivity:", "Disconnected from Device:\n\tADDR : " + bluetoothGatt.getDevice().getAddress());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onDeviceDisconnected(bluetoothGatt);
                }
            });
    }
        @Override
        public void onGattServicesDiscovered(final List<BluetoothGattService> gattServices, boolean valid){

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!checkPermissions()) {//if permissions are not enabled start app settings activity
            Toast.makeText(this, "Please Enable this App's Permissions in Application Manager", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null); //open this package
            intent.setData(uri); //send this app package as an argument to settings intent
            startActivity(intent); //start settings activity
        }
        textViewLog =  ((TextView)findViewById(R.id.textView));
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView = ((ListView) findViewById(R.id.listView));

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String addr  = String.valueOf(((TextView)view ).getText());
                Log.i("MainActivity", "Clicked Item : " + addr);
                if (mGattClientService != null) {
                    if(mGattClientService.isConnected()){
                        textViewLog.append("Disconnecting from device :\n    " + addr + " ...\n");
                        mGattClientService.disconnectFromDevice();
                    }
                    else{
                       textViewLog.append("Trying to connect to device:\n   " + addr + " ...\n");
                        if(!mGattClientService.connectToDevice(addr)){ //if fails then fails to connect to gatt server
                            Toast.makeText(view.getContext(), "Unable to find GATT Server on Device!", Toast.LENGTH_SHORT).show();
                        }
                    }

                };
            }
        });
        adapter.notifyDataSetChanged();
        scanButton = ((Button) findViewById(R.id.buttonScan));
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGattClientService != null) {
                    mGattClientService.scanLeDevice(!mGattClientService.isScanning()); //toggle by NOTting current scan status
                    if(!mGattClientService.isBluetoothEnabled()){ //if not enabled show user
                        Log.i("MainActivity", "Bluetooth Not Enabled");
                        Toast.makeText(v.getContext(), "Bluetooth Not Enabled!", Toast.LENGTH_SHORT).show();
                    }
                    if (mGattClientService.isScanning())//if scanning show button text as
                        scanButton.setText("Stop Scan");
                    else
                        scanButton.setText("Start Scan");
                }


            }
        });
        clearButton = ((Button) findViewById(R.id.buttonClear));

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mGattClientService != null) mGattClientService.clearAvailableDevices();
                adapter.clear();
                adapter.notifyDataSetChanged();
                textViewLog.getEditableText().clear();
            }
        });

       textViewLog.setMovementMethod(new ScrollingMovementMethod());


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "ble_not_supported", Toast.LENGTH_SHORT).show();
            finish();

        } else {
            // Bind to GattClientService
            Intent intent = new Intent(this, GattClientService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.i("ServiceConnection", "GattClientService Connected to activity");
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            mGattClientService = ((GattClientService.BLEServiceBinder) service).getService();
            mGattClientService.setInterface(mBLEInterface);

            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            Log.i("ServiceConnection", "GattClientService Disconnected from activity");

        }
    };

    private void onDeviceFound(final BluetoothDevice device, String id) {
        Log.i("MainActivity", "Found Device ADDR:" + id);
        adapter.add(id);
        adapter.notifyDataSetChanged();
    }
    private void onDeviceConnected(final BluetoothGatt bluetoothGatt) {
        if (bluetoothGatt != null) {
            ((TextView) findViewById(R.id.textView)).append("Connected to GATT Server on device:\n   " + bluetoothGatt.getDevice().getAddress() + "\n");
        }
    }
    private void onDeviceDisconnected(final BluetoothGatt bluetoothGatt) {
        if (bluetoothGatt != null) {
            ((TextView) findViewById(R.id.textView)).append("Disconnected from device:\n   " + bluetoothGatt.getDevice().getAddress() + "\n");
        }
    }
    public boolean checkPermissions(){
            String permission = "android.permission.ACCESS_COARSE_LOCATION";
            int res = this.checkCallingOrSelfPermission(permission);
            return (res == PackageManager.PERMISSION_GRANTED);
    }
}

