package smarter.smartmirror30;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Ric on 23/02/2017.
 */
public class WeatherGetter {
    public WeatherGetter() {
    }

    public void getWeather(Context context) {
        callAPI();
    }

    private JSONObject callAPI (){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;

                try {
                    String a="http://api.openweathermap.org/data/2.5/weather?lat=51.5299&lon=-0.1860" +
                            "&units=metric&appid=" + Constants.weatherAPIkey;
                    url = new URL(a);
                    URLConnection conn = url.openConnection();

                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));

                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        JSONObject json = new JSONObject(inputLine);
                        Values values = Values.getInstance();
                        values.setWeather(json);
                        Log.i("-->", inputLine + "\n");
                    }
                    br.close();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return new JSONObject();
    }
}
