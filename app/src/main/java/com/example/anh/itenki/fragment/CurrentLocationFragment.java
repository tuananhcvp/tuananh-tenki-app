package com.example.anh.itenki.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.anh.itenki.R;
import com.example.anh.itenki.activity.ForecastDetailActivity;
import com.example.anh.itenki.activity.MainActivity;
import com.example.anh.itenki.activity.SplashScreenActivity;
import com.example.anh.itenki.model.ApiClient;
import com.example.anh.itenki.model.currentforecast.OpenWeatherJSon;
import com.example.anh.itenki.utils.LocationService;
import com.example.anh.itenki.utils.SharedPreference;
import com.example.anh.itenki.utils.Utils;
import com.example.anh.itenki.utils.WeatherInfoAPI;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anh on 2017/12/06.
 */

public class CurrentLocationFragment extends Fragment {
    private String curLocation = "";
    private Unbinder unbinder;

    @BindView(R.id.btnDetail)
    Button btnDetail;

    @BindView(R.id.swipeCurrent)
    SwipeRefreshLayout swipeCurrent;

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

        if (SplashScreenActivity.latitude == 0 && SplashScreenActivity.longitude == 0) {
            if (LocationService.mGoogleApiClient.isConnecting() || LocationService.mGoogleApiClient.isConnected()) {
                Log.e("mGoogleApiClient","==> Disconnect");
                LocationService.mGoogleApiClient.disconnect();
            }
            initService();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarName(getString(R.string.title_current_location));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_location, container, false);

        unbinder = ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (SplashScreenActivity.latitude == 0 && SplashScreenActivity.longitude == 0) {
            swipeCurrent.setRefreshing(true);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadCurrentWeatherByLocation(SplashScreenActivity.latitude, SplashScreenActivity.longitude);
                }
            }, 1000);
        } else {
            loadCurrentWeatherByLocation(SplashScreenActivity.latitude, SplashScreenActivity.longitude);
        }

        swipeCurrent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!Utils.isNetworkConnected(getActivity())) {
                    swipeCurrent.setRefreshing(false);
                    showToastCheckInternet();
                } else {
                    loadCurrentWeatherByLocation(SplashScreenActivity.latitude, SplashScreenActivity.longitude);
                }
            }
        });

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.isNetworkConnected(getActivity())) {
                    showToastCheckInternet();
                } else {
                    if (!curLocation.equalsIgnoreCase("")) {
                        Intent detailIntent = new Intent(getActivity(), ForecastDetailActivity.class);
                        detailIntent.putExtra("CurrentLatitude", SplashScreenActivity.latitude);
                        detailIntent.putExtra("CurrentLongitude", SplashScreenActivity.longitude);
                        detailIntent.putExtra("CurrentAddressName", curLocation);
                        startActivity(detailIntent);
                    } else {
                        Utils.showToastNotify(getContext(), getString(R.string.check_data_not_found));
                    }

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void loadCurrentWeatherByLocation(double lat, double lon) {
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
                    Log.d("Response","==> " + weatherJSon);
                    OpenWeatherJSon openWeatherJSon = new Gson().fromJson(weatherJSon, new TypeToken<OpenWeatherJSon>(){}.getType());
                    Log.d("openWeatherJSon","==> " + new Gson().toJson(openWeatherJSon));
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

    public void initService() {
        Intent intent = new Intent(getActivity(), LocationService.class);
        getActivity().startService(intent);
    }

    private void showToastCheckInternet() {
        Utils.showToastNotify(getContext(), getString(R.string.check_internet));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unbind the view to free some memory
        unbinder.unbind();
    }
}

