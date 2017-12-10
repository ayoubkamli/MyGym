package com.example.luca.mygym;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    public static final String PREFS_USR = "PrefsUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);

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
                                        Birthday = "NULL";
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
