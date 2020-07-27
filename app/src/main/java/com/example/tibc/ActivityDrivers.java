package com.example.tibc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityDrivers extends AppCompatActivity {
    private EditText email,pass;
    private Button log,reg;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener fAuthlst;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_driver_login);

        fAuthlst = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null)
                {
                    Intent intent = new Intent(ActivityDrivers.this,DriverMapActivity.class);
                    startActivity(intent);

                }
            }
        };

        email = (EditText) findViewById(R.id.Email);
        pass = (EditText) findViewById(R.id.Password);


        log = (Button) findViewById(R.id.login);
        reg=(Button) findViewById(R.id.register);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mEmail = email.getText().toString();
                final String mPass = pass.getText().toString();

                mAuth.createUserWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(ActivityDrivers.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(ActivityDrivers.this,"Sign up Error", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("User").child("Drivers").child(user_id);
                            current_user_db.setValue(true);
                        }
                    }
                });


            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mEmail = email.getText().toString();
                final String mPass = pass.getText().toString();
                mAuth.signInWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(ActivityDrivers.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(ActivityDrivers.this,"Sign in Error", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(fAuthlst);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(fAuthlst);
    }
}