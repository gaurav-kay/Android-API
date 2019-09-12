////package com.example.apitests;
////
////import android.graphics.Bitmap;
////import android.os.AsyncTask;
////import android.provider.Settings;
////import android.util.Base64;
////
////import com.android.volley.AuthFailureError;
////import com.android.volley.Request;
////import com.android.volley.RequestQueue;
////import com.android.volley.Response;
////import com.android.volley.VolleyError;
////import com.android.volley.toolbox.StringRequest;
////import com.android.volley.toolbox.Volley;
////
////import org.apache.http.*;
////
////import java.io.BufferedOutputStream;
////import java.io.BufferedWriter;
////import java.io.ByteArrayOutputStream;
////import java.io.IOException;
////import java.io.OutputStream;
////import java.io.OutputStreamWriter;
////import java.lang.reflect.Array;
////import java.net.HttpURLConnection;
////import java.net.MalformedURLException;
////import java.net.URL;
////import java.nio.charset.StandardCharsets;
////import java.util.ArrayList;
////import java.util.HashMap;
////import java.util.List;
////import java.util.Map;
////
////public class UploadImage extends AsyncTask<Void, Void, Void> {
////
////    private Bitmap imageBitmap;
////    private static final String SERVER_ADDRESS = "http://127.0.0.1:8000";
////
////    public UploadImage(Bitmap imageBitmap) {
////        this.imageBitmap = imageBitmap;
////    }
////
////    @Override
////    protected Void doInBackground(Void... voids) {
////        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
////        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
////        final String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
////
////        StringRequest request = new StringRequest(Request.Method.POST, SERVER_ADDRESS, new Response.Listener<String>() {
////            @Override
////            public void onResponse(String response) {
////
////            }
////        }, new Response.ErrorListener() {
////            @Override
////            public void onErrorResponse(VolleyError error) {
////
////            }
////        }) {
////            @Override
////            protected Map<String, String> getParams() throws AuthFailureError {
////                Map<String, String> params = new HashMap<>();
////                params.put("image", encodedImage);
////
////                return params;
////            }
////
////        };
////        return null;
////    }
////
////    @Override
////    protected void onPostExecute(Void aVoid) {
////        super.onPostExecute(aVoid);
////    }
////
////
////}
//package com.example.apitests;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.util.LruCache;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.Volley;
//
//public class UploadImage {
//    private static UploadImage instance;
//    private RequestQueue requestQueue;
//    private ImageLoader imageLoader;
//    private static Context ctx;
//
//    private UploadImage(Context context) {
//        ctx = context;
//        requestQueue = getRequestQueue();
//
//        imageLoader = new ImageLoader(requestQueue,
//                new ImageLoader.ImageCache() {
//                    private final LruCache<String, Bitmap>
//                            cache = new LruCache<String, Bitmap>(20);
//
//                    @Override
//                    public Bitmap getBitmap(String url) {
//                        return cache.get(url);
//                    }
//
//                    @Override
//                    public void putBitmap(String url, Bitmap bitmap) {
//                        cache.put(url, bitmap);
//                    }
//                });
//    }
//
//    public static synchronized UploadImage getInstance(Context context) {
//        if (instance == null) {
//            instance = new UploadImage(context);
//        }
//        return instance;
//    }
//
//    public RequestQueue getRequestQueue() {
//        if (requestQueue == null) {
//            // getApplicationContext() is key, it keeps you from leaking the
//            // Activity or BroadcastReceiver if someone passes one in.
//            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
//        }
//        return requestQueue;
//    }
//
//    public <T> void addToRequestQueue(Request<T> req) {
//        getRequestQueue().add(req);
//    }
//
//    public ImageLoader getImageLoader() {
//        return imageLoader;
//    }
//}

package com.example.apitests;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadImage extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "TAG";

    private Uri mImageUri;
    private File mFile;
    private OkHttpClient client;
    private Bitmap bitmap;

    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("image/jpeg");
    private static final String SERVER_ADDRESS = "http://image-processing-flask-api.herokuapp.com";

    UploadImage(Uri imageUri, Bitmap bitmap) {
        this.mImageUri = imageUri;
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(1);
        this.client = new OkHttpClient.Builder()
                            .dispatcher(dispatcher)
                            .build();
        this.mFile = null;
        this.bitmap = bitmap;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        File filePath = Environment.getExternalStorageDirectory();
        File directory = new File(filePath.getAbsolutePath());
        directory.mkdir();
        File file = new File(directory, "tempFile.jpeg");

        Log.d(TAG, "doInBackground: HELLO");
        Log.d(TAG, "doInBackground: " + file);
        
        try {
            Log.d(TAG, "doInBackground: OKAY");
            
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            this.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();

            this.mFile = file;
            Log.d(TAG, "doInBackground: " + this.mFile);

            Log.d(TAG, "doInBackground: written");
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: ERROR", e);
            e.printStackTrace();
        }

//        Request request = new Request.Builder()
//                .url(SERVER_ADDRESS)
//                .post(RequestBody.create(file, MEDIA_TYPE_MARKDOWN))
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//            Log.d(TAG, "run: " + response);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", this.mFile.getName(), RequestBody.create(MEDIA_TYPE_MARKDOWN, this.mFile))
                .build();

        Log.d(TAG, "doInBackground: Body built");

        Request request = new Request.Builder()
                .addHeader("Connection", "keep-alive")
                .url(SERVER_ADDRESS)
                .get()
                .build();

        Log.d(TAG, "doInBackground: SENT REQUEST");

        try {
            this.client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d(TAG, "onFailure: ON FAILURE");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Log.d(TAG, "onResponse: THIS AINT SUCCESSFUL" + response.toString());
                        return;
                    }
                    Log.d(TAG, "onResponse: response is " + response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//            Log.d(TAG, "doInBackground: i be done");
//            System.out.println(response.body().string());
//
//            boolean garbage;
//            garbage = this.mFile.delete();
//            garbage = file.delete();
//            garbage = filePath.delete();
//            garbage = directory.delete();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//
//            boolean garbage;
//            garbage = this.mFile.delete();
//            garbage = file.delete();
//            garbage = filePath.delete();
//            garbage = directory.delete();
//        }

        return null;
    }
}