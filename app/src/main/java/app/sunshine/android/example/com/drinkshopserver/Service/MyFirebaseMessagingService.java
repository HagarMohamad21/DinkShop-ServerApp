package app.sunshine.android.example.com.drinkshopserver.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import app.sunshine.android.example.com.drinkshopserver.R;
import app.sunshine.android.example.com.drinkshopserver.Retrofit.IDrinkShopServer;
import app.sunshine.android.example.com.drinkshopserver.Utils.Common;
import app.sunshine.android.example.com.drinkshopserver.Utils.NotificationHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingServ";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //implement later
        //Show notification
        if(remoteMessage.getData()!=null){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                sendNotificationAPI26(remoteMessage);
            }
            else
                sendNotification(remoteMessage);
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        updateToken(s);

    }

    private void updateToken(String token) {
        IDrinkShopServer mService= Common.getApi();
        mService.getToken
                ("Caffina Drink Shop",token,"1")
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d(TAG, "onResponse: "+response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Map<String,String> Message=remoteMessage.getData();
        String title=Message.get("title");
        String message=Message.get("message");
        Uri defaultSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true).setSound(defaultSound);
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(),builder.build());

    }

    private void sendNotificationAPI26(RemoteMessage remoteMessage) {
        Map<String,String> Message=remoteMessage.getData();
        String title=Message.get("title");
        String message=Message.get("message");
        Uri defaultSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper helper;
        Notification.Builder builder;
        helper=new NotificationHelper(this);
        builder= helper.getBuilder(title,message,defaultSound);
        NotificationManager manager=helper.getNotificationManager();
        manager.notify(new Random().nextInt(),builder.build());
    }
}
