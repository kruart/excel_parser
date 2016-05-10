package ua.kruart.parser.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Arthur on 3/27/2016.
 */
public class Util {
    public static URLConnection getConnection(String imageLink) throws IOException {
        URL url = new URL(imageLink);
        URLConnection urlConnection = url.openConnection();
        urlConnection.addRequestProperty("user-agent", "Opera");
        return urlConnection;
    }
}
