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

    private JSONObject newsJSON;
    private String[] headlines;

    private JSONObject tumblrPostJSON;
    private String joke;
    private String punchline;

    private Values(){
        weatherCode = 0;
        temp = 0;
        joke = "What tea is often hard to swallow?";
        punchline = "Reality";
        headlines = new String[4];
    }

    public static Values getInstance (){
        if (instance==null){
            instance = new Values();
        }
        return instance;
    }

    public JSONObject getMetOfficeJSON() {
        return metOfficeJSON;
    }

    public void setMetOfficeJSON(JSONObject metOfficeJSON) {
        this.metOfficeJSON = metOfficeJSON;
    }

    public JSONObject getTumblrPostJSON() {
        return tumblrPostJSON;
    }

    public void setTumblrPostJSON(JSONObject tumblrPostJSON) {
        this.tumblrPostJSON = tumblrPostJSON;
    }

    public JSONObject getNewsJSON () {
        return newsJSON;
    }

    public void setNewsJSON (JSONObject newsJSON){
        this.newsJSON = newsJSON;
        try {
            JSONArray array = newsJSON.getJSONArray("articles");
            for (int i = 0; i<4; i++){
                headlines[i] = array.getJSONObject(i).getString("title");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String[] getHeadlines (){
        return headlines;
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

    public String getPunchline() {
        if (tumblrPostJSON!=null){
            try{
                punchline = Html.fromHtml(tumblrPostJSON.getJSONObject("response").getJSONArray("posts")
                        .getJSONObject(0).getString("body")).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return punchline;
    }

    public void setPunchline(String punchline) {
        this.punchline = punchline;
    }

    public String getJoke() {
        if (tumblrPostJSON!=null){
            try{
                joke = Html.fromHtml(tumblrPostJSON.getJSONObject("response").getJSONArray("posts")
                        .getJSONObject(0).getString("title")).toString();
            } catch (JSONException e) {
            e.printStackTrace();
            }
        }
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

}
