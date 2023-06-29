package com.example.smartgladiatortask.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.smartgladiatortask.R;
import com.example.smartgladiatortask.db.UsersDatabase;
import com.example.smartgladiatortask.model.home.UserModel;
import com.example.smartgladiatortask.util.PreferenceUtils;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        handler.postDelayed(() -> {
            if (PreferenceUtils.isUserLoggedIn()) {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                finish();
            }
        }, 2000);

    }


}