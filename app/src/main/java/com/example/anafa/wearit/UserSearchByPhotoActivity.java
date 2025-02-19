package com.example.anafa.wearit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TabHost;
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
    TabHost tabHost;
    private boolean uploadedImage = false;
    private static final String LOG_TEXT_KEY = "LOG_TEXT_KEY";
    Boolean isUploadPhotoSelected = false;
    String imageUrlString;
    private GoogleAnalysisImage googleAnalysisImage;
    private PropertyReader propertyReader;
    UI ui = new UI();
    private View progressbar;
    private Bitmap bitmap;

    public static final int List_View_Type = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_by_photo);

        RadioButton cameraRadioButton = findViewById(R.id.takePictureCheckBox);
        RadioButton openPhotoFromFileRadioButton = findViewById(R.id.uploadImageCheckBox);
        dynamicImageView = findViewById(R.id.dynamicPhotoImageView);
        Button searchImageOnGoogle = findViewById(R.id.searchButton);
        propertyReader = new PropertyReader(getBaseContext());
        final String stringApiKeyForAnalyse = propertyReader.getProperties().getProperty("stringApiKeyForAnalyse");

        tabHost = findViewById(R.id.tabHost);
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

        final Context context = this;

        progressbar = findViewById(R.id.progressBar2);
        progressbar.setVisibility(View.INVISIBLE);

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
        searchImageOnGoogle.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if(uploadedImage) {
                    new UseAsyncForTask().execute();

                }
                else
                {
                    Toast.makeText(UserSearchByPhotoActivity.this, "You didn't selected an image to search", Toast.LENGTH_LONG).show();
                }
                
            }

            class UseAsyncForTask extends AsyncTask<String, Void, String>
            {
                @Override
                protected void onPreExecute()
                {
                    progressbar.setVisibility(View.VISIBLE);

                }

                @Override
                protected String doInBackground(String... params) {

                    googleAnalysisImage = new GoogleAnalysisImage(imageUrlString, stringApiKeyForAnalyse);

                    String Response = googleAnalysisImage.imageAnalyzeRequest();
                    String toSearch = AnalysisResponse(Response);
                    return toSearch;
                }

                @Override
                protected void onPostExecute(String result)
                {
                    searchTextAtGoogle(result);

                    tabHost.setVisibility(View.VISIBLE);
                    progressbar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private String AnalysisResponse(String response) {
        String to_Search = EMPTY_STRING;
        String bestScore;
        String secondScore;
        String bestGuessLabels;
        List<String> descriptionList = new ArrayList<>();

        try
        {
            JSONObject jsonToAnalysis = new JSONObject(response);
            JSONArray responses;
            responses = jsonToAnalysis.getJSONArray("responses");

            JSONObject respond = responses.getJSONObject(0);
            JSONObject webDetection = respond.getJSONObject("webDetection");
            JSONArray webEntities = webDetection.getJSONArray("webEntities");

            for (int index = 0; index < webEntities.length(); index++)
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

        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == REQUEST_CAMERA)
            {
                bitmap = (Bitmap) data.getExtras().get("data");
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri selectImageUri = takePicture.getData();
                dynamicImageView.setImageBitmap(bitmap);
                dynamicImageView.setImageURI(selectImageUri);
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteStream);
                String base64Data = Base64.encodeToString(byteStream.toByteArray(),
                        Base64.URL_SAFE);

                imageUrlString = base64Data;

                setBitmap();
            }
            else if(requestCode == SELECT_FILE)
            {
                Uri selectImageUri = data.getData();
                dynamicImageView.setImageURI(selectImageUri);
                isUploadPhotoSelected = true;
                try
                {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteStream);
                    String base64Data = Base64.encodeToString(byteStream.toByteArray(),
                            Base64.URL_SAFE);

                    imageUrlString = base64Data;
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                setBitmap();
            }
        }
    }

    private void setBitmap() {
        Drawable verticalImage = new BitmapDrawable(getResources(), bitmap);
        dynamicImageView.setImageDrawable(verticalImage);
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
        InputMethodManager inputManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        try
        {

            // Get results item list from server
            ArrayList itemListsToShow = ui.genericSearchByText(txtToSearch, propertyReader);
            ArrayList itemListToShowByPrice = (ArrayList) itemListsToShow.get(0);
            ArrayList itemListToShowByStars = (ArrayList) itemListsToShow.get(1);

            showResultsOfSearch(itemListToShowByPrice, itemListToShowByStars);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showResultsOfSearch(ArrayList itemListToShowByPrice, ArrayList itemListToShowByStars) {
        ListView priceListView = findViewById(R.id.ResultsByPriceListView);
        ListView starsListView = findViewById(R.id.ResultsByStarsListView);

        //type=2-->ListView
        // Sorted by price
        UI.showResults(priceListView, this, itemListToShowByPrice, R.layout.content_list_view_results, List_View_Type);
        addClickListener(priceListView, itemListToShowByPrice);

        // Sorted by stars
        UI.showResults(starsListView, this, itemListToShowByStars, R.layout.content_list_view_results, List_View_Type);
        addClickListener(starsListView, itemListToShowByStars);
    }

    private void addClickListener(ListView listView, final ArrayList itemListToShow)
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Item currentItem = (Item) itemListToShow.get(position);
                UI.createJsonAndSendForHistory(currentItem);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentItem.getItemListLink()));
                startActivity(intent);
            }
        });
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