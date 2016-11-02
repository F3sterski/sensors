package com.example.adam.sensory;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener, AdapterView.OnItemClickListener {
    private SensorManager ms;
    private Sensor acc;
    private Sensor mag;
    private List<Sensor> sensorList;
    TextView tvX;
    TextView tvY;
    TextView tvZ;
    TextView kierunek;
    ListView lv;
    private Sensor choosenSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ms = (SensorManager) getSystemService(SENSOR_SERVICE);
        acc = ms.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mag = ms.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        tvX = (TextView) findViewById(R.id.textView5);
        tvY = (TextView) findViewById(R.id.textView6);
        tvZ = (TextView) findViewById(R.id.textView7);
        kierunek = (TextView) findViewById(R.id.textView8);

        lv = (ListView) findViewById (R.id.ListView);
        sensorList = ms.getSensorList(Sensor.TYPE_ALL);
        lv.setAdapter(new ArrayAdapter<Sensor>(this, android.R.layout.simple_list_item_1,  sensorList));
        lv.setClickable(true);
        lv.setOnItemClickListener(this);

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
    public void onSensorChanged(SensorEvent event) {
        System.out.println(choosenSensor == event.sensor);
        if(choosenSensor != null && choosenSensor == event.sensor) {
            tvX.setText(event.values[0] + "");
            tvY.setText(event.values[1] + "");
            tvZ.setText(event.values[2] + "");
        }
    if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
        if(event.values[1] < -6) kierunek.setText("Dol");
        else if(event.values[1] > 6) kierunek.setText("Gora");
        else kierunek.setText("Srodek");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        choosenSensor = (Sensor)lv.getItemAtPosition(position);
        System.out.println(choosenSensor.getName()+" "+choosenSensor.getType());
    }
}
