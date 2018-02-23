package com.example.anh.itenki.di;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by anh on 2018/02/20.
 */

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    Retrofit retrofit();

}
