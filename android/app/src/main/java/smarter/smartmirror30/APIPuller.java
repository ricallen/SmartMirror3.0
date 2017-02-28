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
    private NewsPostListener newsPostListener;
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

    public void setNewsPostListener(NewsPostListener newsPostListener){
        this.newsPostListener = newsPostListener;
    }

    public void removeNewsPostListener(){
        newsPostListener = null;
    }

    public void getWeather (final double longitude, final double latitude){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                try {
                    String a= Constants.metOfficeURL1 + Constants.metOfficeLocationID + Constants.metOfficeURL2
                            + Constants.metOfficeAPIkey;
                    url = new URL(a);
                    URLConnection conn = url.openConnection();

                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));

                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        JSONObject json = new JSONObject(inputLine);
                        values.setMetOfficeJSON(json);
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
                    String a="https://api.tumblr.com/v2/blog/" + Constants.tumblrName +
                            "/posts/text?api_key=" + Constants.tumblrAPIkey;
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

    public void getHeadlines (){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                try {
                    String a= Constants.newsURL + Constants.newsAPIkey;
                    url = new URL(a);
                    URLConnection conn = url.openConnection();

                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));

                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        JSONObject json = new JSONObject(inputLine);
                        values.setNewsJSON(json);
                        Log.i("-->", "in while loop: " + inputLine + "\n");
                    }
                    br.close();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            newsPostListener.onNewsPulled();
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

    public interface NewsPostListener {
        void onNewsPulled();
    }
}
