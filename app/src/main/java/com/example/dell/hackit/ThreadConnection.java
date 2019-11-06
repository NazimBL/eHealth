package com.example.dell.hackit;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

/**
 * Created by DELL on 10/03/2017.
 */

public class ThreadConnection extends Thread {

    private BluetoothSocket bluetoothSocket = null;
    private BluetoothDevice bluetoothDev;
    private BluetoothAdapter BA;
    String op;

    public ThreadConnection(BluetoothDevice bD,String code) {
        this.bluetoothDev = bD;
        BluetoothSocket socket=null;
        op=code;

        try{
            BA=BluetoothAdapter.getDefaultAdapter();
            bluetoothDev=BA.getRemoteDevice(bluetoothDev.getAddress());
            Log.d("Nazim","bluetoothDev: "+bluetoothDev);
            socket=bluetoothDev.createInsecureRfcommSocketToServiceRecord(MainActivity.MY_UUID_SECURE);
            Log.d("Nazim","BluetoothSocket : "+socket);

        }catch (Exception e){
            e.printStackTrace();
        }
        bluetoothSocket=socket;
    }

    @Override
    public void run() {

        // BA.cancelDiscovery();

        try{
            Log.d("Nazim","Thread");
            bluetoothSocket.connect();
            Root.socket=bluetoothSocket;
            Root.connected=true;
            Root.myHandler.post(new Runnable() {
                @Override
                public void run() {
                    Root.con.setEnabled(true);
                    Root.con.setText(" Turn Off ");
                    MainActivity.myToast(Root.activity,"Device Connected !",Root.layoutInflater,Root.viewGroup);

                }
            });

        }catch (Exception e){
            Root.connected=false;
            Log.d("Nazim",e.toString());

            Root.myHandler.post(new Runnable() {
                @Override
                public void run() {

                    MainActivity.myToast(Root.activity,"Connection Error Try Again !",Root.layoutInflater,Root.viewGroup);
                }
            });
            try{
                bluetoothSocket.close();

                Log.d("Nazim","Closing thread");
            }catch (Exception ex){


                 Log.d("Nazim",ex.toString());
            }
        }
    }}

