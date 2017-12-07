package com.example.luca.mygym;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SchedeActivity extends AppCompatActivity {
    public static final String PREFS_USR = "PrefsUser";
    public static final String SCHEDE_DATA = "SchedeData";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schede);
        final RequestQueue queue = Volley.newRequestQueue(this);
        SharedPreferences prefs = getSharedPreferences(PREFS_USR, MODE_PRIVATE);
        String restoredText = prefs.getString("Id_Facebook", null);
        if (restoredText != null) {
            final String Id_Facebook = prefs.getString("Id_Facebook", "No id");//"No id" is the default value.

            // Instantiate the RequestQueue.
            String uri = "http://tscmygym.altervista.org/schede.php";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            try {
                                JSONArray array = new JSONArray(response);
                                SharedPreferences.Editor editor = getSharedPreferences(SCHEDE_DATA, MODE_PRIVATE).edit();
                                editor.putString("obj", array.toString());
                                editor.apply();

                                ArrayList<String> list = new ArrayList<>();

                                for(int i = 0; i < array.length(); i++) {
                                    JSONObject obj = (JSONObject) array.get(i);
                                    Log.i("cazzosi", "Gruppo Muscolare: " + obj.getString("Gruppo Muscolare") +
                                            " | Esercizio: " + obj.getString("Esercizio") +
                                            " | Sezione: " + obj.getString("Sezione"));
                                    if(!list.contains(obj.getString("Sezione"))) {
                                        list.add(obj.getString("Sezione"));
                                    }
                                }

                                //instantiate custom adapter
                                SchedeAdapter adapter = new SchedeAdapter(list, SchedeActivity.this);

                                //handle listview and assign adapter
                                ListView lView = (ListView) findViewById(R.id.list);
                                lView.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i("cazzosi", e.toString());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("cazzono", "cazzo");
                }
            })
            {
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<>();
                    params.put("Id",Id_Facebook);
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }
}
