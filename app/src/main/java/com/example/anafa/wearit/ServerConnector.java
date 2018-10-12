package com.example.anafa.wearit;

import android.os.StrictMode;

import com.example.anafa.wearit.DTO.GenericDTO;
import com.example.anafa.wearit.DTO.SignupDTO;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ServerConnector {

    private static final String EMPTY_STRING = "";
    private final static String BASE_SERVER_URL = "https://mighty-hollows-89031.herokuapp.com/";
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String X_AUTH = "x-auth";
    private static String X_AUTH_Value = EMPTY_STRING;

    private static ServerConnector instance;

    public enum RequestType {
        SIGNUP,
        LOGIN,
        GoogleSearch,
        ForgotPassword,
        Recommended,
        PostToHistory
    }

    private static Map<RequestType, String> requestTypeUrlMap;
    private static Map<RequestType, Class<? extends GenericDTO>> requestTypeDtoMap;


    private ServerConnector()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        initRequestTypeUrlMap();
        initRequestTypeDtoMap();
    }

    static
    {
        instance = new ServerConnector();
    }

    private void initRequestTypeUrlMap() {
        requestTypeUrlMap = new HashMap<>();
        requestTypeUrlMap.put(RequestType.SIGNUP, "auth/signup");
        requestTypeUrlMap.put(RequestType.LOGIN, "auth/login");
        requestTypeUrlMap.put(RequestType.GoogleSearch, "upload/processGoogleSearchData");
        requestTypeUrlMap.put(RequestType.ForgotPassword, "auth/forgotPassword");
        requestTypeUrlMap.put(RequestType.Recommended, "upload/favorites");
        requestTypeUrlMap.put(RequestType.PostToHistory, "upload/history");

    }

    private void initRequestTypeDtoMap() {
        requestTypeDtoMap = new HashMap<>();
        requestTypeDtoMap.put(RequestType.SIGNUP, SignupDTO.class);
    }

    public JSONObject createJSonToServer(HashMap<String,String> map)
    {
        JSONObject RegistrationJson  = new JSONObject(map);

        return RegistrationJson;
    }

    public String sendRequestToServer(JSONObject jsonBody, RequestType requestType)
    {
        StringBuilder response = new StringBuilder();
        try
        {
            URL content = new URL(BASE_SERVER_URL + requestTypeUrlMap.get(requestType));
            HttpURLConnection client = (HttpURLConnection) content.openConnection();
            client.setRequestMethod(POST);
            client.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);

            client.setRequestProperty(X_AUTH, X_AUTH_Value);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    client.getOutputStream());
            wr.writeBytes(jsonBody.toString());
            wr.close();

            //Get Response
            InputStream is;
            if (client.getResponseCode() == 200)
            {
                is = client.getInputStream();
                if (requestType == RequestType.SIGNUP || requestType == RequestType.LOGIN)
                {
                    X_AUTH_Value = client.getHeaderField(X_AUTH);
                }
            } else {
                is = client.getErrorStream();
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return response.toString();
    }

    public String sendGETRequestToServer(RequestType requestType)
    {
        StringBuilder response = new StringBuilder();
        try
        {
            URL content = new URL(BASE_SERVER_URL + requestTypeUrlMap.get(requestType));
            HttpURLConnection client = (HttpURLConnection) content.openConnection();
            client.setRequestMethod(GET);
            client.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
            client.setRequestProperty(X_AUTH, X_AUTH_Value);

            //Get Response
            InputStream is;
            if (client.getResponseCode() == 200)
            {
                is = client.getInputStream();
            } else {
                is = client.getErrorStream();
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    public static ServerConnector getInstance() {
        return instance;
    }
}
