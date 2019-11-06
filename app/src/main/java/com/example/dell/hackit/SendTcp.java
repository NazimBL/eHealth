package com.example.dell.hackit;

import android.util.Log;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by DELL on 10/03/2017.
 */

public class SendTcp {

    public String ipAdress;
    Socket socket;


public SendTcp (final String ip, final int port) {


    new Thread(new Runnable() {
        @Override
        public void run() {
            try {

                ipAdress=ip;
                socket = new Socket("192.168.8.100",9999);
                OutputStream out = socket.getOutputStream();
                PrintWriter output = new PrintWriter(out);
                output.write("GET:email");
                output.flush();
                output.close();
                socket.close();

            } catch (Exception e) {

                Log.d("Nazim", e.toString());
            }
        }
    }).start();

}


    void get(final String key){

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OutputStream out = socket.getOutputStream();
                    PrintWriter output = new PrintWriter(out);
                    output.write("GET:"+key);
                    output.flush();
                    output.close();
                    socket.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }
    void set(final String key, final String value){
       new Thread(new Runnable() {
           @Override
           public void run() {

               try {
                   OutputStream out = socket.getOutputStream();
                   PrintWriter output = new PrintWriter(out);
                   output.write("SET:"+key+":"+value);
                   output.flush();
                   socket.close();
               }catch (Exception e){
                   e.printStackTrace();
               }
           }
       }).start();


    }
}
