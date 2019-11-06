package com.example.dell.hackit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.UUID;

public class MainActivity extends Activity {

    public static Typeface tp;
    public static boolean logged=false;
    public static final String DB="https://smartnurse-fee8a.firebaseio.com/";
    public static final UUID MY_UUID_SECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    Button login_b,facebook_b;
    TextView sign_b,new_here;
    EditText email,password;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Firebase ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stopService(new Intent(MainActivity.this,MyService.class));
        setContentView(R.layout.activity_main);
        init();
        login_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseSignIn();
            }
        });
        facebook_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fbLogin();
                ViewGroup viewGroup=(ViewGroup) findViewById(R.id.toast_layout_id);
               myToast(MainActivity.this,"Let the show begin",getLayoutInflater(),viewGroup);

            }
        });

        sign_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

           myAuthListener();

    }

    void myAuthListener(){

        ///sign in sign_out check
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null ){
                    Log.d("Nazim","User is on ligne");
                    logged=true;
                     startActivity(new Intent(MainActivity.this,Home.class));
                }else  Log.d("Nazim","User signed out");
            }
        };
    }


    void FirebaseSignIn(){

        mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Nazim", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Error",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void init(){
        sign_b=(TextView)findViewById(R.id.sign_up);
        login_b=(Button)findViewById(R.id.login_id);
        new_here=(TextView)findViewById(R.id.new_here);

        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        sign_b.setPaintFlags(sign_b.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        facebook_b=(Button)findViewById(R.id.fb_id);
        tp=Typeface.createFromAsset(getAssets(),"adobe.otf");
        new_here.setTypeface(tp);
        sign_b.setTypeface(tp);
        login_b.setTypeface(tp);
        facebook_b.setTypeface(tp);
        email.setTypeface(tp);
        password.setTypeface(tp);
//        title=(TextView)findViewById(R.id.title);
//        title.setTypeface(tp);

        mAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(MainActivity.this);
        ref=new Firebase(DB);

    }

    public static void myToast(Context activity, String msg,LayoutInflater inflater,ViewGroup viewGroup){

        //(ViewGroup) findViewById(R.id.toast_layout_id)
        View layout = inflater.inflate(R.layout.toast_layout,viewGroup);
        TextView toastText = (TextView) layout.findViewById(R.id.toast_text);
        toastText.setText(msg);
        toastText.setTypeface(MainActivity.tp);
        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
       // Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();

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

    @Override
    public void onBackPressed() {

        finishAffinity();
        System.exit(0);
    }
}
