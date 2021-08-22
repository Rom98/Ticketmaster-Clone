package com.example.eventsearch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public SearchFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment SearchFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static SearchFragment newInstance(String param1, String param2) {
//        SearchFragment fragment = new SearchFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
    private EditText keyword, distance, given_location;
    private Spinner category, metric;
    private RadioGroup locationRadioGroup;
    private RadioButton current, customLocation,button;
    private Button search, clear;
    private String segment,cat,loc,dist;
    private String[] segmentIds = {"KZFzniwnSyZfZ7v7nJ","KZFzniwnSyZfZ7v7nE","KZFzniwnSyZfZ7v7na","KZFzniwnSyZfZ7v7nn","KZFzniwnSyZfZ7v7n1"};
    private String[] units ={"miles", "km"};
    private double lat;
    private double lng;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {        //All logic stuff here

        keyword = getView().findViewById(R.id.keyword);
        category = getView().findViewById(R.id.category);
        distance = getView().findViewById(R.id.distance);
        metric = getView().findViewById(R.id.metric);
        given_location = getView().findViewById(R.id.typed_loc);
        locationRadioGroup = getView().findViewById(R.id.locationRadioGroup);
        current = getView().findViewById(R.id.current);
        customLocation = getView().findViewById(R.id.other_loc);
        search = getView().findViewById(R.id.click_search);
        clear = getView().findViewById(R.id.clear);

        //Populating category drop down
        ArrayAdapter<CharSequence> categoriesAdapter = ArrayAdapter.createFromResource(getContext(),R.array.categories, android.R.layout.simple_spinner_item);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoriesAdapter);

        //Populating to metric drop down
        ArrayAdapter<CharSequence> metricAdapter = ArrayAdapter.createFromResource(getContext(),R.array.metric, android.R.layout.simple_spinner_item);
        metricAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        metric.setAdapter(metricAdapter);

        given_location.setEnabled(false);

        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                given_location.setEnabled(false);
                given_location.setText("");
                given_location.setError(null);
            }
        });

        customLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                given_location.setEnabled(true);
            }
        });

//        int radioId = locationRadioGroup.getCheckedRadioButtonId();
//        button = getView().findViewById(radioId);
//        if(button.getText().equals("Other. Specify Location")){
//            given_location.setFocusableInTouchMode(true);
//        }
//        else{
//           // given_location.setFocusable(false);
//        }

        //Code to check which radio button is enabled to disable text field

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(distance == null){
                    dist = "10";}
                else{
                    dist = distance.getText().toString();}

                if(given_location.getText().toString().length() == 0){
                    loc = Double.toString(lat) + "," + Double.toString(lng);
                    Log.i("current location",loc);
                }
                else{
                    loc = given_location.getText().toString();
                    Log.i("current location",loc);
                }

                if(category.getSelectedItem().toString().equals("All"))
                    cat = "Default";
                else {
                    cat = category.getSelectedItem().toString();
                    cat = cat.replace(" ", "%20");
                }

                boolean validationSuccess = formValidation();
                if(!validationSuccess) {
                    String url = "https://myhw8proj-73215.wl.r.appspot.com/form_data/?keyword=" + keyword.getText().toString().replace(" ", "+") + "&category=" + cat + "&distance=" + dist + "&metric=" + metric.getSelectedItem().toString() + "&location=" + loc;
                    getEventData(url);
                }

            };
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword.setText("");
                keyword.setError(null);
                given_location.setText("");
                given_location.setError(null);
                given_location.setEnabled(false);
                category.setSelection(0);
                metric.setSelection(0);
                distance.setText("10");
                locationRadioGroup.check(R.id.current);
            }
        });
    }

    private void getEventData(String url){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        Log.i("URL", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Resp",response.toString());
                        Intent i = new Intent(getActivity(), eventData.class);
                        i.putExtra("JSON",response.toString());
                        startActivity(i);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }

    private boolean formValidation(){
        boolean keyword_fail = false,location_fail = false;
        int radioId = locationRadioGroup.getCheckedRadioButtonId();
        button = getView().findViewById(radioId);

        if(keyword.getText().toString().trim().equals("")) {
            keyword.setError("Please enter mandatory field");
            keyword_fail = true;
            return true;
        }
        if(given_location.getText().toString().trim().equals("") && button.getText().equals("Other. Specify Location")){
            given_location.setError("Please enter mandatory field");
            location_fail = true;
            return true;
        }

        return false;
    }

    @Override                                                           //First this is called and then onViewCreated is called.All graphical stuff here
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) this.getActivity().getSystemService(getContext().LOCATION_SERVICE);

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location curlocation) {
                    lat  = curlocation.getLatitude();
                    lng  = curlocation.getLongitude();
                    //Log.i("LOCATION",lat+","+lng);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };


            if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }

        }

        else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 4);
        }

        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}