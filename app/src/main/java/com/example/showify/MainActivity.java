package com.example.showify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Switch;

import java.util.logging.Level;
import java.util.logging.Logger;

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
        if (receiver == null) {
            Logger.getLogger("Stopping: ").log(Level.WARNING, "Receiver is null.");
        } else {
            unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver == null) {
            Logger.getLogger("Stopping: ").log(Level.WARNING, "Receiver is null.");
        } else {
            unregisterReceiver(receiver);
        }
    }
}