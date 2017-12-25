package com.example.luca.mygym;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //Variabili per Facebook
    private CallbackManager callbackManager;

    //Variabili per Google
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    //Shared Preferences
    public static final String PREFS_USR = "PrefsUser";

    //Variabili globali
    public HashMap<String, String> Profile = new HashMap<>();
    String uri = "http://tscmygym.altervista.org/LoginActivity.php";
    public Bitmap Picture;
    public Boolean ChangeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //OnClickListener dei bottoni di facebook e google
        findViewById(R.id.login_button).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Profile.put("Id_Facebook", "N.D.");
            Profile.put("Id_Google", account.getId());
            Profile.put("Name", account.getGivenName());
            Profile.put("Surname", account.getFamilyName());
            Profile.put("Birthday", "N.D.");
            Profile.put("Gender", "N.D.");
            //Picture = String.valueOf(account.getPhotoUrl());
            sendDB();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void sendDB() {
        Context context = getApplicationContext();
        VolleyService volleyservice = new VolleyService();
        volleyservice.getString (context, uri, Profile, new VolleyCallback() {
                    @Override
                    public void onSuccess(String data) {
                        Log.i("cazzoResume", data);
                        if (data.contains("TRUE")) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, data,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void facebookLogin(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            Log.i("Response",response.toString());

                            Profile.put("Id_Facebook", response.getJSONObject().getString("id"));
                            Profile.put("Id_Google", "N.D.");
                            Profile.put("Name", response.getJSONObject().getString("first_name"));
                            Profile.put("Surname", response.getJSONObject().getString("last_name"));
                            if (response.getJSONObject().has("birthday")) {
                                Profile.put("Birthday", response.getJSONObject().getString("birthday"));
                            }
                            Profile.put("Gender", response.getJSONObject().getString("gender"));
                            /*try {
                                URL imageUrl = new URL("https://graph.facebook.com/" + Id_Facebook + "/picture?type=large");
                                Picture = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                            } catch (java.io.IOException e) {
                                e.printStackTrace();
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.login_button:
                LoginManager.getInstance().logInWithReadPermissions(
                        this,
                        Arrays.asList("email", "public_profile"));

                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                facebookLogin(loginResult);
                            }

                            @Override
                            public void onCancel() {
                            }

                            @Override
                            public void onError(FacebookException exception) {
                            }
                        });
                break;
        }

        /*SharedPreferences.Editor editor = getSharedPreferences(PREFS_USR, MODE_PRIVATE).edit();
        editor.putString("Id_Facebook", Profile.get("Id_Facebook"));
        editor.putString("Id_Google", Profile.get("Id_Google"));
        editor.apply();*/
    }
}