package com.example.dell.hackit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Reminder extends AppCompatActivity {

    ListView lv;
    Button add, remove;
    TextView textView, help;
    ArrayAdapter<String> arrayAdapter;
    ArrayList list = new ArrayList();
    EditText editText;
    Set<String> set = new HashSet<String>();
    SharedPreferences sharedPreferences;

    static Set<String> t1 = new HashSet<String>();
    static Set<String> t2 = new HashSet<String>();
    static Set<String> t3 = new HashSet<String>();
    static Set<String> t0 = new HashSet<String>();

    public static final String MEDOC_PREF = "medoc_pref";
    Firebase ref;
    ViewGroup viewGroup;
    boolean itemclicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        init();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String med = editText.getText().toString();

                if (!med.equals("") && list.size() < 4 && inputCheck(med)) {
                    list.add(med);
                    editText.setText("");
                    set.add(med);
                    updateAdapter();
                } else {
                    if (!inputCheck(med))
                        MainActivity.myToast(Reminder.this, "You've already noted this name",
                                getLayoutInflater(), viewGroup);
                    if (list.size() > 3)
                        MainActivity.myToast(Reminder.this, "You can't take more than 4 medicines in a day",
                                getLayoutInflater(), viewGroup);
                    if (med.equals(""))
                        MainActivity.myToast(Reminder.this, "Write a medecine name and then click on the add button ",
                                getLayoutInflater(), viewGroup);

                }
                displayHelpText();


            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //firstConnection();
                itemclicked = true;
                Firebase chiilRef = ref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                chiilRef.child("medocs").setValue(list);
                save("set", set);
                Medoc.medoc_list = list;
                Intent intent = new Intent(Reminder.this, Medoc.class);
                intent.putExtra("n", position);
                startActivity(intent);

            }
        });


        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(list.size() - 1);
                set.remove(list.size() - 1);
                updateAdapter();
                displayHelpText();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        save("set", set);
    }

    void init() {


        lv = (ListView) findViewById(R.id.listView);
        add = (Button) findViewById(R.id.add);
        remove = (Button) findViewById(R.id.remove);
        editText = (EditText) findViewById(R.id.edit);
        editText.setTypeface(MainActivity.tp);
        textView = (TextView) findViewById(R.id.clickb);


        add.setTypeface(MainActivity.tp);
        remove.setTypeface(MainActivity.tp);
        TextView didactiel = (TextView) findViewById(R.id.clickb);
        didactiel.setTypeface(MainActivity.tp);
        help = (TextView) findViewById(R.id.help);
        help.setTypeface(MainActivity.tp);

        viewGroup = (ViewGroup) findViewById(R.id.toast_layout_id);
        updateAdapter();
        Firebase.setAndroidContext(Reminder.this);
        ref = new Firebase(MainActivity.DB);

    }


    void updateAdapter() {

        arrayAdapter = new ArrayAdapter(Reminder.this, R.layout.list_view_text, list);
        lv.setAdapter(arrayAdapter);
    }

    //checks if medecine hasn't already been wrote
    boolean inputCheck(String input) {
        boolean tag = true;
        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).toString().equals(input)) tag = false;
            else tag = true;

        }
        return tag;
    }

    void save(String key, Set<String> set) {

        sharedPreferences = getSharedPreferences(MEDOC_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < list.size(); i++) {

            set.add("" + list.get(i));
        }
        editor.putStringSet(key, set);
        editor.commit();
        set.clear();
    }

    void loadList() {

        SharedPreferences load = getSharedPreferences(MEDOC_PREF, 0);
        list.clear();
        if (load.getStringSet("set", null) != null){
            list.addAll(load.getStringSet("set", null));

        }
        updateAdapter();
    }


    void displayHelpText() {

        if (list.size() != 0) help.setText("CLICK on the List to set your Medecine Schedule");
        else
            help.setText("");
    }

}