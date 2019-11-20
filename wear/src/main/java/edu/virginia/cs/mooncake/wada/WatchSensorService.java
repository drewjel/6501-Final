package edu.virginia.cs.mooncake.wada;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.nio.FloatBuffer;

import edu.virginia.cs.mooncake.wada.utils.FileUtil;
import edu.virginia.cs.mooncake.wada.utils.SharedPrefUtil;

public class WatchSensorService extends Service implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer, mGyroscope, mRotationVector, mMagnetometer, mGravity;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    Context context;
    int count, maxCount, rate;
    StringBuilder strBuilder;
    long startTime;
    String file_tag;
    int sensorType;

    static final int senMax = 100;
    float[] x_cache = new float[senMax];
    float[] y_cache = new float[senMax];
    float[] z_cache = new float[senMax];
    int sensorCounter = 0;


    static final int secMax = 5;
    float[] x_mean = new float[secMax];
    float[] y_mean = new float[secMax];
    float[] z_mean = new float[secMax];
    float[] x_dev = new float[secMax];
    float[] y_dev = new float[secMax];
    float[] z_dev = new float[secMax];
    int secondCounter = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakeLockTag");
        wakeLock.acquire();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGravity = mSensorManager
                .getDefaultSensor(Sensor.TYPE_GRAVITY);
        mGyroscope = mSensorManager
                .getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mRotationVector = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mMagnetometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        rate = SensorManager.SENSOR_DELAY_GAME;
        mSensorManager.registerListener(this, mAccelerometer, rate);
        //mSensorManager.registerListener(this, mGravity, rate);
        //mSensorManager.registerListener(this, mGyroscope, rate);
        //mSensorManager.registerListener(this, mRotationVector, rate);
        //mSensorManager.registerListener(this, mMagnetometer, rate);

        startTime = System.currentTimeMillis();
        strBuilder = new StringBuilder();
        startTime = System.currentTimeMillis();
        count = 0;
        maxCount = 60 * 300;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("stop")) {
            //stopSelf(startId);


            //turn off sensor
        } else if (intent != null && intent.hasExtra("start")) {
            //file_tag = intent.getStringExtra("start");
            //file_tag = file_tag + ".wada";


            //turn on sensor
        }

        context = this.getApplicationContext();
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        wakeLock.release();
        sendData();
    }

    public void sendData() {
        if (strBuilder != null) {
            Log.i("MyTAG", "Sending File. Duration: " + (System.currentTimeMillis() - startTime));
            new fileSaveThread(strBuilder).start();
            strBuilder = new StringBuilder();
            count = 0;
        }
    }


    public void performClassifiction()
    {
        FloatBuffer.allocate(6*secMax);
    }

    public void processDataAndClassify()
    {
        x_mean[secondCounter] = 0;
        y_mean[secondCounter] = 0;
        z_mean[secondCounter] = 0;

        x_dev[secondCounter] = 0;
        y_dev[secondCounter] = 0;
        z_dev[secondCounter] = 0;

        for(int i = 0; i<senMax; i++)
        {
            x_mean[secondCounter] += x_cache[i];
            y_mean[secondCounter] += y_cache[i];
            y_mean[secondCounter] += z_cache[i];

        }

        x_mean[secondCounter] /= senMax;
        y_mean[secondCounter] /= senMax;
        z_mean[secondCounter] /= senMax;

        for(int i = 0; i<senMax; i++)
        {
            x_dev[secondCounter] += (x_cache[i] - x_mean[secondCounter])*(x_cache[i] - x_mean[secondCounter]);
            y_dev[secondCounter] += (y_cache[i] - y_mean[secondCounter])* (y_cache[i] - y_mean[secondCounter]);
            y_dev[secondCounter] += (z_cache[i] - z_mean[secondCounter])*(z_cache[i] - z_mean[secondCounter]);
        }

        x_dev[secondCounter] /= senMax;
        y_dev[secondCounter] /= senMax;
        z_dev[secondCounter] /= senMax;

        secondCounter++;
        secondCounter%=secMax;

        if(secondCounter == 0)
        {
            performClassifiction();
        }


    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        sensorType = event.sensor.getType();
        strBuilder.append(event.timestamp);
        strBuilder.append(",");
        strBuilder.append(sensorType);
        strBuilder.append(",");
        strBuilder.append(event.accuracy);
        strBuilder.append(",");
        strBuilder.append(event.values[0]);
        strBuilder.append(",");
        strBuilder.append(event.values[1]);
        strBuilder.append(",");
        strBuilder.append(event.values[2]);
        strBuilder.append("\n");

        this.x_cache[curCounter] = event.values[0];
        this.y_cache[curCounter] = event.values[1];
        this.z_cache[curCounter] = event.values[2];

        curCounter++;
        curCounter %= senMax;

        if (curCounter ==0)
            processDataAndClassify();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class fileSaveThread extends Thread {
        long st;
        StringBuilder sb;

        public fileSaveThread(StringBuilder sb) {
            this.st = st;
            this.sb = sb;
        }

        @Override
        public void run() {
            try {
                Log.i("Thread Called", "for saving sensor samples");
                String str = sb.toString();
                if (str.length() == 0)
                    return;
                FileUtil.saveStringToFile(file_tag, str);

            } catch (Exception ex) {
                Log.i("Sensor File Save", ex.toString());
            }
        }

    }



    /**
     * *****************************************
     */
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
