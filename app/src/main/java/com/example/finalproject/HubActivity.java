package com.example.finalproject;

/**
 * Created by Aamir on 7/4/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.Vector3;
import com.thalmic.myo.XDirection;
import com.thalmic.myo.scanner.ScanActivity;

public class HubActivity {

    private String TAG = "MyO Test";
    Context context;
    DeviceListener mListener;
    static float accelX, accelY, accelZ;
    static float gyroX, gyroY, gyroZ;
    static float orientX, orientY, orientZ;

     void createHub(Context mContext) {
        Hub hub = Hub.getInstance();
         context = mContext;
        if (!hub.init(context)) {
            Log.e(TAG, "Could not initialize the Hub.");
            return;
        }
    }

     void scanFordevice() {
        Intent intent = new Intent(context, ScanActivity.class);
        context.startActivity(intent);
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
                        Toast.makeText(context, "REST", Toast.LENGTH_SHORT).show();
                        break;
                    case FIST:
                        Toast.makeText(context, "FIST", Toast.LENGTH_SHORT).show();
                        break;
                    case WAVE_IN:
                        Toast.makeText(context, "WAVE_IN", Toast.LENGTH_SHORT).show();
                        break;
                    case WAVE_OUT:
                        Toast.makeText(context, "WAVE_OUT", Toast.LENGTH_SHORT).show();
                        break;
                    case FINGERS_SPREAD:
                        Toast.makeText(context, "FINGERS_SPREAD", Toast.LENGTH_SHORT).show();
                        break;
                    case DOUBLE_TAP:
                        Toast.makeText(context, "DOUBLE_TAP", Toast.LENGTH_SHORT).show();
                        break;
                    case UNKNOWN:
                        Toast.makeText(context, "UNKNOWN", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onAccelerometerData(Myo myo, long timestamp, Vector3 accel) {
                accelX = (float) accel.x();
                accelY = (float) accel.y();
                accelZ = (float) accel.z();

//                Toast.makeText(context, "Accel X: "+x+"Y: "+y+"Z: "+z, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onGyroscopeData(Myo myo, long timestamp, Vector3 gyro) {
                gyroX = (float) gyro.x();
                gyroY = (float) gyro.y();
                gyroZ = (float) gyro.z();

//               Toast.makeText(context, "Gyro X: "+x+"Y: "+y+"Z: "+z, Toast.LENGTH_LONG).show();

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

    public float[] getData(String sensorName){
        if(sensorName.equals("A")){
            return new float[]{accelX, accelY, accelZ};
        }
        else if(sensorName.equals("G")){
            return new float[]{gyroX, gyroY, gyroZ};
        }
        else {
            return new float[]{orientX, orientY, orientZ};
        }
    }
}