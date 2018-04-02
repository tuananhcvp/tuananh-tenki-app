package com.example.anh.itenki.usecase;

import android.content.Context;

import com.example.anh.itenki.model.dailyforecast.OpenWeatherDailyJSon;
import com.example.anh.itenki.utils.SharedPreference;
import com.example.anh.itenki.utils.repository.WeatherRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by anh on 2018/03/22.
 */

public class WeatherDailyUseCase extends UseCase {
    private WeatherRepository weatherRepository;
    private Context context;

    @Inject
    public WeatherDailyUseCase(WeatherRepository weatherRepository, Context context,
                               @Named("executeScheduler") Scheduler threadExecutor,
                               @Named("postScheduler") Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.weatherRepository = weatherRepository;
        this.context = context;
    }

    public void excute(double lat, double lon, String appId, UseCaseCallback callback) {
        int posLanguage = SharedPreference.getInstance(context).getInt("Language", 0);

        SingleOnSubscribe<OpenWeatherDailyJSon> emitter = e -> {
            if (posLanguage == 1) {
                e.onSuccess(weatherRepository.getDailyWeatherByLocation(lat, lon, "ja", appId));
            } else {
                e.onSuccess(weatherRepository.getDailyWeatherByLocation(lat, lon, appId));
            }
        };

        Single.create(emitter)
                .subscribeOn(threadExecutor)
                .observeOn(postExecutionThread)
                .subscribe(
                        callback::onSuccess,
                        callback::onError);

    }

    public void excute(String name, String appId, UseCaseCallback callback) {
        int posLanguage = SharedPreference.getInstance(context).getInt("Language", 0);

        SingleOnSubscribe<OpenWeatherDailyJSon> emitter = e -> {
            if (posLanguage == 1) {
                e.onSuccess(weatherRepository.getDailyWeatherByName(name, "ja", appId));
            } else {
                e.onSuccess(weatherRepository.getDailyWeatherByName(name, appId));
            }
        };

        Single.create(emitter)
                .subscribeOn(threadExecutor)
                .observeOn(postExecutionThread)
                .subscribe(
                        callback::onSuccess,
                        callback::onError);

    }

    public interface UseCaseCallback {
        void onSuccess(OpenWeatherDailyJSon entity);

        void onError(Throwable t);
    }
}
