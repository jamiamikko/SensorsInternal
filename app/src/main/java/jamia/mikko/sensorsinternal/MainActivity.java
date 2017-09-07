package jamia.mikko.sensorsinternal;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor gravity;
    private TextView standardGravity;
    private Button startScanning, stopScanning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        standardGravity = (TextView) findViewById(R.id.standardGravity);
        startScanning = (Button) findViewById(R.id.startScan);
        stopScanning = (Button) findViewById(R.id.stopScan);

        if(sensorExists()) {

            gravity = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);


            startScanning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sm.registerListener(MainActivity.this, gravity, SensorManager.SENSOR_DELAY_NORMAL);
                }
            });

            stopScanning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sm.unregisterListener(MainActivity.this);
                }
            });

        } else {

            Toast.makeText(this, getString(R.string.sensorNotFound), Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        standardGravity.setText("");
        float force = sensorEvent.values[0];

        String forceString = String.format("%.02f", force);

        standardGravity.setText(getString(R.string.standardGravity) + " " +  forceString);

        if(force > 2.0) {
            getWindow().getDecorView().setBackgroundColor(Color.RED);
        } else{
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public Boolean sensorExists() {
        if(sm.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
            return true;
        } else {
            return false;
        }
    }

    public void getAllSensors() {
        List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ALL);

        ArrayAdapter myAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, allSensors);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(myAdapter);
    }
}
