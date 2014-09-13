package com.jonathon_vogel.pennapps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pennapps.R;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
    
    public void createGameClick(View v) {
    	Intent intent = new Intent(this, CreateGameActivity.class);
        startActivity(intent);
    }
    
    public void joinGameClick(View v) {
    	Intent intent = new Intent(this, JoinGameActivity.class);
        startActivity(intent);
    }
}
