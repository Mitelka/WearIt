package com.example.anafa.wearit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UserSearchByPhotoActivity extends AppCompatActivity {

    private static final Integer REQUEST_CAMERA = 0;
    private static final Integer SELECT_FILE = 1;
    ImageView dynamicImageView;
    private boolean uploadedImage = false;
    private ScrollView mScroll;
    private TextView mLog;
    private static final String LOG_TEXT_KEY = "LOG_TEXT_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_by_photo);

        RadioButton cameraRadioButton = (RadioButton) findViewById(R.id.takePictureCheckBox);
        RadioButton openPhotoFromFileRadioButton = (RadioButton) findViewById(R.id.uploadImageCheckBox);
        dynamicImageView = (ImageView) findViewById(R.id.dynamicPhotoImageView);
        Button searchImageOnGoogle = (Button)findViewById(R.id.searchButton);

        // Initialize the scrollView components
        mScroll = (ScrollView)findViewById(R.id.scrollLog);
        mLog = (TextView)findViewById(R.id.tvLog);
        mLog.setText("");

        // Take photo from camera
        cameraRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
                uploadedImage = true;
            }
        });

        //Open image from file at device
        openPhotoFromFileRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                uploadedImage = true;
            }
        });

        //search button action
        searchImageOnGoogle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(uploadedImage){
                    Toast.makeText(UserSearchByPhotoActivity.this, "You selected search by image", Toast.LENGTH_LONG).show();
                    Toast.makeText(UserSearchByPhotoActivity.this, "Searching image on google", Toast.LENGTH_LONG).show();

                    // TODO: add google image api
                    try {
                        searchImageAtGoogleUsingGoogleImageAPI();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(UserSearchByPhotoActivity.this, "Exception!!" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    // display results in TextView with scrollView
                    clearResultsTextView();
                    displayMessageWithResults("This is the results of image search in Google API\n");
                }
                else{
                    Toast.makeText(UserSearchByPhotoActivity.this, "You didn't selected an image to search", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == REQUEST_CAMERA){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                dynamicImageView.setImageBitmap(bitmap);
            } else if(requestCode == SELECT_FILE){
                Uri selectImageUri = data.getData();
                dynamicImageView.setImageURI(selectImageUri);
            }

            //TODO: Delete this after succeed to search photo at Google
            // update bitmap to be an adidas gazelle photo - adidas_gazelle.jpg
            dynamicImageView.setImageDrawable(getResources().getDrawable(R.drawable.adidas_gazelle));
        }
    }

    public void displayMessageWithResults(String message) {
        mLog.append(message + "\n");
        mScroll.scrollTo(0, mScroll.getBottom());
    }

    public void onClearBtnClick(View view) {
        clearResultsTextView();
    }

    private void clearResultsTextView() {
        mLog.setText("");
        mScroll.scrollTo(0, mScroll.getBottom());
    }

    // save and restore the text printed at TextView
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(LOG_TEXT_KEY, mLog.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mLog.setText(savedInstanceState.getString(LOG_TEXT_KEY));
    }

    private void searchImageAtGoogleUsingGoogleImageAPI() throws IOException, JSONException {
        //TODO: adding code of search image
        // https.. = The standard URL for the Google Image Search API

        // Required URL arguments:
        // q = This argument supplies the query, or search expression, that is passed into the searcher.
        // v = This argument supplies protocol version number.

        // Optional URL arguments:
        // imgtype=photo = restricts results to photographic images.
        // rsz=4 = This argument supplies an integer from 1–8 indicating the number of results to return per page.
        // userip=192.168.0.1 = This argument supplies the IP address of the end-user on whose behalf the request is being made.
        URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +
                "v=1.0&q=barack%20obama&imgtype=photo&rsz=4&userip=" + getUserIPAddress());
        URLConnection connection = url.openConnection();
        connection.addRequestProperty("Referer", ""/* TODO: Enter the URL of your site here */);

        String line;
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while((line = reader.readLine()) != null) {
            builder.append(line);
        }

        JSONObject json = new JSONObject(builder.toString());
        // TODO: now have some fun with the results...
    }

    private String getUserIPAddress() {
        WifiManager wm = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        String userIPAddress = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        // Only for test
        Toast.makeText(UserSearchByPhotoActivity.this, "IP: " + userIPAddress, Toast.LENGTH_LONG).show();

        return userIPAddress;
    }
}