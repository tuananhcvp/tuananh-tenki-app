package com.example.anh.itenki.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by anh on 2018/02/20.
 */

@ApplicationScope
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    ActivityComponent plus(ActivityModule module);
}
