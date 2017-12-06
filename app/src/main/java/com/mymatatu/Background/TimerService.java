package com.mymatatu.Background;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

/**
 * Created by anonymous on 25-07-2017.
 */

public class TimerService extends Service {
    FragmentManager fm;
    CountDownTimer ct;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(),"Timer for 10 mins has started",Toast.LENGTH_LONG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ct = new CountDownTimer(600000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        };
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
