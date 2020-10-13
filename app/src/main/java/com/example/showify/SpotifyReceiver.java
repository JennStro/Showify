package com.example.showify;


import android.app.Notification;
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
    private final int CHANNEL_ID = 1;

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
                NotificationCompat.BigTextStyle bigTextMessage = new NotificationCompat.BigTextStyle();
                bigTextMessage.bigText("Artist: " + artistName + "\n Track: " + trackName + "\n Album: " + albumName);
                Logger.getLogger("Spotify says:").log(Level.INFO, bigTextMessage.toString());

                NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "Notify")
                        .setSmallIcon(R.drawable.notification_icon)
                        .setStyle(bigTextMessage)
                        .setContentTitle("Spotify says: ")
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setTimeoutAfter(5000)
                        .setAutoCancel(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                notificationManager.notify(CHANNEL_ID, notification.build());
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