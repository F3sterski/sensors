package com.example.adam.objects_in_air;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener{
    private SensorManager ms;
    private Sensor acc;
   // private Sensor mag;
    Button add;
    private TextView tv;
    private EditText et;
    private ArrayList<ObjectInAir> objectsList;
    private float[] actualPosition;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        objectsList = new ArrayList<>();
        ms = (SensorManager) getSystemService(SENSOR_SERVICE);
        acc = ms.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
       // mag = ms.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        tv = (TextView)findViewById(R.id.textView);
        et = (EditText)findViewById(R.id.editText);

        add = (Button)findViewById(R.id.button);
        add.setOnClickListener(this);
    }
    protected void onResume(){
        super.onResume();
        ms.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
       // ms.registerListener(this, mag, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause(){
        super.onPause();
        ms.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        actualPosition = event.values;
        if(objectsList.size()>0 && near(objectsList.get(0).position, event.values))
            tv.setText(objectsList.get(0).name);
        else tv.setText("brak obiektu");

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private boolean near(float[] a, float[] b){
       // System.out.println("a: " + Arrays.toString(a) + " b: " + Arrays.toString(b));
        return Math.abs(a[0]-b[0])<2 && Math.abs(a[1]-b[1])<2 && Math.abs(a[2]-b[2])<2;
    }

    @Override
    public void onClick(View v) {
        float[] copy = Arrays.copyOf(actualPosition,actualPosition.length);
        objectsList.add(new ObjectInAir(et.getText().toString(),copy));
        System.out.println(Arrays.toString(objectsList.get(0).position));
    }

    static final float EARTH_RADIUS = 6378137;
    //lat –szerokość i lon –długość geograficzna w radianach
    static float[] latLonToECEF(float lat,float lon,float height){
        float[] ECEF = new float[3];
        ECEF[0]=(float)((height+ EARTH_RADIUS)*Math.cos(lat)*Math.cos(lon));
        ECEF[1]=(float)((height+ EARTH_RADIUS)*Math.cos(lat)*Math.sin(lon));
        ECEF[2]=(float)((height+ EARTH_RADIUS)*Math.sin(lat));
        return ECEF;
    }
    static float[] latlonToENU(
            float xLat,float xLon,float xHeight,float uLat,float uLon,float uHeight){float[] ecefX=latLonToECEF(xLat,xLon,xHeight);
        float[] ecefU=latLonToECEF(uLat,uLon,uHeight);
        float[] offsetECEF = new float[3];
        offsetECEF[0]=ecefX[0]-ecefU[0];
        offsetECEF[1]=ecefX[1]-ecefU[1];
        offsetECEF[2]=ecefX[2]-ecefU[2];
        float[] enu = new float[3];
        enu[0]= (float)(-Math.sin(uLon)*offsetECEF[0] +Math.cos(uLon)*offsetECEF[1]);
        enu[1]= (float)(-Math.cos(uLon)*Math.sin(uLat)*offsetECEF[0] -Math.sin(uLon)*Math.sin(uLat)*offsetECEF[1] +Math.cos(uLat)*offsetECEF[2]);
        enu[2]= (float)(Math.cos(uLon)*Math.cos(uLat)*offsetECEF[0] +Math.sin(uLon)*Math.cos(uLat)*offsetECEF[1] +Math.sin(uLat)*offsetECEF[2]);
        return enu;
    }
    double getAngle(float[] a, float b[]) {
        double temp = (a[0] * b[0] + a[1] * b[1] + a[2] * b[2]) /
                Math.sqrt(a[0] * a[0] + a[1] * a[1] + a[2] * a[2]) /
                Math.sqrt(b[0] * b[0] + b[1]* b[1] + b[2] * b[2]);
        return Math.acos(temp);
    }

}
