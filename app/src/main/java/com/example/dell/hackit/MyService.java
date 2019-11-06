package com.example.dell.hackit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by DELL on 10/03/2017.
 */


public class MyService extends Service {

    String time;
    ArrayList said=new ArrayList<>();

    public static Set<String> service_set1=new HashSet<String>();
    public static Set<String> service_set2=new HashSet<String>();
    public static Set<String> service_set3=new HashSet<String>();
    public static Set<String> service_set0=new HashSet<String>();
    int index=0;
    String m0="",m1="",m3="",m2="";


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        check(intent);
        return START_NOT_STICKY;
    }
    void check(Intent intent){
        index=intent.getIntExtra("index",0);
        m0=""+intent.getCharSequenceExtra("med0");

        checkTime(service_set0,0);
        if(index>1){
            checkTime(service_set1,1);
            m1=""+intent.getCharSequenceExtra("med1");
        }
        if(index>2){
            checkTime(service_set2,2);
            m2=""+intent.getCharSequenceExtra("med2");
        }
        if(index>4){
            checkTime(service_set3,3);
            m3=""+intent.getCharSequenceExtra("med3");
        }


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {

        super.onDestroy();
        Intent intent=new Intent(MyService.this,Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    void checkTime(final Set<String> seet,final int in){

    new Thread(new Runnable() {
        @Override
        public void run() {

            while(true) {

                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                try {
                    Thread.sleep(4000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                time= Medoc.pad(hour)+":" + Medoc.pad(minute);

                checkList(seet,in);

            }
        }
    }).start();


}
    void checkList(Set<String> set,int index){

                        int i = 0;
        ArrayList list = new ArrayList();
        list.addAll(set);

                while (i < list.size()) {
                    String t = list.get(i).toString();
                    if (time.equals(t) && !checkSaid(t)) {

                        said.add(list.get(i));
                        Intent intent=new Intent(MyService.this,AlertActivity.class);
                        intent.putExtra("med",sendAppropriateName(index));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    i++;
                }
        list.clear();
    }

    String sendAppropriateName(int i){
        String name="";
        if(i==0)name=m0;
        else if (i==1)name=m1;
        else if (i==2)name=m2;
        else if (i==3)name=m3;
        return name;
    }


    boolean checkSaid(String value){

        boolean tag=false;
        for(int i=0;i<said.size();i++){

            if(value.equals(said.get(i)))tag=true;
        }

        return tag;
    }


}
