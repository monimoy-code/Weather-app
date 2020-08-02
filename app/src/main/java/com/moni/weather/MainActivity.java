package com.moni.weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    SearchView searchview;
    TextView text;
    String msg, temperature, speed, humidity, pressure, temp_min, temp_max, city;
    ProgressBar spinner;
    FrameLayout frameLayout;
    ImageButton btn;
    float lat, lon;
    int PERMISSION_ID = 50;
    FusedLocationProviderClient mfusedLocationProviderClient;

    public class DownloadTsk extends AsyncTask<String, Void, String> {

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            frameLayout.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {
                    char curr = (char) data;
                    result = result + curr;
                    data = reader.read();


                }
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            frameLayout.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject obj = new JSONObject(result);
                String code = obj.getString("cod");
                if (code.equals("200")) {
                    String info = obj.getString("weather");
                    String info2 = obj.getString("main");
                    String info3 = obj.getString("wind");

                    JSONArray array = new JSONArray(info);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj2 = array.getJSONObject(i);
                        msg = obj2.getString("description");

                    }
                    JSONObject obj3 = new JSONObject(info2);
                    temperature = obj3.getString("temp");
                    pressure = obj3.getString("pressure");
                    humidity = obj3.getString("humidity");
                    JSONObject obj4 = new JSONObject(info3);
                    speed = obj4.getString("speed");
                    temp_min = obj3.getString("temp_min");
                    temp_max = obj3.getString("temp_max");
                    city = obj.getString("name");
                    Log.d("temperature", temperature);
                    Log.d("pressure", pressure);
                    Log.d("humidity", humidity);
                    Log.d("speed", speed);
                    Log.d("temp_min", temp_min);
                    Log.d("temp_max", temp_max);
                    Log.d("city", city);
                    Log.d("desc", msg);

                    start_second();
                    frameLayout.setVisibility(View.INVISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                }

            } catch (JSONException e) {
                frameLayout.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Oops! City not found", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        public void cancel() {
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchview = findViewById(R.id.Search);
        text = findViewById(R.id.textView);
        spinner = findViewById(R.id.progressBar);
        frameLayout = findViewById(R.id.frame);
        btn = findViewById(R.id.LocationBtn);
        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                DownloadTsk task = new DownloadTsk();
                task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + query + "&units=metric&APPID=f38f0193cf517e931e86281143ca928f");
                task.cancel();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });
    }

    public void start_second() {
        Intent i = new Intent(this, Show_weather.class);
        i.putExtra("type", msg);
        i.putExtra("temp", temperature);
        i.putExtra("pressure", pressure);
        i.putExtra("humidity", humidity);
        i.putExtra("speed", speed);
        i.putExtra("temp_min", temp_min);
        i.putExtra("temp_max", temp_max);
        i.putExtra("city", city);
        startActivity(i);
    }
    private void getLastLocation(){
        if(checkPermission()){
            if(isLocationEnabled()){
                mfusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if(location==null){
                            requestNewLocation();
                        }else {
                            lat = (float)location.getLatitude();
                            lon = (float)location.getLongitude();
                            DownloadTsk downloadTsk = new DownloadTsk();
                            downloadTsk.execute("http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&units=metric&APPID=f38f0193cf517e931e86281143ca928f");
                            downloadTsk.cancel();
                        }
                    }
                });
            }else {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }else {
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(0);
        locationRequest.setNumUpdates(1);
        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mfusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }
    private LocationCallback locationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location lastLocation = locationResult.getLastLocation();
            lat = (float) lastLocation.getLatitude();
            lon = (float) lastLocation.getLongitude();
        }
    };
    private boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_ID){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLastLocation();
        }
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );

    }
}
