package com.example.simon_says;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener {
    androidx.appcompat.widget.AppCompatButton rPlay;
    TextView first,second,three, my_score;
    List<User> users = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        getSupportActionBar().hide();
        rPlay = findViewById(R.id.return_to_play);
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        three = findViewById(R.id.three);
        my_score = findViewById(R.id.my_score);

        rPlay.setOnClickListener(this);

        //NOTE: DEBUG make Toast print the cut current email
        //makeToast(cutEmail(mAuth.getCurrentUser().getEmail()));
        Singleton.getInstance().getMyRef().orderByChild("score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot dst : snapshot.getChildren()){
                    User u = dst.getValue(User.class);
                    users.add(u);
                }
                Collections.reverse(users);
                first.setText("#1 - Level "+users.get(0).getScore()+": "+users.get(0).getName());
                second.setText("#2 - Level "+users.get(1).getScore()+": "+users.get(1).getName());
                three.setText("#3 - Level "+users.get(2).getScore()+": "+users.get(2).getName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                makeToast(error.getDetails());
            }
        });
        Singleton.getInstance().getMyRef().child(Services.cutEmail(Singleton.getInstance().getMAuth().getCurrentUser().getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot != null) {
                    User u = snapshot.getValue(User.class);
                    my_score.setText("Your High Score Is: " + String.valueOf(u.getScore()));
                }
                else
                    my_score.setText("No High Score Go To Play");
            }
            @Override
            public void onCancelled(DatabaseError error) {
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
    public void makeToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }
}