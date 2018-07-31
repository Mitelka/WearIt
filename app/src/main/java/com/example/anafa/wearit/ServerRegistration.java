package com.example.anafa.wearit;

import android.os.AsyncTask;
import android.os.StrictMode;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class ServerRegistration extends AsyncTask<Void, Void, Void> {

    private String Serverurl = "http://10.0.2.2:3000/auth/signup";
    private String FirsName;
    private String lastName;
    private String nickname;
    private String EmailAddress;
    private String Password;
    JSONObject RegistrationJson;


    public ServerRegistration(JSONObject m_RegistrationJson)
    {
        RegistrationJson = m_RegistrationJson;
    }

    public boolean sendRequestToserver(final JSONObject registrationJson)
    {
        //RequestQueue queue = Volley.newRequestQueue(this)
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", "AbCdEfGh123456");

        JsonObjectRequest req = new JsonObjectRequest(Serverurl, new JSONObject(params),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });


        new SendDeviceDetails(Serverurl, registrationJson).execute(Serverurl,registrationJson.toString());

        /*InputStreamReader respond = null;
        try
        {
            URL content = new URL(Serverurl);
            HttpURLConnection client = (HttpURLConnection) content.openConnection();
            client.setRequestMethod("POST");
            client.setRequestProperty("Content-Type","application/json");
            client.setRequestProperty("Host", Serverurl);
            client.setDoOutput(true);
            client.connect();
            OutputStreamWriter printout = new OutputStreamWriter(client.getOutputStream());
            //printout = new DataOutputStream(client.getOutputStream ());
            printout.write(URLEncoder.encode(registrationJson.toString(),"UTF-8"));
            printout.flush ();
            respond = new InputStreamReader(client.getInputStream());
            printout.close ();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return  false;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        sendRequestToserver(RegistrationJson);
        return null;
    }
}
