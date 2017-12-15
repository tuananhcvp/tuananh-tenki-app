package com.example.anh.itenki.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anh.itenki.adapter.HorizontalListViewDailyAdapter;
import com.example.anh.itenki.adapter.NextDaysWeatherAdapter;
import com.example.anh.itenki.model.ApiClient;
import com.example.anh.itenki.model.dailyforecast.OpenWeatherDailyJSon;
import com.example.anh.itenki.model.nextdaysforecast.ListItem;
import com.example.anh.itenki.model.nextdaysforecast.OpenWeatherNextDaysJSon;
import com.example.anh.itenki.utils.Utils;
import com.example.anh.itenki.utils.WeatherInfoAPI;
import com.google.gson.Gson;
import com.sileria.android.view.HorzListView;

import com.example.anh.itenki.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastDetailActivity extends AppCompatActivity {
    private TextView txtSelectedAddr;
    private HorzListView lvDailyWeather;
    private SwipeRefreshLayout swipeDetail;
    private ListView lvNextDayWeather;

    private String selectedAddr, curLocation;
    private double latitude, longitude;

    private NumberFormat format = new DecimalFormat("#0.0");
    private String[] arrDailyTime = {"6AM", "9AM", "12AM", "3PM", "6PM", "9PM", "12PM", "3AM"};
    private String[] arrDailyTemp = new String[8];
    private String[] urlDailyIcon = new String[8];
    private String[] arrNextDaysDate = new String[5];
    private String[] arrNextDays;
    private String[] arrNextDaysTemp = new String[5];
    private String[] urlNextDaysIcon = new String[5];

    private final int TYPE_DAILY_NAME = 100;
    private final int TYPE_DAILY_LOCATION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setActionbarTitle(getResources().getString(R.string.title_forecast_detail), this, getSupportActionBar());
        setContentView(R.layout.activity_forecast_detail);

        init();

        if (selectedAddr != null) {
            txtSelectedAddr.setText(selectedAddr);
            swipeDetail.setRefreshing(true);
            loadForecastDetail(TYPE_DAILY_NAME);

        } else if (curLocation != null) {
            txtSelectedAddr.setText(curLocation);
            swipeDetail.setRefreshing(true);
            loadForecastDetail(TYPE_DAILY_LOCATION);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadForecastDetail(int type) {
        WeatherInfoAPI infoAPI = ApiClient.getClient().create(WeatherInfoAPI.class);

        switch (type) {
            case TYPE_DAILY_NAME:
                Call<OpenWeatherDailyJSon> callDailyName = infoAPI.loadDailyWeatherByName(selectedAddr, getString(R.string.appid_weather));
                Call<OpenWeatherNextDaysJSon> callNextDayName = infoAPI.loadNextDayWeatherByName(selectedAddr, getString(R.string.appid_weather));

                callDailyName.enqueue(new Callback<OpenWeatherDailyJSon>() {
                    @Override
                    public void onResponse(Call<OpenWeatherDailyJSon> call, Response<OpenWeatherDailyJSon> response) {
                        Log.e("DAILY WEATHER", "==> " + new Gson().toJson(response.body()));

                        for (int i = 0;i < 8;i++) {
                            String temp = format.format(response.body().getList().get(i).getMain().getTemp() - 273.15) + "°C";
                            arrDailyTemp[i] = temp;
                            urlDailyIcon[i] = response.body().getList().get(i).getWeather().get(0).getIcon();
                        }

                        HorizontalListViewDailyAdapter dailyAdapter = new HorizontalListViewDailyAdapter(ForecastDetailActivity.this, arrDailyTime, arrDailyTemp, urlDailyIcon);
                        lvDailyWeather.setAdapter(dailyAdapter);
                    }

                    @Override
                    public void onFailure(Call<OpenWeatherDailyJSon> call, Throwable t) {
                        Log.e("DAILY WEATHER", "==> Call Daily Fail");
                        t.printStackTrace();
                    }
                });

                callNextDayName.enqueue(new Callback<OpenWeatherNextDaysJSon>() {
                    @Override
                    public void onResponse(Call<OpenWeatherNextDaysJSon> call, Response<OpenWeatherNextDaysJSon> response) {
                        Log.e("NEXTDAY WEATHER", "==> " + new Gson().toJson(response.body()));
                        swipeDetail.setRefreshing(false);
                        swipeDetail.setEnabled(false);

                        for (int i = 0;i < 5;i++) {
                            String tempMax = format.format(response.body().getList().get(i+1).getTemp().getMax()-273.15) + "°C";
                            String tempMin = format.format(response.body().getList().get(i+1).getTemp().getMin()-273.15) + "°C";
                            arrNextDaysTemp[i] = tempMax + "/" + tempMin;
                            urlNextDaysIcon[i] = response.body().getList().get(i+1).getWeather().get(0).getIcon();
                        }

                        NextDaysWeatherAdapter nextDaysAdapter = new NextDaysWeatherAdapter(ForecastDetailActivity.this, arrNextDays, arrNextDaysDate, urlNextDaysIcon, arrNextDaysTemp     );
                        lvNextDayWeather.setAdapter(nextDaysAdapter);

                        final OpenWeatherNextDaysJSon weather = response.body();
                        lvNextDayWeather.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String date = arrNextDays[position]+"<"+arrNextDaysDate[position]+">";
                                showDailyWeatherDialog(weather, date, position);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<OpenWeatherNextDaysJSon> call, Throwable t) {
                        Log.e("NEXTDAY WEATHER", "==> Call NextDay Fail");
                        swipeDetail.setRefreshing(false);
                        swipeDetail.setEnabled(false);
                    }
                });

                break;

            case TYPE_DAILY_LOCATION:
                Call<OpenWeatherDailyJSon> callDailyLocation = infoAPI.loadDailyWeatherByLocation(latitude, longitude, getString(R.string.appid_weather));
                Call<OpenWeatherNextDaysJSon> callNextDayLocation = infoAPI.loadNextDayWeatherByLocation(latitude, longitude, 7, getString(R.string.appid_weather));

                callDailyLocation.enqueue(new Callback<OpenWeatherDailyJSon>() {
                    @Override
                    public void onResponse(Call<OpenWeatherDailyJSon> call, Response<OpenWeatherDailyJSon> response) {
                        Log.e("DAILY WEATHER", "==> " + new Gson().toJson(response.body()));

                        for (int i = 0;i < 8;i++) {
                            String temp = format.format(response.body().getList().get(i).getMain().getTemp()-273.15)+"°C";
                            arrDailyTemp[i] = temp;
                            urlDailyIcon[i] = response.body().getList().get(i).getWeather().get(0).getIcon();
                        }

                        HorizontalListViewDailyAdapter dailyAdapter = new HorizontalListViewDailyAdapter(ForecastDetailActivity.this, arrDailyTime, arrDailyTemp, urlDailyIcon);
                        lvDailyWeather.setAdapter(dailyAdapter);

                    }

                    @Override
                    public void onFailure(Call<OpenWeatherDailyJSon> call, Throwable t) {
                        Log.e("DAILY WEATHER", "==> Call Daily Fail");
                        t.printStackTrace();
                    }
                });

                callNextDayLocation.enqueue(new Callback<OpenWeatherNextDaysJSon>() {
                    @Override
                    public void onResponse(Call<OpenWeatherNextDaysJSon> call, Response<OpenWeatherNextDaysJSon> response) {
                        Log.e("NEXTDAY WEATHER", "==> " + new Gson().toJson(response.body()));
                        swipeDetail.setRefreshing(false);
                        swipeDetail.setEnabled(false);

                        for (int i = 0;i < 5;i++) {
                            String tempMax = format.format(response.body().getList().get(i+1).getTemp().getMax()-273.15) + "°C";
                            String tempMin = format.format(response.body().getList().get(i+1).getTemp().getMin()-273.15) + "°C";
                            arrNextDaysTemp[i] = tempMax + "/" + tempMin;
                            urlNextDaysIcon[i] = response.body().getList().get(i+1).getWeather().get(0).getIcon();
                        }

                        NextDaysWeatherAdapter nextDaysAdapter = new NextDaysWeatherAdapter(ForecastDetailActivity.this, arrNextDays, arrNextDaysDate, urlNextDaysIcon, arrNextDaysTemp     );
                        lvNextDayWeather.setAdapter(nextDaysAdapter);

                        final OpenWeatherNextDaysJSon weather = response.body();
                        lvNextDayWeather.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String date = arrNextDays[position]+"<"+arrNextDaysDate[position]+">";
                                showDailyWeatherDialog(weather, date, position);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<OpenWeatherNextDaysJSon> call, Throwable t) {
                        Log.e("NEXTDAY WEATHER", "==> Call NextDay Fail");
                        swipeDetail.setRefreshing(false);
                        swipeDetail.setEnabled(false);

                    }
                });

                break;

            default:
                break;
        }
    }

    public void init() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeDetail = (SwipeRefreshLayout)findViewById(R.id.swipeDetail);
        swipeDetail.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        txtSelectedAddr = (TextView)findViewById(R.id.txtSelectedAddr);
        lvDailyWeather = (HorzListView)findViewById(R.id.lvDailyWeather);
        lvNextDayWeather = (ListView)findViewById(R.id.lvNextDayWeather);

        getArrDate();
        getArrDayOfWeek();

        Intent selectedAddrInt = getIntent();
        selectedAddr = selectedAddrInt.getStringExtra("SelectedAddress");
        curLocation = selectedAddrInt.getStringExtra("CurrentAddressName");
        latitude = selectedAddrInt.getDoubleExtra("CurrentLatitude", 0);
        longitude = selectedAddrInt.getDoubleExtra("CurrentLongitude", 0);
    }

    public void getArrDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                arrNextDays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
                break;

            case Calendar.MONDAY:
                arrNextDays = new String[]{"Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
                break;

            case Calendar.TUESDAY:
                arrNextDays = new String[]{"Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                break;

            case Calendar.WEDNESDAY:
                arrNextDays = new String[]{"Thursday", "Friday", "Saturday", "Sunday", "Monday"};
                break;

            case Calendar.THURSDAY:
                arrNextDays = new String[]{"Friday", "Saturday", "Sunday", "Monday", "Tuesday"};
                break;

            case Calendar.FRIDAY:
                arrNextDays = new String[]{"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday"};
                break;

            case Calendar.SATURDAY:
                arrNextDays = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"};
                break;

        }
    }

    public void getArrDate() {
        for (int i = 1; i < 6; i++) {
            Calendar calendar = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            calendar.add(Calendar.DAY_OF_YEAR, i);
            Date days = calendar.getTime();
            arrNextDaysDate[i - 1] = dateFormat.format(days).toString();
        }
    }

    public void showDailyWeatherDialog(OpenWeatherNextDaysJSon nextDaysJSon, String date, int i) {
        View v = this.getLayoutInflater().inflate(R.layout.next_days_weather_info, null);

        TextView tvDate = v.findViewById(R.id.tvDate);
        ImageView imgIconState = v.findViewById(R.id.imgIconState);
        TextView tvTemp = v.findViewById(R.id.tvTemp);
        TextView tvState = v.findViewById(R.id.tvState);
        TextView tvMaxMinTemp = v.findViewById(R.id.tvMaxMinTemp);
        TextView tvMorTemp = v.findViewById(R.id.tvMorTemp);
        TextView tvEveTemp = v.findViewById(R.id.tvEveTemp);
        TextView tvNightTemp = v.findViewById(R.id.tvNightTemp);
        TextView tvWind = v.findViewById(R.id.tvWind);
        TextView tvHum = v.findViewById(R.id.tvHum);
        TextView tvPress = v.findViewById(R.id.tvPress);

        ListItem item = nextDaysJSon.getList().get(i+1);
        String tempDay = format.format(item.getTemp().getDay()-273.15)+"°C";
        String state = item.getWeather().get(0).getDescription();
        String tempMax = format.format(item.getTemp().getMax()-273.15)+"°C";
        String tempMin = format.format(item.getTemp().getMin()-273.15)+"°C";
        String tempMorn = format.format(item.getTemp().getMorn()-273.15)+"°C";
        String tempEve = format.format(item.getTemp().getEve()-273.15)+"°C";
        String tempNight = format.format(item.getTemp().getNight()-273.15)+"°C";
        String wind = item.getSpeed()+"m/s";
        String press = item.getPressure()+"hpa";
        String hum = item.getHumidity()+"%";
        String urlIcon = item.getWeather().get(0).getIcon();

        tvDate.setText(date);
        Glide.with(this).load(getString(R.string.base_icon_url)+urlIcon+".png").into(imgIconState);
        tvTemp.setText(tempDay);
        tvState.setText(state);
        tvMaxMinTemp.setText(tempMax + "/" + tempMin);
        tvMorTemp.setText(getResources().getString(R.string.txt_morning) + ": " + tempMorn);
        tvEveTemp.setText(getResources().getString(R.string.txt_evening) + ": " + tempEve);
        tvNightTemp.setText(getResources().getString(R.string.txt_night) + ": " + tempNight);
        tvWind.setText(getResources().getString(R.string.txt_wind) + ": " + wind);
        tvHum.setText(getResources().getString(R.string.txt_humidity) + ": " + hum);
        tvPress.setText(getResources().getString(R.string.txt_pressure) + ": " + press);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setView(v);
        builder.setCancelable(true);
//        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.cancel();
//            }
//        });

        final AlertDialog dialog = builder.create();
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
//        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

}
