package com.example.showify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
    private ListenToSpotify listenToSpotify = new ListenToSpotify(executorService);
    private Switch switcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.switcher = findViewById(R.id.switchListener);
        switcher.setChecked(false);
    }

    public void switchListener(View view) {
        if (shouldTurnOnListener()) {
            listenToSpotify.turnOn();
        } else {
            listenToSpotify.turnOff();
        }
    }

    private boolean shouldTurnOnListener() {
        return switcher.isChecked();
    }
}