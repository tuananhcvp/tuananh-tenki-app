package com.example.anh.itenki.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anh.itenki.R;
import com.example.anh.itenki.activity.ForecastDetailActivity;
import com.example.anh.itenki.activity.MainActivity;
import com.example.anh.itenki.activity.SplashScreenActivity;
import com.example.anh.itenki.databinding.FragmentNewCurrentLocationBinding;
import com.example.anh.itenki.model.ApiClient;
import com.example.anh.itenki.model.currentforecast.OpenWeatherJSon;
import com.example.anh.itenki.utils.LocationService;
import com.example.anh.itenki.utils.SharedPreference;
import com.example.anh.itenki.utils.Utils;
import com.example.anh.itenki.utils.WeatherInfoAPI;
import com.example.anh.itenki.viewmodel.CurrentForecastViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anh on 2018/02/16.
 */

public class NewCurrentLocationFragment extends Fragment {
    private String curLocation = "";

    private FragmentNewCurrentLocationBinding binding;

    CurrentForecastViewModel viewModel = new CurrentForecastViewModel(getContext());

//    @BindView(R.id.btnDetail)
//    Button btnDetail;

//    @Inject
//    Retrofit retrofit;

    /**
     * NewCurrentLocationFragment initialize
     *
     * @return NewCurrentLocationFragment
     */
    public static NewCurrentLocationFragment newInstance() {
        NewCurrentLocationFragment fragment = new NewCurrentLocationFragment();

        Bundle args = new Bundle();
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
        View layout = inflater.inflate(R.layout.fragment_new_current_location, container, false);
//        ((BaseActivity) getActivity()).getComponent().inject(this);
        binding = FragmentNewCurrentLocationBinding.bind(layout);
        binding.setCurrentModel(viewModel);

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (SplashScreenActivity.latitude == 0 && SplashScreenActivity.longitude == 0) {
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

        binding.btnDetail.setOnClickListener(new View.OnClickListener() {
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

    private void loadCurrentWeatherByLocation(double lat, double lon) {
        int posLanguage = SharedPreference.getInstance(getContext()).getInt("Language", 0);

        WeatherInfoAPI infoAPI = ApiClient.getClient().create(WeatherInfoAPI.class);
        Call<OpenWeatherJSon> callWeather;
        if (posLanguage == 1) {
            callWeather = infoAPI.loadCurrentWeatherByLocation(lat, lon, "ja", getString(R.string.appid_weather));
        } else {
            callWeather = infoAPI.loadCurrentWeatherByLocation(lat, lon, getString(R.string.appid_weather));
        }
        Log.i("OkHttp", "==> " + callWeather.request().url().toString());

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

                    viewModel.setOpenWeather(openWeatherJSon);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<OpenWeatherJSon> call, Throwable t) {
                Log.d("Response","==> Fail");

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
}
