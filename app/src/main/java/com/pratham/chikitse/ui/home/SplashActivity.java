package com.pratham.chikitse.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.pratham.chikitse.data.DataManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION_MS = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Preload all data on a background thread immediately
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        long startTime = System.currentTimeMillis();

        executor.execute(() -> {
            DataManager.getInstance().preloadData(getApplicationContext());
            long elapsed = System.currentTimeMillis() - startTime;
            long remaining = SPLASH_DURATION_MS - elapsed;

            mainHandler.postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }, Math.max(remaining, 0));
        });
    }
}
