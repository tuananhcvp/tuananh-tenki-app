package com.example.anhdt.atenki.model.dailyforecast;

import java.util.List;

/**
 * Created by anhdt on 10/4/16.
 */
public class OpenWeatherDailyJSon {
    private City city;
    private String cod;
    private double message;
    private int cnt;
    private List<ListItem> list;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public List<ListItem> getList() {
        return list;
    }

    public void setList(List<ListItem> list) {
        this.list = list;
    }
}
