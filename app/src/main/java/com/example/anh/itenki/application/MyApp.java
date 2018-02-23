package com.example.anh.itenki.application;

import android.app.Application;

import com.example.anh.itenki.di.ActivityScope;
import com.example.anh.itenki.di.ApplicationComponent;
import com.example.anh.itenki.di.ApplicationModule;
//import com.example.anh.itenki.di.DaggerApplicationComponent;

/**
 * Created by anh on 2018/02/20.
 */

@ActivityScope
public class MyApp extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

//        applicationComponent = DaggerApplicationComponent.builder()
//                .applicationModule(new ApplicationModule())
//                .build();

    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
