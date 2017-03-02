package smarter.smartmirror30;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private Values values;
    private APIPuller aPIPuller;
    private LocationManager lm;
    private Location location;
    private Handler handler;

    private ViewPager viewPager;
    private NewsFragment bbcNewsFragment;
    private NewsFragment sportsNewsFragment;
    private NewsFragment businessNewsFragment;

    private double longitude;
    private double latitude;

    private TextView date;
    private TextView time;
    private ImageView weather;
    private TextView temp;
    private TextView rainPercent;
    private TextView header;
    private RelativeLayout bodyLayout;

    private int delay = 30000;
    private int timeAndDateDelay = 300;
    private int viewPagerDelay = 30000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        values = Values.getInstance();
        handler = new Handler();
        aPIPuller = new APIPuller(handler);
//        locationCheck();
        setUpViews();
        setUpViewPager();
        aPIPuller.setWeatherListener(new APIPuller.WeatherListener() {
            @Override
            public void onWeatherPulled() {
                updateWeather();
            }
        });
        aPIPuller.setTumblrPostListener(new APIPuller.TumblrPostListener() {
            @Override
            public void onTumblrPostPulled() {
                updateNote();
            }
        });
        aPIPuller.setNewsPostListener(new APIPuller.NewsPostListener() {
            @Override
            public void onNewsPulled(int i) {
                switch (i){
                    case 1:
                        bbcNewsFragment.updateNews(values.getHeadlines1());
                        break;
                    case 2:
                        sportsNewsFragment.updateNews(values.getHeadlines2());
                        break;
                    case 3:
                        businessNewsFragment.updateNews(values.getHeadlines3());
                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("-->", "long " + longitude + ", lat " + latitude);
        handler.post(new Runnable() {
            @Override
            public void run() {
                aPIPuller.getWeather();
                aPIPuller.getTumblrPosts();
                aPIPuller.getHeadlines(Constants.newsSourceArray[0], Constants.newsSourceArray[1]
                        , Constants.newsSourceArray[5]);
                handler.postDelayed(this, delay);
            }
        });
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateTimeAndDate();
                handler.postDelayed(this, timeAndDateDelay);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (viewPager.getCurrentItem()<2) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
                else {
                    viewPager.setCurrentItem(0);
                }
                handler.postDelayed(this, viewPagerDelay);
            }
        }, viewPagerDelay);
    }

    @Override
    protected void onDestroy() {
        aPIPuller.removeNewsPostListener();
        aPIPuller.removeOnWeatherPulled();
        aPIPuller.removeTumblrPostListener();
        super.onDestroy();
    }


    private void updateWeather(){
        temp.setText(formatTemp(values.getTemp()));
        rainPercent.setText(formatRainPercent(values.getRainPercent()));
        WeatherImageGetter weatherImageGetter = new WeatherImageGetter();
        String drawableName = weatherImageGetter.getImage(values.getWeatherCode());
        Log.i("-->", drawableName);
        int drawable = getResources().getIdentifier(drawableName, "drawable", this.getPackageName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            weather.setImageDrawable(getResources().getDrawable(drawable, getApplicationContext().getTheme()));
        } else {
            weather.setImageDrawable(getResources().getDrawable(drawable));
        }
    }

    private void updateNote(){
        TumblrNoteCreator tumblrNoteCreator = new TumblrNoteCreator(header, bodyLayout, this, handler);
        tumblrNoteCreator.createTumblrNoteLayout(values.getTumblrPostJSON());
    }

    private void updateTimeAndDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", getResources().getConfiguration().locale);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", getResources().getConfiguration().locale);
        date.setText(dateFormat.format(c.getTime()));
        time.setText(timeFormat.format(c.getTime()));
    }

    private void locationCheck(){
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }
        }
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        location = getLastKnownLocation();
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }

    private void setUpViewPager(){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(3);
        bbcNewsFragment = NewsFragment.newInstance();
        sportsNewsFragment = NewsFragment.newInstance();
        businessNewsFragment = NewsFragment.newInstance();
    }

    private void setUpViews(){
        date = (TextView) findViewById(R.id.dateTxt);
        time = (TextView) findViewById(R.id.timeTxt);
        temp = (TextView) findViewById(R.id.tempTxt);
        rainPercent = (TextView) findViewById(R.id.rainPercentTxt);
        weather = (ImageView) findViewById(R.id.weatherImg);
        header = (TextView) findViewById(R.id.jokeTxt);
        bodyLayout = (RelativeLayout) findViewById(R.id.bodyRelLayout);
    }

    private String formatTemp (int temp){
        return temp + "°C";
    }

    private String formatRainPercent (int rainPercent) {return rainPercent + "%"; }

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

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);

        }
        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch(position){
                case 0: return bbcNewsFragment;
                case 1: return sportsNewsFragment;
                case 2: return businessNewsFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
