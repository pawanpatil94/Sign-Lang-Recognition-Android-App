package com.example.finalproject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_GYROSCOPE;

/**
 * Created by pawan on 7/2/17.
 */

public class TrainScreenActivity extends AppCompatActivity implements SensorEventListener{
    Button trainButton, cancelButton;
    Spinner chooseLetterDropDown;
    SensorManager sensorManager;
    Sensor accelerometer, gyroscope;
    boolean newGesture = false;
    TextView gestureClass;
    VideoView trainVideo;
    String sensorName, gestureRecorded;
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Movies/";
    String fileName = "train_dummy.csv";

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            gestureClass = (TextView) findViewById(R.id.trainGesture);
            gestureClass.setText(gestureRecorded);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_screen);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(TYPE_GYROSCOPE);//

        trainButton = (Button) findViewById(R.id.trainButton);
        cancelButton = (Button) findViewById(R.id.trainCancel);
        chooseLetterDropDown = (Spinner) findViewById(R.id.train_spinner);
        gestureClass = (TextView)findViewById(R.id.trainGesture);
        trainVideo = (VideoView) findViewById(R.id.video);

        // https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
        String[] lettersList = new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ").split("");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, lettersList);
        chooseLetterDropDown.setAdapter(adapter);
        chooseLetterDropDown.setSelection(1);

        trainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGesture = true;

                MainMenu.availableTest.add(chooseLetterDropDown.getSelectedItem().toString());
                sensorManager.registerListener(TrainScreenActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                sensorManager.registerListener(TrainScreenActivity.this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                gestureRecorded = chooseLetterDropDown.getSelectedItem().toString().toUpperCase();
                gestureClass.setText(gestureRecorded);

                String url = "http://192.168.57.1/uploads/vids/A.mp4";
//                String url = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
                trainVideo.setVideoURI(Uri.parse(url));
                trainVideo.start();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGesture = false;
                sensorManager.unregisterListener(TrainScreenActivity.this);
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        sensorName = event.sensor.getName();
        handler.sendEmptyMessage(0);
        try {
            if(newGesture) {
                UploadToServer.writeDataToFile(path + fileName, event.values[0], event.values[1], event.values[2], gestureRecorded);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
