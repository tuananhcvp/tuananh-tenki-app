package com.example.anh.itenki.di;

import android.app.Application;
import android.content.Context;

import com.example.anh.itenki.utils.api.PlatformAPI;
import com.example.anh.itenki.utils.repository.WeatherRepository;
import com.example.anh.itenki.utils.repository.WeatherRepositoryInterface;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anh on 2018/02/19.
 */

@Module
public class ApplicationModule {
    public static final String BASE_URL = "http://api.openweathermap.org/";

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @ApplicationScope
    public Context provideApplicationContext() {
        return application.getApplicationContext();
    }

    @Provides
    @ApplicationScope
    public PlatformAPI providePlatformAPI() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(PlatformAPI.class);
    }

    @Provides
    @ApplicationScope
    public WeatherRepositoryInterface provideWeatherRepositoryInterface(PlatformAPI platformAPI) {
        return new WeatherRepository(platformAPI);
    }
}
