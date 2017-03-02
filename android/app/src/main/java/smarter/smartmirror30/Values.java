package smarter.smartmirror30;

import android.text.Html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ric on 23/02/2017.
 */
public class Values {

    private static Values instance;


    private JSONObject metOfficeJSON;
    private int weatherCode;
    private int temp;
    private int rainPercent;

    private String[] headlines1;
    private String[] headlines2;
    private String[] headlines3;

    private JSONObject tumblrPostJSON;
    private String joke;
    private String punchline;

    private Values(){
        weatherCode = 0;
        temp = 0;
        joke = "Test Headline";
        punchline = "Test Body";
        headlines1 = new String[4];
        headlines2 = new String[4];
        headlines3 = new String[4];
    }

    public static Values getInstance (){
        if (instance==null){
            instance = new Values();
        }
        return instance;
    }

    public void setMetOfficeJSON(JSONObject metOfficeJSON) {
        this.metOfficeJSON = metOfficeJSON;
    }

    public void setTumblrPostJSON(JSONObject tumblrPostJSON) {
        this.tumblrPostJSON = tumblrPostJSON;
    }

    public JSONObject getTumblrPostJSON(){
        return tumblrPostJSON;
    }

    public void setNewsJSON1 (JSONObject newsJSON){
        try {
            JSONArray array = newsJSON.getJSONArray("articles");
            for (int i = 0; i<4; i++){
                headlines1[i] = array.getJSONObject(i).getString("title");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setNewsJSON2 (JSONObject newsJSON){
        try {
            JSONArray array = newsJSON.getJSONArray("articles");
            for (int i = 0; i<4; i++){
                headlines2[i] = array.getJSONObject(i).getString("title");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setNewsJSON3 (JSONObject newsJSON){
        try {
            JSONArray array = newsJSON.getJSONArray("articles");
            for (int i = 0; i<4; i++){
                headlines3[i] = array.getJSONObject(i).getString("title");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String[] getHeadlines1 (){
        return headlines1;
    }
    public String[] getHeadlines2 (){
        return headlines2;
    }
    public String[] getHeadlines3 (){
        return headlines3;
    }

    public int getWeatherCode(){
        if (metOfficeJSON !=null) {
            try {
                weatherCode = metOfficeJSON.getJSONObject("SiteRep").getJSONObject("DV").getJSONObject("Location")
                        .getJSONArray("Period").getJSONObject(0).getJSONArray("Rep")
                        .getJSONObject(0).getInt("W");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return weatherCode;
    }

    public int getTemp(){
        if (metOfficeJSON !=null) {
            try {
                temp = metOfficeJSON.getJSONObject("SiteRep").getJSONObject("DV").getJSONObject("Location")
                        .getJSONArray("Period").getJSONObject(0).getJSONArray("Rep")
                        .getJSONObject(0).getInt("FDm");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    public int getRainPercent(){
        if (metOfficeJSON !=null) {
            try {
                rainPercent = metOfficeJSON.getJSONObject("SiteRep").getJSONObject("DV").getJSONObject("Location")
                        .getJSONArray("Period").getJSONObject(0).getJSONArray("Rep")
                        .getJSONObject(0).getInt("PPd");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rainPercent;
    }

}
