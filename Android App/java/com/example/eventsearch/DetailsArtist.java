package com.example.eventsearch;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsArtist newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsArtist extends Fragment {

    private String artists=new String("");
    private String spotify_data,genre,id,event;
    private TableLayout tb;

    public void insert_data(String s){
        Log.i("SPOTIFY JSON DATA ",s);
        tb = getView().findViewById(R.id.spot_tb);
        if(s.length() == 14){
            TableRow tr = new TableRow(getContext());
            TextView x1 = new TextView(getContext());
            TextView x2 = new TextView(getContext());
            TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
            x1.setLayoutParams(params2);
            x2.setLayoutParams(params2);
            x1.setText(artists);
            x1.setTypeface(null, Typeface.BOLD);
            x1.setTextSize(18);
            x2.setText("No details.");
            x2.setTextSize(18);
            tr.addView(x1);
            tr.addView(x2);
            tb.addView(tr);
        }
        else {
            try {
                JSONObject object = new JSONObject(s);
                JSONArray jArray = object.getJSONArray("results");
                JSONObject json_data = jArray.getJSONObject(0);
                // Log.i("SPOTIFY JSON DATA length ", String.valueOf(jArray.length()));

                if (jArray.length() == 0) {
//                    TableRow tr = new TableRow(getContext());
//                    TextView x1 = new TextView(getContext());
//                    TextView x2 = new TextView(getContext());
//                    TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
//                    x1.setLayoutParams(params2);
//                    x2.setLayoutParams(params2);
//                    x1.setText(json_data.getString("Name"));
//                    x1.setTypeface(null, Typeface.BOLD);
//                    x2.setText("No details.");
//                    tr.addView(x1);
//                    tr.addView(x2);
//                    tb.addView(tr);
                } else {
                    TableRow tr1 = new TableRow(getContext());
                    TextView t1 = new TextView(getContext());
                    TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                    t1.setLayoutParams(params1);
                    t1.setText(json_data.getString("Name"));
                    t1.setGravity(Gravity.CENTER);
                    t1.setTypeface(null, Typeface.BOLD);
                    t1.setTextSize(18);
                    tr1.addView(t1);
                    tr1.setPadding(20,20,20,20);
                    tb.addView(tr1);

                    TableRow tr2 = new TableRow(getContext());
                    TextView t2 = new TextView(getContext());
                    TextView t3 = new TextView(getContext());
                    TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
                    t2.setLayoutParams(params2);
                    t3.setLayoutParams(params2);
                    t2.setText("Name");
                    t2.setTypeface(null, Typeface.BOLD);
                    t2.setTextSize(18);
                    t3.setText(json_data.getString("Name"));
                    t3.setTextSize(18);
                    tr2.addView(t2);
                    tr2.addView(t3);
                    tr2.setPadding(20,20,20,20);
                    tb.addView(tr2);

                    if (!json_data.getString("Followers").equals("")) {
                        TableRow tr3 = new TableRow(getContext());
                        TextView t4 = new TextView(getContext());
                        TextView t5 = new TextView(getContext());
                        t4.setLayoutParams(params2);
                        t5.setLayoutParams(params2);
                        t4.setText("Followers");
                        t4.setTypeface(null, Typeface.BOLD);
                        t4.setTextSize(18);
                        t5.setText(json_data.getString("Followers"));
                        t5.setTextSize(18);
                        tr3.addView(t4);
                        tr3.addView(t5);
                        tr3.setPadding(20,20,20,20);
                        tb.addView(tr3);
                    }

                    if (!json_data.getString("Popularity").equals("")) {
                        TableRow tr4 = new TableRow(getContext());
                        TextView t6 = new TextView(getContext());
                        TextView t7 = new TextView(getContext());
                        t6.setLayoutParams(params2);
                        t7.setLayoutParams(params2);
                        t6.setText("Popularity");
                        t6.setTypeface(null, Typeface.BOLD);
                        t6.setTextSize(18);
                        t7.setText(json_data.getString("Popularity"));
                        t7.setTextSize(18);
                        tr4.addView(t6);
                        tr4.addView(t7);
                        tr4.setPadding(20,20,20,20);
                        tb.addView(tr4);
                    }

                    if (!json_data.getString("Link").equals("")) {
                        TableRow tr5 = new TableRow(getContext());
                        TextView t8 = new TextView(getContext());
                        TextView t9 = new TextView(getContext());
                        t8.setLayoutParams(params2);
                        t9.setLayoutParams(params2);
                        t8.setText("CheckAt");
                        t8.setTypeface(null, Typeface.BOLD);
                        t8.setTextSize(18);
                        String a = json_data.getString("Link").replace("\\", "");
                        Log.i("SPOTIFY LINK AFTER CHANGE", a);
                        String map = String.format("<a href=\"%s\">Spotify</a> ", a);
                        Log.i("SPOTIFY LINK AFTER format", map);
                        Spannable spannedText = (Spannable) Html.fromHtml(map);
                        //t9.setText(Html.fromHtml(map));
                        t9.setMovementMethod(LinkMovementMethod.getInstance());
                        Spannable processedText = removeUnderlines(spannedText);
                        t9.setText(processedText);
                        t9.setTextSize(18);
                        tr5.addView(t8);
                        tr5.addView(t9);
                        tr5.setPadding(20,20,20,20);
                        tb.addView(tr5);
                    }
                }

            } catch (Exception e) {
            }
        }
        TextView t1 = new TextView(getContext());
        //t1.setText();
    }

    public static Spannable removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            p_Text.setSpan(span, start, end, 0);
        }
        return p_Text;
    }

    public void GET_ARTISTS(String id){
        String url = "https://myhw8proj-73215.wl.r.appspot.com/event_data/?Id=" + id;
        //Log.i("URL in GET_ARTIST", url);
        final String[] str = new String[1];
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String s = response.toString();
                        Log.i("RESPONSE", s);
                        try {
                            JSONObject object = new JSONObject(s);
                            JSONArray jArray = object.getJSONArray("results");
                            JSONObject json_data = jArray.getJSONObject(0);
                            artists = json_data.getString("Artist");
                            //Log.i("NEW ARTISTS INSIDE DETAILSAARTIST", str[0]);
                        }catch (Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
        //return str[0];
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            artists= extras.getString("ARTIST");
            genre = extras.getString("GENRE");
            id = extras.getString("ID");
            event = extras.getString("EVENT");
        }

        //GET_ARTISTS(id);

        artists = artists.replace("|","-");
        String[] str = artists.split(" - ");
        //Log.i("After replacement", artists);

        for(int i = 0 ; i < str.length ; i++) {
            String url = "https://myhw8proj-73215.wl.r.appspot.com//spotify_data/?Event_Name=" + str[i].replace(" ","+");
            Log.i("URL for artist details", url);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String s = response.toString();
                            //Log.i("lmlklmaf",data);
                            insert_data(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            queue.add(request);
        }


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_artist, container, false);
    }
}