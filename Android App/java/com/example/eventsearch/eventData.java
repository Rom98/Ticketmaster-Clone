package com.example.eventsearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class eventData extends AppCompatActivity {

    RecyclerView recyclerView;
    eventItemsAdapter adapter;

    public String EventData;
    public ImageView Heart;
    private TextView t;
    JSONArray jArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_items);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Results");
        t = findViewById(R.id.event_list_text);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String data = extras.getString("JSON");
            Log.i("responseJSON", data);
            EventData = data;
        }
        if(EventData.length() == 14){
            t.setText("No data.");
            t.setVisibility(View.VISIBLE);
        }
        else{
            t.setVisibility(View.INVISIBLE);
        }

        List<items> d = new ArrayList<>();
        try {

            JSONObject object = new JSONObject(EventData);                      //Extract data from main results and display info for each item
            JSONArray jArray = object.getJSONArray("results");
            Log.i("Data inside eventData activity", jArray.toString());

//        Log.i("Data inside eventData activity", jArray.toString());
            // Extract data from json and store into ArrayList as class objects
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);;

                items Data = new items();

                Data.name = json_data.getString("Event");
                Data.venue = json_data.getString("Venue");
                Data.date = json_data.getString("Date");
                Data.genre = json_data.getString("Genre");
                Data.id = json_data.getString("Id");
                Data.artist = json_data.getString("Artist");
                Data.Lat = json_data.getString("Lat");
                Data.Lng = json_data.getString("Lng");
                d.add(Data);
            }

            recyclerView = findViewById(R.id.recycle_items);
            adapter = new eventItemsAdapter(this, d);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            recyclerView.setAdapter(adapter);


        } catch (JSONException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}