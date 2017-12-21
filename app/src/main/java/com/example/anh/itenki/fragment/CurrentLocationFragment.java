package com.example.anh.itenki.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.anh.itenki.R;
import com.example.anh.itenki.activity.ForecastDetailActivity;
import com.example.anh.itenki.activity.MainActivity;
import com.example.anh.itenki.activity.SplashScreenActivity;
import com.example.anh.itenki.model.ApiClient;
import com.example.anh.itenki.model.currentforecast.OpenWeatherJSon;
import com.example.anh.itenki.utils.SharedPreference;
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

/**
 * Created by anh on 2017/12/06.
 */

public class CurrentLocationFragment extends Fragment {
    private Button btnDetail;
    private SwipeRefreshLayout swipeCurrent;
    private String curLocation = "";

    /**
     * CurrentLocationFragment initialize
     *
     * @return CurrentLocationFragment
     */
    public static CurrentLocationFragment newInstance() {
        Bundle args = new Bundle();
        CurrentLocationFragment fragment = new CurrentLocationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarName(getResources().getString(R.string.title_current_location));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_location, container, false);

        btnDetail = v.findViewById(R.id.btnDetail);
        swipeCurrent = v.findViewById(R.id.swipeCurrent);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadCurrentWeatherByLocation(SplashScreenActivity.latitude, SplashScreenActivity.longitude);

        swipeCurrent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!Utils.isNetworkConnected(getActivity())) {
                    swipeCurrent.setRefreshing(false);
                    Toasty.info(getContext(), getString(R.string.check_internet), Toast.LENGTH_SHORT, true).show();
                } else {
                    loadCurrentWeatherByLocation(SplashScreenActivity.latitude, SplashScreenActivity.longitude);
                }
            }
        });

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.isNetworkConnected(getActivity())) {
                    Toasty.info(getContext(), getString(R.string.check_internet), Toast.LENGTH_SHORT, true).show();
                } else {
                    if (!curLocation.equalsIgnoreCase("")) {
                        Intent detailIntent = new Intent(getActivity(), ForecastDetailActivity.class);
                        detailIntent.putExtra("CurrentLatitude", SplashScreenActivity.latitude);
                        detailIntent.putExtra("CurrentLongitude", SplashScreenActivity.longitude);
                        detailIntent.putExtra("CurrentAddressName", curLocation);
                        startActivity(detailIntent);
                    } else {
                        Toasty.info(getContext(), getString(R.string.check_data_not_found), Toast.LENGTH_SHORT, true).show();
                    }

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void loadCurrentWeatherByLocation(double lat, double lon) {
        swipeCurrent.setRefreshing(true);
        int posLanguage = SharedPreference.getInstance(getContext()).getInt("Language", 0);

        WeatherInfoAPI infoAPI = ApiClient.getClient().create(WeatherInfoAPI.class);
        Call<OpenWeatherJSon> callWeather;
        if (posLanguage == 1) {
            callWeather = infoAPI.loadCurrentWeatherByLocation(lat, lon, "ja", getString(R.string.appid_weather));
        } else {
            callWeather = infoAPI.loadCurrentWeatherByLocation(lat, lon, getString(R.string.appid_weather));
        }
        Log.i("OkHttp", "==> " + callWeather.request().url().toString());
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
                    curLocation = openWeatherJSon.getName();
                    Utils.loadCurrentWeather(getActivity(), openWeatherJSon);
                    swipeCurrent.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<OpenWeatherJSon> call, Throwable t) {
                Log.d("Response","==> Fail");
                swipeCurrent.setRefreshing(false);
            }
        });

    }
}

