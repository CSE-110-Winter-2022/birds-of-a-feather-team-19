package com.example.birds_of_a_feather_team_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PhotoURL extends AppCompatActivity {
    public String photoURL;
    public ImageView image;
    public Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phote_url);
        image = (ImageView) findViewById(R.id.imageView);
        b1 = (Button) findViewById(R.id.url_btn);

        b1.setOnClickListener(new View.OnClickListener()) {
            @Override
            public void onClick(View v) {
                image.setImageBitmap(getBitmapFromURL(photoURL));
            }
        };
    }

    public void setURL(){
        EditText url = (EditText)findViewById(R.id.photo_url);
        photoURL = "https://photos.google.com/share/AF1QipNWWuE0uHCr_7Ea9AlFLvRkGI6xC0cV9JJuMayWH-wLIVUA-zoxnIX3ECdE-uY1rQ/photo/AF1QipO7GFCGvIPfOO2X_q87yEIcAqGWN77WBkVmCL3o?key=bTJOUnM2UkR2SnVoMzlJV3FFZEhvWlotUHQwcm53";
        //photoURL = url.getText().toString();
    }

    /**public void onSubmitURL(View view) {
        //setURL();
        //Intent intent = new Intent(this, AddClassActivity.class);
        //intent.putExtra("photo_url", photoURL);
        //startActivity(intent);
        String tempURL = "https://photos.google.com/share/AF1QipNWWuE0uHCr_7Ea9AlFLvRkGI6xC0cV9JJuMayWH-wLIVUA-zoxnIX3ECdE-uY1rQ/photo/AF1QipO7GFCGvIPfOO2X_q87yEIcAqGWN77WBkVmCL3o?key=bTJOUnM2UkR2SnVoMzlJV3FFZEhvWlotUHQwcm53";
        image.setImageBitmap(getBitmapFromURL(tempURL));
    }
     **/

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }


    public void onSubmitURL(View view) {

    }
}
/**
class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
 https://photos.google.com/share/AF1QipNWWuE0uHCr_7Ea9AlFLvRkGI6xC0cV9JJuMayWH-wLIVUA-zoxnIX3ECdE-uY1rQ/photo/AF1QipO7GFCGvIPfOO2X_q87yEIcAqGWN77WBkVmCL3o?key=bTJOUnM2UkR2SnVoMzlJV3FFZEhvWlotUHQwcm53
    private String url;
    private ImageView imageView;

    public ImageLoadTask(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        imageView.setImageBitmap(result);
    }

}
 **/