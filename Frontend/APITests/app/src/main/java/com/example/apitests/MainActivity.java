package com.example.apitests;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final static int PICK_IMAGE_REQUEST = 1;

    private Button mButtonChooseImage, mButtonUploadImage;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.image_view);
        mImageView.setImageResource(android.R.color.transparent);

        // Preview image and send it to server
        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUploadImage = findViewById(R.id.button_upload_image);


        // Button onClicks
        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        mButtonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);

                Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            }
        });
    }

    // Select image driver
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    // Selects image and displays it
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mImageView = findViewById(R.id.image_view);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            Uri mImageUri = data.getData();
            mImageView.setImageURI(mImageUri);
        }
        else {
            Log.d("Image loading", "Failed!");
            Toast.makeText(this, "Image loading failed", Toast.LENGTH_SHORT).show();
        }
    }
}
