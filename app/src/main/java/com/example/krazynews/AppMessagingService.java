package com.example.krazynews;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class AppMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("nortificationToken", s).apply();
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("nortificationToken", "empty");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        JSONObject jsonObject = new JSONObject(remoteMessage.getData());
        Log.d("remoteMessage", "onMessageReceived: "+ jsonObject);
        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),remoteMessage.getData().get("id"));
    }

    public void showNotification(String title, String message, String id){
        Notification.Builder builder = null;
        Intent i  = new Intent(this, MainActivity.class);
        i.putExtra("notificationsNewsId",id);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,1,i,PendingIntent.FLAG_UPDATE_CURRENT);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this,"AppNotification")
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                    .setColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
        }

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(999,builder.build());
    }
}
