package com.example.dell.hackit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.dell.hackit.Reminder.MEDOC_PREF;


public class Medoc extends Activity {

    TextView m;
    Button validate,addT,remove;
    ImageButton back_b,next_b;
    ViewGroup viewGroup;

    ArrayAdapter<String> arrayAdapter_time;
    ArrayList list_time = new ArrayList();

    Set<String> t_set1=new HashSet<String>();
    Set<String> t_set2=new HashSet<String>();
    Set<String> t_set3=new HashSet<String>();
    Set<String> t_set0=new HashSet<String>();

    static ArrayList medoc_list=new ArrayList();

    ListView lv_time;

    SharedPreferences sharedPreferences;
    private int hour, minute;
    String time="";
    static final int TIME_DIALOG_ID = 999;
    int listIndex=0;
    final static String TIME_DAY_PREF="time_pref";
    static boolean again=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medoc);
        init();

        MainActivity.myToast(Medoc.this,"Click on the arrows to shift between your medecine list Schedule",getLayoutInflater(),viewGroup);

        addT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(TIME_DIALOG_ID);
                time= pad(hour)+":"+pad(minute);

            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    list_time.remove(list_time.size() - 1);
                }catch (Exception e){

                }
              updateAdapter();
                if(listIndex==0)t_set0.remove(t_set0.size()-1);
               else if(listIndex==1)t_set1.remove(t_set1.size()-1);
               else if(listIndex==2)t_set2.remove(t_set2.size()-1);
                else if(listIndex==3)t_set3.remove(t_set3.size()-1);
            }
        });

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAppropriateList();
               myDialog();
                Log.d("Nazim","set0 not empty"+t_set0.isEmpty());
                Log.d("Nazim","set1 not empty"+t_set1.isEmpty());
                Log.d("Nazim","set2 not empty"+t_set2.isEmpty());
                Log.d("Nazim","set3 not empty"+t_set3.isEmpty());
            }
        });

        next_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveAppropriateList();
                listIndex++;
                if(listIndex>medoc_list.size()-1)listIndex=0;
                m.setText(""+medoc_list.get(listIndex));
                loadInit();
            }
        });
        back_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveAppropriateList();
                listIndex--;
                if(listIndex<0)listIndex=medoc_list.size()-1;
                m.setText(""+medoc_list.get(listIndex));
                loadInit();

            }
        });
    }

     void goToService(){
         Intent intent=new Intent(Medoc.this, MyService.class);
         intent.putExtra("index",medoc_list.size());
         sendMedocsInfo(intent);
         startService(intent);
     }

    void saveAppropriateList(){

        if(listIndex==0)
        {
            save("tset0",t_set0);
            Reminder.t0=t_set0;
        }
        else if(listIndex==1)
        {
            save("tset1",t_set1);
            Reminder.t1=t_set1;
        }
        else if(listIndex==2){
            save("tset2",t_set2);
            Reminder.t2=t_set2;
        }
        else if(listIndex==3){

            save("tset3",t_set3);
            Reminder.t3=t_set3;
        }

    }

    void updateSets(String value){

        if(listIndex ==0) t_set0.add(value);
        else if(listIndex ==1) t_set1.add(value);
        else if(listIndex ==2) t_set2.add(value);
       else if(listIndex ==3) t_set3.add(value);

    }

    void save(String key,Set<String> set){
        sharedPreferences=getSharedPreferences(TIME_DAY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        for(int i=0;i<list_time.size();i++){

            set.add(""+list_time.get(i));
        }
        editor.putStringSet(key,set);
        editor.commit();
        set.clear();

    }

    void loadInit(){
        SharedPreferences load = getSharedPreferences(TIME_DAY_PREF, 0);

        list_time.clear();
        if(listIndex==0) {
            if (load.getStringSet("tset0", null) != null) list_time.addAll(load.getStringSet("tset0", null));
        }
        else if(listIndex==1){

            if(load.getStringSet("tset1",null)!=null) {list_time.addAll(load.getStringSet("tset1",null));}
        }
        else if(listIndex==2){
            if(load.getStringSet("tset2",null)!=null) list_time.addAll(load.getStringSet("tset2",null));
        }
        else if(listIndex==3){
            if(load.getStringSet("tset3",null)!=null) list_time.addAll(load.getStringSet("tset3",null));
        }
        updateAdapter();

    }

void sendMedocsInfo(Intent i){
    SharedPreferences load = getSharedPreferences(TIME_DAY_PREF, 0);

    i.putExtra("med0",""+medoc_list.get(0));
    if (load.getStringSet("tset0", null) != null) {
        MyService.service_set0=load.getStringSet("tset0", null);
        Log.d("Nazim"," actual time");
    }

    if(medoc_list.size()>1){
        i.putExtra("med1",""+medoc_list.get(1));
        if (load.getStringSet("tset1", null) != null) MyService.service_set1=load.getStringSet("tset1", null);
    }
     if(medoc_list.size()>2){

        i.putExtra("med2",""+medoc_list.get(2));
        if (load.getStringSet("tset2", null) != null) MyService.service_set2=load.getStringSet("tset2", null);

    }
     if(medoc_list.size()>3){

        i.putExtra("med3",""+medoc_list.get(3));
        if (load.getStringSet("tset3", null) != null) MyService.service_set3=load.getStringSet("tset3", null);

    }
}


    void fillMedocWithTime(ArrayList list){

        SharedPreferences load = getSharedPreferences(TIME_DAY_PREF, 0);
        list.add(0,medoc_list.get(0)+" "+putTimeInList(load.getStringSet("tset0", null)));
        if (medoc_list.size()>1){

            list.add(1,medoc_list.get(1)+" "+putTimeInList(load.getStringSet("tset1", null)));
        }
        if (medoc_list.size()>2){

            list.add(2,medoc_list.get(2)+" "+putTimeInList(load.getStringSet("tset2", null)));
        }
        if (medoc_list.size()>3){
            list.add(3,medoc_list.get(3)+" "+putTimeInList(load.getStringSet("tset3", null)));

        }

    }
    String putTimeInList(Set<String> set) {
        ArrayList list0 = new ArrayList();
        String times="";
      if(set!=null)  list0.addAll(set);

        if (list0.size()!=0) {
            Log.d("Nazim","sets not empty");
            for(int i=0;i<list0.size();i++ ) {
                times=times+"\n"+list0.get(i);
            }
        }
        return times;
    }

    void myDialog(){

        ArrayList lista=new ArrayList();

        fillMedocWithTime(lista);

        List<String> malist = new ArrayList<String>();
         for(int i=0;i<medoc_list.size();i++){
             malist.add(lista.get(i).toString());
                                }
        final CharSequence[] medocs = malist.toArray(new String[malist.size()]);
        AlertDialog.Builder builder=new AlertDialog.Builder(Medoc.this);
        builder.setIcon(R.drawable.icon);
        builder.setItems(medocs, new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
});
        builder.setTitle("Schedule");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                goToService();
                MainActivity.myToast(Medoc.this,"Your Medecine Schedule has been saved",getLayoutInflater(),viewGroup);
                startActivity(new Intent(Medoc.this,Reminder.class));
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        AlertDialog dialog=builder.create();
        dialog.show();

    }
    //write date/hour correctly
    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this,
                        timePickerListener, hour, minute,false);

        }
        return null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveAppropriateList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadInit();
        if(again){
            goToService();
            again=false;
        }
    }

    void updateAdapter(){
        arrayAdapter_time= new ArrayAdapter(Medoc.this, android.R.layout.simple_list_item_1, list_time);
        lv_time.setAdapter(arrayAdapter_time);
    }
    void init(){
        m=(TextView)findViewById(R.id.medoc);
        m.setTypeface(MainActivity.tp);

        remove=(Button)findViewById(R.id.rem);
        remove.setTypeface(MainActivity.tp);

        addT=(Button)findViewById(R.id.add_time);
        addT.setTypeface(MainActivity.tp);
        validate=(Button)findViewById(R.id.validate);
        validate.setTypeface(MainActivity.tp);
        viewGroup=(ViewGroup) findViewById(R.id.toast_layout_id);

        back_b=(ImageButton)findViewById(R.id.back_id);
        next_b=(ImageButton)findViewById(R.id.next_id);

        lv_time = (ListView)findViewById(R.id.lv_time);
        TextView didactiel=(TextView)findViewById(R.id.time_to);
        didactiel.setTypeface(MainActivity.tp);

        updateAdapter();

        listIndex=getIntent().getIntExtra("n",0);
        m.setText(""+medoc_list.get(listIndex));
        loadInit();

    }
    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;

                    time=pad(hour)+":"+pad(minute);
                    list_time.add(time);
                    updateSets(time);
                    updateAdapter();


                }
            };

}
