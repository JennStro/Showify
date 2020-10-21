package com.example.showify;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.logging.Level;
import java.util.logging.Logger;

import static android.content.Context.POWER_SERVICE;

public class SpotifyReceiver extends BroadcastReceiver {

    private static String formerTrackId;
    private final int CHANNEL_ID = 1;
    private final Activity activity;
    private TextView textView;

    static final class BroadcastTypes {
        static final String SPOTIFY_PACKAGE = "com.spotify.music";
        static final String METADATA_CHANGED = SPOTIFY_PACKAGE + ".metadatachanged";
    }

    public SpotifyReceiver(Activity activity) {
        this.activity = activity;
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

                NotificationCompat.InboxStyle notificationStyle =  new NotificationCompat.InboxStyle();
                notificationStyle.addLine("Artist: " + artistName);
                notificationStyle.addLine("Track: " + trackName);
                notificationStyle.addLine("Album: " + albumName);

                String message = "Artist: " + artistName + "\n" + "Track: " + trackName + "\n" + "Album: " + albumName;

                Logger.getLogger("Spotify says:").log(Level.INFO, message);

                wakeUpScreen(context);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                notificationManager.getNotificationChannel("Notify").enableLights(true);
                notificationManager.getNotificationChannel("Notify").setLockscreenVisibility(1);

                NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "Notify")
                        .setSmallIcon(R.drawable.notification_icon)
                        .setStyle(notificationStyle)
                        .setContentTitle("Spotify says: ")
                        .setPriority(notificationManager.IMPORTANCE_NONE)
                        .setWhen(0)
                        .setTimeoutAfter(5000)
                        .setAutoCancel(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);



                RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.notification);

                NotificationCompat.Builder myNotification = new NotificationCompat.Builder(context, "Notify")
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContent(notificationLayout)
                        //.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                        .setCustomContentView(notificationLayout)
                        .setTimeoutAfter(5000)
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
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"Shofity::WakingUp");
            wakeLock.acquire(10000);
            PowerManager.WakeLock wakeLock_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "Shofity::WakingUp");
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