package com.example.luca.mygym;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DescrizioneEsercizioActivity extends AppCompatActivity {
    public static final String SCHEDE_DATA = "SchedeData";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descrizione_esercizio);

        TextView txtViewEs = (TextView) this.findViewById(R.id.textViewE);
        TextView txtViewGm = (TextView) this.findViewById(R.id.textViewGM);

        String es = getIntent().getStringExtra("Esercizio");

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
                if(obj.getString("Esercizio").equals(es)) {
                    txtViewGm.setText("Gruppo Muscolare: " + obj.getString("Gruppo Muscolare"));
                    txtViewEs.setText("Esercizio: " + obj.getString("Esercizio"));
                }
            }
        } catch (JSONException e) {
            Log.e("lillo", "exception", e);
        }
    }
}
