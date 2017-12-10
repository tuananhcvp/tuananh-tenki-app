package com.example.anh.itenki.utils;

import com.example.anh.itenki.model.currentforecast.OpenWeatherJSon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by anh on 2017/12/06.
 */

public interface WeatherInfoAPI {
    @GET
    Call<OpenWeatherJSon> getCurrentWeather(@Url String url);

    @GET("data/2.5/weather")
    Call<OpenWeatherJSon> loadCurrentWeatherByLocation(@Query("lat") Double lat, @Query("lon") Double lon, @Query("appid") String appid);

    @GET("data/2.5/weather")
    Call<OpenWeatherJSon> loadCurrentWeatherByName(@Query("q") String name, @Query("appid") String appid);

}
