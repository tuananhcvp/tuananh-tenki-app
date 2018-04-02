package com.example.anh.itenki.utils.repository;

import com.example.anh.itenki.model.currentforecast.OpenWeatherJSon;
import com.example.anh.itenki.model.dailyforecast.OpenWeatherDailyJSon;
import com.example.anh.itenki.model.nextdaysforecast.OpenWeatherNextDaysJSon;
import com.example.anh.itenki.utils.api.PlatformAPI;

import java.io.IOException;

import javax.inject.Inject;

public class WeatherRepository implements WeatherRepositoryInterface {
    private PlatformAPI platformAPI;

    @Inject
    public WeatherRepository(PlatformAPI platformAPI) {
        this.platformAPI = platformAPI;
    }

    @Override
    public OpenWeatherJSon getCurrentWeatherByLocation(Double lat, Double lon, String appId) throws IOException {
        return platformAPI.loadCurrentWeatherByLocation(lat, lon, appId).execute().body();
    }

    @Override
    public OpenWeatherJSon getCurrentWeatherByLocation(Double lat, Double lon, String lang, String appId) throws IOException {
        return platformAPI.loadCurrentWeatherByLocation(lat, lon, lang, appId).execute().body();
    }

    @Override
    public OpenWeatherJSon getCurrentWeatherByName(String name, String appId) throws IOException {
        return platformAPI.loadCurrentWeatherByName(name, appId).execute().body();
    }

    @Override
    public OpenWeatherJSon getCurrentWeatherByName(String name, String lang, String appId) throws IOException {
        return platformAPI.loadCurrentWeatherByName(name, lang, appId).execute().body();
    }

    @Override
    public OpenWeatherDailyJSon getDailyWeatherByLocation(Double lat, Double lon, String appId) throws IOException {
        return platformAPI.loadDailyWeatherByLocation(lat, lon, appId).execute().body();
    }

    @Override
    public OpenWeatherDailyJSon getDailyWeatherByLocation(Double lat, Double lon, String lang, String appId) throws IOException {
        return platformAPI.loadDailyWeatherByLocation(lat, lon, lang, appId).execute().body();
    }

    @Override
    public OpenWeatherDailyJSon getDailyWeatherByName(String name, String appId) throws IOException {
        return platformAPI.loadDailyWeatherByName(name, appId).execute().body();
    }

    @Override
    public OpenWeatherDailyJSon getDailyWeatherByName(String name, String lang, String appId) throws IOException {
        return platformAPI.loadDailyWeatherByName(name, lang, appId).execute().body();
    }

    @Override
    public OpenWeatherNextDaysJSon getNextDayWeatherByLocation(Double lat, Double lon, int cnt, String appId) throws IOException {
        return platformAPI.loadNextDayWeatherByLocation(lat, lon, cnt, appId).execute().body();
    }

    @Override
    public OpenWeatherNextDaysJSon getNextDayWeatherByLocation(Double lat, Double lon, int cnt, String lang, String appId) throws IOException {
        return platformAPI.loadNextDayWeatherByLocation(lat, lon, cnt, lang, appId).execute().body();
    }

    @Override
    public OpenWeatherNextDaysJSon getNextDayWeatherByName(String name, int cnt, String appId) throws IOException {
        return platformAPI.loadNextDayWeatherByName(name, cnt, appId).execute().body();
    }

    @Override
    public OpenWeatherNextDaysJSon getNextDayWeatherByName(String name, int cnt, String lang, String appId) throws IOException {
        return platformAPI.loadNextDayWeatherByName(name, cnt, lang, appId).execute().body();
    }


}
