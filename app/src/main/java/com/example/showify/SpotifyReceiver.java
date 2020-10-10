package com.example.showify;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SpotifyReceiver extends BroadcastReceiver {

    private static String formerTrackId;

    static final class BroadcastTypes {
        static final String SPOTIFY_PACKAGE = "com.spotify.music";
        static final String METADATA_CHANGED = SPOTIFY_PACKAGE + ".metadatachanged";
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Logger.getLogger("Received:").log(Level.INFO, "Receiver got a message");
        if (action.equals(BroadcastTypes.METADATA_CHANGED)) {
            String trackId = intent.getStringExtra("id");
            if (!messageIsDuplicate(trackId)) {
                String artistName = intent.getStringExtra("artist");
                String albumName = intent.getStringExtra("album");
                String trackName = intent.getStringExtra("track");
                Logger.getLogger("Spotify says:").log(Level.INFO, "Artist: " + artistName + " Track: " + trackName + " Album: " + albumName);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notify")
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("hei")
                        .setContentText("hello")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setTimeoutAfter(50)
                        .setAutoCancel(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                notificationManager.notify(1, builder.build());
            }
        }
    }

    private boolean messageIsDuplicate(String trackid) {
        if (trackid.equals(formerTrackId)) {
            return true;
        }
        formerTrackId = trackid;
        return false;
    }
}