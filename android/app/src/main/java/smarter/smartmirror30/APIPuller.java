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

    public void connectToURL(final String a, final int type){
        final URLConnectionListener urlConnectionListener = new URLConnectionListener() {
            @Override
            public void onURLConnectionCompleted(int type, JSONObject jsonObject) {
                switch (type){ //0 - weather, 1 - tumblr, 2 - news1, 3 - news2, 4 - news3
                    case 0: values.setMetOfficeJSON(jsonObject);
                        weatherListener.onWeatherPulled();
                        break;
                    case 1: values.setTumblrPostJSON(jsonObject);
                        tumblrPostListener.onTumblrPostPulled();
                        break;
                    case 2: values.setNewsJSON1(jsonObject);
                        newsPostListener.onNewsPulled(1);
                        break;
                    case 3: values.setNewsJSON2(jsonObject);
                        newsPostListener.onNewsPulled(2);
                        break;
                    case 4: values.setNewsJSON3(jsonObject);
                        newsPostListener.onNewsPulled(3);
                        break;
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                try {
                    url = new URL(a);
                    URLConnection conn = url.openConnection();

                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));

                    String inputLine;
                    JSONObject jsonObject = new JSONObject();
                    while ((inputLine = br.readLine()) != null) {
                        jsonObject = new JSONObject(inputLine);
                        Log.i("-->", "in while loop: " + inputLine + "\n");
                    }
                    br.close();
                    final JSONObject finalJsonObject = jsonObject;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            urlConnectionListener.onURLConnectionCompleted(type, finalJsonObject);
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

    public void getWeather (){
        String a= Constants.metOfficeURL1 + Constants.metOfficeLocationID + Constants.metOfficeURL2
                + Constants.metOfficeAPIkey;
        connectToURL(a, 0);
    }

    public void getTumblrPosts (){
        String a= Constants.tumblrURL1 + Constants.tumblrName +
                Constants.tumblrURL2 + Constants.tumblrAPIkey;
        connectToURL(a, 1);
    }

    public void getHeadlines (final String source1, final String source2, final String source3){
        String a= Constants.newsURL1 + source1
                + Constants.newsURL2 + Constants.newsAPIkey;
        connectToURL(a, 2);

        a= Constants.newsURL1 + source2
                + Constants.newsURL2 + Constants.newsAPIkey;
        connectToURL(a, 3);

        a= Constants.newsURL1 + source3
                + Constants.newsURL2 + Constants.newsAPIkey;
        connectToURL(a, 4);
    }

    public interface WeatherListener {
        void onWeatherPulled();
    }

    public interface TumblrPostListener {
        void onTumblrPostPulled();
    }

    public interface NewsPostListener {
        void onNewsPulled(int i);
    }

    public interface URLConnectionListener{
        void onURLConnectionCompleted(int type, JSONObject jsonObject);
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

}
