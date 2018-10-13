package com.example.anafa.wearit;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ListView listViewContent;

    public static final int List_View_Type = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ArrayList itemList = SendToServerGetReqForHistory();
        if (!itemList.isEmpty())
        {
            showHistory(itemList);
            addClickListener(listViewContent, itemList);
        }

    }

    private void showHistory(ArrayList itemList) {
        listViewContent = findViewById(R.id.ResultsListView);

        //type=2-->ListView
        UI.showResults(listViewContent, this, itemList, R.layout.content_list_view_results, List_View_Type);
    }

    // Get recommended list from server
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

    private void addClickListener(ListView listView, final ArrayList itemListToShow) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                Item currentItem = (Item) itemListToShow.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentItem.getItemListLink()));
                startActivity(intent);
            }
        });
    }
}
