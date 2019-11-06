package com.example.dell.hackit;

import android.app.Activity;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HelpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        TextView t0=(TextView)findViewById(R.id.t0);
        TextView t1=(TextView)findViewById(R.id.t1);
        TextView t2=(TextView)findViewById(R.id.t2);
        TextView t3=(TextView)findViewById(R.id.t3);
        TextView t4=(TextView)findViewById(R.id.t4);
        TextView t5=(TextView)findViewById(R.id.t5);
        TextView t6=(TextView)findViewById(R.id.t6);
        TextView t7=(TextView)findViewById(R.id.t7);
        TextView t8=(TextView)findViewById(R.id.t8);
        TextView t9=(TextView)findViewById(R.id.t9);
        TextView t10=(TextView)findViewById(R.id.t10);

        t0.setTypeface(MainActivity.tp);
        t1.setTypeface(MainActivity.tp);
        t2.setTypeface(MainActivity.tp);
        t3.setTypeface(MainActivity.tp);
        t4.setTypeface(MainActivity.tp);
        t5.setTypeface(MainActivity.tp);
        t6.setTypeface(MainActivity.tp);
        t7.setTypeface(MainActivity.tp);
        t8.setTypeface(MainActivity.tp);
        t9.setTypeface(MainActivity.tp);
        t10.setTypeface(MainActivity.tp);

        t1.setPaintFlags(t1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        t5.setPaintFlags(t5.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        t8.setPaintFlags(t8.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        t6.setPaintFlags(t6.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);



    }
}
