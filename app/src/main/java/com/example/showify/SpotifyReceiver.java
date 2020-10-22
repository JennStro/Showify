package com.example.showify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.logging.Level;
import java.util.logging.Logger;

import static android.content.Context.POWER_SERVICE;

public class SpotifyReceiver extends BroadcastReceiver {

    private static String formerTrackId;
    private final int CHANNEL_ID = 1;

    static final class BroadcastTypes {
        static final String SPOTIFY_PACKAGE = "com.spotify.music";
        static final String METADATA_CHANGED = SPOTIFY_PACKAGE + ".metadatachanged";
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
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

                String message = "Artist: " + artistName + "\n" + "Track: " + trackName + "\n" + "Album: " + albumName;

                Logger.getLogger("Spotify says:").log(Level.INFO, message);

                wakeUpScreen(context);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.notification);
                notificationLayout.setTextViewText(R.id.artist, "Artist: " + artistName);
                notificationLayout.setTextViewText(R.id.track, "Track: " + trackName);
                notificationLayout.setTextViewText(R.id.album, "Album: " + albumName);

                NotificationCompat.Builder myNotification = new NotificationCompat.Builder(context, "Notify")
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContent(notificationLayout)
                        .setCustomContentView(notificationLayout)
                        .setTimeoutAfter(10000)
                        .setAutoCancel(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setPriority(notificationManager.IMPORTANCE_HIGH)
                        .setCustomHeadsUpContentView(notificationLayout);

                notificationManager.notify(CHANNEL_ID, myNotification.build());
            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void wakeUpScreen(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
        if (!powerManager.isInteractive()){
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"Showify::WakingUp");
            wakeLock.acquire(10000);
            PowerManager.WakeLock wakeLock_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "Showify::WakingUp");
            wakeLock_cpu.acquire(10000);
            wakeLock.release();
            wakeLock_cpu.release();
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