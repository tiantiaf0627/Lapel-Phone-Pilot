package com.example.tiantianfeng.posh_mobile_pilot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {


    private Button record_btn;


    private PendingIntent pendingIntent;

    public static final long ALARM_TRIGGER_AT_TIME = SystemClock.elapsedRealtime() + 10000;
    public static final long ALARM_INTERVAL = 1000 * 60 * 3;

    private AlarmManager alarmManager;

    private final boolean ENABLE_BT = true;
    private final boolean ENABLE_RECORD = false;

    private final boolean ENABLE_DISCOVERABLE = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    private void init() {



        record_btn = (Button) findViewById(R.id.record_btn);
        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if (ENABLE_RECORD) {

            /*
            *   Repeat the recording services every 15min (It will vary according to test results)
            */
            alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

            Intent record_Intent = new Intent(this, Record_Service.class);
            pendingIntent = PendingIntent.getService(MainActivity.this, 1, record_Intent, PendingIntent.FLAG_UPDATE_CURRENT);

            /*
            *   Alarm set repeat is not exact and can have significant drift
            * */
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, ALARM_TRIGGER_AT_TIME, pendingIntent);

        }


        if (ENABLE_BT) {
            Intent bt_Intent = new Intent(this, Bluetooth_Scan.class);
            startService(bt_Intent);
        }

        if (ENABLE_DISCOVERABLE) {

            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }

            if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
                startActivity(discoverableIntent);
            }

        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alarmManager.cancel(pendingIntent);

    }
}
