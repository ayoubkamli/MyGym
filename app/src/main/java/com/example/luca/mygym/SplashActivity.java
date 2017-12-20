package com.example.luca.mygym;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

/**
 * La SplashActiviy è la prima activity a partire, serve a controllare se si è già loggati con
 * Facebook o Google, e si viene mandati all'activity di login in caso non si sia già effettuato,
 * oppure alla Main activity.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Variabile booleana che restituisce true se si è loggati con Facebook
        final boolean loggedInFacebook;
        loggedInFacebook = isFacebookLoggedIn();
        //Variabile booleana che restituisce true se si è loggati con Google
        final boolean loggedInGoogle;
        loggedInGoogle = isSignedIn();
        //Funzione per mettere in pausa l'esecuzione e dare la possibilità di visualizzare l'immagine di background
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                if (loggedInFacebook || loggedInGoogle) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                } else{
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        }, 300);
    }

    public boolean isFacebookLoggedIn(){
        return AccessToken.getCurrentAccessToken() != null;
    }
    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }
}
