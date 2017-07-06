package com.example.finalproject;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;

import java.io.IOException;
import java.util.Arrays;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_GYROSCOPE;

/**
 * Created by arjun on 6/28/17.
 */

public class TestScreenActivity extends Activity{
    TextView gestureClass;
    boolean breakOut = false;
    boolean newGesture = false;
//    long lastTime = System.currentTimeMillis();
    final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Movies/";
    String fileName = "test_dummy2.csv";
    String result;
    SensorManager sensorManager;
    Sensor accelerometer, gyroscope;
    private Spinner chooseLetterDropDown;
    private Button scanButton, cancelButton;
    String gestureRecorded;
    String[] lettersList;
//    final private String TAG = "TEST";
    HubActivity h;

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            gestureClass = (TextView) findViewById(R.id.gestureClass);
            gestureClass.setText(result);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing_screen);

        if (TrainScreenActivity.newGesture == true){
            TrainScreenActivity.newGesture = false;
        }

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(TYPE_GYROSCOPE);

        h = new HubActivity();
        h.createHub(this);
        h.setLockPolicy();
        h.ontSendData();

        scanButton = (Button) findViewById(R.id.scanButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        chooseLetterDropDown = (Spinner) findViewById(R.id.choose_letter);
        gestureClass = (TextView) findViewById(R.id.gestureClass);

        // https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
        lettersList = MainMenu.availableTest.toArray(new String[MainMenu.availableTest.size()]);
        Arrays.sort(lettersList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                lettersList);
        chooseLetterDropDown.setAdapter(adapter);
        chooseLetterDropDown.setSelection(0);


        scanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            if(lettersList.length == 0 || lettersList == null) {
                Toast.makeText(TestScreenActivity.this, "Please run train module!", Toast.LENGTH_LONG).show();
            }
            else {
                newGesture = true;
                gestureRecorded = chooseLetterDropDown.getSelectedItem().toString().toUpperCase();

                breakOut = false;
                final String text = chooseLetterDropDown.getSelectedItem().toString();
                Log.d("TestScreen", "Selected Value from dropdown: " + text);
                Toast.makeText(TestScreenActivity.this, path+fileName, Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            UploadToServer.writeDataToFile(path + fileName, h.accelX, h.accelY, h.accelZ,
                                    h.gyroX, h.gyroY, h.gyroZ, h.orientX, h.orientY, h.orientZ, "0");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, 1000);
                new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String file = path + fileName;
                                Log.d("TestScreen", "Selected Value from dropdown: " + text);
                                while (true) {
                                    if (breakOut) {
                                        break;
                                    }
                                    try {
                                        result = UploadToServer.uploadToServer(file);
                                        System.out.println("Result: "+ result);
//                                        Toast.makeText(TestScreenActivity.this, result, Toast.LENGTH_LONG).show();
                                    }
                                    catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    handler.sendEmptyMessage(0);
                                }
                            }
                        }).start();
                    }
                }
            });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breakOut = true;
                newGesture = false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        h.createAndAddListner();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Hub.getInstance().removeListener(h.mListener);
    }
}