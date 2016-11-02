package com.example.adam.rotation_app;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
    private SensorManager ms;
    private Sensor acc;
    private Sensor mag;
    float[] mGravs = new float[3];
    float[] mGeoMags = new float[3];
    float[] mInclinationM = new float[16];
    float[] mRotationM = new float[3];
    float[] orientation = new float[3];
    EditText et1 = null;
    EditText et2 = null;
    EditText et3 = null;
    Button b = null;
    TextView tv1 = null;
    TextView tv2 = null;
    TextView tv3 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ms = (SensorManager) getSystemService(SENSOR_SERVICE);
        acc = ms.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mag = ms.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        et1 = (EditText) findViewById(R.id.editText2);
        et2 = (EditText) findViewById(R.id.editText3);
        et3 = (EditText) findViewById(R.id.editText4);
        b = (Button) findViewById(R.id.button);
        b.setOnClickListener(this);
        tv1 = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);

    }

    protected void onResume(){
        super.onResume();
        ms.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
        ms.registerListener(this, mag, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause(){
        super.onPause();
        ms.unregisterListener(this);
    }

    @Override
    public void onClick(View v) {
        mRotationM[0] = Float.valueOf(et1.getText().toString());
        mRotationM[1] = Float.valueOf(et2.getText().toString());
        mRotationM[2] = Float.valueOf(et3.getText().toString());
        System.out.println(Arrays.toString(mRotationM));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                System.arraycopy(event.values, 0, mGravs, 0, 3);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(event.values, 0, mGeoMags, 0, 3);
                break;
            default:
                return;
        }

        // If mGravs and mGeoMags have values then find rotation matrix
        if (mGravs != null && mGeoMags != null) {

            // checks that the rotation matrix is found
            if(SensorManager.getRotationMatrix(mRotationM, null, mGravs, mGeoMags)){
                SensorManager.getOrientation(mRotationM, orientation);
                tv1.setText(String.valueOf(Math.toDegrees(orientation[0])));
                tv1.setText(String.valueOf(Math.toDegrees(orientation[1])));
                tv3.setText(String.valueOf(Math.toDegrees(orientation[2])));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
