package com.example.myprog.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.myprog.Currencies;
import com.example.myprog.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

public class GoogleMap extends AppCompatActivity {

    com.google.android.gms.maps.GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapView);
        mMap = mapFragment.getMap();
        Intent intent = getIntent();
        String location= intent.getStringExtra("region")+", "+ intent.getStringExtra("city") +", "+ intent.getStringExtra("address");
        String nameBank = intent.getStringExtra("nameBank");
        Toast.makeText(getApplicationContext(), location + ", " + nameBank, Toast.LENGTH_SHORT).show();

        String url = "https://maps.googleapis.com/maps/api/geocode/json?";

        try {
            location = URLEncoder.encode(location, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String address = "address=" + location;
        String sensor = "sensor=false";
        url = url + address + "&" + sensor;

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();
            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        }catch(Exception e){
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {
        String data = null;

        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result){
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{
        JSONObject jObject;

        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            Currencies.GeocoderJsonParser parser = new Currencies.GeocoderJsonParser();

            try{
                jObject = new JSONObject(jsonData[0]);
                places = parser.parse(jObject);
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String,String>> list){
            mMap.clear();
            CameraPosition cameraPosition;
            Intent intent = getIntent();
            String nameBank = intent.getStringExtra("nameBank");
            for(int i=0;i<list.size();i++){

                HashMap<String, String> hmPlace = list.get(i);

                Double lat = Double.parseDouble(hmPlace.get("lat"));
                Double lng = Double.parseDouble(hmPlace.get("lng"));
                String name = nameBank + ", " + hmPlace.get("formatted_address");
                String loc = hmPlace.get("lat")+", "+hmPlace.get("lng");
                Toast.makeText(getApplicationContext(), loc, Toast.LENGTH_SHORT).show();

                LatLng latLng = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(name)
                );

                cameraPosition = new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(14)
                        .bearing(45)
                        .tilt(20)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                mMap.animateCamera(cameraUpdate);
            }
        }
    }
}
