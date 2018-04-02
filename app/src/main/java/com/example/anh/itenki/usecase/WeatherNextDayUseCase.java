package com.example.anh.itenki.usecase;

import android.content.Context;

import com.example.anh.itenki.model.nextdaysforecast.OpenWeatherNextDaysJSon;
import com.example.anh.itenki.utils.SharedPreference;
import com.example.anh.itenki.utils.repository.WeatherRepository;

import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by anh on 2018/03/22.
 */

public class WeatherNextDayUseCase extends UseCase {
    private WeatherRepository weatherRepository;
    private Context context;


    public WeatherNextDayUseCase(WeatherRepository weatherRepository, Context context,
                                 @Named("executeScheduler") Scheduler threadExecutor,
                                 @Named("postScheduler") Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.weatherRepository = weatherRepository;
        this.context = context;
    }

    public void excute(double lat, double lon, int cnt, String appId, UseCaseCallback callback) {
        int posLanguage = SharedPreference.getInstance(context).getInt("Language", 0);

        SingleOnSubscribe<OpenWeatherNextDaysJSon> emitter = e -> {
            if (posLanguage == 1) {
                e.onSuccess(weatherRepository.getNextDayWeatherByLocation(lat, lon, cnt, "ja", appId));
            } else {
                e.onSuccess(weatherRepository.getNextDayWeatherByLocation(lat, lon, cnt, appId));
            }
        };

        Single.create(emitter)
                .subscribeOn(threadExecutor)
                .observeOn(postExecutionThread)
                .subscribe(
                        callback::onSuccess,
                        callback::onError);

    }

    public void excute(String name, int cnt, String appId, UseCaseCallback callback) {
        int posLanguage = SharedPreference.getInstance(context).getInt("Language", 0);

        SingleOnSubscribe<OpenWeatherNextDaysJSon> emitter = e -> {
            if (posLanguage == 1) {
                e.onSuccess(weatherRepository.getNextDayWeatherByName(name, cnt, "ja", appId));
            } else {
                e.onSuccess(weatherRepository.getNextDayWeatherByName(name, cnt, appId));
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
        void onSuccess(OpenWeatherNextDaysJSon entity);

        void onError(Throwable t);
    }
}
