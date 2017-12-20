package com.example.luca.mygym;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String PREFS_USR = "PrefsUser";
    ImageView ProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ProfilePicture = findViewById(R.id.ProfilePicture);
        final TextView Name = findViewById(R.id.Name);
        final TextView Surname = findViewById(R.id.Surname);
        final TextView Birthday = findViewById(R.id.Birthday);
        final TextView Gender = findViewById(R.id.Gender);
        final TextView Height = findViewById(R.id.Height);
        final TextView Weight = findViewById(R.id.Weight);
        ImageButton Homebtn = findViewById(R.id.HomeButton);
        ImageButton EditProfilebtn = findViewById(R.id.EditButton);

        Homebtn.setOnClickListener(this);
        EditProfilebtn.setOnClickListener(this);


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
                            Log.i("cazzoresp", response);

                            try {
                                JSONArray array = new JSONArray(response);

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject obj = (JSONObject) array.get(i);
                                    Name.setText("Name: " + obj.getString("Nome").toUpperCase());
                                    Surname.setText("Surname: " + obj.getString("Cognome").toUpperCase());
                                    Birthday.setText("Birthday: " + obj.getString("Nascita").toUpperCase());
                                    Gender.setText("Gender: " + obj.getString("Sesso").toUpperCase());
                                    Height.setText("Height: " + obj.getString("Altezza").toUpperCase());
                                    Weight.setText("Weight: " + obj.getString("Peso").toUpperCase());
                                   // new DownloadImageTask((ImageView) findViewById(R.id.ProfilePicture))
                                            //.execute("https://graph.facebook.com/me/picture?access_token=<" + AccessToken.getCurrentAccessToken().toString() + ">");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.HomeButton:
                Intent profiloIntent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(profiloIntent);
                break;
            case R.id.EditButton:

                break;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                Log.i("cazzosi", "NON SAPREI");
            } catch (Exception e) {
                Log.e("cazzo", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            Log.i("cazzosi", "sto cambiando immagine");
        }
    }
}
