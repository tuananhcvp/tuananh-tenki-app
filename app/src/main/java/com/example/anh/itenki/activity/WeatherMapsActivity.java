package com.example.anh.itenki.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Button btnReload;
    private RelativeLayout layoutWarning;
    private boolean hasGPS;
    private ProgressDialog dialog;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    protected static final int REQUEST_FIND_PLACE = 120;

    public ArrayList<Marker> listMarker = new ArrayList<>();
    public HashMap<Marker, OpenWeatherJSon> markerInfo = new HashMap<>();
    public HashMap<Marker, Bitmap> markerBitmap = new HashMap<>();

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

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.isNetworkConnected(WeatherMapsActivity.this)) {
                    layoutWarning.setVisibility(RelativeLayout.VISIBLE);
                } else {
                    layoutWarning.setVisibility(RelativeLayout.GONE);
                    initView();
                }
            }
        });

        if (!Utils.isNetworkConnected(this)) {
            Toasty.info(getApplicationContext(), getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        } else {
            layoutWarning.setVisibility(RelativeLayout.GONE);
            initView();
        }

    }

    private void initView() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        hasGPS = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!hasGPS && SharedPreference.getInstance(this).getBoolean("isPermision",false)) {
            if (LocationService.mGoogleApiClient != null) {
                if (!LocationService.mGoogleApiClient.isConnecting() && !LocationService.mGoogleApiClient.isConnected()) {
                    LocationService.mGoogleApiClient.connect();
                }
            }
            settingsRequest();
        } else {
            setUpMap();
            currentLocationWeather();
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

            addEvent();

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

    private void addEvent() {
        if (mMap == null) {
            return;
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (!Utils.isNetworkConnected(WeatherMapsActivity.this)) {
                    layoutWarning.setVisibility(RelativeLayout.VISIBLE);
                } else {
                    if (listMarker.size() > 4) {
                        listMarker.get(0).setVisible(false);
                        markerInfo.remove(listMarker.get(0));
                        markerBitmap.remove(listMarker.get(0));
                        listMarker.remove(0);
                    }

                    layoutWarning.setVisibility(RelativeLayout.GONE);
                    moveAndShowWeatherNewPlace(latLng);
                }
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.hideInfoWindow();
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (Marker mk : listMarker) {
                    if (mk.getId().equalsIgnoreCase(marker.getId())) {
                        GoogleMapWeatherInfoAdapter infoAdapter = new GoogleMapWeatherInfoAdapter(markerInfo.get(mk), WeatherMapsActivity.this, markerBitmap.get(mk));
                        mMap.setInfoWindowAdapter(infoAdapter);
                        marker.showInfoWindow();
                    }
                }
                return true;
            }
        });
    }

    private void moveAndShowWeatherNewPlace(final LatLng latLng) {
        if (latLng != null) {
            float zoom = mMap.getCameraPosition().zoom;
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latLng.latitude + 0.008, latLng.longitude))      // Sets the center of the map to location user
                    .zoom(zoom)                                                         // Sets the zoom
                    .build();                                                           // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            loadCurrentWeatherByLocation(latLng.latitude, latLng.longitude);
        }
    }

    private void settingsRequest() {
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
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            SharedPreference.getInstance(this).putBoolean("isPermisionLocation",true);
            if (resultCode == RESULT_OK) {
                dialog.show();
                if (LocationService.mGoogleApiClient.isConnecting() || LocationService.mGoogleApiClient.isConnected()) {
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

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        currentLocationWeather();
                    }
                }, 4000);
            }
        } else if (requestCode == REQUEST_FIND_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber() + place.getLatLng().latitude);

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude,place.getLatLng().longitude), 13));
                Log.e("Check load location", "Done");
            }
        }
    }

    /**
     * Search Button onClick event
     */
    public void findPlace(View view) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
            startActivityForResult(intent, REQUEST_FIND_PLACE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
        if (!Utils.isNetworkConnected(this)) {
            layoutWarning.setVisibility(RelativeLayout.VISIBLE);
        } else {
            layoutWarning.setVisibility(RelativeLayout.GONE);
        }
    }

    private void currentLocationWeather() {
        double lat = SplashScreenActivity.latitude;
        double lon = SplashScreenActivity.longitude;

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat + 0.008, lon))   // Sets the center of the map to location user
                .zoom(15)                               // Sets the zoom
                .build();                               // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                loadCurrentWeatherByLocation(SplashScreenActivity.latitude, SplashScreenActivity.longitude);
//            }
//        }, 1000);  //Do something after 1000ms

        loadCurrentWeatherByLocation(SplashScreenActivity.latitude, SplashScreenActivity.longitude);
    }

    public void initService() {
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
    }

    private void loadCurrentWeatherByLocation(final double lat, final double lon) {
        int posLanguage = SharedPreference.getInstance(this).getInt("Language", 0);

        WeatherInfoAPI infoAPI = ApiClient.getClient().create(WeatherInfoAPI.class);
        Call<OpenWeatherJSon> callWeather;

        if (posLanguage == 1) {
            callWeather = infoAPI.loadCurrentWeatherByLocation(lat, lon, getString(R.string.appid_weather));
        } else {
            callWeather = infoAPI.loadCurrentWeatherByLocation(lat, lon, getString(R.string.appid_weather));
        }
        Log.i("OkHttp", "==> " + callWeather.request().url().toString());
        // Cuộc gọi bất đồng bọ (chạy dưới background)
        callWeather.enqueue(new Callback<OpenWeatherJSon>() {
            @Override
            public void onResponse(Call<OpenWeatherJSon> call, Response<OpenWeatherJSon> response) {
                try {
                    dialog.dismiss();
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response));
                    String weatherJSon = jsonObject.get("body").toString();
                    Log.d("Response","==> " + weatherJSon);
                    final OpenWeatherJSon openWeatherJSon = new Gson().fromJson(weatherJSon, new TypeToken<OpenWeatherJSon>(){}.getType());
                    Log.d("openWeatherJSon","==> " + new Gson().toJson(openWeatherJSon));

                    String urlIcon = getString(R.string.base_icon_url) + openWeatherJSon.getWeather().get(0).getIcon() + ".png";
                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
                    imageLoader.loadImage(urlIcon, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            GoogleMapWeatherInfoAdapter infoAdapter = new GoogleMapWeatherInfoAdapter(openWeatherJSon, WeatherMapsActivity.this, loadedImage);
                            mMap.setInfoWindowAdapter(infoAdapter);
                            MarkerOptions option = new MarkerOptions();
                            option.position(new LatLng(lat, lon));
                            option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            Marker marker = mMap.addMarker(option);
                            listMarker.add(marker);
                            markerInfo.put(marker, openWeatherJSon);
                            markerBitmap.put(marker, loadedImage);
                            Log.e("NumberOfMarkerAfterDel", ": " + listMarker.size() + "-" + markerInfo.size() + "-" + markerBitmap.size());
                            marker.showInfoWindow();
                        }
                    }) ;

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

}
