package com.example.anh.itenki.application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.anh.itenki.di.ActivityComponent;
import com.example.anh.itenki.di.ActivityModule;
//import com.example.anh.itenki.di.DaggerActivityComponent;

/**
 * Created by anh on 2018/02/19.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ActivityComponent getComponent() {
        if (activityComponent == null) {
//            DaggerActivityComponent.builder()
//                    .activityModule(new ActivityModule(this))
//                    .build();
        }
        return activityComponent;
    }
}
