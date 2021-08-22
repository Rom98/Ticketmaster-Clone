package com.example.eventsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{     //implements LocationListener

    private static final int NUM_PAGES = 2;

    private ViewPager2 viewpager2;
    private TabLayout sfTabLayout;
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//
//        if (ActivityCompat.checkSelfPermission(MainActivity.this,
//                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
////            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
////                Location location = task.getResult();
////                if (location != null) {
////                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
////
////                    List<Address> addresses = null;
////
//////                        addresses = geocoder.getFromLocation(
//////                                location.getLatitude(), location.getLongitude(), 1);
////                    //Toast.makeText(MainActivity.this, "here", Toast.LENGTH_LONG).show();
////
////
////                    Toast.makeText(MainActivity.this, location.getLatitude() + " , " + location.getLongitude(), Toast.LENGTH_LONG).show();
////                }
////
////            });
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//        }
//
//        else {
//            ActivityCompat.requestPermissions(MainActivity.this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 4);
//        }
//

        viewpager2 = findViewById(R.id.viewpager);
        viewpager2.setUserInputEnabled(false);

        pagerAdapter = new SearchFragmentPagerAdapter(this);
        viewpager2.setAdapter(pagerAdapter);

        sfTabLayout = findViewById(R.id.SearchFormtabLayout);

        sfTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Search")) {
                    viewpager2.setCurrentItem(0, true);
                } else if (tab.getText().equals("Favourites")) {
                    viewpager2.setCurrentItem(1, true);
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

    private class SearchFragmentPagerAdapter extends FragmentStateAdapter {

        public SearchFragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            switch (position) {
                case 0:
                    return new SearchFragment();
                case 1:
                    return new FavouriteFragment();
                default:
                    return new SearchFragment();
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

}