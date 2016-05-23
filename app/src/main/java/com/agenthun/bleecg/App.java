package com.agenthun.bleecg;

import android.app.Application;
import android.content.Context;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/4 上午6:48.
 */
public class App extends Application {
    private static Context mApplicationContext;
    private static String token;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static Context getContext() {
        return mApplicationContext;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        App.token = token;
    }
}
