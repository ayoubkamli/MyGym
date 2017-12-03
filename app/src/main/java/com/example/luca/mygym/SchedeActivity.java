package com.example.luca.mygym;

import android.annotation.SuppressLint;
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

import java.util.HashMap;
import java.util.Map;

public class SchedeActivity extends AppCompatActivity {
    public static final String PREFS_USR = "PrefsUser";



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schede);
        final RequestQueue queue = Volley.newRequestQueue(this);
        final TextView t = (TextView)findViewById(R.id.nomecognome);

        SharedPreferences prefs = getSharedPreferences(PREFS_USR, MODE_PRIVATE);
        String restoredText = prefs.getString("Id_Facebook", null);
        if (restoredText != null) {
            final String Id_Facebook = prefs.getString("Id_Facebook", "No id");//"No id" is the default value.
            t.setText("Ciao, " + Id_Facebook);

            // Instantiate the RequestQueue.
            String uri = "http://tscmygym.altervista.org/schede.php";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Log.i("cazzosi", response);
                            t.setText(response);
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
