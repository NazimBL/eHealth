package com.example.dell.hackit;

import android.app.Notification;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by DELL on 10/03/2017.
 */

public class ThreadCommunication extends Thread {

    InputStream connectedinputStream;
    OutputStream connectedoutputStream;
    BluetoothSocket bluetoothSocket;
    boolean dala_dala=false;

    float c;
    String op;
    private byte[] mmBuffer;

    public ThreadCommunication(BluetoothSocket connectedbtsocket,String code) {

        InputStream inputStream = null;
        OutputStream outputStream = null;
        bluetoothSocket = connectedbtsocket;
        op=code;
        try {
            inputStream = connectedbtsocket.getInputStream();
            outputStream = connectedbtsocket.getOutputStream();

        } catch (Exception e) {
            Log.d("Nazim", e.toString());
        }

        connectedinputStream = inputStream;
        connectedoutputStream = outputStream;

    }

    @Override
    public void run() {

        mmBuffer = new byte[1024];
        int numBytes;
        Log.d("Nazim","Runn fct");
        //Root.tag=true;
        while (true) {

            try {

                numBytes =connectedinputStream.read(mmBuffer);

               final String readMessage = new String(mmBuffer,0, numBytes);
               //decoment later
                //Log.d("Nazim",readMessage);
                if(readMessage!=""){
                    Root.myHandler.post(new Runnable() {
                        @Override
                        public void run() {

                      try{

                          float float_value=Float.parseFloat(readMessage);
                            if(float_value>12 && !dala_dala)
                            {
                                Root.temp_text.setText(float_value+"°");
                                Root.temperature=float_value;

                            }
                          else if (float_value>=12 && dala_dala ){
                                Root.humidity_text.setText(float_value+"%");
                                Root.humidity=float_value;
                            }
                          dala_dala=!dala_dala;
                      }catch (Exception e){
                          //use this outuput to improve your code
                          // Log.d("Nazim",ex.toString());
                      }
                     }
                    });
                }

            } catch (IOException e) {
                Log.d("Nazim", "Input stream was disconnected", e);
                break;
            }
        }

    }
    public void write(String op){
       // Root.tag=false;
        try {
            connectedoutputStream.write(op.getBytes());
            Log.d("Nazim","OKKKK");
        } catch (IOException e) {
            Log.d("Nazim",e.toString());
        }

    }


}
