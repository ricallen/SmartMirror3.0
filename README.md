# SmartMirror3.0

## Intended for use in smart mirror applications using android tablets running Android 4.1 and above.


Application uses weather from the [Met Office Datapoint](http://www.metoffice.gov.uk/datapoint) API. Notes are pulled from a private [tumblr](https://www.tumblr.com/docs/en/api/v2) API. News is pulled from three sources from [newsapi.org](https://newsapi.org/). Developer accounts with all three must be created and input for this app to work.

A contstants file must be generated in SmartMirror3.0/android/app/src/main/java/smarter/smartmirror30/Constants.java containing the users personal api keys. It must follow the general structure.
```java
public class Constants {

   public static String tumblrURL1 = "https://api.tumblr.com/v2/blog/";
    public static String tumblrURL2 = "/posts/?api_key=";
    public static String tumblrName = "foo.tumblr.com";
    public static String tumblrAPIkey = "tumblrAPIkeyString";

    public static String newsURL1 = "https://newsapi.org/v1/articles?source=";
    public static String newsURL2 = "&sortBy=top&apiKey=";
    public static String[] newsSourceArray = {"bbc-news", "bbc-sport", "bloomberg", "time", "techcrunch"
            , "business-insider", "financial-times", "engadget"};
    public static String newsAPIkey = "newAPIkeyString";

    public static String metOfficeURL1 = "http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/";
    public static String metOfficeURL2 = "?res=daily&key=";
    public static String metOfficeLocationID = "0000"; // Location ID (obtained through met office api"
    public static String metOfficeAPIkey = "metOfficeAPIkeyString";
}
```

## Notes
Icons designed by [Freepik](http://www.freepik.com/) from Flaticon
