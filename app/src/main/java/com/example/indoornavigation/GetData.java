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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GetData extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor mLight;

    private int counter = 1;

    float xSum = 0;
    float ySum = 0;
    float zSum = 0;

    float xAverage = 0;
    float yAverage = 0;
    float zAverage = 0;

    EditText roomNumberText;
    Button saveButton;
    Button senseButton;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        roomNumberText = (EditText) findViewById(R.id.roomNumber);
        saveButton = (Button) findViewById(R.id.save);
        senseButton = (Button) findViewById(R.id.sense);

        saveButton.setEnabled(false);
        senseButton.setEnabled(true);
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

        xSum = xSum + x;
        ySum = ySum + y;
        zSum = zSum + z;

        int xId = 0;
        int yId = 0;
        int zId = 0;

        switch (counter) {

            case 1:
                xId = R.id.x1;
                yId = R.id.y1;
                zId = R.id.z1;
                break;

            case 2:
                xId = R.id.x2;
                yId = R.id.y2;
                zId = R.id.z2;
                break;

            case 3:
                xId = R.id.x3;
                yId = R.id.y3;
                zId = R.id.z3;
                break;

            case 4:
                xId = R.id.x4;
                yId = R.id.y4;
                zId = R.id.z4;
                break;

            case 5:
                xId = R.id.x5;
                yId = R.id.y5;
                zId = R.id.z5;

                xAverage = xSum/5;
                yAverage = ySum/5;
                zAverage = zSum/5;

                TextView xView = (TextView) findViewById(R.id.xAverage);
                TextView yView = (TextView) findViewById(R.id.yAverage);
                TextView zView = (TextView) findViewById(R.id.zAverage);

                xView.setText(String.valueOf(xAverage));
                yView.setText(String.valueOf(yAverage));
                zView.setText(String.valueOf(zAverage));

                saveButton.setEnabled(true);
                senseButton.setEnabled(false);

                break;
        }

        counter++;

        // Do something with this sensor value.
        TextView xView = (TextView) findViewById(xId);
        TextView yView = (TextView) findViewById(yId);
        TextView zView = (TextView) findViewById(zId);

        xView.setText(String.valueOf(x));
        yView.setText(String.valueOf(y));
        zView.setText(String.valueOf(z));

        sensorManager.unregisterListener(this);
    }

    public void onSense(View view)
    {
        sensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onSave(View view)
    {
        try
        {
            SQLiteOpenHelper indoorNavigationDatabaseHelper = new IndoorNavigationDatabaseHelper(this);

            SQLiteDatabase db = indoorNavigationDatabaseHelper.getWritableDatabase();

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

    /*
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
     */
}