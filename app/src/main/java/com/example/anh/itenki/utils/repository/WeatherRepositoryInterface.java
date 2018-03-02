package com.example.anh.itenki.utils.repository;

import com.example.anh.itenki.model.currentforecast.OpenWeatherJSon;

import java.io.IOException;

/**
 * Created by anh on 2018/03/01.
 */

public interface WeatherRepositoryInterface {
    OpenWeatherJSon getCurrentWeatherByLocation(Double lat, Double lon, String appId) throws IOException;

    OpenWeatherJSon getCurrentWeatherByLocation(Double lat, Double lon, String lang,String appId) throws IOException;
}
