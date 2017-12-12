package com.example.anh.itenki.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anh.itenki.R;
import com.example.anh.itenki.activity.SplashScreenActivity;
import com.example.anh.itenki.model.currentforecast.OpenWeatherJSon;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

/**
 * Created by anh on 2017/12/06.
 */

public class Utils {
    public static void loadCurrentWeather(Activity activity, OpenWeatherJSon weatherJSon) {
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
        String urlIconSky = activity.getString(R.string.base_icon_url)+weatherJSon.getWeather().get(0).getIcon()+".png";
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

    public static void setActionbarTitle(String title, Activity context, ActionBar actionBar) {
        TextView txtTitle = new TextView(context);
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        txtTitle.setLayoutParams(layoutparams);
        txtTitle.setText(title);
        txtTitle.setTextColor(Color.WHITE);
        txtTitle.setGravity(Gravity.CENTER_VERTICAL);
        txtTitle.setTextSize(22);

        Typeface fontType = Typeface.createFromAsset(context.getAssets(), "fonts/Pacifico.ttf");
        txtTitle.setTypeface(fontType);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(txtTitle);
    }

    public static void initProgressDialog(Activity context, ProgressDialog dialog) {
        dialog.setProgressStyle(android.R.style.Theme_Translucent_NoTitleBar);
//        dialog.setTitle(ssTitle);
        dialog.setMessage(context.getString(R.string.dialog_data_loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
    }
}

