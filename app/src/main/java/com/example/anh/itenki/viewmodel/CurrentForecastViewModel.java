package com.example.anh.itenki.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.anh.itenki.BR;
import com.example.anh.itenki.R;
import com.example.anh.itenki.model.currentforecast.OpenWeatherJSon;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import javax.inject.Inject;

/**
 * Created by anh on 2018/02/16.
 */

public class CurrentForecastViewModel extends BaseObservable {
    NumberFormat format = new DecimalFormat("#0.0");

    private String addressName;
    private String imageIconUrl;
    private String mainTemp;
    private String mainState;
    private String maxMinTemp;
    private String wind;
    private String press;
    private String humidity;
    private String state;
    private String sunriseTime;
    private String sunsetTime;

    private Context context;

    @Inject
    public CurrentForecastViewModel(Context context) {
        this.context = context;
    }

    public void setOpenWeather(OpenWeatherJSon openWeather) {
        addressName = openWeather.name;
        imageIconUrl = getIconSkyUrl(openWeather);
        mainTemp = getStringTemp(openWeather.main.temp);
        mainState = openWeather.weather.get(0).main;
        maxMinTemp = getStringTemp(openWeather.main.tempMax) + "/" + getStringTemp(openWeather.main.tempMin);
        wind = openWeather.wind.speed + "m/s";
        press = openWeather.main.pressure + "hpa";
        humidity = openWeather.main.humidity + "%";
        state = openWeather.weather.get(0).description;
        sunriseTime = getStringTime(openWeather.sys.sunrise);
        sunsetTime = getStringTime(openWeather.sys.sunset);

        notifyPropertyChanged(BR.addressName);
        notifyPropertyChanged(BR.imageIconUrl);
        notifyPropertyChanged(BR.mainTemp);
        notifyPropertyChanged(BR.mainState);
        notifyPropertyChanged(BR.maxMinTemp);
        notifyPropertyChanged(BR.wind);
        notifyPropertyChanged(BR.press);
        notifyPropertyChanged(BR.humidity);
        notifyPropertyChanged(BR.state);
        notifyPropertyChanged(BR.sunriseTime);
        notifyPropertyChanged(BR.sunsetTime);
    }

    @Bindable
    public String getAddressName() {
        return addressName;
    }

    @Bindable
    public String getImageIconUrl() {
        return imageIconUrl;
    }

    @Bindable
    public String getMainTemp() {
        return mainTemp;
    }

    @Bindable
    public String getMainState() {
        return mainState;
    }

    @Bindable
    public String getMaxMinTemp() {
        return maxMinTemp;
    }

    @Bindable
    public String getWind() {
        return wind;
    }

    @Bindable
    public String getPress() {
        return press;
    }

    @Bindable
    public String getHumidity() {
        return humidity;
    }

    @Bindable
    public String getState() {
        return state;
    }

    @Bindable
    public String getSunriseTime() {
        return sunriseTime;
    }

    @Bindable
    public String getSunsetTime() {
        return sunsetTime;
    }

    private String getIconSkyUrl(OpenWeatherJSon openWeather) {
        return context.getString(R.string.base_icon_url) + openWeather.weather.get(0).icon + ".png";
    }

    private String getStringTemp(double temp) {
        return format.format(temp - 273.15) + "°C";
    }

    private String getStringTime(long time) {
        Date timeSunrise = new Date(time * 1000);
        return timeSunrise.getHours() + ":" + timeSunrise.getMinutes();
    }
}
