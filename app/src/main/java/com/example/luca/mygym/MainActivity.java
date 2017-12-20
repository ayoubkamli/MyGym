package com.example.luca.mygym;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button profilebtn = findViewById(R.id.Profile);
        Button workoutbtn = findViewById(R.id.Workout);
        Button statsbtn = findViewById(R.id.Stats);
        Button morebtn = findViewById(R.id.More);

        profilebtn.setOnClickListener(this);
        workoutbtn.setOnClickListener(this);
        statsbtn.setOnClickListener(this);
        morebtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Profile:
                Intent profiloIntent = new Intent(MainActivity.this, ProfiloActivity.class);
                startActivity(profiloIntent);
                break;
            case R.id.Workout:
                Intent schedeIntent = new Intent(MainActivity.this, SchedeActivity.class);
                startActivity(schedeIntent);
                break;
            case R.id.Stats:
                //Intent profiloIntent = new Intent(MainActivity.this, ProfiloActivity.class);
                //startActivity(profiloIntent);
                break;
            case R.id.More:
                //Intent profiloIntent = new Intent(MainActivity.this, ProfiloActivity.class);
                //startActivity(profiloIntent);
                break;
        }
    }
}
