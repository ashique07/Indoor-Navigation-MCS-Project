package com.example.indoornavigation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GetData1 extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor mLight;
    private long startTime;

    ArrayList<Float> xList = new ArrayList<Float>();
    ArrayList<Float> yList = new ArrayList<Float>();
    ArrayList<Float> zList = new ArrayList<Float>();

    float xAverage = 0;
    float yAverage = 0;
    float zAverage = 0;

    EditText roomNumberText;
    Button saveButton;
    SQLiteOpenHelper indoorNavigationDatabaseHelper;
    SQLiteDatabase db;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data1);

        indoorNavigationDatabaseHelper = new IndoorNavigationDatabaseHelper(this);
        db = indoorNavigationDatabaseHelper.getWritableDatabase();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        roomNumberText = (EditText) findViewById(R.id.roomNumber);
        saveButton = (Button) findViewById(R.id.save);
        saveButton.setEnabled(false);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        float x = Math.abs(event.values[0]);
        float y = Math.abs(event.values[1]);
        float z = Math.abs(event.values[2]);

        // Do something with this sensor value.
        xList.add(x);
        yList.add(y);
        zList.add(z);

        // Do something with this sensor value.
        if(System.currentTimeMillis() - startTime > 5000)
        {
            sensorManager.unregisterListener(this);
            TextView xAverageView = (TextView) findViewById(R.id.xAverage);
            TextView yAverageView = (TextView) findViewById(R.id.yAverage);
            TextView zAverageView = (TextView) findViewById(R.id.zAverage);

            float xSum = 0;
            float ySum = 0;
            float zSum = 0;

            for(int i = 0; i < xList.size(); i++)
            {
                xSum = xSum + xList.get(i);
                ySum = ySum + yList.get(i);
                zSum = zSum + zList.get(i);
            }

            xAverage = xSum / xList.size();
            yAverage = ySum / yList.size();
            zAverage = zSum / zList.size();

            xAverageView.setText("X Average: " + String.valueOf(xAverage));
            yAverageView.setText("Y Average: " + String.valueOf(yAverage));
            zAverageView.setText("Z Average: " + String.valueOf(zAverage));
            saveButton.setEnabled(true);
        }
    }

    public void onSave(View view)
    {
        try
        {
            ContentValues magneticRow = new ContentValues();

            magneticRow.put("ROOM_NUMBER", roomNumberText.getText().toString());
            magneticRow.put("X_VALUE", xAverage);
            magneticRow.put("Y_VALUE", yAverage);
            magneticRow.put("Z_VALUE", zAverage);

            db.insert("MAGNETIC_FIELD", null, magneticRow);

            Toast toast = Toast.makeText(this, "New row inserted !", Toast.LENGTH_LONG);
            toast.show();

        }
        catch (SQLException e){

            Toast toast = Toast.makeText(this, "DB Anavailable", Toast.LENGTH_LONG);
            toast.show();

        }

    }

    public void onClear(View view)
    {
        try {
            db.delete("MAGNETIC_FIELD", null, null);

            Toast toast = Toast.makeText(this, "All values in table are deleted", Toast.LENGTH_LONG);
            toast.show();
        }
        catch (SQLException e){
            Toast toast = Toast.makeText(this, "DB Anavailable", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis();
        sensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

}