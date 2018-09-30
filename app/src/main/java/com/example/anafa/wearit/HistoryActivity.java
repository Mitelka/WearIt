package com.example.anafa.wearit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class HistoryActivity extends AppCompatActivity {

    ListView listViewContent;
    // TODO: Delete custom datasorce
    String[] itemNameArr = {"Adidas", "LV"};
    String[] itemPriceArr = {"13.98$", "56.9$"};
    String[] itemLinkArr = {"www.adidas.com", "www.aliexpress/lv.co.il"};
    Integer[] imageIDArr = {R.drawable.adidas_gazelle, R.drawable.wearitphoto};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // TODO: Get history list from user
        // Show history list results
        showResults();
    }

    private void showResults() {
        listViewContent = (ListView) findViewById(R.id.ResultsListView);

        CustomListAdapter customListAdapter = new CustomListAdapter(this,
                itemNameArr, itemPriceArr, itemLinkArr, imageIDArr);
        listViewContent.setAdapter(customListAdapter);
    }
}
