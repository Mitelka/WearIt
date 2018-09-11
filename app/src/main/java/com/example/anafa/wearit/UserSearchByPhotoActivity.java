package com.example.anafa.wearit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

public class UserSearchByPhotoActivity extends AppCompatActivity {

    private static final String EMPTY_STRING = "";
    private static final Integer REQUEST_CAMERA = 0;
    private static final Integer SELECT_FILE = 1;
    ImageView dynamicImageView;
    private boolean uploadedImage = false;
    private ScrollView mScroll;
    public TextView mLog;
    private static final String LOG_TEXT_KEY = "LOG_TEXT_KEY";
    Boolean isUploadPhotoSelected = false;
    String imageUrlString;
    private ServerConnector serverConnector;
    private GoogleAnalysisImage googleAnalysisImage;
    private GoogleSearch googleSearch = new GoogleSearch();
    private PropertyReader propertyReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_by_photo);

        RadioButton cameraRadioButton = (RadioButton) findViewById(R.id.takePictureCheckBox);
        RadioButton openPhotoFromFileRadioButton = (RadioButton) findViewById(R.id.uploadImageCheckBox);
        dynamicImageView = (ImageView) findViewById(R.id.dynamicPhotoImageView);
        Button searchImageOnGoogle = (Button)findViewById(R.id.searchButton);
        propertyReader = new PropertyReader(getBaseContext());
        final String stringApiKeyForAnalyse = propertyReader.getProperties().getProperty("stringApiKeyForAnalyse");

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


                    googleAnalysisImage = new GoogleAnalysisImage(imageUrlString, stringApiKeyForAnalyse);

                    String Response = googleAnalysisImage.imageAnalyzeRequest();
                    String toSearch =  AnalysisResponse(Response);

                    searchTextAtGoogle(toSearch);
                }

                else {
                    Toast.makeText(UserSearchByPhotoActivity.this, "You didn't selected an image to search", Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    private String AnalysisResponse(String response) {
        String to_Search = EMPTY_STRING;

        String bestScore;
        String secondScore;
        String bestGuessLabels;

        try
        {
            JSONObject jsonToAnalysis = new JSONObject(response);

            JSONArray responses;
            responses = jsonToAnalysis.getJSONArray("responses");

            JSONObject respon = responses.getJSONObject(0);
            JSONObject webDetection = respon.getJSONObject("webDetection");
            JSONArray webEntities = webDetection.getJSONArray("webEntities");
            bestScore = webEntities.getJSONObject(0).getString("description");
            secondScore = webEntities.getJSONObject(1).getString("description");

            JSONArray bestGuessLabelsObject = webDetection.getJSONArray("bestGuessLabels");
            bestGuessLabels = bestGuessLabelsObject.getJSONObject(0).getString("label");

            to_Search = bestGuessLabels + " " + bestScore  + " " + secondScore;
        }

        catch (JSONException e)
        {
            e.printStackTrace();
            Toast.makeText(UserSearchByPhotoActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

        return to_Search;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap picture= null;

        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == REQUEST_CAMERA){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                dynamicImageView.setImageBitmap(bitmap);
            } else if(requestCode == SELECT_FILE){
                Uri selectImageUri = data.getData();
                dynamicImageView.setImageURI(selectImageUri);
                isUploadPhotoSelected = true;
                try
                {
                    picture = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.JPEG, 90, byteStream);
                    String base64Data = Base64.encodeToString(byteStream.toByteArray(),
                            Base64.URL_SAFE);

                    imageUrlString = base64Data;

                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }
            Drawable verticalImage = new BitmapDrawable(getResources(),picture);
            dynamicImageView.setImageDrawable(verticalImage);
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

    private void searchTextAtGoogle(String txtToSearch)
    {
        String responseMessage;
        InputMethodManager inputManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        try {
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

}