package com.example.bryan.unitconverter20;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class CurrencyFragment extends Fragment {
    private static final String TAG = "UnitConverterTag";

    private EditText textA;
    private Spinner optionSpinnerA;

    private EditText textB;
    private Spinner optionSpinnerB;

    private View currentView = null;    //used for getting the currentView, used for showing error messages
    private boolean textAeditingNow = false;    //Used to coordinate access between TextWatchers
    private boolean textBeditingNow = false;

    //Data to store into save instance
    private String currentCategory = "";
    private String currentOptionA = "";
    private String currentOptionAText = "";
    private String currentOptionB = "";
    private String currentOptionBText = "";
    private int optionACurrentSelection = 0;
    private int optionBCurrentSelection = 0;


    public CurrencyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.currency_fragment_layout, container, false);
        currentView = rootView;
        setRetainInstance(true);

        //Initialize EditTexts
        textA = (EditText) rootView.findViewById(R.id.currency_optionAText);
        textB = (EditText) rootView.findViewById(R.id.currency_optionBText);

        //Initialize Spinners
        optionSpinnerA = (Spinner) rootView.findViewById(R.id.currency_optionA);
        optionSpinnerB = (Spinner) rootView.findViewById(R.id.currency_optionB);

        //Bind Spinners to an Array, then set its default item to be the first item
        //1. optionA Spinner
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                getActivity().getApplicationContext(), R.layout.dropdown_item, getResources().getStringArray(R.array.currencies));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionSpinnerA.setAdapter(adapter1);
        optionSpinnerA.setSelection(0);

        //2. optionB Spinner
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                getActivity().getApplicationContext(), R.layout.dropdown_item, getResources().getStringArray(R.array.currencies));
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionSpinnerB.setAdapter(adapter2);
        optionSpinnerB.setSelection(0);

        //Set an setOnItemSelectedListener on each of the Spinners
        //1. optionA Spinner
        optionSpinnerA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Log.i(TAG, "optionA: picked item at position: " + pos);
                currentOptionA = parent.getItemAtPosition(pos).toString();
                optionSpinnerA.setPrompt(currentOptionA);

                optionACurrentSelection = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //2. optionB Spinner
        optionSpinnerB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Log.i(TAG, "optionB picked item at position: " + pos);
                currentOptionB = parent.getItemAtPosition(pos).toString();
                optionSpinnerB.setPrompt(currentOptionB);

                optionBCurrentSelection = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        convert("a");



        return rootView;
//        return inflater.inflate(R.layout.currency_fragment_layout, container, false);
    }

    public void convert(String whichOption){
        Log.i(TAG, "entered convert");
//        DataRetriever k = new DataRetriever();
        new DataRetriever().execute();
//        startActivity(new Intent(CurrencyFragment.this.getActivity(),
//                DataRetriever.class));
    }



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){

    }


    @Override
    public void onPause(){
        super.onPause();
    }

    public View getCurrentView(){
        return currentView;
    }

//    /**
//     * Created by Bryan on 12/17/2016.
//     * http://stackoverflow.com/questions/29465996/how-to-get-json-object-using-httpurlconnection-instead-of-volley
//     */
//    private class DataRetriever extends AsyncTask<Void, Void, String> {
//        private static final String TAG = "UnitConverterTag";
//        private String BASE_URL = "http://api.fixer.io/latest?base=";
//        private String SYMBOLS_URL = "http://api.fixer.io/latest?symbols=USD,GBP";
//        private HttpURLConnection urlConnection;
////    HashMap<String, Double>
//
//        @Override
//        protected String doInBackground (Void... params){
//            Log.i(TAG, "entered doInBackground");
//            Log.i(TAG, "BASE_URL + USD: " + BASE_URL + "USD");
//            StringBuilder builder = new StringBuilder();
//            try{
//                URL url = new URL("https://docs.oracle.com/javase/tutorial/networking/urls/readingWriting.html");
//                urlConnection = (HttpURLConnection) url.openConnection();
//                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//
//                String line;
//                while((line = reader.readLine()) != null){
//                    builder.append(line);
//                }
//
//            }
//            catch(Exception e){
//                e.printStackTrace();
//            }
//            finally {
//                urlConnection.disconnect();
//            }
//
//            Log.i(TAG, "doInBackground output: \n" + builder.toString());
//            return builder.toString();
//
//        }
//
//        @Override
//        protected void onPostExecute(String result){
//            Log.i(TAG, "onPostExecute output: \n" + result);
//        }
//    }

}
