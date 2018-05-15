package com.example.merts.scheduleme;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;

/**
 * Created by furkan on 6.05.2018.
 */

public class LocationReceiver extends BroadcastReceiver {
    //private FusedLocationProviderClient fusedLocationProviderClient;
    //private PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent ıntent) {

        //fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(context);
        LocationResult locationResult=LocationResult.extractResult(ıntent);
        //pendingIntent = PendingIntent.getBroadcast(context, 1, ıntent, PendingIntent.FLAG_NO_CREATE);
        Location targetLocation=new Location("");
        SharedPreferences settings=context.getSharedPreferences("preferences",Context.MODE_PRIVATE);
        double tlat=settings.getFloat("tlat",0);
        double tlon=settings.getFloat("tlon",0);
        targetLocation.setLatitude(tlat);
        targetLocation.setLongitude(tlon);
        float selecteddistance=settings.getFloat("selecteddistance",0);
        if(locationResult!=null){
            Location location=locationResult.getLastLocation();
            if(location!=null){
            System.out.println(location.getLatitude());
            System.out.println(targetLocation.getLatitude());
            if(location.distanceTo(targetLocation)<selecteddistance){
                Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                if (alarmUri == null)
                {
                    alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                }
                Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
                ringtone.play();
                NotificationHelper mNotificationHelper = new NotificationHelper(context);
                NotificationCompat.Builder nb = mNotificationHelper.getC2Notification(settings.getString("title",""),settings.getString("description",""));
                mNotificationHelper.getManager().notify(2, nb.build());

            }

            }

    }
}}