package com.example.anhdt.atenki.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anhdt.atenki.R;
import com.example.anhdt.atenki.model.currentforecast.OpenWeatherJSon;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

/**
 * Created by anhdt on 8/22/17.
 */
public class Utils {
    public static void loadCurrentWeather(Activity activity, OpenWeatherJSon weatherJSon){
        if (weatherJSon==null) return;
        NumberFormat format = new DecimalFormat("#0.0");

        TextView tvCurLocation = (TextView)activity.findViewById(R.id.tvCurLocation);
        ImageView imgIconSky = (ImageView)activity.findViewById(R.id.imgIconSky);
        TextView tvTemp = (TextView)activity.findViewById(R.id.tvTemp);
        TextView tvStateMain = (TextView)activity.findViewById(R.id.tvStateMain);
        TextView tvMaxMinTemp = (TextView)activity.findViewById(R.id.tvMaxMinTemp);
        TextView tvWind = (TextView)activity.findViewById(R.id.tvWind);
        TextView tvPress = (TextView)activity.findViewById(R.id.tvPress);
        TextView tvHumidity = (TextView)activity.findViewById(R.id.tvHumidity);
        TextView tvState = (TextView)activity.findViewById(R.id.tvState);
        TextView tvSunrise = (TextView)activity.findViewById(R.id.tvSunrise);
        TextView tvSunset = (TextView)activity.findViewById(R.id.tvSunset);

        String curLocation = weatherJSon.getName().toString();
        String temp = format.format(weatherJSon.getMain().getTemp()-273.15)+"°C";
        String urlIconSky = "http://openweathermap.org/img/w/"+weatherJSon.getWeather().get(0).getIcon()+".png";
        String stateMain = weatherJSon.getWeather().get(0).getMain().toString();
        String maxMinTemp = format.format(weatherJSon.getMain().getTemp_max()-273.15)+"°C/"+format.format(weatherJSon.getMain().getTemp_min()-273.15)+"°C";
        String wind = weatherJSon.getWind().getSpeed()+"m/s";
        String press = weatherJSon.getMain().getPressure()+"hpa";
        String humidity = weatherJSon.getMain().getHumidity()+"%";
        String state = weatherJSon.getWeather().get(0).getDescription().toString();
        Date timeSunrise = new Date(weatherJSon.getSys().getSunrise()*1000);
        String sunrise = timeSunrise.getHours()+":"+timeSunrise.getMinutes();
        Date timeSunset = new Date(weatherJSon.getSys().getSunset()*1000);
        String sunset = timeSunset.getHours()+":"+timeSunset.getMinutes();

        tvCurLocation.setText(curLocation);
        Glide.with(activity).load(urlIconSky).into(imgIconSky);
        tvTemp.setText(temp);
        tvStateMain.setText(stateMain);
        tvMaxMinTemp.setText(maxMinTemp);
        tvWind.setText("Wind: "+wind);
        tvPress.setText("Press: "+press);
        tvHumidity.setText("Hum: "+humidity);
        tvState.setText("State: "+state);
        tvSunrise.setText("Sunrise: "+sunrise);
        tvSunset.setText("Sunset: "+sunset);
    }

    public static boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
