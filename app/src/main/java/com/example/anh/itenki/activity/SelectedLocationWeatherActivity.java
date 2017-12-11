package com.example.anh.itenki.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anh.itenki.R;
import com.example.anh.itenki.model.ApiClient;
import com.example.anh.itenki.model.currentforecast.OpenWeatherJSon;
import com.example.anh.itenki.utils.Utils;
import com.example.anh.itenki.utils.WeatherInfoAPI;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anh on 2017/12/06.
 */

public class SelectedLocationWeatherActivity extends AppCompatActivity {

    private Button btnDetail;
    private String selectedLocation;
    private TextView txtCurAddress;
    private SwipeRefreshLayout swipeSelected;
    private boolean isDataEmpty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_current_location);

        swipeSelected = (SwipeRefreshLayout)findViewById(R.id.swipeCurrent);
        swipeSelected.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);

        txtCurAddress = (TextView)findViewById(R.id.tvCurLocation);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWeatherByAddress();

        btnDetail = (Button)findViewById(R.id.btnDetail);
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.isNetworkConnected(SelectedLocationWeatherActivity.this)) {
                    Toasty.info(getApplicationContext(), getString(R.string.check_internet), Toast.LENGTH_SHORT, true).show();
                    return;
                } else {
                    if (!isDataEmpty) {
                        Intent detailIntent = new Intent(SelectedLocationWeatherActivity.this, ForecastDetailActivity.class);
                        detailIntent.putExtra("SelectedAddress", selectedLocation);
                        startActivity(detailIntent);
                    } else {
                        swipeSelected.setRefreshing(false);
                        Toasty.info(getApplicationContext(), getString(R.string.check_data_not_found), Toast.LENGTH_SHORT, true).show();
                    }
                }

            }
        });

        swipeSelected.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!Utils.isNetworkConnected(SelectedLocationWeatherActivity.this)) {
                    Toasty.info(getApplicationContext(), getString(R.string.check_internet), Toast.LENGTH_SHORT, true).show();
                    swipeSelected.setRefreshing(false);
                } else {
                    if (!isDataEmpty) {
                        loadCurrentWeatherByCityName(selectedLocation);
                    } else {
                        swipeSelected.setRefreshing(false);
                        Toasty.info(getApplicationContext(), getString(R.string.check_data_not_found), Toast.LENGTH_SHORT, true).show();
                    }
                }
            }
        });
    }

    private void getWeatherByAddress() {
        Intent intent = getIntent();
        selectedLocation = intent.getStringExtra("SelectedAddress");
        txtCurAddress.setText(selectedLocation);
        loadCurrentWeatherByCityName(selectedLocation);
    }

    public void loadCurrentWeatherByCityName(String city) {
        swipeSelected.setRefreshing(true);

        WeatherInfoAPI infoAPI = ApiClient.getClient().create(WeatherInfoAPI.class);
        Call<OpenWeatherJSon> callWeather = infoAPI.loadCurrentWeatherByName(city, getString(R.string.appid_weather));
        // Cuộc gọi bất đồng bọ (chạy dưới background)
        callWeather.enqueue(new Callback<OpenWeatherJSon>() {
            @Override
            public void onResponse(Call<OpenWeatherJSon> call, Response<OpenWeatherJSon> response) {
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response));
                    String weatherJSon = jsonObject.get("body").toString();
                    Log.d("Response","==> "+weatherJSon);
                    OpenWeatherJSon openWeatherJSon = new Gson().fromJson(weatherJSon, new TypeToken<OpenWeatherJSon>(){}.getType());
                    Log.d("openWeatherJSon","==> "+new Gson().toJson(openWeatherJSon));
                    Utils.loadCurrentWeather(SelectedLocationWeatherActivity.this, openWeatherJSon);
                    swipeSelected.setRefreshing(false);
                    isDataEmpty = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                    swipeSelected.setRefreshing(false);
                    isDataEmpty = true;
                    Toasty.info(getApplicationContext(), getString(R.string.check_data_not_found), Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onFailure(Call<OpenWeatherJSon> call, Throwable t) {
                Log.d("Response","==> Fail");
                t.printStackTrace();
                swipeSelected.setRefreshing(false);
                isDataEmpty = true;
            }
        });
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
}