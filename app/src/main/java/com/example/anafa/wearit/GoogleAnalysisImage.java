package com.example.anafa.wearit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

            //Send request
            BufferedWriter httpRequestBodyWriter = new BufferedWriter(new
                    OutputStreamWriter(client.getOutputStream()));
            httpRequestBodyWriter.write
                    ("{\"requests\":  [{ \"features\":  [ {\"type\": \"LABEL_DETECTION\""
                            +"}], \"image\": {\"source\": { \"gcsImageUri\":"
                            +" \"gs://vision-sample-images/4_Kittens.jpg\"}}}]}");
            httpRequestBodyWriter.close();

            //Get Response
            String res = client.getResponseMessage();
            InputStream is;

            if (client.getInputStream() == null) {
                System.out.println("No stream");
            }

            Scanner httpResponseScanner = new Scanner(client.getInputStream());
            while (httpResponseScanner.hasNext()) {
                String line = httpResponseScanner.nextLine();
                response.append(line);
                System.out.println(line);  //  alternatively, print the line of response
            }
            httpResponseScanner.close();

            /* if (client.getResponseCode() == 200)
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
            rd.close();*/

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

}
