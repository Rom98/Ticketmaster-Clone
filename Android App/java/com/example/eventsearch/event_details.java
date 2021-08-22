package com.example.eventsearch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HeterogeneousExpandableList;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

public class event_details extends AppCompatActivity {

    private String id,venue,event_name,date,genre,artist,LAT, LNG,event_x;
    public String individual_event_data, spotify_data, venue_data;
    private static final int NUM_PAGES = 3;

    private TextView txt;
    private ViewPager2 viewpager2;
    private TabLayout eDetailsTabLayout;
    private FragmentStateAdapter pagerAdapter;
    private ImageView Tweet, Heart;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();
        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
                return;
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String data = extras.getString("ID");
            Log.i("responseID", data);
            id = data;
            venue = extras.getString("VENUE");
            event_name = extras.getString("EVENT");
            event_x = extras.getString("EVENTX");
            date = extras.getString("DATE");
            genre = extras.getString("GENRE");
            artist = extras.getString("ARTIST");
            LAT = extras.getString("Latitude");
            LNG = extras.getString("Longitude");
        }

        Tweet = findViewById(R.id.twitter);                                 //TWITTER THING
        Tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetUrl = "https://twitter.com/intent/tweet?text=Check out "+ event_x + " at "+venue+".";
                Uri uri = Uri.parse(tweetUrl.replace(" ", "+"));
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });

        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserEventPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Heart = findViewById(R.id.event_details_heart);                    //FAVOURITE AN EVENT IN DETAILS

        if(sp.contains(id)){
            //Heart.setBackgroundResource(R.drawable.heart_outline_white);
            Heart.setBackgroundResource(R.drawable.heart_fill_red);

        }
        else
            Heart.setBackgroundResource(R.drawable.heart_fill_white);

        Heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sp.contains(id)){
                    editor.remove(id);
                    Heart.setBackgroundResource(R.drawable.heart_fill_white);
                }
                else{
                    String s = event_x+","+venue+","+date+","+genre+","+artist+","+LAT+","+LNG;
                    Log.i("fav data inserted", s);
                    editor.putString(id, s);
                    Heart.setBackgroundResource(R.drawable.heart_fill_red);
                }
                editor.commit();
            }
        });

        txt = findViewById(R.id.details_eventname);
        txt.setTypeface(null, Typeface.BOLD);
        txt.setText(event_x);


        viewpager2 = findViewById(R.id.details_viewpager);
        viewpager2.setUserInputEnabled(true);

        pagerAdapter = new SearchFragmentPagerAdapter(this);
        viewpager2.setAdapter(pagerAdapter);

        eDetailsTabLayout = findViewById(R.id.EventDetailsTabLayout);

        eDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Events")) {
                    viewpager2.setCurrentItem(0, true);
                } else if (tab.getText().equals("Artist(s)")) {
                    viewpager2.setCurrentItem(1, true);
                }
                else if (tab.getText().equals("Venue")){
                    viewpager2.setCurrentItem(2, true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


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


    private class SearchFragmentPagerAdapter extends FragmentStateAdapter {

        public SearchFragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            switch (position) {
                case 0:
                    return new DetailsEvent();
                case 1:
                    return new DetailsArtist();
                case 2:
                    return new DetailsVenue();
                default:
                    return new DetailsEvent();
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

}