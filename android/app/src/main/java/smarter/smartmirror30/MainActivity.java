package smarter.smartmirror30;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 2;
    String permission = Manifest.permission.ACCESS_FINE_LOCATION;
    Values values;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView weather = (TextView)findViewById(R.id.weatherTxt);
        WeatherGetter weatherGetter = new WeatherGetter();
        weatherGetter.getWeather(this);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("-->", "out of sleep");
        values = Values.getInstance();
        String forecast = values.getForecast();
        weather.setText(forecast==null?"Null":forecast);
        Log.i("-->", forecast==null?"Null":forecast);
    }
}
