package smarter.smartmirror30;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ric on 23/02/2017.
 */
public class Values {
    private static Values instance;
    private JSONObject weather;
    private String forecast;
    private String temp;
    private String joke;
    private String punchline;

    private Values(){
        forecast = "Retrieving...";
        temp = "0";
        joke = "What tea is often hard to swallow?";
        punchline = "Reality";

    }

    public static Values getInstance (){
        if (instance==null){
            instance = new Values();
        }
        return instance;
    }

    public JSONObject getWeather() {
        return weather;
    }

    public void setWeather(JSONObject weather) {
        this.weather = weather;
    }

    public String getForecast(){
        if (weather!=null) {
            try {
                forecast = weather.getJSONArray("weather").getJSONObject(0).getString("main").toUpperCase();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return forecast;
    }

    public String getTemp(){
        if (weather!=null) {
            try {
                temp = weather.getJSONObject("main").getString("temp");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    public String getPunchline() {
        return punchline;
    }

    public void setPunchline(String punchline) {
        this.punchline = punchline;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }


}
