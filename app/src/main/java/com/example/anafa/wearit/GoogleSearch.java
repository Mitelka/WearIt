package com.example.anafa.wearit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

public class GoogleSearch
{
    public static final String EMPTY_STRING = "";
    private String stringApiKey;
    private String searchEngineID;

    public GoogleSearch(String stringApiKey, String searchEngineID) {
        this.stringApiKey = stringApiKey;
        this.searchEngineID = searchEngineID;
    }

    public GoogleSearch()
    {
    }

    public String searchAtGoogle(String searchString) throws IOException
    {
        String fullurlString = createStringURL(searchString);
        URL url = new URL(fullurlString);
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

    private String createStringURL(String searchString)
    {
        InputStream input = null;
        String urlString = EMPTY_STRING;
        String beginningUrl = "https://www.googleapis.com/customsearch/v1/siterestrict?";

        if(searchString.contains(" ")) {
            searchString = searchString.replace(" ", "+");
        }

        urlString = beginningUrl + "key=" + stringApiKey + "&cx=" + searchEngineID + "&q=" + searchString + "&quotaUser" + getUserIPAddress();

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
