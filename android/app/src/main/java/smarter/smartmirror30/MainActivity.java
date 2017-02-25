package smarter.smartmirror30;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends Activity {

    private Values values;
    private WeatherGetter weatherGetter;
    private LocationManager lm;
    private Location location;

    private TextView weather;
    private TextView temp;
    private TextView wind;
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
        wind = (TextView) findViewById(R.id.windTxt);
        joke = (TextView) findViewById(R.id.jokeTxt);
        punchline = (TextView) findViewById(R.id.punchlineTxt);
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }
        }
        location = getLastKnownLocation();
        weather.setText(values.getForecast());
        temp.setText(formatTemp(values.getTemp()));
        wind.setText(formatWind(values.getWind()));
        joke.setText(values.getPunchline());
        punchline.setText(values.getJoke());
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        Log.i("-->", "long " + longitude + ", lat " + latitude);
        weatherGetter.getWeather(longitude, latitude);
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
        wind.setText(formatWind(values.getWind()));
        joke.setText(values.getJoke());
        punchline.setText(values.getPunchline());
    }

    private String formatTemp (String temp){
        return temp + "°C";
    }

    private String formatWind (String[] wind) {return wind[0] + "m/s: " + wind[1]; }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                } else {
                    //not granted
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private Location getLastKnownLocation() {
        List<String> providers = lm.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = lm.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

}
