package com.example.simon_says;

public class User {
    private String name;
    private int score;

    public User(){}
    public User(String name, int score){
        setName(name);
        setScore(score);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "User{" +
                ", name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
