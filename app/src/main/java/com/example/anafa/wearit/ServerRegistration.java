package com.example.anafa.wearit;

import android.os.AsyncTask;
import android.os.StrictMode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class ServerRegistration extends AsyncTask<Void, Void, Void> {

    private String Serverurl = "http://10.0.2.2:3000/auth";
    private String FirsName;
    private String lastName;
    private String nickname;
    private String EmailAddress;
    private String Password;
    JSONObject RegistrationJson;


    public ServerRegistration(JSONObject m_RegistrationJson)
    {
        RegistrationJson = m_RegistrationJson;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public boolean sendRequestToserver(JSONObject registrationJson)
    {
        DataOutputStream printout;
        InputStream respond = null;
        try
        {
            URL content = new URL(Serverurl);
            HttpURLConnection client = (HttpURLConnection) content.openConnection();
            client.setRequestMethod("POST");
            client.setRequestProperty("Content-Type","application/json");
            client.setRequestProperty("Host", Serverurl);
            client.setDoOutput(true);
            client.connect();

            printout = new DataOutputStream(client.getOutputStream ());
            printout.writeBytes(URLEncoder.encode(registrationJson.toString(),"UTF-8"));
            printout.flush ();
            respond = client.getInputStream();
            printout.close ();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  false;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        sendRequestToserver(RegistrationJson);
        return null;
    }
}
