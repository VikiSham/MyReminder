package com.example.myreminder;

import static com.example.myreminder.R.drawable.*;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm is ON!", Toast.LENGTH_SHORT).show();
        MediaPlayer mp=MediaPlayer.create(context,R.raw.gong);
        mp.start();
        Intent go = new Intent(context, Something.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, go, PendingIntent.FLAG_IMMUTABLE);
        String channelId = "channel_id";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(baseline_crisis_alert_24)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setContentTitle("It's notification for you")
                .setContentText("Click to continue")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, notification);
    }
}