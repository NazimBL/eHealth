package com.example.dell.hackit;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.speech.tts.TextToSpeech;

import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;



public class Root extends Activity {

    public static boolean connected = false;
    public static BluetoothSocket socket;
    public static Handler myHandler = new Handler();
    public static TextView temp_text,heartBeat,humidity_text;
    public static float temperature,heartbit=60,humidity;
    public static Button con;
    public static  Context activity;
    public static boolean call_tag=false;
    public static ViewGroup viewGroup;
    public static LayoutInflater layoutInflater;
    public static Firebase ref,childRef;

    BluetoothDevice btdevice;
    BluetoothAdapter BA;
    Set<BluetoothDevice> pairedDevices;
    String phone1="",phone2="",op="read";
    TextToSpeech textToSpeech;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        init();
        getPhoneFromDb();
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BA = BluetoothAdapter.getDefaultAdapter();

                if(!connected) {
                    if (!BA.isEnabled()) on();
                    con.setEnabled(false);
                    connectToDevice();
                }
                if(connected)turnOffBt();}
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(connected){
                    ThreadCommunication comm=new ThreadCommunication(socket,op);
                    op="read";
                    comm.start();
                    startService(new Intent(Root.this,MonitorService.class));
                    check(5000);
                }
                else MainActivity.myToast(Root.this,"NOT CONNECTED",getLayoutInflater(),viewGroup);

            }
        });
    }

    public void on(){
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turning On Bluetooth",Toast.LENGTH_LONG).show();
        }
    }
    public void check(final long checkTimeInMilis){


        new Thread(new Runnable() {
            @Override
            public void run() {

                while(true) {

                    tempCheckThreeshold();
                    try {
                        Thread.sleep(checkTimeInMilis);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    void getPhoneFromDb(){
        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    phone1 = dataSnapshot.getValue(User.class).getSos1();
                    phone2 = dataSnapshot.getValue(User.class).getSos2();
                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                MainActivity.myToast(Root.this,"Connect to Acess your Profile Info",getLayoutInflater(),viewGroup);
            }
        });

    }
    void tempCheckThreeshold(){

        if ((temperature > 20 || heartbit > 120)&& !call_tag ) {
            call_tag=true;
            if(Sos.emergency_call) call(phone1);
            if(Sos.emergency_sms)sendSms();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak("your temperature is quite high you should see someone", TextToSpeech.QUEUE_FLUSH, null, null);
            }
        }
    }

    public void call(String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        startActivity(callIntent);

    }
    public void sendSms(){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone1, null, Sos.sosSms, null, null);

    }
    public  void buildAlertMessage() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Time to take your Medecine !")
                .setCancelable(false)

                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {

                        dialog.cancel();
                        startActivity(new Intent(Root.this,Medoc.class));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    void turnOffBt(){

        if(BA.isEnabled() && connected){
            connected=false;
            try{
                socket.close();
            }catch (Exception e){

            }

        }
        con.setText(" Connect Bracelet ");
    }
    void init() {

        call_tag=false;
        activity=Root.this;
        humidity_text=(TextView)findViewById(R.id.humidity);
        humidity_text.setTypeface(MainActivity.tp);
        viewGroup=(ViewGroup) findViewById(R.id.toast_layout_id);

        layout=(LinearLayout)findViewById(R.id.activity_root);
        layoutInflater=getLayoutInflater();

        Firebase.setAndroidContext(Root.this);
        ref=new Firebase(MainActivity.DB);
        childRef=ref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        temp_text=(TextView)findViewById(R.id.temp);
        temp_text.setTypeface(MainActivity.tp);
        heartBeat=(TextView)findViewById(R.id.heart);
        heartBeat.setTypeface(MainActivity.tp);
        con=(Button) findViewById(R.id.device_sw);
        con.setTypeface(MainActivity.tp);

        textToSpeech=new TextToSpeech(Root.this, new android.speech.tts.TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != android.speech.tts.TextToSpeech.ERROR) {

                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

    }
    void connectToDevice(){
        pairedDevices = BA.getBondedDevices();
        ArrayList list = new ArrayList();

        for (BluetoothDevice bt : pairedDevices) {
            list.add(bt.getName());
            if (bt.getName().equals("HC-06")) {
                btdevice = bt;
                Log.d("Nazim", "detected");
            }

        }
        if (btdevice != null) {
            ThreadConnection connection = new ThreadConnection(btdevice, op);
            connection.start();

        }
    }
}