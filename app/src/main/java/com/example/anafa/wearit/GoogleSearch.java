package com.example.anafa.wearit;

import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class GoogleSearch
{

    public GoogleSearch()
    {
    }

    public String searchImageAtGoogle() throws IOException, JSONException
    {
        String urlString = createStringURL();

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String line;
        StringBuilder responsebuilder = new StringBuilder();
        Integer responseCode = connection.getResponseCode();
        String responseMessage = connection.getResponseMessage();

        if(responseCode == 200)
        { // response OK
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((line = reader.readLine()) != null) {
                responsebuilder.append(line + "\n");
            }

            reader.close();
            connection.disconnect();
        }
        else
            { // response problem
                 responsebuilder.append("Http ERROR response " + responseMessage + "\n" + "Make sure to replace in code your own Google API key and Search Engine ID");
            }
            return responsebuilder.toString();
    }

    private String createStringURL() {
        String beginningUrl = "https://www.googleapis.com/customsearch/v1?";
        String apiKey = "";
        String customSearchEngineID = "";
        String searchQuery = " leo messi";

        if(searchQuery.contains(" ")) {
            searchQuery = searchQuery.replace(" ", "+");
        }

        String urlString = beginningUrl + "key=" + apiKey + "&cx=" + customSearchEngineID + "&q=" + searchQuery+ "&quotaUser" + getUserIPAddress();
        //+ "&searchType=image"

        return urlString;
    }

    private String getUserIPAddress() {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
