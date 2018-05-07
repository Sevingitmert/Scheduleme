package com.example.merts.scheduleme;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;


/**
 * Created by furkan on 29.04.2018.
 */

public class AlertReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent ıntent) {


        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null)
        {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        NotificationHelper mNotificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = mNotificationHelper.getC2Notification(ıntent.getExtras().getString("title"),ıntent.getExtras().getString("description"));
        mNotificationHelper.getManager().notify(2, nb.build());

        /*NotificationHelper notificationHelper=new NotificationHelper(context);
        NotificationCompat.Builder nb=notificationHelper.getC1Notification();
        notificationHelper.getManager().notify(1,nb.build());*/

    }
}
