package com.example.showify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
    private ListenToSpotify listenToSpotify = new ListenToSpotify(executorService);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void switchListener() {
        Switch switcher = findViewById(R.id.switchListener);
        if (switcher.isChecked()) {
            listenToSpotify.turnOff();
        } else {
            listenToSpotify.turnOn();
        }
    }
}