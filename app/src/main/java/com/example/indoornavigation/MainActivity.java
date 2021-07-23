package com.example.indoornavigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0)
                {
                    //Get Data
                    Intent intent = new Intent (MainActivity.this, GetData1.class);

                    startActivity(intent);
                }
                if(position == 1)
                {
                    //Track Location
                    Intent intent = new Intent(MainActivity.this, TrackLocation.class);

                    startActivity(intent);

                }
                if(position == 2)
                {
                    //View, Delete and Edit Data
                    Intent intent = new Intent (MainActivity.this, ViewData.class);

                    startActivity(intent);

                }
            }
        };

        ListView listView = (ListView)findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);
    }
}