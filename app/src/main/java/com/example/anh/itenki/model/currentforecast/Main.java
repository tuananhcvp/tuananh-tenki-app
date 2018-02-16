package com.example.anh.itenki.model.currentforecast;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by anh on 2017/12/06.
 */

public class Main implements Serializable {
    @SerializedName("temp")
    public double temp;

    @SerializedName("pressure")
    public double pressure;

    @SerializedName("humidity")
    public double humidity;

    @SerializedName("temp_min")
    public double tempMin;

    @SerializedName("temp_max")
    public double tempMax;

    public double sea_level;
    public double grnd_level;

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getTemp_max() {
        return tempMax;
    }

    public void setTemp_max(double temp_max) {
        this.tempMax = temp_max;
    }

    public double getTemp_min() {
        return tempMin;
    }

    public void setTemp_min(double temp_min) {
        this.tempMin = temp_min;
    }

    public double getGrnd_level() {
        return grnd_level;
    }

    public void setGrnd_level(double grnd_level) {
        this.grnd_level = grnd_level;
    }

    public double getSea_level() {
        return sea_level;
    }

    public void setSea_level(double sea_level) {
        this.sea_level = sea_level;
    }
}
