package com.example.anh.itenki.application;

import android.app.Application;

import com.example.anh.itenki.di.ActivityScope;
import com.example.anh.itenki.di.ApplicationComponent;
import com.example.anh.itenki.di.ApplicationModule;
import com.example.anh.itenki.di.DaggerApplicationComponent;

/**
 * Created by anh on 2018/02/20.
 */

@ActivityScope
public class MyApplication extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
