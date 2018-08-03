package com.example.anafa.wearit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

public class UserSearchByPhotoActivity extends AppCompatActivity {

    private static final Integer REQUEST_CAMERA = 0;
    private static final Integer SELECT_FILE = 1;
    ImageView dynamicImageView;
    private boolean uploadedImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_by_photo);

        RadioButton cameraRadioButton = (RadioButton) findViewById(R.id.takePictureCheckBox);
        RadioButton openPhotoFromFileRadioButton = (RadioButton) findViewById(R.id.uploadImageCheckBox);
        dynamicImageView = (ImageView) findViewById(R.id.dynamicPhotoImageView);
        Button searchImageOnGoogle = (Button)findViewById(R.id.searchButton);

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
}