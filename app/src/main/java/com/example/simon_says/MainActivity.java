package com.example.simon_says;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase database;
    DatabaseReference myRef;
    EditText password,email;
    Button signIn,register;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        signIn = findViewById(R.id.sign_in);
        register = findViewById(R.id.register);

        signIn.setOnClickListener(this);
        register.setOnClickListener(this);

        mAuth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this,MainActivity2.class);
            startActivity(intent);
        }
    }
    public void makeToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onClick(View v) {
        if(v == register) {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                createInRealTime();
                                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

        if(v == signIn){
            mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //Sign in success
                        Toast.makeText(getApplicationContext(),"sign in successful", Toast.LENGTH_LONG).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Authentication field.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    public void createInRealTime() {
        //TODO  create a new user in real time
        String email = mAuth.getCurrentUser().getEmail();
        //makeToast(cutEmail(email));
        User user = new User(cutEmailToName(email),0);
        myRef.child(cutEmail(email)).setValue(user);
    }
    //TODO to example the parameter = "nativma22@gmail.com" send back "nativma22@gmail"
    public String cutEmail(String str){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<str.length();i++){
            if(str.charAt(i) == '.') break;
            else
                sb.append(str.charAt(i));
        }
        return String.valueOf(sb);
    }
    //TODO to example the parameter = "nativma22@gmail.com" send back "nativma22"
    public String cutEmailToName(String str){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<str.length();i++){
            if(str.charAt(i) == '@') break;
            else
                sb.append(str.charAt(i));
        }
        return String.valueOf(sb);
    }
}