package com.example.user.travel;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.travel.db.config.TravelDbHelper;
import com.example.user.travel.db.config.TravelDestination;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TravelDbHelper mHelper;
    private ListView mTravelListView;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateUI();
    }

    private void updateUI() {
        ArrayList<String> travelList = new ArrayList<>();

        mHelper = new TravelDbHelper(this);
        mTravelListView = (ListView) findViewById(R.id.add_travel_destination);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TravelDestination.TravelEntry.TABLE,
                new String[]{TravelDestination.TravelEntry._ID, TravelDestination.TravelEntry.COL_TRAVEL_TITLE},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TravelDestination.TravelEntry.COL_TRAVEL_TITLE);
            Log.d(TAG, "Travel Destination: " + cursor.getString(idx));
            travelList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.travel_list,
                    R.id.add_travel_destination,
                    travelList);
            mTravelListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(travelList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_travel:
                Log.i(TAG, "Add a new travel");
                final EditText travelEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new travel destination")
                        .setMessage("Where do you want to travel?")
                        .setView(travelEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String travel = String.valueOf(travelEditText.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(TravelDestination.TravelEntry.COL_TRAVEL_TITLE, travel);
                                db.insertWithOnConflict(TravelDestination.TravelEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                updateUI();
                            }
                        }
                        )
                        .setNegativeButton("Cancel", null)
                        .create();

                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void deleteTravel(View view) {
        View parent = (View) view.getParent();
        TextView travelTextView = (TextView) parent.findViewById(R.id.add_travel_destination);
        String travel = String.valueOf(travelTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TravelDestination.TravelEntry.TABLE,
                TravelDestination.TravelEntry.COL_TRAVEL_TITLE + " = ?",
                new String[]{travel});
        db.close();
        updateUI();
    }
}
