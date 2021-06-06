package com.example.simon_says;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Singleton {
    private static Singleton instance;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private Singleton(){
        mAuth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
    }
    public static Singleton getInstance() {
        if(instance == null)
            instance = new Singleton();
        return instance;
    }
    public FirebaseAuth getMAuth(){
        return this.mAuth;
    }
    public DatabaseReference getMyRef(){ return this.myRef; }
}
