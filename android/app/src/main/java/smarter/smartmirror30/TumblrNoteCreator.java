package smarter.smartmirror30;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
                    TextView bodyTxt = new TextView(context);
                    bodyTxt.setText(Html.fromHtml(bodyString));
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    params.addRule(RelativeLayout.ALIGN_LEFT, RelativeLayout.TRUE);
                    relativeLayout.addView(bodyTxt, params);
                    String headerString = Html.fromHtml(postArray
                            .getJSONObject(0).getString("title")).toString();
                    header.setText(headerString);
                    break;
                }
                else if (postArray.getJSONObject(i).getString("type").equals("photo")){
                    photoPost(postArray, i);
                    String headerString = postArray.getJSONObject(i)
                            .getString("caption").replace("<p>", "").replace("</p>", "");
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
        photoPullListener = new PhotoPullListener() {
            @Override
            public void onPhotoPulled(final Bitmap bitmap) {
                if (bitmap!=null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ImageView imageView = new ImageView(context);
                            imageView.setImageBitmap(bitmap);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT);
                            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                            relativeLayout.addView(imageView, params);
                        }
                    });
                }
            }
        };
        String url = null;
        try {
            url = postArray.getJSONObject(i).getJSONArray("photos").getJSONObject(0)
                    .getJSONArray("alt_sizes").getJSONObject(1).getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String finalUrl = url;
        Log.i("-->", "url src: " + url);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getBitmapFromURL(finalUrl);
            }
        }).start();
    }

}