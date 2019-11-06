package com.example.dell.hackit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;


public class Sos extends AppCompatActivity {

    public static String sosSms="PATIENT NEED HELP";
    public final static String SOS_PREF="SOS";
    public static boolean emergency_call=false;
    public static boolean emergency_sms=false;

    EditText edit1,edit2,sms;
    TextView t1,t2;
    Button done;
    Switch call_Switch,sms_Switch;
    SharedPreferences sharedPreferences;
    Firebase ref;
    Firebase chiilRef;
    boolean dialog_tag=false;
    boolean e1=false,e2=false,s=false;
    String ph1="",ph2="";
    ViewGroup viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        ini();
        getInfoFromDb();


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 ph1=edit1.getText().toString();
                 ph2=edit2.getText().toString();

                if(!ph1.equals("")){

                    dialog_tag=true;
                    e1=true;
                }
                if(!ph2.equals("")){
                    dialog_tag=true;
                    e2=true;
                }
                if(!sms.getText().toString().equals("")){
                    dialog_tag=true;
                    s=true;
                }
                if(!dialog_tag)MainActivity.myToast(Sos.this,
                        "Write something then click to modify your informations",
                         getLayoutInflater(),viewGroup);
                else myDialog();

            }
        });

        call_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!isChecked)emergency_call=false;
                else emergency_call=true;
            }
        });
        sms_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)emergency_sms=false;
                else emergency_sms=true;
            }
        });
    }


    void save(String key,String value){
        sharedPreferences=getSharedPreferences(SOS_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();

    }
    String load(String key){

        SharedPreferences load = getSharedPreferences(SOS_PREF, 0);
        return load.getString(key,"NULL");

    }
    void getInfoFromDb(){

        Firebase chiilRef=ref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        chiilRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {


                String p1=dataSnapshot.getValue(User.class).getSos1();
                String p2="";
                try {
                     p2 = dataSnapshot.getValue(User.class).getSos2();
                }catch (Exception e) {
                    t2.setText("NULL");
                }

                t1.setText("  "+p1);
                t2.setText("  "+p2);

                save("p1",p1);
                save("p2",p2);
                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                MainActivity.myToast(Sos.this,"DataBase Error",getLayoutInflater(),viewGroup);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        t1.setText("  "+load("p1"));
        t2.setText("  "+load("p2"));
        sosSms=load("sms");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!t1.getText().toString().equals("")) {
            save("p1",t1.getText().toString());
            try {
                save("p2", t2.getText().toString());
            }catch (Exception e){

            }
        }
        save("sms",sosSms);
    }

    void myDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(Sos.this).setTitle("Sos Settings")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(e1){
                            t1.setText("  "+ph1);
                            chiilRef.child("sos1").setValue(ph1);
                            save("p1",ph1);
                        }

                        if(e2){
                            t2.setText("  "+ph2);
                            chiilRef.child("sos2").setValue(ph2);
                            save("p2",ph2);
                        }
                        if(s){
                            sosSms=sms.getText().toString();
                             MainActivity.myToast(Sos.this,"Auto Sms changed to : "+sosSms,getLayoutInflater(),viewGroup);
                            save("sms",sosSms);
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setMessage("Are you sure you want to change your default configuration ?").setCancelable(false);
        builder.show();

    }
    void ini(){
        t1=(TextView)findViewById(R.id.p1);
        edit1=(EditText)findViewById(R.id.edit1);
        t2=(TextView)findViewById(R.id.p2);
        edit2=(EditText)findViewById(R.id.edit2);

        edit1.setTypeface(MainActivity.tp);
        edit2.setTypeface(MainActivity.tp);
        viewGroup=(ViewGroup) findViewById(R.id.toast_layout_id);
        t2.setTypeface(MainActivity.tp);
        t1.setTypeface(MainActivity.tp);

        sms=(EditText)findViewById(R.id.sms);
        sms.setTypeface(MainActivity.tp);
        done=(Button)findViewById(R.id.done);
        done.setTypeface(MainActivity.tp);
        dialog_tag=false;
        e1=false;
        e2=false;
        s=false;

        call_Switch=(Switch)findViewById(R.id.sw);
        call_Switch.setTypeface(MainActivity.tp);
        sms_Switch=(Switch)findViewById(R.id.sw2);
        sms_Switch.setTypeface(MainActivity.tp);
        Firebase.setAndroidContext(Sos.this);
        ref=new Firebase(MainActivity.DB);
        chiilRef=ref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }



}
