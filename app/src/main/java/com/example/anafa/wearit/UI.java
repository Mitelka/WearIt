package com.example.anafa.wearit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UI {

    public static final int Grid_View_Type = 1;
    public static final int List_View_Type = 2;
    private ServerConnector serverConnector = ServerConnector.getInstance();
    private static final String EMPTY_STRING = "";

    //type=1-->GridView
    //type=2-->ListView
    public static void showResults(View view, Activity activity, ArrayList itemList, int showResultsAt, int type)
    {
        CustomArrayListAdapter myAdapter = new CustomArrayListAdapter(activity, showResultsAt, itemList);

        if (type == Grid_View_Type) {
            ((GridView) view).setAdapter(myAdapter);
        } else if (type == List_View_Type) {
            ((ListView) view).setAdapter(myAdapter);
        }
    }


    public ArrayList genericSearchByText(String txtToSearch, PropertyReader propertyReader)
    {
        ArrayList itemListToShow = new ArrayList();
        String stringApiKey = propertyReader.getProperties().getProperty("StringapiKey");
        String customSearchEngingID = propertyReader.getProperties().getProperty("StringcustomSearchEngineID");
        GoogleSearch googleSearch = new GoogleSearch(stringApiKey, customSearchEngingID);
        String responseMessage;
        try {

            responseMessage = googleSearch.searchAtGoogle(txtToSearch);

            JSONObject sendTOServer = modifyJsonForServer(responseMessage);
            String ServerResponse = serverConnector.sendRequestToServer(sendTOServer, ServerConnector.RequestType.GoogleSearch);

            JSONObject resultFromServer = new JSONObject(ServerResponse);
            JSONArray resultByPrice = new JSONArray();
            JSONArray resultByRank = new JSONArray();

            resultByPrice = resultFromServer.getJSONArray("googleResultSortedByPrice");
            resultByRank = resultFromServer.getJSONArray("googleResultSortedByRank");

            //TODO  check if ARRAYS ARE EMPTY

            ArrayList itemListToShowByPrice = createArrayResultToShow(resultByPrice);
            ArrayList itemListToShowByStars = createArrayResultToShow(resultByRank);

            itemListToShow.add(itemListToShowByPrice);
            itemListToShow.add(itemListToShowByStars);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemListToShow;
    }


    public ArrayList createArrayResultToShow(JSONArray resultFromServer) {
        ArrayList listToReturn = new ArrayList<>();

        for (int i = 0; i < resultFromServer.length(); i++) {
            JSONObject current;
            String itemName = EMPTY_STRING;
            String itemImage = EMPTY_STRING;
            String itemPrice = EMPTY_STRING;
            String itemLink = EMPTY_STRING;
            String itemStars = EMPTY_STRING;

            try {
                current = resultFromServer.getJSONObject(i);
                if (current.has("itemName")) {
                    itemName = current.getString("itemName");
                }
                if (current.has("image")) {
                    itemImage = current.getString("image");
                }
                if (current.has("itemPrice")) {
                    itemPrice = current.getString("itemPrice");
                }
                if (current.has("link")) {
                    itemLink = current.getString("link");
                }
                if (current.has("rank")) {
                    itemStars = current.getString("rank");
                }

                listToReturn.add(new Item(itemName, itemImage, itemPrice, itemLink, itemStars));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return listToReturn;
    }

    private JSONObject modifyJsonForServer(String responseMessage) throws JSONException {
        String link = EMPTY_STRING;
        String displayLink = EMPTY_STRING;

        JSONArray modifyItemsArray = new JSONArray();
        JSONObject GoogleSearchjson = new JSONObject(responseMessage);
        JSONObject GoogleSearchjsonSendToServer = new JSONObject();
        JSONObject sendTOServer = new JSONObject();
        JSONArray items;
        items = GoogleSearchjson.getJSONArray("items");
        for(int i = 0 ; i< items.length(); i++)
        {
            JSONObject putInItem = new JSONObject();
            if (items.getJSONObject(i).has("link"))
            {
                link = items.getJSONObject(i).getString("link");
                putInItem.put("link", link);
            }
            if (items.getJSONObject(i).has("displayLink"))
            {
                displayLink = items.getJSONObject(i).getString("displayLink");
                putInItem.put("displayLink", displayLink);
            }

            modifyItemsArray.put(putInItem);
        }

        GoogleSearchjsonSendToServer.put("items", modifyItemsArray);

        sendTOServer.put("googleSearchResult", GoogleSearchjsonSendToServer);
        return sendTOServer;
    }
}