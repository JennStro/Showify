package com.example.showify;



import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SpotifyReceiver extends BroadcastReceiver {

    private static String formerTrackId;
    private final int CHANNEL_ID = 1;
    private TextView textView;

    static final class BroadcastTypes {
        static final String SPOTIFY_PACKAGE = "com.spotify.music";
        static final String METADATA_CHANGED = SPOTIFY_PACKAGE + ".metadatachanged";
    }

    public SpotifyReceiver(TextView textView) {
        this.textView = textView;
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

                NotificationCompat.InboxStyle notificationStyle =  new NotificationCompat.InboxStyle();
                notificationStyle.addLine("Artist: " + artistName);
                notificationStyle.addLine("Track: " + trackName);
                notificationStyle.addLine("Album: " + albumName);

                String message = "Artist: " + artistName + "\n" + "Track: " + trackName + "\n" + "Album: " + albumName;

                if (!(textView == null)) {
                    textView.setText(message);
                    textView.invalidate();
                    Logger.getLogger("TextView: ").log(Level.INFO, textView.getText().toString());
                }


                Logger.getLogger("Spotify says:").log(Level.INFO, "Artist: " + artistName + "\n" + "Track: " + trackName + "\n" + "Album: " + albumName);

                RemoteViews expandedView = new RemoteViews(context.getPackageName(), R.layout.notification_large);

                NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "Notify")
                        .setSmallIcon(R.drawable.notification_icon)
                        //.setStyle(notificationStyle)
                        .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                        .setCustomContentView(expandedView)
                        .setContentTitle("Spotify says: ")
                        .setContentText("Artist: " + artistName + "\n" + "Track: " + trackName + "\n" + "Album: " + albumName)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setTimeoutAfter(10000)
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