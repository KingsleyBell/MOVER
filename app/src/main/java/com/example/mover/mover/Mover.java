package com.example.mover.mover;

import android.app.Application;

/**
 * Created by LUKE on 2016/08/01.
 */
public class Mover extends Application {

    private static String user;

    public static String getUser() {
        return user;
    }

    public static void setUser(String u) {
        user = u;
    }

}
