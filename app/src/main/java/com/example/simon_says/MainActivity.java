package com.example.simon_says;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText password,email;
    androidx.appcompat.widget.AppCompatButton signIn,register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        signIn = findViewById(R.id.sign_in);
        register = findViewById(R.id.register);

        signIn.setOnClickListener(this);
        register.setOnClickListener(this);

        if(Singleton.getInstance().getMAuth().getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this,MainActivity2.class);
            startActivity(intent);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                Singleton.getInstance().getMAuth().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    createInRealTime();
                                    makeToast("success");
                                } else {
                                    makeToast("Authentication failed.");
                                }
                            }
                        });
                break;
            case R.id.sign_in:
                Singleton.getInstance().getMAuth().signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Sign in success
                            makeToast("sign in successful");
                            FirebaseUser user = Singleton.getInstance().getMAuth().getCurrentUser();
                            Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                            startActivity(intent);
                        }
                        else{
                            makeToast("Authentication field.");
                        }
                    }
                });
                break;
        }
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
    public void makeToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }
    public void createInRealTime() {
        //TODO  create a new user in real time
        String email = Singleton.getInstance().getMAuth().getCurrentUser().getEmail();
        User user = new User(Services.cutEmailToName(email),0);
        Singleton.getInstance().getMyRef().child(Services.cutEmail(email)).setValue(user);
    }

}