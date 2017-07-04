package com.example.finalproject;

import java.io.IOException;
import java.util.Arrays;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_GYROSCOPE;

/**
 * Created by arjun on 6/28/17.
 */

public class TestScreenActivity extends Activity implements SensorEventListener{
    TextView gestureClass;
    boolean breakOut = false;
    boolean newGesture = false;
    long lastTime = System.currentTimeMillis();
    final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Movies/";
    String fileName = "test_dummy2.csv";
    String result;
    SensorManager sensorManager;
    Sensor accelerometer, gyroscope;
    private Spinner chooseLetterDropDown;
    private Button scanButton, cancelButton;
    String gestureRecorded;
    String[] lettersList;

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
//        TrainScreenActivity.sensorManager.unregisterListener(TestScreenActivity.this);
        if (TrainScreenActivity.newGesture == true){
            TrainScreenActivity.newGesture = false;
        }
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(TYPE_GYROSCOPE);

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

                sensorManager.registerListener(TestScreenActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//                sensorManager.registerListener(TestScreenActivity.this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                gestureRecorded = chooseLetterDropDown.getSelectedItem().toString().toUpperCase();
//                gestureClass.setText(gestureRecorded);

                breakOut = false;
                final String text = chooseLetterDropDown.getSelectedItem().toString();
                Log.d("TestScreen", "Selected Value from dropdown: " + text);
                Toast.makeText(TestScreenActivity.this, path+fileName, Toast.LENGTH_LONG).show();
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
    public void onSensorChanged(SensorEvent event) {
        if(newGesture) {
            try {
                newGesture = false;
                UploadToServer.writeDataToFile(path + fileName, event.values[0], event.values[1], event.values[2], "0");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}