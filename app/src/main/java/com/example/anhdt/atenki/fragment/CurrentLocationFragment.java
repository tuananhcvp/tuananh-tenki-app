package com.example.anhdt.atenki.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.anhdt.atenki.R;
import com.example.anhdt.atenki.activity.MainActivity;
import com.example.anhdt.atenki.activity.SplashScreenActivity;
import com.example.anhdt.atenki.model.currentforecast.OpenWeatherJSon;
import com.example.anhdt.atenki.utils.Utils;
import com.example.anhdt.atenki.utils.WeatherService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anhdt on 8/18/17.
 */
public class CurrentLocationFragment extends Fragment {
    private Button btnDetail;
    private SwipeRefreshLayout swipeCurrent;

    public CurrentLocationFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_location, container, false);

        btnDetail = (Button)v.findViewById(R.id.btnDetail);
        swipeCurrent = (SwipeRefreshLayout)v.findViewById(R.id.swipeCurrent);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadCurrentWeatherByLocation(SplashScreenActivity.latitude, SplashScreenActivity.longitude);

        swipeCurrent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!Utils.isNetworkConnected(getActivity())){
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

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void loadCurrentWeatherByLocation(double lat, double lon){
        swipeCurrent.setRefreshing(true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                // Sử dụng GSON cho việc parse và maps JSON data tới Object
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Khởi tạo các cuộc gọi cho Retrofit 2.0
        WeatherService weatherService = retrofit.create(WeatherService.class);

        Call<OpenWeatherJSon> call = weatherService.getCurrentWeather("http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid="+getString(R.string.appid_weather));
        // Cuộc gọi bất đồng bọ (chạy dưới background)
        call.enqueue(new Callback<OpenWeatherJSon>() {
            @Override
            public void onResponse(Call<OpenWeatherJSon> call, Response<OpenWeatherJSon> response) {
//                Log.d("Response","==> "+ new Gson().toJson(response));
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response));
                    String weatherJSon = jsonObject.get("body").toString();
                    Log.d("Response","==> "+weatherJSon);
                    OpenWeatherJSon openWeatherJSon = new Gson().fromJson(weatherJSon, new TypeToken<OpenWeatherJSon>(){}.getType());
                    Log.d("openWeatherJSon","==> "+new Gson().toJson(openWeatherJSon));
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
