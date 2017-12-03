package com.example.luca.mygym;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;


import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;

    private static final int RC_SIGN_IN = 9003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton gploginButton = (SignInButton) findViewById(R.id.sign_in_button);

        gploginButton.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()) {
                String name = result.getSignInAccount().getGivenName();
                String surname = result.getSignInAccount().getFamilyName();
                String id = result.getSignInAccount().getId();
                Uri bo = result.getSignInAccount().getPhotoUrl();

                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(this);
                String uri = String.format("http://tscmygym.altervista.org/reg.php",
                        id,
                        name,
                        surname);

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                Log.i("cazzosi", response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("cazzono", "cazzo");
                    }
                });
// Add the request to the RequestQueue.
                queue.add(stringRequest);

                /*Intent intent = new Intent(MainActivity.this, SchedeActivity.class);
                intent.putExtra("Name:", name);
                intent.putExtra("Surname", surname);
                startActivity(intent);*/
            }
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {   }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.sign_in_button:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }
}
