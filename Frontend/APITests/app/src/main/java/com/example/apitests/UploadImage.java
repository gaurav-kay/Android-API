package com.example.apitests;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Base64;
import org.apache.http.*;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UploadImage extends AsyncTask<Void, Void, Void> {

    private Bitmap imageBitmap;
    private static final String SERVER_ADDRESS = "http://127.0.0.1:8000";

    public UploadImage(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        List<Settings.NameValueTable>

        try {
            URL url = new URL(SERVER_ADDRESS + "/post");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            OutputStream outputStream = new BufferedOutputStream(httpURLConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_16));
            writer.write(encodedImage);
            writer.flush();
            writer.close();
            outputStream.close();

            httpURLConnection.connect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }


}
