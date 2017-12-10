package com.example.anh.itenki.model.dailyforecast;

import java.util.List;

/**
 * Created by anh on 2017/12/06.
 */

public class ListItem {
    private long dt;
    private Main main;
    private List<WeatherItem> weather;
    private Clouds clouds;
    private Wind wind;
    private Rain rain;
    private SysList sys;
    private String dt_txt;

    public Clouds getClouds() {
        return clouds;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }

    public SysList getSys() {
        return sys;
    }

    public void setSys(SysList sys) {
        this.sys = sys;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public List<WeatherItem> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherItem> weather) {
        this.weather = weather;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }
}
