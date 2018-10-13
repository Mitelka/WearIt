package com.example.anafa.wearit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    public static final int List_View_Type = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ArrayList itemList = SendToServerGetReqForHistory();
        if (!itemList.isEmpty())
        {
            showHistory(itemList);
        }

    }

    private void showHistory(ArrayList itemList) {

        ListView listView = findViewById(R.id.ResultsListView);

        //type=2-->ListView
        UI.showResults(listView, this, itemList, R.layout.content_list_view_results, List_View_Type);
    }

    private ArrayList SendToServerGetReqForHistory()
    {
        UI ui = new UI();
        ArrayList itemList = new ArrayList<>();
        String serverResponse = ServerConnector.getInstance().sendGETRequestToServer(ServerConnector.RequestType.PostToHistory);

        try
        {
            JSONArray resultFromServer = new JSONArray(serverResponse);
            itemList = ui.createArrayResultToShow(resultFromServer);
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return itemList;

    }
}
