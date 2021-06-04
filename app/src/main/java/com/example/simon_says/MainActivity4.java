package com.example.simon_says;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity4 extends AppCompatActivity implements View.OnClickListener {
    androidx.appcompat.widget.AppCompatButton logOut,git;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        git = findViewById(R.id.git);
        logOut = findViewById(R.id.log_out);

        git.setOnClickListener(this);
        logOut.setOnClickListener(this);

        mAuth= FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
    }

    @Override
    public void onClick(View v) {
        if(v.equals(logOut)){
             mAuth.signOut();
             Toast.makeText(getApplicationContext(), "user log out", Toast.LENGTH_SHORT).show();
             Intent intent = new Intent(MainActivity4.this,MainActivity.class);
             startActivity(intent);
        }
        if(v.equals(git)){
            //https://github.com/NativMaatuk/Simon-Says
            //String url = "http://www.gobloggerslive.com";
            String url = "https://github.com/NativMaatuk/Simon-Says";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }
}