package com.example.anh.itenki.model.currentforecast;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by anh on 2017/12/06.
 */

public class Sys implements Serializable {
    public double message;
    public String country;

    @SerializedName("sunrise")
    public long sunrise;

    @SerializedName("sunset")
    public long sunset;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }
}
