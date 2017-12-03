package com.example.luca.mygym;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.AccessToken;

/**
 * La SplashActiviy è la prima activity a partire, serve a controllare se si è già loggati con
 * Facebook o Google, e si viene mandati all'activity di login in caso non si sia già effettuato
 * oppure alla prima activity stabilita.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Variabile booleana utilizzata per vedere se si è loggati con Facebook
        boolean loggedInFb;
        loggedInFb = isFacebookLoggedIn();
        if (loggedInFb) {
            Intent intent = new Intent(SplashActivity.this, SchedeActivity.class);
            startActivity(intent);
        } else{
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public boolean isFacebookLoggedIn(){
        return AccessToken.getCurrentAccessToken() != null;
    }
}
