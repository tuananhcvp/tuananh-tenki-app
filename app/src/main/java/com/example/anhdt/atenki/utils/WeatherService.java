package com.example.anhdt.atenki.utils;

import com.example.anhdt.atenki.model.currentforecast.OpenWeatherJSon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;


/**
 * Created by anhdt on 8/9/17.
 */
public interface WeatherService {
    @GET
    Call<OpenWeatherJSon> getCurrentWeather(@Url String url);
}
