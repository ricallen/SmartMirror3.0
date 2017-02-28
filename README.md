# SmartMirror3.0

##Intended for use in smart mirror applications using android tablets running kitkat and above.


Application uses weather from the [Met Office Datapoint](http://www.metoffice.gov.uk/datapoint) API. And notes/list are pulled from a private [tumblr](tumblr.com) API. Developer accounts with both must be created and input for this app to work.

A contstants file must be generated in SmartMirror3.0/android/app/src/main/java/smarter/smartmirror30/Constants.java containing the users personal api keys. It must follow the general structure...
```java
public class Constants {

    public static String tumblrName = "foo.tumblr.com";
    public static String tumblrAPIkey = "tumblrApiKeyString";

    public static String newsURL = "https://newsapi.org/v1/articles?source=bbc-news&sortBy=top&apiKey=";
    public static String newsAPIkey = "newApiKeyString";

    //link = metOfficeURL1 + metOfficeLocationID + metOfficeURL2 + metOfficeAPIkey
    public static String metOfficeURL1 = "http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/json/";
    public static String metOfficeURL2 = "?res=daily&key=";
    public static String metOfficeLocationID = "0000"; // location ID obtained via a call to the met office datapoint api
    public static String metOfficeAPIkey = "metOfficeApiKeyString";
}
```
