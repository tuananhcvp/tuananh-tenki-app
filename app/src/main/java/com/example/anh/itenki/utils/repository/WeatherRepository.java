package com.example.anh.itenki.utils.repository;

import com.example.anh.itenki.model.currentforecast.OpenWeatherJSon;
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
}
