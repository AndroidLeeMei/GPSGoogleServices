package com.example.auser.gpsgoogleservices;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    TextView tv2, tv,tv3,tv4,tv5,tv6,tv7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        tv = (TextView) findViewById(R.id.textView);
        tv4 = (TextView) findViewById(R.id.textView4);
        tv5 = (TextView) findViewById(R.id.textView5);
        tv6 = (TextView) findViewById(R.id.textView6);
        tv7 = (TextView) findViewById(R.id.textView7);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }
    //========GoogleApiClient.ConnectionCallbacks import start=============================================================

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
//        Log.d("LOC1", mLastLocation.getLongitude() + "," + mLastLocation.getLatitude());

        if (mLastLocation != null)
        {
// 取到手機地點後的程式碼
            Log.d("LOC1", mLastLocation.getLongitude() + "," + mLastLocation.getLatitude());
            tv2.setText("定位完成");
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    //========GoogleApiClient.ConnectionCallbacks import end=============================================================


    //=======GoogleApiClient.OnConnectionFailedListener  import start================
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        tv2.setText("定位失敗");
    }
    //=======GoogleApiClient.OnConnectionFailedListener  import end================
    public void Geocoder(View target){
        Geocoder geocoder=new Geocoder(MainActivity.this);
        try {
            List<Address> list=geocoder.getFromLocation(24.9318495, 121.1717214, 3);//一次取下3筆資料
            Address addr =list.get(0);
            String str=addr.getAddressLine(0);
            tv5.setText(str);

            addr =list.get(1);
            str=addr.getAddressLine(1);
            tv6.setText(str);

            addr =list.get(2);
            str=addr.getAddressLine(2);
            tv7.setText(str);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void latlng(View target){
        RequestQueue queue=Volley.newRequestQueue(MainActivity.this);
//        StringRequest request=new StringRequest()
        StringRequest request=new StringRequest(
                "https://maps.googleapis.com/maps/api/geocode/json?address=24.954264,%20121.226011&key=AIzaSyBVWPD9PS5_DorD_wje7yYXXcI0CLbK2q0"
//                "https…://maps.googleapis.com/maps/api/elevation/json?locations=24.956562,%20121.226110&key=AIzaSyBVWPD9PS5_DorD_wje7yYXXcI0CLbK2q0"
//        StringRequest request = new StringRequest("https://maps.googleapis.com/maps/api/elevation/json?locations=24.9303237,121.1715947&key=AIzaSyC3qUJpugVjMS2jMwnQfZ6mvKEmJwgAB6o"

                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj=new JSONObject(response);
                    JSONArray array=obj.getJSONArray("results");
                    JSONObject obj1=array.getJSONObject(0);
                    String  result=obj1.getString("formatted_address");
                    tv4.setText(String.valueOf(result));
//                    tv.setText("sss");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
        queue.start();

    }

    //=============gson=============================================
    public void gson(View target){
        RequestQueue queue=Volley.newRequestQueue(MainActivity.this);
        StringRequest request=new StringRequest(
                "https://maps.googleapis.com/maps/api/elevation/json?locations=24.956562,%20121.226110&key=AIzaSyBVWPD9PS5_DorD_wje7yYXXcI0CLbK2q0"
//        StringRequest request = new StringRequest("https://maps.googleapis.com/maps/api/elevation/json?locations=24.9303237,121.1715947&key=AIzaSyC3qUJpugVjMS2jMwnQfZ6mvKEmJwgAB6o"

                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                    Gson gson=new Gson();
                    Elevation ele=gson.fromJson(response,Elevation.class);
                    tv3.setText(String.valueOf(ele.results[0].elevation));
//                    tv.setText("sss");


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
        queue.start();
    }
    //=============JSONObject================================
    public void click1(View target){
        RequestQueue queue=Volley.newRequestQueue(MainActivity.this);
        StringRequest request=new StringRequest(
                "https://maps.googleapis.com/maps/api/elevation/json?locations=24.956562,%20121.226110&key=AIzaSyBVWPD9PS5_DorD_wje7yYXXcI0CLbK2q0"
//        StringRequest request = new StringRequest("https://maps.googleapis.com/maps/api/elevation/json?locations=24.9303237,121.1715947&key=AIzaSyC3qUJpugVjMS2jMwnQfZ6mvKEmJwgAB6o"

                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj=new JSONObject(response);
                    JSONArray array=obj.getJSONArray("results");
                    JSONObject obj1=array.getJSONObject(0);
                    double result=obj1.getDouble("elevation");
                    tv.setText(String.valueOf(result));
//                    tv.setText("sss");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
        queue.start();

    }
}
