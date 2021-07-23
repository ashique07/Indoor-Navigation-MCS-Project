package com.example.indoornavigation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ViewData extends ListActivity {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ListView listMagneticFields = getListView();

        try
        {
            SQLiteOpenHelper indoorNavigationDatabaseHelper = new IndoorNavigationDatabaseHelper(this);

            db = indoorNavigationDatabaseHelper.getReadableDatabase();

            cursor = db.query("MAGNETIC_FIELD",
                    new String[]{"_id", "ROOM_NUMBER", "X_VALUE", "Y_VALUE", "Z_VALUE"},
                    null, null, null, null, null);

            CursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    R.layout.row_layout,
                    cursor,
                    new String[]{"ROOM_NUMBER", "X_VALUE", "Y_VALUE", "Z_VALUE"},
                    new int[]{R.id.FirstText, R.id.SecondText, R.id.ThirdText, R.id.FourthText},
                    0);

            listMagneticFields.setAdapter(listAdapter);

        }
        catch (SQLiteException e)
        {
            Toast toast = Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //DELETE

    }
}