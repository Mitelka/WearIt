package com.example.anafa.wearit;

import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserSearchByTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_by_text);

        final TextView text = (TextView) findViewById(R.id.textToSearch);
        Button searchBtn = (Button) findViewById(R.id.searchButton);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtToSearch = text.getText().toString();
                Toast.makeText(UserSearchByTextActivity.this, "product name to search: " + txtToSearch , Toast.LENGTH_LONG).show();
                if(txtToSearch != "") {
                    String url = "https://www.google.co.il/search?&q=" + txtToSearch + "&oq=" + txtToSearch + "&userip=" + getUserIPAddress();
                    // {google:baseURL}search?q=%s&{google:RLZ}{google:originalQueryForSuggestion}{google:assistedQueryStats}{google:searchFieldtrialParameter}{google:iOSSearchLanguage}{google:searchClient}{google:sourceId}{google:contextualSearchVersion}ie={inputEncoding}
                    WebView webview = (WebView) findViewById(R.id.myWebView);
                    webview.setWebViewClient(new WebViewClient());
                    webview.getSettings().setJavaScriptEnabled(true);
                    webview.loadUrl(url);
                } else {
                    Toast.makeText(UserSearchByTextActivity.this, "You didn't entered product name to search" , Toast.LENGTH_LONG).show();
                }


            }
        });
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

    private String getUserIPAddress() {
        WifiManager wm = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        String userIPAddress = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

//        // Only for test
//        Toast.makeText(UserSearchByTextActivity.this, "IP: " + userIPAddress, Toast.LENGTH_LONG).show();

        return userIPAddress;
    }


}
