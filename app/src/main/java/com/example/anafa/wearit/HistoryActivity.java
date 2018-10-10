package com.example.anafa.wearit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ListView listViewContent;

    public static final int List_View_Type = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // TODO: Get history list from user
        // Show history list results
        showHistory();
    }

    private void showHistory() {

        ListView listView = (ListView) findViewById(R.id.ResultsListView);

        //TODO: Get recommended list from server
        //TODO: DELETE after getting this ArrayList from SERVER

        ArrayList itemList = new ArrayList<>();
        itemList.add(new Item("Adidas", R.drawable.adidas_gazelle, "13.98$", "www.adidas.com", 5));
        itemList.add(new Item("LV", R.drawable.wearitphoto, "56.9$", "www.aliexpress/lv.co.il", 4));

        //type=2-->ListView
        UI.showResults(listView, this, itemList, R.layout.content_list_view_results, List_View_Type);
    }
}
