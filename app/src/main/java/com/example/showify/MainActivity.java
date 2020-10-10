package com.example.showify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private Switch switcher;
    private SpotifyReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.switcher = findViewById(R.id.switchListener);
        this.receiver = new SpotifyReceiver();
        IntentFilter filter = new IntentFilter("com.spotify.music.metadatachanged");
        this.registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(receiver);
        } catch(IllegalArgumentException e) {

            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(receiver);
        } catch(IllegalArgumentException e) {

            e.printStackTrace();
        }
    }
}