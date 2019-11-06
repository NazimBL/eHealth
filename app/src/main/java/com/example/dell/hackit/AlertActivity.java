package com.example.dell.hackit;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

public class AlertActivity extends Activity {

    static MediaPlayer sound;
    TextView meds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        sound=MediaPlayer.create(getApplicationContext(),R.raw.alarm);
        sound.start();
        Medoc.again=true;

        meds=(TextView)findViewById(R.id.medic);
        meds.setTypeface(MainActivity.tp);
        Log.d("Nazim","medoc :"+getIntent().getStringExtra("med"));
        meds.setText(""+getIntent().getStringExtra("med"));
        ViewGroup viewGroup= (ViewGroup) findViewById(R.id.toast_layout_id);
        MainActivity.myToast(AlertActivity.this,"Time to take your medecine",getLayoutInflater(),viewGroup);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sound.pause();
    }

    @Override
    public void onBackPressed() {
        sound.pause();

        Medoc.again=true;
        Intent i=new Intent(AlertActivity.this,Medoc.class);
        startActivity(i);
        finish();

    }

}
