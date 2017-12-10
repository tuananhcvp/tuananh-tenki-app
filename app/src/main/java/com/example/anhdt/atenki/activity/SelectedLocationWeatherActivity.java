package com.example.anhdt.atenki.activity;

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

import com.example.anhdt.atenki.R;
import com.example.anhdt.atenki.model.currentforecast.OpenWeatherJSon;
import com.example.anhdt.atenki.utils.Utils;
import com.example.anhdt.atenki.utils.WeatherService;
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
 * Created by anhdt on 8/22/17.
 */
public class SelectedLocationWeatherActivity extends AppCompatActivity {
    private Button btnDetail;
    private String selectedLocation;
    private TextView txtCurAddress;
    private SwipeRefreshLayout swipeSelected;

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
                if (!Utils.isNetworkConnected(SelectedLocationWeatherActivity.this)){
                    Toasty.info(getApplicationContext(), getString(R.string.check_internet), Toast.LENGTH_SHORT, true).show();
                    return;
                }
                else {
//                    Intent detailIntent = new Intent(SelectedLocationWeatherActivity.this, ForecastDetailActivity.class);
//                    detailIntent.putExtra("SelectedAddress", selectedLocation);
//                    startActivity(detailIntent);
                }

            }
        });

        swipeSelected.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!Utils.isNetworkConnected(SelectedLocationWeatherActivity.this)){
                    Toasty.info(getApplicationContext(), getString(R.string.check_internet), Toast.LENGTH_SHORT, true).show();
                    swipeSelected.setRefreshing(false);
                } else {
                    loadCurrentWeatherByCityName(selectedLocation);
                }
            }
        });
    }

    private void getWeatherByAddress(){
        Intent intent = getIntent();
        selectedLocation = intent.getStringExtra("SelectedAddress");
        txtCurAddress.setText(selectedLocation);
        loadCurrentWeatherByCityName(selectedLocation);
    }

    public void loadCurrentWeatherByCityName(String city){
        swipeSelected.setRefreshing(true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                // Sử dụng GSON cho việc parse và maps JSON data tới Object
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Khởi tạo các cuộc gọi cho Retrofit 2.0
        WeatherService weatherService = retrofit.create(WeatherService.class);

        Call<OpenWeatherJSon> call = weatherService.getCurrentWeather("http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+getString(R.string.appid_weather));
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
                    Utils.loadCurrentWeather(SelectedLocationWeatherActivity.this, openWeatherJSon);
                    swipeSelected.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<OpenWeatherJSon> call, Throwable t) {
                Log.d("Response","==> Fail");
                swipeSelected.setRefreshing(false);
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
