package com.example.dell.hackit;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends ActionBarActivity {

    TextView settings,control,reminder,sos,profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

          profile.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent=new Intent(Home.this,Profile.class);
                  intent.putExtra("Person",getIntent().getSerializableExtra("Person"));
                  startActivity(intent);
              }
          });

        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Sos.class));
            }
        });
        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Home.this,Reminder.class);
                intent.putExtra("Person",getIntent().getSerializableExtra("Person"));
                startActivity(intent);

            }
        });
        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Root.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,HelpActivity.class));
            }
        });
    }

    void init(){

        settings=(TextView) findViewById(R.id.setting);
        settings.setTypeface(MainActivity.tp);
        control=(TextView) findViewById(R.id.control);
        control.setTypeface(MainActivity.tp);
        profile=(TextView) findViewById(R.id.profile);
        profile.setTypeface(MainActivity.tp);
        reminder=(TextView) findViewById(R.id.reminder);
        reminder.setTypeface(MainActivity.tp);
        sos=(TextView) findViewById(R.id.sos);
        sos.setTypeface(MainActivity.tp);

        stopService(new Intent(Home.this,MyService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_log, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        if(id==R.id.logout){
           myLogoutDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder=new AlertDialog.Builder(Home.this).setTitle("Quit")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finishAffinity();
                        System.exit(0);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setMessage("Are you sure you want to quit ?").setCancelable(false);
        builder.show();

    }
    void myLogoutDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(Home.this).setTitle("Quit")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        MainActivity.logged=false;
                        startActivity(new Intent(Home.this,MainActivity.class));
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setMessage("Are you sure you want to Logout ?").setCancelable(false);
        builder.show();

    }


}
