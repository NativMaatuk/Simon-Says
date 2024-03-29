package com.example.simon_says;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    private int countTaps=0,lvl = 1,highScore=1,speed=800, flagSpeed=1;
    TextView topLabel, level, status;
    androidx.appcompat.widget.AppCompatButton leftTop, rightTop, leftBottom, rightBottom, start, show_score, info, slow,normal,fast;
    List<Integer> alerts = new ArrayList<>();
    List<Integer> taps = new ArrayList<>();
    Random random = new Random();
    MediaPlayer st,red,blue,yellow,green,lose;
    Intent intent = getIntent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getSupportActionBar().hide();

        topLabel = findViewById(R.id.topLabel);
        leftBottom = findViewById(R.id.leftBottom);
        rightBottom = findViewById(R.id.rightBottom);
        rightTop = findViewById(R.id.rightTop);
        leftTop = findViewById(R.id.leftTop);
        start = findViewById(R.id.start);
        show_score = findViewById(R.id.high_score);
        level = findViewById(R.id.level);
        status = findViewById(R.id.status);
        info = findViewById(R.id.info);
        slow = findViewById(R.id.slow);
        normal = findViewById(R.id.normal);
        fast = findViewById(R.id.fast);

        leftBottom.setOnClickListener(this);
        rightBottom.setOnClickListener(this);
        rightTop.setOnClickListener(this);
        leftTop.setOnClickListener(this);
        start.setOnClickListener(this);
        show_score.setOnClickListener(this);
        info.setOnClickListener(this);
        slow.setOnClickListener(this);
        normal.setOnClickListener(this);
        fast.setOnClickListener(this);

        //NOTE: DEBUG print the mail of current user
        //makeToast(mAuth.getCurrentUser().getEmail().toString());

        //NOTE: DEBUG print the current high score of user
        //myRef.child("nativma42@gmail")

        st =  MediaPlayer.create(this, R.raw.start);
        red =  MediaPlayer.create(this, R.raw.red);
        blue =  MediaPlayer.create(this, R.raw.blue);
        green =  MediaPlayer.create(this, R.raw.green);
        yellow =  MediaPlayer.create(this, R.raw.yellow);
        lose =  MediaPlayer.create(this, R.raw.lose);
        lockButton();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.high_score:
                intent = new Intent(MainActivity2.this,MainActivity3.class);
                startActivity(intent);
                break;
            case R.id.info:
                intent = new Intent(MainActivity2.this,MainActivity4.class);
                startActivity(intent);
                break;
            case R.id.start:
                lockButton();
                st.start();
                startGame();
                break;
            //1 = rightTop | 2 = leftTop | 3 = rightBottom | 4 = leftBottom
            case R.id.rightBottom:
                playAlert(3);
                this.taps.add(3);
                checkTaps();
                break;
            case R.id.rightTop:
                playAlert(1);
                this.taps.add(1);
                checkTaps();
                break;
            case R.id.leftBottom:
                playAlert(4);
                this.taps.add(4);
                checkTaps();
                break;
            case R.id.leftTop:
                playAlert(2);
                this.taps.add(2);
                checkTaps();
                break;
            case R.id.slow:
                speed = 900;
                flagSpeed =1;
                //TODO need to change color of button
                //slow.setBackgroundColor(getResources().getColor(R.color.black));
                break;
            case R.id.normal:
                speed = 800;
                flagSpeed =2;
                break;
            case R.id.fast:
                speed = 620;
                flagSpeed =3;
                break;
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
        normal.setClickable(false);
        this.start.setVisibility(View.INVISIBLE);
        //1 = slow
        if(flagSpeed == 1){
            slow.setVisibility(View.INVISIBLE);
            fast.setVisibility(View.INVISIBLE);
            normal.setText("Slow");
        }
        //2 = normal
        if(flagSpeed == 2){
            slow.setVisibility(View.INVISIBLE);
            fast.setVisibility(View.INVISIBLE);
            normal.setText("Normal");
        }
        //3 = fast
        if(flagSpeed == 3){
            slow.setVisibility(View.INVISIBLE);
            fast.setVisibility(View.INVISIBLE);
            normal.setText("Fast");
        }
        level.setText("Level: " + String.valueOf(lvl));
        addAlert();
    }

    public void checkTaps() {
        //NOTE: restartGame() -> clear taps list and Alert list
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
            lose.start();
            normal.setClickable(true);
            lockButton();
            //NOTE: check if the score in the DB is Greater than current score
            Singleton.getInstance().getMyRef().child(Services.cutEmail(Singleton.getInstance().getMAuth().getCurrentUser().getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                        User u = snapshot.getValue(User.class);
                        if (highScore > u.getScore()) {
                            u.setScore(highScore);
                            Singleton.getInstance().getMyRef().child(Services.cutEmail(Singleton.getInstance().getMAuth().getCurrentUser().getEmail())).setValue(u);
                        }
                        else
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
        status.setText("Watch");
        slow.setText("Slow");
        normal.setText("Normal");
        fast.setText("Fast");
        slow.setVisibility(View.VISIBLE);
        normal.setVisibility(View.VISIBLE);
        fast.setVisibility(View.VISIBLE);
        flagSpeed =1;
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

    public synchronized void sendAllAlert() {
        Thread timer = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(2000);
                    for (int alert : alerts) {
                        playAlert(alert);
                        Thread.sleep(speed);
                    }
                    freeButton();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timer.start();
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

}
