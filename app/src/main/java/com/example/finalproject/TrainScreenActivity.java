package com.example.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_GYROSCOPE;

/**
 * Created by pawan on 7/2/17.
 */

public class TrainScreenActivity extends AppCompatActivity implements SensorEventListener{
    Button trainButton, cancelButton;
    Context context;
    Spinner chooseLetterDropDown;
    static SensorManager sensorManager;
    Sensor accelerometer, gyroscope;
    static boolean newGesture = false;
    TextView gestureClass;
    ImageView trainImage;
    String sensorName, gestureRecorded;
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Movies/";
//    String path = "/storage/";
    String fileName = "train_dummy.csv";
    Bitmap bitmap;
    String [] letterList;

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            gestureClass = (TextView) findViewById(R.id.trainGesture);
//            gestureClass.setText(gestureRecorded);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_screen);

        Toast.makeText(TrainScreenActivity.this, path, Toast.LENGTH_SHORT).show();

        context = TrainScreenActivity.this;
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(TYPE_GYROSCOPE);//

        trainButton = (Button) findViewById(R.id.trainButton);
        cancelButton = (Button) findViewById(R.id.trainCancel);
        chooseLetterDropDown = (Spinner) findViewById(R.id.train_spinner);
        gestureClass = (TextView)findViewById(R.id.trainGesture);
        trainImage = (ImageView) findViewById(R.id.imageView);

        // https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
        letterList = new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ").split("");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, letterList);
        chooseLetterDropDown.setAdapter(adapter);
        chooseLetterDropDown.setSelection(1);

        trainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGesture = true;
                MainMenu.availableTest.add(chooseLetterDropDown.getSelectedItem().toString());
                sensorManager.registerListener(TrainScreenActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                gestureRecorded = chooseLetterDropDown.getSelectedItem().toString().toUpperCase();

                setPhoto(trainImage, gestureRecorded);
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

    private void setPhoto(ImageView image, String gestureRecorded) {
        switch(gestureRecorded){
            case "A": image.setImageResource(R.drawable.a);
                break;
            case "B": image.setImageResource(R.drawable.b);
                break;
            case "C": image.setImageResource(R.drawable.c);
                break;
            case "D": image.setImageResource(R.drawable.d);
                break;
            case "E": image.setImageResource(R.drawable.e);
                break;
            case "F": image.setImageResource(R.drawable.f);
                break;
            case "G": image.setImageResource(R.drawable.g);
                break;
            case "H": image.setImageResource(R.drawable.h);
                break;
            case "I": image.setImageResource(R.drawable.i);
                break;
            case "J": image.setImageResource(R.drawable.j);
                break;
            case "K": image.setImageResource(R.drawable.k);
                break;
            case "L": image.setImageResource(R.drawable.l);
                break;
            case "M": image.setImageResource(R.drawable.m);
                break;
            case "N": image.setImageResource(R.drawable.n);
                break;
            case "O": image.setImageResource(R.drawable.o);
                break;
            case "P": image.setImageResource(R.drawable.p);
                break;
            case "Q": image.setImageResource(R.drawable.q);
                break;
            case "R": image.setImageResource(R.drawable.r);
                break;
            case "S": image.setImageResource(R.drawable.s);
                break;
            case "T": image.setImageResource(R.drawable.t);
                break;
            case "U": image.setImageResource(R.drawable.u);
                break;
            case "V": image.setImageResource(R.drawable.v);
                break;
            case "W": image.setImageResource(R.drawable.w);
                break;
            case "X": image.setImageResource(R.drawable.x);
                break;
            case "Y": image.setImageResource(R.drawable.y);
                break;
            case "Z": image.setImageResource(R.drawable.z);
                break;
            default:break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        sensorName = event.sensor.getName();
//        handler.sendEmptyMessage(0);
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
