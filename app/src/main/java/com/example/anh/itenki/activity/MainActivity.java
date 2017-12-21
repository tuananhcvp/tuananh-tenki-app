package com.example.anh.itenki.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.anh.itenki.R;
import com.example.anh.itenki.fragment.AboutAppFragment;
import com.example.anh.itenki.fragment.CurrentLocationFragment;
import com.example.anh.itenki.fragment.NoteFragment;
import com.example.anh.itenki.fragment.SelectLocationFragment;
import com.example.anh.itenki.fragment.SettingFragment;
import com.example.anh.itenki.utils.LocationService;
import com.example.anh.itenki.utils.SharedPreference;
import com.example.anh.itenki.utils.Utils;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private boolean hasGPS;
    public BroadcastReceiver receiver = null;
    public static String[] country;
    public static String[] cityArr;
    public static List<String> japanCityList;

    private NavigationView navigationView;
    private ProgressDialog dialog;

    public static final int NOTIFY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dialog = new ProgressDialog(this);

        getAllCityData();
        getJapanCityData();
        initGPS();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        if (SplashScreenActivity.latitude != 0 && SplashScreenActivity.longitude != 0) {
            callFragment(CurrentLocationFragment.newInstance());
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_currentLocation) {
            Log.d("Location","==> Lat:" + SplashScreenActivity.latitude + " -- Lon:" + SplashScreenActivity.longitude);
            navigationView.getMenu().getItem(0).setChecked(true);
            navigationView.getMenu().getItem(5).getSubMenu().getItem(0).setChecked(false);

            callFragment(CurrentLocationFragment.newInstance());

        } else if (id == R.id.nav_selectLocation) {
            navigationView.getMenu().getItem(1).setChecked(true);
            navigationView.getMenu().getItem(5).getSubMenu().getItem(0).setChecked(false);

            callFragment(SelectLocationFragment.newInstance());

        } else if (id == R.id.nav_googleMap) {
            Intent mapIntent = new Intent(MainActivity.this, WeatherMapsActivity.class);
            startActivity(mapIntent);

        } else if (id == R.id.nav_note) {
            navigationView.getMenu().getItem(3).setChecked(true);
            navigationView.getMenu().getItem(5).getSubMenu().getItem(0).setChecked(false);

            callFragment(NoteFragment.newInstance());

        } else if (id == R.id.nav_setting) {
            navigationView.getMenu().getItem(4).setChecked(true);
            navigationView.getMenu().getItem(5).getSubMenu().getItem(0).setChecked(false);

            callFragment(SettingFragment.newInstance());

        } else if (id == R.id.nav_about) {
            navigationView.getMenu().getItem(5).getSubMenu().getItem(0).setChecked(true);
            navigationView.getMenu().getItem(0).setChecked(false);
            navigationView.getMenu().getItem(1).setChecked(false);
            navigationView.getMenu().getItem(2).setChecked(false);
            navigationView.getMenu().getItem(3).setChecked(false);
            navigationView.getMenu().getItem(4).setChecked(false);

            callFragment(AboutAppFragment.newInstance());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    public void initService() {
        Intent intent = new Intent(MainActivity.this, LocationService.class);
        startService(intent);
    }

    /**
     * Stop location service
     */
    public void stopService() {
        Intent intent = new Intent(MainActivity.this, LocationService.class);
        stopService(intent);

    }

    private void settingsRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(1000);
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
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);

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

    /**
     *
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void initView() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        hasGPS = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!hasGPS && SharedPreference.getInstance(this).getBoolean("isPermision",false)) {
            if (LocationService.mGoogleApiClient != null) {
                if (!LocationService.mGoogleApiClient.isConnecting() && !LocationService.mGoogleApiClient.isConnected()) {
                    LocationService.mGoogleApiClient.connect();
                }
            }
            settingsRequest();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initService();
                    SharedPreference.getInstance(this).putBoolean("isPermision",true);
                } else {
                    SharedPreference.getInstance(this).putBoolean("isPermision",false);
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            SharedPreference.getInstance(this).putBoolean("isPermisionLocation",true);
            if (resultCode == RESULT_OK) {
                Utils.initProgressDialog(MainActivity.this, dialog);
                dialog.show();

                if (LocationService.mGoogleApiClient.isConnecting() || LocationService.mGoogleApiClient.isConnected()) {
                    Log.e("mGoogleApiClient","==> Disconnect");
                    LocationService.mGoogleApiClient.disconnect();
                }
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
                        dialog.dismiss();
                        callFragment(CurrentLocationFragment.newInstance());
                    }
                }, 4500);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResumeHomeActivity","==> 1");
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(LocationService.BROADCAST_ACTION);
//        registerReceiver(receiver, filter);

        initView();
    }

    private void callFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fmContent, fragment);
        transaction.commit();
    }

    /**
     *
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void initGPS() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                initService();
            }
            // Do something for lollipop and above versions
        } else {
            SharedPreference.getInstance(this).putBoolean("isPermision",true);
            // do something for phones running an SDK before lollipop
            initService();
        }

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                final LatLng temp = new LatLng(extras.getDouble("Latitude"),extras.getDouble("Longitude"));
                Log.d("inReceiver","Lat:" + temp.latitude + "--Lon:" + temp.longitude);
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        stopService();
        SharedPreference.getInstance(this).putDouble("latitude", SplashScreenActivity.latitude);
        SharedPreference.getInstance(this).putDouble("longitude", SplashScreenActivity.longitude);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            if (receiver != null) {
                this.unregisterReceiver(receiver);
                receiver = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setActionBarName(String title) {
        Utils.setActionbarTitle(title, this, getSupportActionBar());
    }

    //Get cities in Japan from json file
    private void getJapanCityData() {
        japanCityList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset("japancity.json"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                String str = (String) object.get("slug");
                String city = str.substring(0, 1).toUpperCase() + str.substring(1);
                japanCityList.add(city);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.sort(japanCityList, String.CASE_INSENSITIVE_ORDER);
        Log.e("JAPAN CITY", "==> " + new Gson().toJson(japanCityList));
    }

    //Get cities in the world from json file
    private void getAllCityData() {
        ArrayList<String> city = new ArrayList<>();
        try {
            JSONObject jsonRoot = new JSONObject(loadJSONFromAsset("cities.json"));
            JSONArray listCountryArr = new JSONArray();
            listCountryArr = jsonRoot.names();
            country = new String[listCountryArr.length()];
            for (int i = 0; i < listCountryArr.length(); i++) {
                country[i] = listCountryArr.getString(i);
            }

            for (String c : country) {
                JSONArray jsonArray = jsonRoot.getJSONArray(c);
                for (int m = 0; m < jsonArray.length(); m++) {
                    city.add(jsonArray.getString(m));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cityArr = new String[city.size()];
        for (int j = 0; j < city.size(); j++) {
            cityArr[j] = city.get(j);
        }
    }

    private String loadJSONFromAsset(String file) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * Refresh navigation view when language change
     */
    public void refreshNavigationView() {
        this.setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

}
