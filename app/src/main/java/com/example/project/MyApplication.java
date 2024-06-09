package com.example.project;

import android.app.Application;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        PeriodicWorkRequest weeklyWorkRequest = new PeriodicWorkRequest.Builder(WeeklyNotificationWorker.class, 7, TimeUnit.DAYS)
                .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("weekly_notification", ExistingPeriodicWorkPolicy.KEEP, weeklyWorkRequest);
    }

    private long calculateInitialDelay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 5);
        calendar.set(Calendar.SECOND, 0);

        long currentTime = System.currentTimeMillis();
        long targetTime = calendar.getTimeInMillis();

        if (targetTime <= currentTime) {
            targetTime += TimeUnit.DAYS.toMillis(7);
        }

        return targetTime - currentTime;
    }
}