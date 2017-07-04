package com.example.finalproject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by pawan on 6/25/17.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MainMenu extends AppCompatActivity implements BluetoothAdapter.LeScanCallback {

    private String deviceName;
    private Handler mHandler;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private TextView emgDataText;
    private TextView         gestureText;

    public static final int MENU_LIST = 0;
    private Button trainButton, testButton, tutorialButton;
    final Context context = this;
    static HashSet<String> availableTest = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        testButton = (Button) findViewById(R.id.TestButton);
        tutorialButton = (Button) findViewById(R.id.tutorialButton);
        trainButton = (Button) findViewById(R.id.mainTrain);

        // Re-diect to test screen
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent testScreen = new Intent(context, TestScreenActivity.class);
                startActivity(testScreen);
            }
        });

        // Re-diect to tutorial screen
        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tutorialScreen = new Intent(context, TutorialScreenActivity.class);
                startActivity(tutorialScreen);
            }
        });

        trainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trainScreen = new Intent(context, TrainScreenActivity.class);
                startActivity(trainScreen);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, MENU_LIST, 0, "Find Myo Device");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case MENU_LIST:
                Intent findDevice = new Intent(context, ListActivity.class);
                startActivity(findDevice);
                return true;
        }
        return false;
    }

    /** Define of BLE Callback */
    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (deviceName.equals(device.getName())) {
            mBluetoothAdapter.stopLeScan(this);
            // Trying to connect GATT
            HashMap<String,View> views = new HashMap<String,View>();

        }
    }
}
