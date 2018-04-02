package com.example.anh.itenki.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.anh.itenki.BR;

import javax.inject.Inject;

/**
 * Created by anh on 2018/03/22.
 */

public class DetailForecastViewModel extends BaseObservable {
    public Context context;
    private String address;

    @Inject
    public DetailForecastViewModel(Context context) {
        this.context = context;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    @Bindable
    public String getAddress() {
        return address;
    }

}
