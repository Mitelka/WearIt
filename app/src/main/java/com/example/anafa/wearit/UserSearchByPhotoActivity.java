package com.example.anafa.wearit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

public class UserSearchByPhotoActivity extends AppCompatActivity {

    ImageView dynamicImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_by_photo);

        RadioButton cameraRadioButton = (RadioButton) findViewById(R.id.takePictureCheckBox);
        dynamicImageView = (ImageView) findViewById(R.id.dynamicPhotoImageView);

        cameraRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");

        dynamicImageView.setImageBitmap(bitmap);
    }


}
