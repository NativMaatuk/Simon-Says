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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        getSupportActionBar().hide();
        git = findViewById(R.id.git);
        logOut = findViewById(R.id.log_out);

        git.setOnClickListener(this);
        logOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.log_out:
                Singleton.getInstance().getMAuth().signOut();
                Toast.makeText(getApplicationContext(), "user log out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity4.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.git:
                String url = "https://github.com/NativMaatuk/Simon-Says";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
        }
    }
}