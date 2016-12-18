package com.example.bryan.unitconverter20;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Bryan on 12/17/2016.
 * http://stackoverflow.com/questions/29465996/how-to-get-json-object-using-httpurlconnection-instead-of-volley
 */

    public class DataRetriever extends AsyncTask<Void, Void, String> {
        private static final String TAG = "UnitConverterTag";
        private static final String LATEST_URL = "http://api.fixer.io/latest";
        private static final String BASE_URL = "http://api.fixer.io/latest?base=";
        private static final String SYMBOLS_URL = "http://api.fixer.io/latest?symbols=USD,GBP";
        private HttpURLConnection urlConnection;
//    HashMap<String, Double>

        @Override
        protected String doInBackground (Void... params){
            Log.i(TAG, "DataRetriever doInBackground() ");
            StringBuilder builder = new StringBuilder();
            try{
                URL url = new URL(LATEST_URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while((line = reader.readLine()) != null){
                    Log.i(TAG,"line: " + line);
                    builder.append(line);
                }

            }
            catch (MalformedURLException e) {
                Log.i(TAG, "MalformedURLException: " + e.getMessage());
                e.printStackTrace();
            }
            catch (ProtocolException e) {
                Log.i(TAG, "ProtocolException: " + e.getMessage());
                e.printStackTrace();

            }
            catch (IOException e) {
                Log.i(TAG, "IOException: " + e.getMessage());
                e.printStackTrace();
            }
            catch(Exception e){
                Log.i(TAG, "Exception e: " + e.getMessage());
                e.printStackTrace();
            }

            return builder.toString();

        }

        @Override
        protected void onPostExecute(String result){
            Log.i(TAG, "onPostExecute output: " + result);
        }
    }



