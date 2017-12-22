package com.example.anh.itenki.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.anh.itenki.R;
import com.example.anh.itenki.utils.LocationService;
import com.example.anh.itenki.utils.SharedPreference;
import com.example.anh.itenki.utils.Utils;

import es.dmoral.toasty.Toasty;

public class SplashScreenActivity extends AppCompatActivity {
    private boolean _active = false;
    private int _splashTime = 2500;
    public static double latitude;
    public static double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        initService();

        int posLanguage = SharedPreference.getInstance(this).getInt("Language", 0);
        Log.e("LANGUAGE", "==> " + posLanguage);
        if (posLanguage == 0) {
            Utils.setLocaleLanguage(this, "en");
        } else if (posLanguage == 1) {
            Utils.setLocaleLanguage(this, "ja");
        }

        if (Utils.isNetworkConnected(this)) {
            splash();
        } else {
            Toasty.info(getApplicationContext(), getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }

        final SwipeRefreshLayout swipe = (SwipeRefreshLayout)findViewById(R.id.swipeSplash);
        swipe.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isNetworkConnected(SplashScreenActivity.this) && !_active) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            SplashScreenActivity.this.startActivity(mainIntent);
                            swipe.setRefreshing(false);
                        }
                    }, 3000);
                } else if (!Utils.isNetworkConnected(SplashScreenActivity.this)) {
                    Toasty.info(getApplicationContext(), getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                    swipe.setRefreshing(false);
                }

            }
        });

    }

    private void splash() {
        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    _active = true;
                    while (waited < _splashTime) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    SplashScreenActivity.this.finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        splashThread.start();
    }

    public void initService() {
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
    }
}
