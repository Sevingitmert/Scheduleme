package com.example.merts.scheduleme;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Created by furkan on 29.04.2018.
 */

public class NotificationHelper extends ContextWrapper {
    public static final String c1ID="c1id";
    public static final String c1name="c 1";
    public static final String c2ID="c2id";
    public static final String c2name="c 2";
    public NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
       createChannels();
    }}
    @TargetApi(Build.VERSION_CODES.O)
    public void createChannels(){
        final NotificationChannel c1=new NotificationChannel(c1ID,c1name, NotificationManager.IMPORTANCE_DEFAULT);
        c1.enableLights(true);
        c1.enableVibration(true);
        c1.setLightColor(R.color.colorPrimary);
        c1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(c1);
        NotificationChannel c2=new NotificationChannel(c2ID,c2name, NotificationManager.IMPORTANCE_DEFAULT);
        c2.enableLights(true);
        c2.enableVibration(true);
        c2.setLightColor(R.color.colorPrimary);
        c2.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(c2);
    }
    public NotificationManager getManager(){
        if(mManager==null){
            mManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
    public NotificationCompat.Builder getC1Notification(){
        return new NotificationCompat.Builder(getApplicationContext(),c1ID)
               // .setContentTitle(title)
               // .setContentText(description)
                .setSmallIcon(R.drawable.ic_one);

    }

    public NotificationCompat.Builder getC2Notification(String title,String description){
        return new NotificationCompat.Builder(getApplicationContext(),c2ID)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_two);

    }

}
