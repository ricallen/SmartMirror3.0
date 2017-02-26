package smarter.smartmirror30;

import android.os.Handler;
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
public class APIPuller {

    private Values values;
    private WeatherListener weatherListener;
    private TumblrPostListener tumblrPostListener;
    private Handler handler;

    public APIPuller(Handler handler) {
        values = Values.getInstance();
        this.handler = handler;
    }

    public void setWeatherListener(WeatherListener weatherListener){
        this.weatherListener = weatherListener;
    }

    public void removeOnWeatherPulled(){
        weatherListener = null;
    }

    public void setTumblrPostListener(TumblrPostListener tumblrPostListener){
        this.tumblrPostListener = tumblrPostListener;
    }

    public void removeTumblrPostListener(){
        tumblrPostListener = null;
    }

    public void getWeather (final double longitude, final double latitude){
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
                        values.setWeatherJSON(json);
                        Log.i("-->", "in while loop: " + inputLine + "\n");
                    }
                    br.close();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            weatherListener.onWeatherPulled();
                        }
                    });
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

    public void getTumblrPosts (){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                try {
                    String a="https://api.tumblr.com/v2/blog/w2zs5ecxl7n6t1bj.tumblr.com" +
                            "/posts/text?api_key=qQiMc9MoxvYkzS1Kht2CS0LQsm9ocu9ArjXsG2CJoZClMHfc4A";
                    url = new URL(a);
                    URLConnection conn = url.openConnection();

                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));

                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        JSONObject json = new JSONObject(inputLine);
                        values.setTumblrPostJSON(json);
                        Log.i("-->", "in while loop: " + inputLine + "\n");
                    }
                    br.close();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tumblrPostListener.onTumblrPostPulled();
                        }
                    });
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

    public interface WeatherListener {
        void onWeatherPulled();
    }

    public interface TumblrPostListener {
        void onTumblrPostPulled();
    }
}
