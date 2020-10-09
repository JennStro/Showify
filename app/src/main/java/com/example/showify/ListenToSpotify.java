package com.example.showify;


import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListenToSpotify  {

    private final ExecutorService executorService;

    public ListenToSpotify(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void turnOff() {
        Logger.getLogger("Listen to spotify:").log(Level.INFO, "Stopped.");
    }

    public void turnOn() {
        Logger.getLogger("Listen to spotify:").log(Level.INFO, "Started.");
    }
}
