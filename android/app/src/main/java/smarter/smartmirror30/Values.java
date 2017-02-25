package smarter.smartmirror30;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ric on 23/02/2017.
 */
public class Values {
    private static Values instance;
    private JSONObject weatherJSON;
    private String forecast;
    private String temp;
    private String[] wind;
    private String joke;
    private String punchline;

    private Values(){
        forecast = "Retrieving...";
        temp = "0";
        wind = new String[2];
        wind[0] = "0.0";
        wind[1] = "000";
        joke = "What tea is often hard to swallow?";
        punchline = "Reality";
    }

    public static Values getInstance (){
        if (instance==null){
            instance = new Values();
        }
        return instance;
    }

    public JSONObject getWeatherJSON() {
        return weatherJSON;
    }

    public void setWeatherJSON(JSONObject weatherJSON) {
        this.weatherJSON = weatherJSON;
    }

    public String getForecast(){
        if (weatherJSON !=null) {
            try {
                forecast = weatherJSON.getJSONArray("weatherJSON").getJSONObject(0).getString("main").toUpperCase();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return forecast;
    }

    public String getTemp(){
        if (weatherJSON !=null) {
            try {
                temp = weatherJSON.getJSONObject("main").getString("temp");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    public String[] getWind(){
        if (weatherJSON !=null) {
            try {
                wind[0] = weatherJSON.getJSONObject("wind").getString("speed");
                wind[1] = weatherJSON.getJSONObject("wind").getString("deg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return wind;
    }

    public String getPunchline() { return punchline; }

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
