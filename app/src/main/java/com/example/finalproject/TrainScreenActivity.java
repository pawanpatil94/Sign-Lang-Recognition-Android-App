package com.example.finalproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.Vector3;
import com.thalmic.myo.XDirection;

import java.io.IOException;

/**
 * Created by pawan on 7/2/17.
 */

public class TrainScreenActivity extends AppCompatActivity{
    Button trainButton, cancelButton;
    Spinner chooseLetterDropDown;
    static boolean newGesture = false;
    TextView gestureClass;
    TextView mTextView;
    VideoView trainVideo;
    String gestureRecorded;
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Movies/";
    String fileName = "train_dummy.csv";
    String [] letterList;

    private String TAG = "MyO Test";
    Context context;
    DeviceListener mListener;
    float accelX, accelY, accelZ;
    float gyroX, gyroY, gyroZ;
    float orientX, orientY, orientZ;
    int i = 0;

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            gestureClass = (TextView) findViewById(R.id.trainGesture);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_screen);

        context = TrainScreenActivity.this;

        createHub(this);
        setLockPolicy();
        ontSendData();

        trainButton = (Button) findViewById(R.id.trainButton);
        cancelButton = (Button) findViewById(R.id.trainCancel);
        chooseLetterDropDown = (Spinner) findViewById(R.id.train_spinner);
        gestureClass = (TextView)findViewById(R.id.trainGesture);
        trainVideo = (VideoView) findViewById(R.id.videoView);
        mTextView = (TextView) findViewById(R.id.mTextView);

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
                gestureRecorded = chooseLetterDropDown.getSelectedItem().toString().toUpperCase();

                String url = "http://192.168.43.237/uploads/vids/" + gestureRecorded +".mp4";
                trainVideo.setVideoURI(Uri.parse(url));
                trainVideo.start();

                i = 0;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (newGesture && i<50) {
                            try {
                                i++;
                                UploadToServer.writeDataToFile(path + fileName, accelX, accelY, accelZ,
                                        gyroX, gyroY, gyroZ, orientX, orientY, orientZ, gestureRecorded);
                            } catch(IOException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGesture = false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        createAndAddListner();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Hub.getInstance().removeListener(mListener);
    }

    void createHub(Context mContext) {
        Hub hub = Hub.getInstance();
        context = mContext;
        if (!hub.init(context)) {
            Log.e(TAG, "Could not initialize the Hub.");
            return;
        }
    }

    void setLockPolicy() {
        Hub.getInstance().setLockingPolicy(Hub.LockingPolicy.NONE);
    }

    void createAndAddListner() {

        mListener = new AbstractDeviceListener() {
            @Override
            public void onConnect(Myo myo, long timestamp) {
                Toast.makeText(context, "Myo Connected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDisconnect(Myo myo, long timestamp) {
                Toast.makeText(context, "Myo Disconnected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPose(Myo myo, long timestamp, Pose pose) {
                switch (pose) {
                    case REST:
//                        Toast.makeText(context, "REST", Toast.LENGTH_SHORT).show();
                        break;
                    case FIST:
//                        Toast.makeText(context, "FIST", Toast.LENGTH_SHORT).show();
                        break;
                    case WAVE_IN:
//                        Toast.makeText(context, "WAVE_IN", Toast.LENGTH_SHORT).show();
                        break;
                    case WAVE_OUT:
//                        Toast.makeText(context, "WAVE_OUT", Toast.LENGTH_SHORT).show();
                        break;
                    case FINGERS_SPREAD:
//                        Toast.makeText(context, "FINGERS_SPREAD", Toast.LENGTH_SHORT).show();
                        break;
                    case DOUBLE_TAP:
//                        Toast.makeText(context, "DOUBLE_TAP", Toast.LENGTH_SHORT).show();
                        break;
                    case UNKNOWN:
//                        Toast.makeText(context, "UNKNOWN", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onAccelerometerData(Myo myo, long timestamp, Vector3 accel) {
                accelX = (float) accel.x();
                accelY = (float) accel.y();
                accelZ = (float) accel.z();
            }

            @Override
            public void onGyroscopeData(Myo myo, long timestamp, Vector3 gyro) {
                gyroX = (float) gyro.x();
                gyroY = (float) gyro.y();
                gyroZ = (float) gyro.z();
            }


            @Override
            public void onOrientationData(Myo myo, long timestamp, Quaternion rotation){
                orientX = (float) Math.toDegrees(Quaternion.roll(rotation));
                orientY = (float) Math.toDegrees(Quaternion.pitch(rotation));
                orientZ = (float) Math.toDegrees(Quaternion.yaw(rotation));

                if (myo.getXDirection() == XDirection.TOWARD_ELBOW) {
                    orientX *= -1;
                    orientY *= -1;
                }
            }
        };

        Hub.getInstance().addListener(mListener);
    }

    void ontSendData() {
        if (Hub.getInstance().isSendingUsageData()) {
            Hub.getInstance().setSendUsageData(false);
        }
    }

}
