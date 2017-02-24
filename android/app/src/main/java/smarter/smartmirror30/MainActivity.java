package smarter.smartmirror30;

import android.Manifest;
import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends Activity {

    private Values values;
    private WeatherGetter weatherGetter;

    private TextView weather;
    private TextView temp;
    private TextView joke;
    private TextView punchline;

    private int delay = 5000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        values = Values.getInstance();
        weatherGetter = new WeatherGetter();
        weather = (TextView)findViewById(R.id.weatherTxt);
        temp = (TextView) findViewById(R.id.tempTxt);
        joke = (TextView) findViewById(R.id.jokeTxt);
        punchline = (TextView) findViewById(R.id.punchlineTxt);
    }

    @Override
    protected void onStart() {
        super.onStart();
        weather.setText(values.getForecast());
        temp.setText(formatTemp(values.getTemp()));
        joke.setText(values.getPunchline());
        punchline.setText(values.getJoke());
    }

    @Override
    protected void onResume() {
        super.onResume();
        weatherGetter.getWeather(this);
        final Handler h = new Handler();
        h.postDelayed(new Runnable(){
            public void run(){
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        updateValues();
                    }
                });
                h.postDelayed(this, delay);
            }
        }, delay);
    }

    private void updateValues(){
        weather.setText(values.getForecast());
        temp.setText(formatTemp(values.getTemp()));
        joke.setText(values.getJoke());
        punchline.setText(values.getPunchline());
    }

    private String formatTemp (String temp){
        return temp + "°C";
    }
}
