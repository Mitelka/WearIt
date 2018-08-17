package com.example.anafa.wearit;

import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class UserSearchByTextActivity extends AppCompatActivity
{
    private GoogleSearch googleSearch;
    private ServerConnector serverConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_by_text);

        final TextView text = (TextView) findViewById(R.id.textToSearch);
        Button searchBtn = (Button) findViewById(R.id.searchButton);

        searchBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String txtToSearch = text.getText().toString();
                Toast.makeText(UserSearchByTextActivity.this, "product name to search: " + txtToSearch , Toast.LENGTH_LONG).show();
                if(txtToSearch != "")
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
        googleSearch = new GoogleSearch();
        String responseMessage;
        InputMethodManager inputManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        try
        {
            responseMessage = googleSearch.searchAtGoogle(txtToSearch);

            serverConnector = new ServerConnector();
            JSONObject GoogleSearchjson = new JSONObject(responseMessage);
            String GoogleSearchResponse = serverConnector.sendRequestToServer(GoogleSearchjson, ServerConnector.RequestType.GoogleSearch);
            //TODO: DO somtehing with the GoogleSearchResponse
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(this, "Cannot connect googleSearch", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    public void searchButtonClickHandler(View view) {
    }
//    public class MyWebViewClient extends WebViewClient {
//        public MyWebViewClient(UserSearchByTextActivity userSearchByTextActivity) {
//            public MyWebViewClient() {
//                super();
//                //start anything you need to
//            }
//
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                //Do something to the urls, views, etc.
//            }
//        }
//    }

}
