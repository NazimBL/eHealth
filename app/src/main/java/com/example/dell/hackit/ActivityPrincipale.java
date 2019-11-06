package com.example.dell.hackit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityPrincipale extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principale);
        Typeface tp= Typeface.createFromAsset(getAssets(),"adobe.otf");
        TextView slogan=(TextView)findViewById(R.id.slogan);
        slogan.setTypeface(tp);
        waitAGodDamnMinute();

    }
    void waitAGodDamnMinute(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(2000);
                    if(!MainActivity.logged)  startActivity(new Intent(ActivityPrincipale.this,MainActivity.class));
                    else  startActivity(new Intent(ActivityPrincipale.this,Home.class));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
     finish();

    }
}
