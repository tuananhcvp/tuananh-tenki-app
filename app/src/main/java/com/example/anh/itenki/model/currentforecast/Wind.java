package com.example.anh.itenki.model.currentforecast;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by anh on 2017/12/06.
 */

public class Wind implements Serializable {
    @SerializedName("speed")
    public double speed;

    public double deg;

    public double getDeg() {
        return deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
