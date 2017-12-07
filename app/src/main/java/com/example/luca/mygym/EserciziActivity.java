package com.example.luca.mygym;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EserciziActivity extends AppCompatActivity {
    public static final String SCHEDE_DATA = "SchedeData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esercizi);

        String s = getIntent().getStringExtra("Sezione");

        SharedPreferences prefs = getSharedPreferences(SCHEDE_DATA, MODE_PRIVATE);
        String response = prefs.getString("obj","0");//second parameter is necessary ie.,Value to return if this preference does not exist.

        JSONArray array;
        try {
            array = new JSONArray(response);
            ArrayList<String> list = new ArrayList<>();

            for(int i = 0; i < array.length(); i++) {
                JSONObject obj = (JSONObject) array.get(i);
                Log.i("lillodef", "Gruppo Muscolare: " + obj.getString("Gruppo Muscolare") +
                        " | Esercizio: " + obj.getString("Esercizio") +
                        " | Sezione: " + obj.getString("Sezione"));
                if(obj.getString("Sezione").equals(s)) {
                    list.add(obj.getString("Esercizio"));
                }
                //instantiate custom adapter
                EserciziAdapter adapter = new EserciziAdapter(list, EserciziActivity.this);

                //handle listview and assign adapter
                ListView lView = (ListView) findViewById(R.id.list);
                lView.setAdapter(adapter);
            }
        } catch (JSONException e) {
            Log.e("lillo", "exception", e);
        }




    }
}
