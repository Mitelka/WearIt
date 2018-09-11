package com.example.anafa.wearit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class GoogleAnalysisImage
{
    private String imageUrlStringToAnalyze;
    private final static String BASE_SERVER_URL = "https://vision.googleapis.com/v1/images:annotate?key=";
    private static final String POST = "POST";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    public static final String WEB_DETECTION = "WEB_DETECTION";
    public static final String FACE_DETECTION = "FACE_DETECTION";
    public static final String LABEL_DETECTION = "LABEL_DETECTION";

    private String stringApiKey;

    public GoogleAnalysisImage(String imageUrlString, String stringApiKeyForAnalyse)
    {
        imageUrlStringToAnalyze = imageUrlString;
        stringApiKey = stringApiKeyForAnalyse;
    }

    public String imageAnalyzeRequest()
    {
        StringBuilder response = new StringBuilder();
        try
        {
            URL content = new URL(BASE_SERVER_URL + stringApiKey);
            HttpURLConnection client = (HttpURLConnection) content.openConnection();
            client.setRequestMethod(POST);
            client.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
            client.setDoOutput(true);

            //Send request
            BufferedWriter httpRequestBodyWriter = new BufferedWriter(new
                    OutputStreamWriter(client.getOutputStream()));
            JSONObject output = getJsonObject(imageUrlStringToAnalyze, WEB_DETECTION);

            httpRequestBodyWriter.write(output.toString());
            httpRequestBodyWriter.close();

            //Get Response
            if (client.getInputStream() == null) {
                System.out.println("No stream");
            }

            Scanner httpResponseScanner = new Scanner(client.getInputStream());
            while (httpResponseScanner.hasNext()) {
                String line = httpResponseScanner.nextLine();
                response.append(line);
            }
            httpResponseScanner.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return response.toString();
    }

    public static JSONObject getJsonObject(String imageUrlStringToAnalyze, String detection) {
        JSONArray requests = new JSONArray();
        JSONObject output = new JSONObject();
        JSONObject image = new JSONObject();
        JSONArray features = new JSONArray();
        JSONObject type = new JSONObject();
        String WhatInfo = detection;


        JSONObject insideRequests = new JSONObject();

        try
        {
            type.put("type", WhatInfo);
            image.put("content", imageUrlStringToAnalyze);
            features.put(type);
            insideRequests.put("features", features);
            insideRequests.put("image", image);

            requests.put(0, insideRequests);

            output.put("requests", requests);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return output;
    }
}
