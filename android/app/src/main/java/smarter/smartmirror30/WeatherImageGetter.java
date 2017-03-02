package smarter.smartmirror30;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by Ric on 02/03/2017.
 */
public class WeatherImageGetter {

    public WeatherImageGetter (){
    }

    public String getImage(int code){
        switch (code){
            case 0: return "halfmoonshape";
            case 1: return "sunbright";
            case 2: return "halfmoonshape";
            case 3:
            case 4: return "cloudysky";
            case 5:
            case 6:
            case 7:
            case 8: return "simplecloud";
            case 9:
            case 10:
            case 11:
            case 12: return "soft_rain";
            case 13:
            case 14:
            case 15: return "rainpronosticsymbol";
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21: return "cloudwithhail";
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27: return "snowcloud";
            case 28:
            case 29:
            case 30: return "thunderstormpronostic";
            default: return "sunbright";
        }
    }
}
