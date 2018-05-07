package com.example.merts.scheduleme;

import android.app.IntentService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;


import com.google.android.gms.location.LocationResult;

/**
 * Created by furkan on 4.05.2018.
 */

public class LocationReceiver extends IntentService {


    public LocationReceiver() {

        super("Schedulemealksdlamsd");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent ıntent) {
        Location location1 = new Location("");

        SharedPreferences settings = getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        double tlat = settings.getFloat("tlat", 0);
        double tlon = settings.getFloat("tlon", 0);
        String address = settings.getString("address", "");
        location1.setLatitude(tlat);
        location1.setLongitude(tlon);


        if (LocationResult.hasResult(ıntent)) {

            LocationResult locationResult = LocationResult.extractResult(ıntent);
            Location location = locationResult.getLastLocation();

            if (location != null) {
                System.out.println("amksdma" + String.valueOf(location.getLatitude()));
                /*Intent i = new Intent(getApplicationContext(), LocationBroadcast.class);
                i.putExtra("title", settings.getString("title", ""));
                i.putExtra("title", settings.getString("description", ""));
                i.putExtra("tlat",tlat);
                i.putExtra("tlon",tlon);
                i.putExtra("lat",location.getLatitude());
                i.putExtra("lon",location.getLongitude());
                sendBroadcast(i);
*/

                if(location.distanceTo(location1)<300){
                    Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    if (alarmUri == null)
                    {
                        alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    }
                    Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmUri);
                    ringtone.play();
                    System.out.println("mesafe 300den küçük");
                    NotificationHelper mNotificationHelper = new NotificationHelper(getApplicationContext());
                    NotificationCompat.Builder nb = mNotificationHelper.getC2Notification(settings.getString("title",""),settings.getString("description",""));
                    mNotificationHelper.getManager().notify(2, nb.build());
                }


            }
        }

    }
}
