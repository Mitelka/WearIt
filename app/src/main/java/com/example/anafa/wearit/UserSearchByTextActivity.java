package com.example.anafa.wearit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserSearchByTextActivity extends AppCompatActivity {
    private static final String EMPTY_STRING = "";
    public static final int List_View_Type = 2;
    private PropertyReader propertyReader;
    private GoogleSearch googleSearch;
    private ServerConnector serverConnector;
    UI ui = new UI();
    TabHost tabHost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_by_text);

        propertyReader = new PropertyReader(getBaseContext());
        String stringApiKey = propertyReader.getProperties().getProperty("StringapiKey");
        String customSearchEngingID = propertyReader.getProperties().getProperty("StringcustomSearchEngineID");
        googleSearch = new GoogleSearch(stringApiKey, customSearchEngingID);

        final TextView text = (TextView) findViewById(R.id.textToSearch);
        Button searchBtn = (Button) findViewById(R.id.searchButton);

        //TODO: Throw to other activity --> the same as at Search By Photo
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setVisibility(View.INVISIBLE);
        tabHost.setup();

        //tab1
        TabHost.TabSpec spec = tabHost.newTabSpec("Price");
        spec.setContent(R.id.tab1);
        spec.setIndicator("SORTED BY PRICE");
        tabHost.addTab(spec);

        //tab2
        spec = tabHost.newTabSpec("tab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("SORTED BY STARS");
        tabHost.addTab(spec);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtToSearch = text.getText().toString();
                Toast.makeText(UserSearchByTextActivity.this, "product name to search: " + txtToSearch, Toast.LENGTH_LONG).show();
                if (!txtToSearch.equals(EMPTY_STRING)) {
                    searchTextAtGoogle(txtToSearch);
                    tabHost.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(UserSearchByTextActivity.this, "You didn't entered product name to search", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void searchTextAtGoogle(String txtToSearch) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        try {
            ArrayList itemListToShow;

            // Get results item list from server
            ArrayList itemListsToShow = ui.genericSearchByText(txtToSearch, propertyReader);
            ArrayList itemListToShowByPrice = (ArrayList) itemListsToShow.get(0);
            ArrayList itemListToShowByStars = (ArrayList) itemListsToShow.get(1);

            showResultsOfSearch(itemListToShowByPrice, itemListToShowByStars);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Cannot connect googleSearch", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void showResultsOfSearch(ArrayList itemListToShowByPrice, ArrayList itemListToShowByStars) {
        ListView priceListView = (ListView) findViewById(R.id.ResultsByPriceListView);
        ListView starsListView = (ListView) findViewById(R.id.ResultsByStarsListView);

        //type=2-->ListView
        // Sorted by price
        UI.showResults(priceListView, this, itemListToShowByPrice, R.layout.content_list_view_results, List_View_Type);
        addClickListener(priceListView, itemListToShowByPrice);

        // Sorted by stars
        UI.showResults(starsListView, this, itemListToShowByStars, R.layout.content_list_view_results, List_View_Type);
        addClickListener(starsListView, itemListToShowByStars);
    }

    private void addClickListener(ListView listView, final ArrayList itemListToShow) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {

                Item currentItem = (Item) itemListToShow.get(position);
                createJsonAndSendForHistory(currentItem);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentItem.getItemListLink()));
                startActivity(intent);
            }
        });
    }

    private void createJsonAndSendForHistory(Item currentItem)
    {
        HashMap<String,String> mapToSend = new HashMap<String, String>();
        mapToSend.put("image", currentItem.getItemListImage());
        mapToSend.put("link", currentItem.getItemListLink());
        mapToSend.put("itemName", currentItem.getItemListName());
        mapToSend.put("itemPrice", currentItem.getItemListPrice());
        mapToSend.put("rank", currentItem.getItemListStars());
        try
        {
            JSONObject JsonForHistory  = new JSONObject(mapToSend);
            String registrationResponse = ServerConnector.getInstance().sendRequestToServer(JsonForHistory, ServerConnector.RequestType.PostToHistory);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
