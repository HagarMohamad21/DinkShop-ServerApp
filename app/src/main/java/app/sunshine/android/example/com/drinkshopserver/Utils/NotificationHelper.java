package app.sunshine.android.example.com.drinkshopserver.Utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import app.sunshine.android.example.com.drinkshopserver.R;

public class NotificationHelper extends ContextWrapper {
    public static String CHANNEL_ID="app.sunshine.android.example.com.drinkshop";
    public static String CHANNER_NAME="CAFFINA";
    NotificationManager notificationManager;
    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            creatChannel();
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void creatChannel() {
        NotificationChannel channel=new NotificationChannel(CHANNEL_ID
                ,CHANNER_NAME,NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getNotificationManager().createNotificationChannel(channel);

    }
    public NotificationManager getNotificationManager(){
        if(notificationManager==null)
            notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        return notificationManager ;}
    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getBuilder(String title, String message, Uri sound){
        return new Notification.Builder(getApplicationContext(),CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(sound)
                .setContentTitle(title)
                .setContentText(message);
    }
}
