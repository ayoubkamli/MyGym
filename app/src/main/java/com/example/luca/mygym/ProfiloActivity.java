package com.example.luca.mygym;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfiloActivity extends AppCompatActivity {
    public static final String PREFS_USR = "PrefsUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        final TextView Name = (TextView) findViewById(R.id.Name);
        final TextView Surame = (TextView) findViewById(R.id.Surname);
        final TextView Birthday = (TextView) findViewById(R.id.Birthday);
        final TextView Gender = (TextView) findViewById(R.id.Gender);
        final TextView Height = (TextView) findViewById(R.id.Height);
        final TextView Weight = (TextView) findViewById(R.id.Weight);

        final RequestQueue queue = Volley.newRequestQueue(this);
        SharedPreferences prefs = getSharedPreferences(PREFS_USR, MODE_PRIVATE);
        String restoredText = prefs.getString("Id_Facebook", null);
        if (restoredText != null) {
            final String Id_Facebook = prefs.getString("Id_Facebook", "No id");//"No id" is the default value.

            // Instantiate the RequestQueue.
            String uri = "http://tscmygym.altervista.org/getProfile.php";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Log.i("cazzo", response);

                            try {
                                JSONArray array = new JSONArray(response);

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject obj = (JSONObject) array.get(i);
                                    Name.setText("Name: " + obj.getString("Nome"));

                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("cazzono", "cazzo");
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Id", Id_Facebook);
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }
}
