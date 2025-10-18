package com.app.coffeemanagementapplication;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.util.Locale;

public class BaseActivity extends AppCompatActivity {
    private long lastClickTime = 0L;

    // set up configuration
    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        if (overrideConfiguration != null) {
            int uiMode = overrideConfiguration.uiMode;
            overrideConfiguration.setTo(getBaseContext().getResources().getConfiguration());
            overrideConfiguration.uiMode = uiMode;
        }
        super.applyOverrideConfiguration(overrideConfiguration);
    }



    private interface BackPressAction {
        void onBackPress();
    }

    // action when click back button
    public void changeBackPressCall(BackPressAction backPressCall) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                backPressCall.onBackPress();
            }

        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    // hide navigation bar
    private void hideNavigationBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        hideNavigationBar();
    }
    // prevent multiple touch


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            long currentTime = SystemClock.elapsedRealtime();
            long clickInterval = 300L;
            if (currentTime - lastClickTime < clickInterval) {
                return true;
            }
            lastClickTime = currentTime;
        }
        return super.dispatchTouchEvent(ev);
    }

}