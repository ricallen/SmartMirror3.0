package smarter.smartmirror30;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by Ric on 02/03/2017.
 */
public class TumblrNoteCreator {

    private TextView header;
    private RelativeLayout relativeLayout;
    private Context context;
    private PhotoPullListener photoPullListener;
    private Handler handler;

    public TumblrNoteCreator(TextView header, RelativeLayout relativeLayout, Context context, Handler handler){
        this.header = header;
        this.relativeLayout = relativeLayout;
        this.context = context;
        this.handler = handler;
    }

    public void createTumblrNoteLayout(JSONObject tumblrJSON){
        relativeLayout.removeAllViews();
        try {
            JSONArray postArray = tumblrJSON.getJSONObject("response").getJSONArray("posts");
            for (int i = 0; i<postArray.length(); i++){
                if (postArray.getJSONObject(i).getString("type").equals("text")){
                    String bodyString = postArray.getJSONObject(i).getString("body");
                    SmartMirrorTextView bodyTxt = new SmartMirrorTextView(context);
                    bodyTxt.setText(Html.fromHtml(bodyString));
                    bodyTxt.setTextSize(context.getResources().getDimension(R.dimen.textview_body_size));
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    params.addRule(RelativeLayout.ALIGN_LEFT, RelativeLayout.TRUE);
                    relativeLayout.addView(bodyTxt, params);
                    String headerString = Html.fromHtml(postArray
                            .getJSONObject(0).getString("title")).toString();
                    header.setGravity(Gravity.LEFT);
                    header.setText(headerString);
                    break;
                }
                else if (postArray.getJSONObject(i).getString("type").equals("photo")){
                    photoPost(postArray, i);
                    String headerString = postArray.getJSONObject(i)
                            .getJSONObject("reblog").getString("comment").replace("<p>", "").replace("</p>", "");
                    header.setGravity(Gravity.CENTER_HORIZONTAL);
                    header.setText(headerString);
                    break;
                }
                else {
                }
                String bodyString = "No Posts Available";
                TextView bodyTxt = new TextView(context);
                bodyTxt.setText(bodyString);
                relativeLayout.addView(bodyTxt);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface PhotoPullListener{
        void onPhotoPulled(Bitmap bitmap);
    }

    public void getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            photoPullListener.onPhotoPulled(myBitmap);
        } catch (IOException e) {
            e.printStackTrace();
            photoPullListener.onPhotoPulled(null);
        }
    }

    public void photoPost(JSONArray postArray, int i){
        ImageView imageView = new ImageView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        relativeLayout.addView(imageView, params);
        String url = null;
        try {
            url = postArray.getJSONObject(i).getJSONArray("photos").getJSONObject(0)
                    .getJSONArray("alt_sizes").getJSONObject(0).getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Glide
                .with( context )
                .load( url )
                .placeholder( getPlaceHolderImage() )
                .error( R.drawable.error )
                .fitCenter()
                .into( imageView );
    }

    private int getPlaceHolderImage(){
        Random rand = new Random();
        int randomNum = rand.nextInt((1) + 1);
        switch (randomNum){
            case 0: return R.drawable.icecream;
            case 1: return R.drawable.cooldudes;
            case 2: return R.drawable.summer;
            default: return R.drawable.summer;
        }
    }

}
