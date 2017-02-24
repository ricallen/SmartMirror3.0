package smarter.smartmirror30;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ric on 23/02/2017.
 */
public class Values {
    public static Values instance;
    public JSONObject weather;
    public String forecast;

    private Values(){

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
        try {
            String forecast = weather.getJSONArray("weather").getJSONObject(0).getString("main");
            Log.i("-->", "weather " + forecast);
            return forecast;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
