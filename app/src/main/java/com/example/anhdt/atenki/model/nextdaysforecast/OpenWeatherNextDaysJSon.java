package com.example.anhdt.atenki.model.nextdaysforecast;

import java.util.List;

/**
 * Created by anhdt on 10/4/16.
 */
public class OpenWeatherNextDaysJSon {
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

    public List<ListItem> getList() {
        return list;
    }

    public void setList(List<ListItem> list) {
        this.list = list;
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
}
