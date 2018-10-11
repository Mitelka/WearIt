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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class UserSearchByPhotoActivity extends AppCompatActivity {

    private static final String EMPTY_STRING = "";
    private static final Integer REQUEST_CAMERA = 0;
    private static final Integer SELECT_FILE = 1;
    ImageView dynamicImageView;
    private boolean uploadedImage = false;
    //TODO: mLog - example for save content by inverting phone
    //public TextView mLog;
    private static final String LOG_TEXT_KEY = "LOG_TEXT_KEY";
    Boolean isUploadPhotoSelected = false;
    String imageUrlString;
    private ServerConnector serverConnector;
    private GoogleAnalysisImage googleAnalysisImage;
    private PropertyReader propertyReader;

    public static final int List_View_Type = 2;

    ListView listViewContent;
    // TODO: Delete custom datasorce
    String[] itemNameArr = {"Adidas", "LV"};
    String[] itemPriceArr = {"13.98$", "56.9$"};
    String[] itemLinkArr = {"www.adidas.com", "www.aliexpress/lv.co.il"};
    Integer[] imageIDArr = {R.drawable.adidas_gazelle, R.drawable.wearitphoto};
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
//        mLog = (TextView)findViewById(R.id.tvLog);
//        mLog.setText("");

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

    // Same as at UserSearchByText
    //TODO: don't forget to delete ScrollView if not necessary
    private void showResults() {
        listViewContent = (ListView) findViewById(R.id.ResultsListView);

        CustomListAdapter customListAdapter = new CustomListAdapter(this,
                itemNameArr, itemPriceArr, itemLinkArr, imageIDArr,
                R.layout.content_list_view_results);
        listViewContent.setAdapter(customListAdapter);
    }

    private String AnalysisResponse(String response) {
        String to_Search = EMPTY_STRING;

        String bestScore= EMPTY_STRING;
        String secondScore = EMPTY_STRING;
        String bestGuessLabels;
        List<String> descriptionList = new ArrayList<>();

        try
        {
            JSONObject jsonToAnalysis = new JSONObject(response);
            JSONArray responses;
            responses = jsonToAnalysis.getJSONArray("responses");

            JSONObject respon = responses.getJSONObject(0);
            JSONObject webDetection = respon.getJSONObject("webDetection");
            JSONArray webEntities = webDetection.getJSONArray("webEntities");

            for (int index = 0; index< webEntities.length(); index++)
            {
                if (webEntities.getJSONObject(index).has("description"))
                {
                        descriptionList.add(webEntities.getJSONObject(index).getString("description"));
                }
            }

            bestScore = descriptionList.get(0);
            secondScore = descriptionList.get(1);

            JSONArray bestGuessLabelsObject = webDetection.getJSONArray("bestGuessLabels");
            bestGuessLabels = bestGuessLabelsObject.getJSONObject(0).getString("label");

            to_Search = bestGuessLabels + " " + bestScore  + " " + secondScore;
        }

        catch (JSONException e)
        {
            e.printStackTrace();
            Toast.makeText(UserSearchByPhotoActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

        to_Search = removeDuplicate(to_Search);
        return to_Search;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap picture= null;

        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == REQUEST_CAMERA)
            {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri selectImageUri = takePicture.getData();
                dynamicImageView.setImageBitmap(bitmap);
                dynamicImageView.setImageURI(selectImageUri);
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteStream);
                String base64Data = Base64.encodeToString(byteStream.toByteArray(),
                        Base64.URL_SAFE);

                imageUrlString = base64Data;

                Drawable verticalImage = new BitmapDrawable(getResources(),bitmap);
                dynamicImageView.setImageDrawable(verticalImage);
            }
            else if(requestCode == SELECT_FILE)
            {
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

                Drawable verticalImage = new BitmapDrawable(getResources(),picture);
                dynamicImageView.setImageDrawable(verticalImage);

            }
        }
    }


    // save and restore the text printed at TextView
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        outState.putString(LOG_TEXT_KEY, mLog.getText().toString());
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        mLog.setText(savedInstanceState.getString(LOG_TEXT_KEY));
//    }

    private void searchTextAtGoogle(String txtToSearch)
    {
        String stringApiKey = propertyReader.getProperties().getProperty("StringapiKey");
        String customSearchEngingID = propertyReader.getProperties().getProperty("StringcustomSearchEngineID");
        GoogleSearch googleSearch = new GoogleSearch(stringApiKey, customSearchEngingID );

        String responseMessage;
        InputMethodManager inputManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        try {
            responseMessage = googleSearch.searchAtGoogle(txtToSearch);

            serverConnector = new ServerConnector();
            JSONObject sendTOServer = modifyJsonForServer(responseMessage);

            String ServerResponse = serverConnector.sendRequestToServer(sendTOServer, ServerConnector.RequestType.GoogleSearch);
            //TODO: DO somtehing with the ServerResponseResponse

            showResultsOfSearch();
        }

        catch (Exception e)
        {
            Toast toast = Toast.makeText(this, "Cannot connect googleSearch", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private JSONObject modifyJsonForServer(String responseMessage) throws JSONException {
        String link = EMPTY_STRING;
        String displayLink = EMPTY_STRING;

        JSONArray modifyItemsArray = new JSONArray();
        JSONObject GoogleSearchjson = new JSONObject(responseMessage);
        JSONObject GoogleSearchjsonSendToServer = new JSONObject();
        JSONObject sendTOServer = new JSONObject();
        JSONArray items;
        items = GoogleSearchjson.getJSONArray("items");
        for(int i = 0 ; i< items.length(); i++)
        {
            JSONObject putInItem = new JSONObject();
            if (items.getJSONObject(i).has("link"))
            {
                link = items.getJSONObject(i).getString("link");
                putInItem.put("link", link);
            }
            if (items.getJSONObject(i).has("displayLink"))
            {
                displayLink = items.getJSONObject(i).getString("displayLink");
                putInItem.put("displayLink", displayLink);
            }

            modifyItemsArray.put(putInItem);
        }

        GoogleSearchjsonSendToServer.put("items", modifyItemsArray);

        sendTOServer.put("googleSearchResult", GoogleSearchjsonSendToServer);
        return sendTOServer;
    }

    private void showResultsOfSearch() {
        ListView listView = (ListView) findViewById(R.id.ResultsListView);

        //TODO: Get results item list list from server
        //TODO: DELETE after getting this ArrayList from SERVER

        ArrayList itemList = new ArrayList<>();
        itemList.add(new Item("Adidas", R.drawable.adidas_gazelle, "13.98$", "www.adidas.com", 5));
        itemList.add(new Item("LV", R.drawable.wearitphoto, "56.9$", "www.aliexpress/lv.co.il", 4));

        //type=2-->ListView
        UI.showResults(listView, this, itemList, R.layout.content_list_view_results, List_View_Type);
    }

    public  String removeDuplicate(String input)
    {
        StringBuilder out = new StringBuilder();
        String[] temp = input.split(" ");

        LinkedHashSet<String> lhtemp =
                new LinkedHashSet<>(Arrays.asList(temp));

        //create array from the LinkedHashSet
        temp = lhtemp.toArray(new String[ lhtemp.size() ]);

        for (String string : temp)
        {
            if (out.length() > 0)
            {
                out.append(" ");
            }
            out.append(string);
        }

        return out.toString();
    }

}