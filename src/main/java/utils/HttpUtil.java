package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

    /**
     * perform GET request to given URL and returns the response as string
     *
     * @param request
     * @return String
     * @throws IOException
     */
    public static String get(URL request) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) request.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null)
            sb.append(line);
        reader.close();
        return sb.toString();
    }
}
