package com.example.luca.mygym;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

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
    public String Id_Facebook = "N.D.";
    public String Id_Google = "N.D.";
    public String Name = "N.D.";
    public String Surname = "N.D.";
    public String Birthday = "N.D.";
    public String Gender = "N.D.";
    public String Picture = "N.D.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = findViewById(R.id.login_button);

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        final RequestQueue queue = Volley.newRequestQueue(this);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {// App code

                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {
                                    final String IdFacebook = object.getString("id");
                                    final String Name = object.getString("first_name");
                                    final String Surname = object.getString("last_name");
                                    final String Birthday ;
                                    if (object.has("birthday")) {
                                        Birthday = object.getString("birthday");
                                    } else {
                                        Birthday = "null";
                                    }
                                    final String Gender = object.getString("gender");
                                    final String Image_url = "http://graph.facebook.com/" + IdFacebook + "/picture?type=large";

                                    // Instantiate the RequestQueue.
                                    String uri = "http://tscmygym.altervista.org/reg.php";

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
                                    })
                                    {
                                        @Override
                                        protected Map<String,String> getParams(){
                                            Map<String,String> params = new HashMap<>();
                                            params.put("Id", IdFacebook);
                                            params.put("Name", Name);
                                            params.put("Surname", Surname);
                                            params.put("Birthday", Birthday);
                                            params.put("Gender", Gender);
                                            params.put("Image_url", Image_url);

                                            return params;
                                        }
                                    };
                                    // Add the request to the RequestQueue.
                                    queue.add(stringRequest);

                                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_USR, MODE_PRIVATE).edit();
                                    editor.putString("Id_Facebook", IdFacebook);
                                    editor.apply();

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, birthday, gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();

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

    // [START handleSignInResult]
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Id_Google = account.getId();
            Name = account.getGivenName();
            Surname = account.getFamilyName();
            Picture = String.valueOf(account.getPhotoUrl());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.login_fb:
                facebookLogin();
                break;
        }
    }
}
