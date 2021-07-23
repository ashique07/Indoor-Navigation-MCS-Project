package com.example.indoornavigation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TrackLocation extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor mLight;
    private SQLiteDatabase db;
    private Cursor cursor;
    private long startTime;
    private long endTime;
    ArrayList<Float> xList = new ArrayList<Float>();
    ArrayList<Float> yList = new ArrayList<Float>();
    ArrayList<Float> zList = new ArrayList<Float>();

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_location);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        try
        {
            SQLiteOpenHelper indoorNavigationDatabaseHelper = new IndoorNavigationDatabaseHelper(this);

            db = indoorNavigationDatabaseHelper.getReadableDatabase();

            cursor = db.query("MAGNETIC_FIELD",
                    new String[]{"ROOM_NUMBER", "X_VALUE", "Y_VALUE", "Z_VALUE"},
                    null, null, null, null, null);
        }
        catch (SQLiteException e)
        {
            Toast toast = Toast.makeText(this, "DB Anavailable", Toast.LENGTH_LONG);
            toast.show();
        }

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

        xList.add(x);
        yList.add(y);
        zList.add(z);

        // Do something with this sensor value.
        if(System.currentTimeMillis() - startTime > 5000)
        {
            sensorManager.unregisterListener(this);
            displayResult(xList, yList, zList);
        }
    }

    public void displayResult(ArrayList<Float> xList, ArrayList<Float> yList, ArrayList<Float> zList){


        ArrayList<Integer> roomNumbersResult = new ArrayList<Integer>();
        ArrayList<Float> distanceResult = new ArrayList<Float>();

        for(int i = 0; i < xList.size(); i++) {

            HashMap<String, Float> distanceMap = new HashMap<String, Float>();
            float x = xList.get(i);
            float y = yList.get(i);
            float z = zList.get(i);

            Log.d("X: ", String.valueOf(x));
            Log.d("Y: ", String.valueOf(y));
            Log.d("Z: ", String.valueOf(z));

            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                String roomNumber = cursor.getString(0);
                float xRow = cursor.getFloat(1);
                float yRow = cursor.getFloat(2);
                float zRow = cursor.getFloat(3);

                float distance = (float) Math.sqrt((x - xRow) * (x - xRow) + (y - yRow) * (y - yRow) + (z - zRow) * (z - zRow));

                //Putting distance and room number in distanceMap
                distanceMap.put(roomNumber, distance);
            }

            Log.d("DISTANCE MAP", distanceMap.toString());

            //FINDING THE MINIMUM DISTANCE
            Map.Entry<String, Float> min = null;

            for (Map.Entry<String, Float> entry : distanceMap.entrySet()) {
                if (min == null || min.getValue() > entry.getValue()) {
                    min = entry;
                }
            }

            //resultMap.put(min.getKey(), min.getValue());
            roomNumbersResult.add(Integer.parseInt(min.getKey()));
            distanceResult.add(min.getValue());
        }

        Log.d("Room Number Result ", roomNumbersResult.toString());
        Log.d("Distance Result ", distanceResult.toString());

        ArrayList<TrackLocationModel> items = new ArrayList<>();
        for(int i = 0; i < xList.size(); i++)
        {
            TrackLocationModel trackLocationModel = new TrackLocationModel(
                    xList.get(i).toString(),
                    yList.get(i).toString(),
                    zList.get(i).toString(),
                    roomNumbersResult.get(i).toString(),
                    distanceResult.get(i).toString()
            );

            items.add(trackLocationModel);
        }

        Log.d("DATA ITEMS TO SHOW", items.toString());

        //Display 36 x,y,z of sensed magnetic field, room with minimum distance, minimum distance
//create your adapter, use the nameAndAgeList ArrayList
CustomListViewAdapter listViewAdapter = new CustomListViewAdapter(this, items);

//get your listView and use your adapter
ListView listView = (ListView) findViewById(R.id.list);
listView.setAdapter(listViewAdapter);


            //K NEAREST NEIGHBOUR
            int k = (int) Math.sqrt(roomNumbersResult.size());

            int [] a = new int[k];

            for(int i = 0; i < k; i++)
            {
                Float minimumResult = Float.POSITIVE_INFINITY;
                int minimumResultIndex = 0;

                //FINDING THE MINIMUM DISTANCE FROM distanceResult list
                for (int j = 0; j < distanceResult.size(); j++) {
                    if (distanceResult.get(j) < minimumResult) {
                        minimumResultIndex = j;
                        minimumResult = distanceResult.get(j);
                    }
                }
                a[i] = roomNumbersResult.get(minimumResultIndex);
                distanceResult.remove(minimumResultIndex);
                roomNumbersResult.remove(minimumResultIndex);
            }

            Arrays.sort(a);
            Log.d("a ARRAY: ", Arrays.toString(a));
            int currentCount = 1;
            int maxCount = 1;
            int maxValue = 1;

            for(int i = 0; i < a.length-1; i++)
            {
                if(a[i] == a[i+1])
                {
                    currentCount++;
                }
                else
                {
                    if(currentCount > maxCount)
                    {
                        maxCount = currentCount;
                        maxValue = a[i];
                    }

                    currentCount = 1;
                }
            }
            if(currentCount > maxCount)
            {
                maxCount = currentCount;
                maxValue = a[a.length-1];
            }

        TextView location = (TextView)findViewById(R.id.location);
        location.setText("You are in front of room number: " + maxValue);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
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
}