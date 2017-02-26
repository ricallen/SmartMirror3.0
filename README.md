# SmartMirror3.0

##Intended for use in smart mirror applications using android tablets running kitkat and above.


Application uses weather from the [openweathermap](http://openweathermap.org/) API. And notes/list are pulled from a private [tumblr](tumblr.com) API. Developer accounts with both must be created and input for this app to work.

A contstants file must be generated in SmartMirror3.0/android/app/src/main/java/smarter/smartmirror30/Constants.java containing the users personal api keys. It must follow the general structure...
```java
public class Constants {
    public static String weatherAPIkey = "weatherAPIKeyString";
    public static String tumblrName = "tumblrURL.tumblr.com";
    public static String tumblrAPIkey = "tumblrAPIKeyString";
}
```
