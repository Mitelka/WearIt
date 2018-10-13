package com.example.anafa.wearit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class UserMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private boolean ttsInitialized;
    private ServerConnector serverConnector;

    //Show as GridView
    GridView gridView;

    public static final int Grid_View_Type = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.MESSAGE_KEY);

        TextView textView = (TextView) findViewById(R.id.messageDisplayTextView);
        textView.setText("Hello "+ message + "!");
//        saySomething("Hello "+ message);
//        saySomething(textView.getText().toString());

        //tts = new TextToSpeech(getApplicationContext(), this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ArrayList itemList = SendToServerGetReqForRecommended();
        if (!itemList.isEmpty())
        {
            showRecommended(itemList);
            addClickListener(gridView, itemList);
        }
    }

    private ArrayList SendToServerGetReqForRecommended()
    {
        UI ui = new UI();
        ArrayList itemList = new ArrayList<>();
        String serverResponse = ServerConnector.getInstance().sendGETRequestToServer(ServerConnector.RequestType.Recommended);

        try
        {
            JSONObject resultFromServer = new JSONObject(serverResponse);
            JSONArray ArrayFromServer = resultFromServer.getJSONArray("favorites");
            itemList = ui.createArrayResultToShow(ArrayFromServer);
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return itemList;

    }

    private void showRecommended(ArrayList itemListToShow) {

        gridView = (GridView) findViewById(R.id.ResultsGridView);

        //type=1-->GridView
        UI.showResults(gridView, this, itemListToShow, R.layout.content_grid_view_results, Grid_View_Type);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        else if (id == R.id.action_logoff) {
            logOffFunc();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logOffFunc() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        //TODO: Deleting the start activity on each condition and adding it outside the loop
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent intent = new Intent(this, UserSearchByPhotoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, UserSearchByPhotoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_history)
        {
            Intent intent = new Intent(this, HistoryActivity.class);
            ProgressBar progressBar = new ProgressBar();
            progressBar.getProgressBar(this);
            startActivity(intent);
        } else if (id == R.id.nav_tools) {
            Intent intent = new Intent(this, ToolsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            //TODO: Add our WearIt! app link
            String message = "\nLet me recommend you WearIt! application: *WearIt! app link* \n\n";

            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "WearIt!");
            i.putExtra(Intent.EXTRA_TEXT, message);

            startActivity(Intent.createChooser(i, "choose where to share this application"));

        } else if (id == R.id.nav_send) {
            Intent i = new Intent(Intent.ACTION_SEND);

            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"wearitapp2018@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            i.putExtra(Intent.EXTRA_TEXT   , "Write email");

            startActivity(Intent.createChooser(i, "Send mail..."));

            //Toast.makeText(UserMenuActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_switch_user) {
            //TODO: Log off from current user

            //Switch to LoginActivity to make login from another user
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_log_off) {
            logOffFunc();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void searchByTextButtonClickHandler(View view) {
        Intent intent = new Intent(this, UserSearchByTextActivity.class);
        startActivity(intent);
    }

    public void searchByPhotoButtonClickHandler(View view) {
        Intent intent = new Intent(this, UserSearchByPhotoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                displayMessage("This language isn't supported");
            } else {
                ttsInitialized = true;
            }
        } else {
            displayMessage("TTS initialization failed");
        }
    }

    private void displayMessage(String message) {
        Toast.makeText(UserMenuActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void saySomething(String speech) {
        if(!ttsInitialized){
            displayMessage("TextToSpeech wasn't initialized");
        } else {
            tts.speak(speech, TextToSpeech.QUEUE_FLUSH, null, "speech");
        }
    }

    private void addClickListener(GridView gridView, final ArrayList itemListToShow) {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                Item currentItem = (Item) itemListToShow.get(position);
                UI.createJsonAndSendForHistory(currentItem);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentItem.getItemListLink()));
                startActivity(intent);
            }
        });
    }
}
