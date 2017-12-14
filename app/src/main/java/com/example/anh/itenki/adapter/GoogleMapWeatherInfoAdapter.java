package com.example.anh.itenki.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anh.itenki.R;
import com.example.anh.itenki.activity.MainActivity;
import com.example.anh.itenki.model.currentforecast.OpenWeatherJSon;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

/**
 * Created by anh on 2017/12/12.
 */

public class GoogleMapWeatherInfoAdapter implements GoogleMap.InfoWindowAdapter {
    private Activity context;
    private Marker maker;
    private OpenWeatherJSon openWeatherJSon;
    private NumberFormat format = new DecimalFormat("#0.0");
    private Bitmap bmIcon;

    public GoogleMapWeatherInfoAdapter(Activity context) {
        this.context = context;
    }

    public GoogleMapWeatherInfoAdapter(OpenWeatherJSon openWeatherJSon, Activity context, Bitmap bitmap) {
        this(context);
        this.openWeatherJSon = openWeatherJSon;
        this.bmIcon = bitmap;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View v = context.getLayoutInflater().inflate(R.layout.next_days_weather_info, null);

        TextView tvAddress = v.findViewById(R.id.tvDate);
        ImageView imgIconState = v.findViewById(R.id.imgIconState);
        TextView tvTemp = v.findViewById(R.id.tvTemp);
        TextView tvStateMain = v.findViewById(R.id.tvState);
        TextView tvMaxMinTemp = v.findViewById(R.id.tvMaxMinTemp);
        TextView tvWind = v.findViewById(R.id.tvMorTemp);
        TextView tvPress = v.findViewById(R.id.tvEveTemp);
        TextView tvHum = v.findViewById(R.id.tvNightTemp);
        TextView tvState = v.findViewById(R.id.tvWind);
        TextView tvSunrise = v.findViewById(R.id.tvHum);
        TextView tvSunset = v.findViewById(R.id.tvPress);

        String curLocation = openWeatherJSon.getName().toString();
        String temp = format.format(openWeatherJSon.getMain().getTemp()-273.15)+"°C";
        String urlIconSky = context.getString(R.string.base_icon_url)+openWeatherJSon.getWeather().get(0).getIcon()+".png";
        String stateMain = openWeatherJSon.getWeather().get(0).getMain().toString();
        String maxMinTemp = format.format(openWeatherJSon.getMain().getTemp_max()-273.15)+"°C/"+
                format.format(openWeatherJSon.getMain().getTemp_min()-273.15)+"°C";
        String wind = openWeatherJSon.getWind().getSpeed()+"m/s";
        String press = openWeatherJSon.getMain().getPressure()+"hpa";
        String humidity = openWeatherJSon.getMain().getHumidity()+"%";
        String state = openWeatherJSon.getWeather().get(0).getDescription().toString();
        Date timeSunrise = new Date(openWeatherJSon.getSys().getSunrise()*1000);
        String sunrise = timeSunrise.getHours()+":"+timeSunrise.getMinutes();
        Date timeSunset = new Date(openWeatherJSon.getSys().getSunset()*1000);
        String sunset = timeSunset.getHours()+":"+timeSunset.getMinutes();

        tvAddress.setText(curLocation);
//        Glide.with(context).load(urlIconSky).asBitmap().into(imgIconState);
        imgIconState.setImageBitmap(bmIcon);
        tvTemp.setText(temp);
        tvStateMain.setText(stateMain);
        tvMaxMinTemp.setText(maxMinTemp);
        tvWind.setText(context.getString(R.string.txt_wind)+wind);
        tvPress.setText(context.getString(R.string.txt_pressure)+press);
        tvHum.setText(context.getString(R.string.txt_humidity)+humidity);
        tvState.setText(context.getString(R.string.txt_state)+state);
        tvSunrise.setText(context.getString(R.string.txt_sunrise)+sunrise);
        tvSunset.setText(context.getString(R.string.txt_sunset)+sunset);

        v.setBackgroundColor(ContextCompat.getColor(context, R.color.skyblue));
        v.setLayoutParams(new LinearLayout.LayoutParams(800, 1000));

        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
