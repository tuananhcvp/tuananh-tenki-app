package com.example.anh.itenki.model.dailyforecast;

/**
 * Created by anh on 2017/12/06.
 */

public class City {
    private long id;
    private String name;
    private Coord coord;
    private String country;
    private int population;
    private SysCity sys;

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public SysCity getSys() {
        return sys;
    }

    public void setSys(SysCity sys) {
        this.sys = sys;
    }
}
