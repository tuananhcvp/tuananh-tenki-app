package com.example.anh.itenki.usecase;

import android.content.Context;

import com.example.anh.itenki.model.currentforecast.OpenWeatherJSon;
import com.example.anh.itenki.utils.SharedPreference;
import com.example.anh.itenki.utils.repository.WeatherRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;

/**
 * Created by anh on 2018/03/06.
 */

public class WeatherCurrentUseCase extends UseCase {
    private WeatherRepository weatherRepository;
    private Context context;

    @Inject
    public WeatherCurrentUseCase(WeatherRepository weatherRepository, Context context,
                                  @Named("executeScheduler") Scheduler threadExecutor,
                                  @Named("postScheduler") Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.weatherRepository = weatherRepository;
        this.context = context;
    }

    public void excute(double lat, double lon, String appId, UseCaseCallback callback) {
        int posLanguage = SharedPreference.getInstance(context).getInt("Language", 0);

        SingleOnSubscribe<OpenWeatherJSon> emitter = e -> {
            if (posLanguage == 1) {
                e.onSuccess(weatherRepository.getCurrentWeatherByLocation(lat, lon, "ja", appId));
            } else {
                e.onSuccess(weatherRepository.getCurrentWeatherByLocation(lat, lon, appId));
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
        void onSuccess(OpenWeatherJSon entity);

        void onError(Throwable t);
    }
}
