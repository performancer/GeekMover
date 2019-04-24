package com.example.geekmover;

class UserData {
    private static final UserData ourInstance = new UserData();

    static UserData getInstance() {
        return ourInstance;
    }

    private int height, weight;

    private UserData() {
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }
}
