package com.example.apitests;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";

    private final static int PICK_IMAGE_REQUEST = 1;
    private static final String SERVER_ADDRESS = "http://image-processing-flask-api.herokuapp.com";
    private static final int REQUEST_CODE = 10;

    private Button mButtonChooseImage, mButtonUploadImage;
    protected ImageView mImageView;
    private Uri mImageUri;
    protected ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.image_view);
        mImageView.setImageResource(android.R.color.transparent);

        // Preview image and send it to server
        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUploadImage = findViewById(R.id.button_upload_image);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        mButtonUploadImage.setVisibility(View.GONE);
        doRequestPermissions();

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

                Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();

                UploadImage uploadImageTask = new UploadImage(mImageUri, bitmap, MainActivity.this);
                uploadImageTask.execute();
            }
        });
    }

    private void doRequestPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(this, "Provide permissions!", Toast.LENGTH_SHORT).show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            mButtonUploadImage.setVisibility(View.VISIBLE);
        }
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
            mImageUri = data.getData();
            mImageView.setImageURI(mImageUri);
        }
        else {
            Log.d("Image loading", "Failed!");
            Toast.makeText(this, "Image loading failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay!

                mButtonUploadImage.setVisibility(View.VISIBLE);
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                mButtonUploadImage.setVisibility(View.GONE);
            }
        }
    }

    public void displayToast() {
        Toast.makeText(this, "Received ", Toast.LENGTH_LONG).show();
    }
}
