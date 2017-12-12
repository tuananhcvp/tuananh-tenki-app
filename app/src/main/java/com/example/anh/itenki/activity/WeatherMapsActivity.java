package com.example.anh.itenki.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.anh.itenki.R;
import com.example.anh.itenki.adapter.GoogleMapWeatherInfoAdapter;
import com.example.anh.itenki.model.ApiClient;
import com.example.anh.itenki.model.currentforecast.OpenWeatherJSon;
import com.example.anh.itenki.utils.LocationService;
import com.example.anh.itenki.utils.SharedPreference;
import com.example.anh.itenki.utils.Utils;
import com.example.anh.itenki.utils.WeatherInfoAPI;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.data;

public class WeatherMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Button btnReload;
    private RelativeLayout layoutWarning;
    private boolean hasGPS;
    private ProgressDialog dialog;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        layoutWarning = findViewById(R.id.layoutWarning);
        btnReload = findViewById(R.id.btnReload);
        dialog = new ProgressDialog(this);
        Utils.initProgressDialog(WeatherMapsActivity.this, dialog);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (!Utils.isNetworkConnected(this)) {
            Toast.makeText(getApplicationContext(), "Check your internet connection!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            layoutWarning.setVisibility(RelativeLayout.GONE);
            initView();

        }

    }

    private void setUpMap() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setMyLocationEnabled(true);
            mMap.setTrafficEnabled(true);
            mMap.setIndoorEnabled(true);
            mMap.setBuildingsEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);

//            GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
//                @Override
//                public void onMyLocationChange(Location location) {
//                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
//                }
//            };
//            mMap.setOnMyLocationChangeListener(myLocationChangeListener);
        }
    }

    public void initView() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        hasGPS = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!hasGPS && SharedPreference.getInstance(this).getBoolean("isPermision",false)) {
            if (LocationService.mGoogleApiClient != null) {
                if(!LocationService.mGoogleApiClient.isConnecting() && !LocationService.mGoogleApiClient.isConnected()) {
                    LocationService.mGoogleApiClient.connect();
                }
            }
            settingsRequest();
        } else {
            setUpMap();
            currentLocationWeather();
        }

    }

    public void settingsRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(1 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(LocationService.mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(WeatherMapsActivity.this, REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CHECK_SETTINGS) {
            SharedPreference.getInstance(this).putBoolean("isPermisionLocation",true);
            if(resultCode == RESULT_OK) {
                if(LocationService.mGoogleApiClient.isConnecting() || LocationService.mGoogleApiClient.isConnected()) {
                    Log.e("mGoogleApiClient","==> Disconnect");
                    LocationService.mGoogleApiClient.disconnect();
                }
                setUpMap();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initService();
                    }
                }, 3000);  //Do something after 3000ms

            }
        }
    }

    private void currentLocationWeather() {
        double lat = SplashScreenActivity.latitude;
        double lon = SplashScreenActivity.longitude;
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                new LatLng(lat, lon), 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat+0.008, lon))     // Sets the center of the map to location user
                .zoom(15)                               // Sets the zoom
//                        .bearing(90)                  // Sets the orientation of the camera to east
//                        .tilt(40)                     // Sets the tilt of the camera to 30 degrees
                .build();                               // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadCurrentWeatherByLocation(SplashScreenActivity.latitude, SplashScreenActivity.longitude);
            }
        }, 1000);  //Do something after 1000ms
    }

    public void initService() {
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
    }

    public void loadCurrentWeatherByLocation(final double lat, final double lon) {
        dialog.show();

        WeatherInfoAPI infoAPI = ApiClient.getClient().create(WeatherInfoAPI.class);
        Call<OpenWeatherJSon> callWeather = infoAPI.loadCurrentWeatherByLocation(lat, lon, getString(R.string.appid_weather));
        // Cuộc gọi bất đồng bọ (chạy dưới background)
        callWeather.enqueue(new Callback<OpenWeatherJSon>() {
            @Override
            public void onResponse(Call<OpenWeatherJSon> call, Response<OpenWeatherJSon> response) {
                try {
                    dialog.dismiss();
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response));
                    String weatherJSon = jsonObject.get("body").toString();
                    Log.d("Response","==> "+weatherJSon);
                    OpenWeatherJSon openWeatherJSon = new Gson().fromJson(weatherJSon, new TypeToken<OpenWeatherJSon>(){}.getType());
                    Log.d("openWeatherJSon","==> "+new Gson().toJson(openWeatherJSon));

                    GoogleMapWeatherInfoAdapter infoAdapter = new GoogleMapWeatherInfoAdapter(openWeatherJSon, WeatherMapsActivity.this);
                    mMap.setInfoWindowAdapter(infoAdapter);
                    MarkerOptions options = new MarkerOptions();
                    options.position(new LatLng(lat, lon));
                    Marker marker = mMap.addMarker(options);
                    marker.showInfoWindow();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<OpenWeatherJSon> call, Throwable t) {
                Log.d("Response","==> Fail");
                dialog.dismiss();
            }
        });

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}
