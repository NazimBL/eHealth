package com.example.dell.hackit;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends Activity {

    EditText name,email,pass,age,person1_phone,person2_phone;
    Button done;
    TextView reg,help;
    RadioButton fem,male;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private Firebase ref;

    ViewGroup viewGroup;
    User person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerManagement();

            }
        });

    }
    void init(){

        viewGroup=(ViewGroup) findViewById(R.id.toast_layout_id);
        person1_phone=(EditText)findViewById(R.id.person1_phone);
        person2_phone=(EditText)findViewById(R.id.person2_phone);

        reg=(TextView)findViewById(R.id.reg);
        reg.setTypeface(MainActivity.tp);


        person1_phone.setTypeface(MainActivity.tp);
        person2_phone.setTypeface(MainActivity.tp);

         fem=(RadioButton)findViewById(R.id.female);
         male=(RadioButton)findViewById(R.id.male);

        help=(TextView)findViewById(R.id.help);
        help.setTypeface(MainActivity.tp);
        email=(EditText)findViewById(R.id.email);
        email.setTypeface(MainActivity.tp);
        pass=(EditText)findViewById(R.id.password);
        pass.setTypeface(MainActivity.tp);

        name=(EditText)findViewById(R.id.name);
        name.setTypeface(MainActivity.tp);

        age=(EditText) findViewById(R.id.age);
        age.setTypeface(MainActivity.tp);

        done=(Button)findViewById(R.id.done);

        mAuth=FirebaseAuth.getInstance();
        Firebase.setAndroidContext(RegisterActivity.this);

        ref=new Firebase(MainActivity.DB);

        myAuthListener();


    }
    void registerManagement(){

        if(!checkRegisterInputs()){

            MainActivity.myToast(RegisterActivity.this,"Incomplete content !",getLayoutInflater(),viewGroup);
        }
        else {

         createUserObj();
            register();
        }
    }
    void register(){
        mAuth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d("Nazim", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            MainActivity.myToast(RegisterActivity.this,""+task.getException().toString(),getLayoutInflater(),viewGroup);
                        }else{

                               Intent intent=new Intent(RegisterActivity.this,Home.class);
                               intent.putExtra("Person",person);
                               startActivity(intent);
                        }
                    }
                });

    }

    void createUserObj(){

        int age_int=Integer.parseInt(age.getText().toString());
        person=new User(name.getText().toString(),genderCheck(),age_int);
        person.setSos1(person1_phone.getText().toString());

        String person2num=person2_phone.getText().toString();
        if (!TextUtils.isEmpty(person2num)) person.setSos2(person2num);

    }
    void myAuthListener(){
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                 user=firebaseAuth.getCurrentUser();
                if(user!=null){

                    Firebase chiilRef=ref.child("users").child(user.getUid());
                    chiilRef.setValue(person);
                    MainActivity.logged=true;

                }else  Log.d("Nazim","User signed out");
            }
        };

    }
    String genderCheck(){

        String gender="";

        if(fem.isChecked())gender="Female";
        else if(male.isChecked())gender="Male";

        return gender;
    }

    boolean checkRegisterInputs(){
        boolean tag=true;
        if(email.getText().toString().equals("") || pass.getText().toString().equals("") ||name.getText().toString().equals("")
                || person1_phone.getText().toString().equals("") || TextUtils.isEmpty(age.getText().toString())
                ||TextUtils.isEmpty(genderCheck())){
            tag=false;
        }
        return tag;
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
