package com.example.tiantianfeng.posh_mobile_pilot;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by tiantianfeng on 9/14/17.
 */

public class Bluetooth_Scan extends Service {


    private BluetoothDevice bluetoothDevice;
    private BluetoothAdapter bluetoothAdapter;

    private String DEBUG = "TILEs";

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        Log.d(DEBUG, "Start BT service");
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        bluetoothAdapter.getScanMode();

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        bluetoothAdapter.enable();

        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);
        bluetoothAdapter.startDiscovery();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
                unregisterReceiver(mReceiver);
                stopSelf();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d(DEBUG, device.getName() + ": " + device.getAddress());
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
