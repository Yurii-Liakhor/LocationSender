package com.example.runtracker;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Yurii on 13.10.2016.
 */

public class PostRequest {
    private static final String TAG = "PostRequest";

    public void postSender(double lat, double lng){
        String latitude = Double.toString(lat);
        String longitude = Double.toString(lng);
        String response = "";
        try{
            URL url = new URL("http://kpi-krok.esy.es/post.php");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            String urlParameters = "lat=" + latitude + "&lng=" + longitude;
            Log.i(TAG, urlParameters);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode=connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
                Log.i(TAG, response);
            }
            else {
                response="";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
