package com.example.anafa.wearit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserSearchByTextActivity extends AppCompatActivity
{
    private static final String EMPTY_STRING = "";
    private PropertyReader propertyReader;
    private GoogleSearch googleSearch;
    private ServerConnector serverConnector;

    ListView listViewContent;
    // TODO: Delete custom datasorce
    String[] itemNameArr = {"Adidas", "LV"};
    String[] itemPriceArr = {"13.98$", "56.9$"};
    String[] itemLinkArr = {"www.adidas.com", "www.aliexpress/lv.co.il"};
    Integer[] imageIDArr = {R.drawable.adidas_gazelle, R.drawable.wearitphoto};

    public static final int List_View_Type = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_by_text);

        propertyReader = new PropertyReader(getBaseContext());
        String stringApiKey = propertyReader.getProperties().getProperty("StringapiKey");
        String customSearchEngingID = propertyReader.getProperties().getProperty("StringcustomSearchEngineID");
        googleSearch = new GoogleSearch(stringApiKey, customSearchEngingID);

        final TextView text = (TextView) findViewById(R.id.textToSearch);
        Button searchBtn = (Button) findViewById(R.id.searchButton);

        searchBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String txtToSearch = text.getText().toString();
                Toast.makeText(UserSearchByTextActivity.this, "product name to search: " + txtToSearch , Toast.LENGTH_LONG).show();
                if(!txtToSearch.equals(EMPTY_STRING))
                {
                    searchTextAtGoogle(txtToSearch);
                }
                else
                {
                    Toast.makeText(UserSearchByTextActivity.this, "You didn't entered product name to search" , Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private void searchTextAtGoogle(String txtToSearch)
    {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        try
        {
            UI ui = new UI();
            ArrayList itemListToShow;

            itemListToShow= ui.genericSearchByText(txtToSearch, propertyReader);
            showResultsOfSearch(itemListToShow);
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(this, "Cannot connect googleSearch", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void showResultsOfSearch(ArrayList itemListToShow)
    {
        ListView listView = (ListView) findViewById(R.id.ResultsListView);

        //TODO: Get results item list list from server
        //TODO: DELETE after getting this ArrayList from SERVER

        //type=2-->ListView
        UI.showResults(listView, this, itemListToShow, R.layout.content_list_view_results, List_View_Type);
    }
}
