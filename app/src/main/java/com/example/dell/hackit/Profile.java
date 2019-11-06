package com.example.dell.hackit;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Set;

public class Profile extends AppCompatActivity {

    TextView name_t,age_t,gender_t;
    User user;
    SharedPreferences sharedPreferences;
    public final static String PROFILE_PREF="profile";
    boolean tag=false;
    Firebase ref;
    ViewGroup viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        fristConnexion();
        setProfileInfo();

    }

    void init(){
        name_t=(TextView)findViewById(R.id.name_t);
        name_t.setTypeface(MainActivity.tp);
        age_t=(TextView)findViewById(R.id.age_t);
        age_t.setTypeface(MainActivity.tp);
        gender_t=(TextView)findViewById(R.id.gender_t);
        gender_t.setTypeface(MainActivity.tp);
        sharedPreferences=getSharedPreferences(PROFILE_PREF, Context.MODE_PRIVATE);
        viewGroup=(ViewGroup) findViewById(R.id.toast_layout_id);

        Firebase.setAndroidContext(Profile.this);
        ref=new Firebase(MainActivity.DB);

        user=(User)getIntent().getSerializableExtra("Person");

    }
    void setProfileInfo(){
        if(user!=null && tag){

            name_t.setText("Name : "+user.getName());
            age_t.setText("Age : "+user.getAge());
            gender_t.setText("Gender : "+user.getGender());

            save("name",user.getName());
            save("gender",user.getGender());
            saveInt("age",user.getAge());

        }else{

            name_t.setText("Name : "+loadPrefList("name"));
            SharedPreferences load = getSharedPreferences(PROFILE_PREF, 0);
            age_t.setText("Age : "+load.getInt("age",0));
            gender_t.setText("Gender : "+loadPrefList("gender"));

            if(load.getInt("age",0)==0){

                getInfoFromDb();
            }
        }
    }
    void save(String key,String value){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();

    }
    void saveInt(String key,int val){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(key,val);
        editor.commit();
    }
    String loadPrefList(String key){
        SharedPreferences load = getSharedPreferences(PROFILE_PREF, 0);
        return load.getString(key,"NULL");
    }

    void getInfoFromDb(){

        Firebase chiilRef=ref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        chiilRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String name = dataSnapshot.getValue(User.class).getName();
                    String gender = dataSnapshot.getValue(User.class).getGender();
                    int age = dataSnapshot.getValue(User.class).getAge();
                    name_t.setText("Name : "+name);
                    age_t.setText("Age : "+age);
                    gender_t.setText("Gender : "+gender);

                    save("name",name);
                    save("gender",gender);
                    saveInt("age",age);
                }catch (Exception c){
                    MainActivity.myToast(Profile.this,"DataBase Connexion Error ",getLayoutInflater(),viewGroup);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                MainActivity.myToast(Profile.this,"DataBase Error",getLayoutInflater(),viewGroup);
            }
        });
    }
    void fristConnexion()
    {
        if(!tag && user!=null){

            save("name",user.getName());
            save("gender",user.getGender());
            saveInt("age",user.getAge());

        }
        tag=true;

    }

}
