package com.example.dell.hackit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.firebase.client.Firebase;

import java.util.Calendar;

import static com.example.dell.hackit.Root.call_tag;
import static com.example.dell.hackit.Root.childRef;


/**
 * Created by DELL on 29/03/2017.
 */

public class MonitorService extends Service {
Firebase ref,childref;


    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(MonitorService.this);
        ref=new Firebase(MainActivity.DB);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

         check(5000);

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void check(final long checkTimeInMilis) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                int count = 0;
                while (true) {

                    try {
                        Thread.sleep(checkTimeInMilis);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                    if (count == 60) {
                        //count= 5000 ms *2*5*6 = 5 mn ( save data in server each 5 minutes)
                        Calendar c = Calendar.getInstance();
                        String time = Medoc.pad(c.get(Calendar.HOUR)) + "h" + Medoc.pad(c.get(Calendar.MINUTE));
                        String value = time + ":Temp=" + Root.temperature + "/Humi=" + Root.humidity;
                        childRef.child("data").push().setValue(value);
                        count = 0;
                        call_tag = false;
                    }
                }
            }
        }).start();

    }
    }
