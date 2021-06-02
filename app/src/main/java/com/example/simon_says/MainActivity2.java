package com.example.simon_says;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase database;
    DatabaseReference myRef;
    private int countTaps=0,lvl = 1,highScore=1;
    TextView topLabel, level, status;
    androidx.appcompat.widget.AppCompatButton leftTop, rightTop, leftBottom, rightBottom, start, show_score, log_out;
    List<Integer> alerts = new ArrayList<>();
    List<Integer> taps = new ArrayList<>();
    Random random = new Random();
    MediaPlayer st,red,blue,yellow,green;
    private FirebaseAuth mAuth;
    Intent intent = getIntent();
    //FirebaseDatabase database;
    //DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        topLabel = findViewById(R.id.topLabel);
        leftBottom = findViewById(R.id.leftBottom);
        rightBottom = findViewById(R.id.rightBottom);
        rightTop = findViewById(R.id.rightTop);
        leftTop = findViewById(R.id.leftTop);
        start = findViewById(R.id.start);
        show_score = findViewById(R.id.high_score);
        level = findViewById(R.id.level);
        status = findViewById(R.id.status);
        log_out = findViewById(R.id.log_out);

        leftBottom.setOnClickListener(this);
        rightBottom.setOnClickListener(this);
        rightTop.setOnClickListener(this);
        leftTop.setOnClickListener(this);
        start.setOnClickListener(this);
        show_score.setOnClickListener(this);
        log_out.setOnClickListener(this);

        mAuth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        //TODO DEBUG print the mail of current user
        //makeToast(mAuth.getCurrentUser().getEmail().toString());

        //TODO DEBUG print the current high score of user
        //myRef.child("nativma42@gmail")

        st =  MediaPlayer.create(this, R.raw.start);
        red =  MediaPlayer.create(this, R.raw.start);
        blue =  MediaPlayer.create(this, R.raw.start);
        green =  MediaPlayer.create(this, R.raw.start);
        yellow =  MediaPlayer.create(this, R.raw.start);
        lockButton();
    }


    @Override
    public void onClick(View v) {
        //TODO need to add references of Log Out - mAuth.signOut();

        if (show_score.equals(v)) {
            //TODO exit from mouth
            Intent intent = new Intent(MainActivity2.this,MainActivity3.class);
            startActivity(intent);
        }
        if(log_out.equals(v)){
             mAuth.signOut();
             Toast.makeText(getApplicationContext(), "user log out", Toast.LENGTH_SHORT).show();
             Intent intent = new Intent(MainActivity2.this,MainActivity.class);
             startActivity(intent);
        }
        if (start.equals(v)) {
            lockButton();
            st.start();
            startGame();
        }
        //1 = rightTop | 2 = leftTop | 3 = rightBottom | 4 = leftBottom
        if (rightBottom.equals(v)) {
            playAlert(3);
            this.taps.add(3);
            checkTaps();
        }
        if (rightTop.equals(v)) {
            playAlert(1);
            this.taps.add(1);
            checkTaps();
        }
        if(leftBottom.equals(v)){
            playAlert(4);
            this.taps.add(4);
            checkTaps();
        }
        if(leftTop.equals(v)){
            playAlert(2);
            this.taps.add(2);
            checkTaps();
        }
    }

    public void clearAlert() {
        this.alerts.clear();
    }

    public void playAlert(int id) {
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(300); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.ABSOLUTE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in

        Button btn;
        switch (id) {
            //1 = rightTop | 2 = leftTop | 3 = rightBottom | 4 = leftBottom
            case 1:
                btn = (Button) findViewById(R.id.rightTop);
                red.start();
                break;
            case 2:
                btn = (Button) findViewById(R.id.leftTop);
                yellow.start();
                break;
            case 3:
                btn = (Button) findViewById(R.id.rightBottom);
                blue.start();
                break;
            case 4:
                btn = (Button) findViewById(R.id.leftBottom);
                green.start();
                break;
            default:
                // code block
                return;
        }
        btn.startAnimation(animation);
    }


    public void makeToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    //TODO need to add button repeat
    public void repeatLevel() {
        sendAllAlert();
    }

    public void clearTaps() {
        this.taps.clear();
    }

    public void startGame() {
        this.start.setVisibility(View.INVISIBLE);
        level.setText("Level: " + String.valueOf(lvl));
        addAlert();
    }

    public void checkTaps() {
        //TODO Note restartGame() -> clear taps list and Alert list
        int ind = taps.size()-1;
        countTaps++;
        if(this.taps.get(ind).equals(this.alerts.get(ind))) {
            if(countTaps == lvl) {
                System.out.println(countTaps + " " + lvl);
                countTaps=0;
                clearTaps();
                nextLevel();
            }
        }
        else {
            //TODO check if the score in the DB is Greater than current score
            myRef.child(cutEmail(mAuth.getCurrentUser().getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    User u = snapshot.getValue(User.class);
                    if(highScore > u.getScore()){
                        u.setScore(highScore);
                        myRef.child(cutEmail(mAuth.getCurrentUser().getEmail())).setValue(u);
                    }
                    restartGame();
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    makeToast(error.getDetails());
                }
            });
        }
    }


    public void restartGame() {
        makeToast("You lose");
        clearAlert();
        clearTaps();
        countTaps =0;
        highScore =1;
        lvl = 1;
        level.setText("Level: " + String.valueOf(lvl));
        this.start.setVisibility(View.VISIBLE);
    }

    public void nextLevel() {

        lvl++;
        highScore++;
        level.setText("Level: " + String.valueOf(lvl));
        addAlert();
    }

    public void addAlert() {
        lockButton();
        this.alerts.add(random.nextInt(3) + 1);
        sendAllAlert();
    }

    public void sendAllAlert() {
        //TODO need to fix
        Thread timer = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(2000);
                    for (int alert : alerts) {
                        playAlert(alert);
                        Thread.sleep(800);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timer.start();
        freeButton();
    }
    public void freeButton(){
        leftTop.setClickable(true);
        leftBottom.setClickable(true);
        rightTop.setClickable(true);
        rightBottom.setClickable(true);
        status.setText("Repeat");
    }
    public void lockButton(){
        leftTop.setClickable(false);
        leftBottom.setClickable(false);
        rightTop.setClickable(false);
        rightBottom.setClickable(false);
        status.setText("Watch");
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

}
