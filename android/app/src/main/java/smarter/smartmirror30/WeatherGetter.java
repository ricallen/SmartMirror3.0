package smarter.smartmirror30;

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

    private double longitude;
    private double latitude;
    public WeatherGetter() {
    }


    public void getWeather (final double longitude, final double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;

                try {
                    String a="http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon="
                            + longitude + "&units=metric&appid=" + Constants.weatherAPIkey;
                    url = new URL(a);
                    URLConnection conn = url.openConnection();

                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));

                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        JSONObject json = new JSONObject(inputLine);
                        Values values = Values.getInstance();
                        values.setWeatherJSON(json);
                        Log.i("-->", "in while loop: " + inputLine + "\n");
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
    }
}
