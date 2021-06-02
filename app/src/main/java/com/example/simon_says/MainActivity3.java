package com.example.simon_says;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener {
    androidx.appcompat.widget.AppCompatButton rPlay;
    TextView first,second,three;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<User> users = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        rPlay = findViewById(R.id.return_to_play);
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        three = findViewById(R.id.three);

        rPlay.setOnClickListener(this);

        mAuth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        //TODO DEBUG make Toast print the cut current email
        //makeToast(cutEmail(mAuth.getCurrentUser().getEmail()));
        myRef.orderByChild("score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot dst : snapshot.getChildren()){
                    User u = dst.getValue(User.class);
                    users.add(u);
                }
                Collections.reverse(users);
                first.setText(users.get(0).getName()+" Level:"+users.get(0).getScore());
                second.setText(users.get(1).getName()+" Level:"+users.get(1).getScore());
                three.setText(users.get(2).getName()+" Level:"+users.get(2).getScore());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                makeToast(error.getDetails());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.equals(rPlay)){
            Intent intent = new Intent(MainActivity3.this,MainActivity2.class);
            startActivity(intent);
        }
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
    public void makeToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }
}