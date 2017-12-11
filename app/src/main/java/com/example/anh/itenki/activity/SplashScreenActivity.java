package com.example.anh.itenki.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.anh.itenki.R;
import com.example.anh.itenki.utils.LocationService;

public class SplashScreenActivity extends AppCompatActivity {
    private boolean _active = false;
    private int _splashTime = 2500;
    private ImageView imgSplash;
    public static double latitude, longitude;
    public static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = (int) (displayMetrics.heightPixels/displayMetrics.density);
//        int width = (int) (displayMetrics.widthPixels/displayMetrics.density);

        imgSplash = (ImageView)findViewById(R.id.imgSplash);
//        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(displayMetrics.widthPixels, displayMetrics.heightPixels);
//        imgSplash.setLayoutParams(param);
        initService();


        if(isNetworkConnected()) {
            splash();
        } else {
            Toast.makeText(getApplicationContext(), "Check your network connection!", Toast.LENGTH_SHORT).show();

        }

        final SwipeRefreshLayout swipe = (SwipeRefreshLayout)findViewById(R.id.swipeSplash);
        swipe.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isNetworkConnected()&&!_active) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            SplashScreenActivity.this.startActivity(mainIntent);
                            swipe.setRefreshing(false);
                        }
                    }, 3000);
                } else if (!isNetworkConnected()) {
                    Toast.makeText(getApplicationContext(), "Check your network connection!", Toast.LENGTH_SHORT).show();
                    swipe.setRefreshing(false);
                }

            }
        });

    }

    public void splash() {
        Thread splashThread = new Thread(){
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

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void initService() {
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
    }
}
