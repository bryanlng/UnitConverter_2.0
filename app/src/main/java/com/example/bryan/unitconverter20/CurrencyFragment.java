package com.example.bryan.unitconverter20;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class CurrencyFragment extends Fragment {
    private static final String TAG = "UnitConverterTag";

    private EditText textA;
    private Spinner optionSpinnerA;

    private EditText textB;
    private Spinner optionSpinnerB;

    private Button compute_button;
    private Button clearA;
    private Button clearB;

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

    private HashMap<String, Double> currencies;
    private String current_base = "";
    private long lastTimeUpdated = 0;

    //Macros to replace Magic numbers
    public static final long TWELVE_HOURS_IN_MILLIS = 43200000;  //12*60*60*100

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

        currencies = new HashMap<String, Double>();

        //Initialize EditTexts
        textA = (EditText) rootView.findViewById(R.id.currency_optionAText);
        textB = (EditText) rootView.findViewById(R.id.currency_optionBText);

        //First check if there was a configuration change. If there was, restore values
        Bundle utilitiesBundle = Utilities.getBundleFromCurrencyFragment();
        if( savedInstanceState != null || utilitiesBundle != null) {
            //In the case where a fragment change back to UnitFragment, meaning that values are not null, but savedInstanceState == null

            //Case 1: Fragment change, but there was no configuration change. Thus, grab saved state from Utilities
            if (savedInstanceState == null && utilitiesBundle != null) {
                //get the bundle from the Utilities class ==> make it to be savedInstanceState
                Log.i(TAG, "savedInstanceState == null, so we must get from Utilities, then restore");
                savedInstanceState = utilitiesBundle;
            }

            //Case 2: Configuration change, no fragment change.
            else {
                Log.i(TAG, "savedInstanceState != null, not getting from Utilities, so we must restore");
            }
            currentOptionA = savedInstanceState.getString("currentOptionA");
            currentOptionB = savedInstanceState.getString("currentOptionB");
            optionACurrentSelection = savedInstanceState.getInt("optionACurrentSelection");
            optionBCurrentSelection = savedInstanceState.getInt("optionBCurrentSelection");

            //Set the text that was previously floating on the EditTexts
            textA.setText(savedInstanceState.getString("currentOptionAText"));
            textB.setText(savedInstanceState.getString("currentOptionBText"));

            //Set Currency Fragment stuff
            current_base = savedInstanceState.getString("current_base");
            textAeditingNow = savedInstanceState.getBoolean("textAeditingNow");
            textBeditingNow = savedInstanceState.getBoolean("textBeditingNow");
            lastTimeUpdated = savedInstanceState.getLong("lastTimeUpdated");

            //Restore previous state of hashmap
            String[] keys = savedInstanceState.getStringArray("keys");
            double[] values = savedInstanceState.getDoubleArray("values");

            for(int i = 0; i < keys.length; i++){
                currencies.put(keys[i],values[i]);
            }
        }

        printHashMap();

        //Initialize Spinners
        optionSpinnerA = (Spinner) rootView.findViewById(R.id.currency_optionA);
        optionSpinnerB = (Spinner) rootView.findViewById(R.id.currency_optionB);

        //Bind Spinners to an Array, then set its default item to be the first item
        //1. optionA Spinner
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                getActivity().getApplicationContext(), R.layout.dropdown_item, getResources().getStringArray(R.array.currencies_all));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionSpinnerA.setAdapter(adapter1);
        optionSpinnerA.setSelection(optionACurrentSelection);   //default = 0, the first item
        currentOptionA = "USD";

        //2. optionB Spinner
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                getActivity().getApplicationContext(), R.layout.dropdown_item, getResources().getStringArray(R.array.currencies_all));
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionSpinnerB.setAdapter(adapter2);
        optionSpinnerB.setSelection(optionBCurrentSelection);   //default = 0, the first item
        currentOptionB = "USD";

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

        //Compute Button.
        //Decided not to use TextWatchers, because 1) Networks are unreliable, 2) Async task breaks our logic
        compute_button = (Button) rootView.findViewById(R.id.compute_button);
        compute_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compute(v);
            }
        });

        //Clear buttons
        clearA = (Button) rootView.findViewById(R.id.clearA);
        clearB = (Button) rootView.findViewById(R.id.clearB);

        clearA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textA.setText("");
            }
        });

        clearB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textB.setText("");
            }
        });




//        retrieveAndCompute("optionA");



        return rootView;
//        return inflater.inflate(R.layout.currency_fragment_layout, container, false);
    }

    /*
           General method to convert stuff
           Cases to check for:
            1. If hashmap is empty
                -Means we need to update, then also compute
            2. If starting currency == base currency
                A) If we haven't updated in more than 12 hours
                    -Means we need to update, then also compute
                B) Else
                    -We currently have the most updated data
                    -Thus, no need to update. Just compute
            3. If ending currency == base currency
                A) If we haven't updated in more than 12 hours
                    -Means we need to update, then also compute
                B) Else
                    -We currently have the most updated data
                    -Thus, no need to update. Compute the inverse.
                    Ex:
                        -Trying to go from HKD --> USD.
                        -We have all the data for USD --> other currencies
                        -Thus, we need to find the USD mapping, then reverse it
                        -Ex: Convert 700 HKD --> ?? USD
                        -1 USD = 7.7624 HKD
                        -Thus, do 700 / 7.7624 ==> 90.178
            4. Else
                -We don't currently have the data for the currencies we want to convert
                -Ex:
                    -Have USD --> other currencies
                    -But want to compute HKD --> JPY
                -Thus, we need to update, then also compute

     */
    public void compute(View v){
        String optionA = textA.getText().toString();
        String optionB = textB.getText().toString();

        //If the categories are the same, prompt user to change them
        if(currentOptionA.equals(currentOptionB)){
            showAlertDialog(getResources().getString(R.string.same_category),v);
        }
        else{
            //Case 1: EditText A filled with text, EditText B empty
            if(!optionA.equals("") && optionB.equals("")){
                if(currencies.isEmpty()){
                    retrieveAndCompute("optionA", currentOptionA);
                }
                else if(current_base.equals(currentOptionA)){
                    long currentTime = System.currentTimeMillis();
                    if(hasTwelveHoursElapsed(lastTimeUpdated,currentTime)){
                        retrieveAndCompute("optionA", currentOptionA);
                    }

                    else{
                        Double new_currency_multiplier = convert_from("optionA");
                        Double curr_value = Double.parseDouble(textA.getText().toString());
                        String result = truncateToNDecimalPlaces(curr_value * new_currency_multiplier, 2);
                        textB.setText(result);

                        //Update fields
                        currentOptionBText = result;
                        currentOptionAText = textA.getText().toString();
                    }
                }
                else if(current_base.equals(currentOptionB)){
                    long currentTime = System.currentTimeMillis();
                    if(hasTwelveHoursElapsed(lastTimeUpdated,currentTime)){
                        retrieveAndCompute("optionA", currentOptionA);
                    }

                    else{
                        Double new_currency_multiplier = convert_from("optionA");
                        Double curr_value = Double.parseDouble(textA.getText().toString());
                        String result = truncateToNDecimalPlaces(curr_value / new_currency_multiplier, 2);
                        textB.setText(result);

                        //Update fields
                        currentOptionBText = result;
                        currentOptionAText = textA.getText().toString();
                    }
                }
                else{
                    retrieveAndCompute("optionA", currentOptionA);
                }
            }

            //Case 2: EditText B filled with text, EditText A empty
            //Same code as above, except we do it to EditText A
            else if(!optionB.equals("") && optionA.equals("")){
                if(currencies.isEmpty()){
                    retrieveAndCompute("optionB", currentOptionB);
                }
                else if(current_base.equals(currentOptionB)){
                    long currentTime = System.currentTimeMillis();
                    if(hasTwelveHoursElapsed(lastTimeUpdated,currentTime)){
                        retrieveAndCompute("optionB", currentOptionB);
                    }

                    else{
                        Double new_currency_multiplier = convert_from("optionB");
                        Double curr_value = Double.parseDouble(textB.getText().toString());
                        String result = truncateToNDecimalPlaces(curr_value * new_currency_multiplier, 2);
                        textA.setText(result);

                        //Update fields
                        currentOptionAText = result;
                        currentOptionBText = textB.getText().toString();
                    }
                }
                else if(current_base.equals(currentOptionA)){
                    long currentTime = System.currentTimeMillis();
                    if(hasTwelveHoursElapsed(lastTimeUpdated,currentTime)){
                        retrieveAndCompute("optionB", currentOptionB);
                    }

                    else{
                        Double new_currency_multiplier = convert_from("optionB");
                        Double curr_value = Double.parseDouble(textB.getText().toString());
                        String result = truncateToNDecimalPlaces(curr_value / new_currency_multiplier, 2);
                        textA.setText(result);

                        //Update fields
                        currentOptionAText = result;
                        currentOptionBText = textB.getText().toString();
                    }
                }
                else{
                    retrieveAndCompute("optionB", currentOptionB);
                }
            }

            //Bad Cases,
            else{
                //where both EditTexts are empty
                if(optionA.equals("") && optionB.equals("")){
                    showAlertDialog(getResources().getString(R.string.no_text_entered),v);
                }

                //where both are full
                else if(!optionA.equals("") && !optionB.equals("")){
                    showAlertDialog(getResources().getString(R.string.both_entered),v);
                }



            }
        }

    }

    /*
         Pulls the latest values, puts them into the HashMap,
         does the computation (by calling compute), then sets
         the appropriate textfields
     */
    public void retrieveAndCompute(String whichOption, String startingCurrency){
        Log.i(TAG, "entered retrieveAndCompute");
        Log.i(TAG, "retrieveAndCompute, current thread: " + Thread.currentThread().getName());
        long start = System.currentTimeMillis();

        currencies.clear();     //Flush our HashMap, so that we don't have any of the old values

        DataRetriever k = new DataRetriever();
        k.execute(whichOption,startingCurrency);
//        startActivity(new Intent(CurrencyFragment.this.getActivity(),
//                DataRetriever.class));
    }

    /*
           Given which textField to convert from,
           finds the equivalent currency
           Ex: optionA = USD, optionB = HKD
           If we were given optionA, then we would convert from USD --> HKD

           Precondition: Values have been updated and are in the Hashmap

           Only take the first 3 letters
           Ex: USD (US Dollar) --> take the "USD" part of it
     */
    public double convert_from(String whichOption){
        if(whichOption.equals("optionA"))
            return currencies.get(currentOptionB.substring(0,3));
        else{
            return currencies.get(currentOptionA.substring(0,3));
        }
    }

    /*
        Things to save:
        1. currentOptionA, currentOptionAText, currentOptionB, currentOptionBText
        2. textAeditingNow, textBeditingNow, current_base, lastTimeUpdated, currencies
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putString("currentOptionA", currentOptionA);
        savedInstanceState.putString("currentOptionAText" ,currentOptionAText);
        savedInstanceState.putString("currentOptionB", currentOptionB);
        savedInstanceState.putString("currentOptionBText" ,currentOptionBText);
        savedInstanceState.putInt("optionACurrentSelection", optionACurrentSelection);
        savedInstanceState.putInt("optionBCurrentSelection", optionBCurrentSelection);

        savedInstanceState.putString("current_base" ,current_base);
        savedInstanceState.putBoolean("textAeditingNow", textAeditingNow);
        savedInstanceState.putBoolean("textBeditingNow",textBeditingNow);
        savedInstanceState.putLong("lastTimeUpdated", lastTimeUpdated);

        //Putting data from curriences HashMap into
        //Save keys into string array, values into double array
        //Indexes are pairs. Ex: (string array[0], double array[0]) is a pair
        //http://stackoverflow.com/questions/46898/how-to-efficiently-iterate-over-each-entry-in-a-map
        String[] keys = new String[currencies.keySet().size()];
        double[] values = new double[currencies.values().size()];
        int i = 0;
        if(!currencies.isEmpty()){
            for(Map.Entry<String, Double> entry : currencies.entrySet()){
                Log.i(TAG, "(" + entry.getKey() + "," + entry.getValue() + ")");
                keys[i] = entry.getKey();
                values[i] = entry.getValue();
                i++;
            }
            Log.i(TAG, "Size of hashmap is " + currencies.size() + ", should be 30");

        }

        savedInstanceState.putStringArray("keys", keys);
        savedInstanceState.putDoubleArray("values", values);

        //Create a copy of the bundle to be put inside Utilties
        Utilities.setBundleFromCurrencyFragment(savedInstanceState);

        //Lastly, call the parent class's equivalent method
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onPause(){
        Bundle onPauseBundle = new Bundle();
        //Save stuff here
        //Basically do what we did in onSaveInstanceState

        onPauseBundle.putString("currentOptionA", currentOptionA);
        onPauseBundle.putString("currentOptionAText" ,currentOptionAText);
        onPauseBundle.putString("currentOptionB", currentOptionB);
        onPauseBundle.putString("currentOptionBText" ,currentOptionBText);
        onPauseBundle.putInt("optionACurrentSelection", optionACurrentSelection);
        onPauseBundle.putInt("optionBCurrentSelection", optionBCurrentSelection);

        onPauseBundle.putString("current_base" ,current_base);
        onPauseBundle.putBoolean("textAeditingNow", textAeditingNow);
        onPauseBundle.putBoolean("textBeditingNow",textBeditingNow);
        onPauseBundle.putLong("lastTimeUpdated", lastTimeUpdated);

        //Putting data from curriences HashMap into
        //Save keys into string array, values into double array
        //Indexes are pairs. Ex: (string array[0], double array[0]) is a pair
        //http://stackoverflow.com/questions/46898/how-to-efficiently-iterate-over-each-entry-in-a-map
        String[] keys = new String[currencies.keySet().size()];
        double[] values = new double[currencies.values().size()];
        int i = 0;
        if(!currencies.isEmpty()){
            for(Map.Entry<String, Double> entry : currencies.entrySet()){
                Log.i(TAG, "(" + entry.getKey() + "," + entry.getValue() + ")");
                keys[i] = entry.getKey();
                values[i] = entry.getValue();
            }
            Log.i(TAG, "Size of hashmap is " + currencies.size() + ", should be 30");
        }

        onPauseBundle.putStringArray("keys", keys);
        onPauseBundle.putDoubleArray("values", values);

        //Create a copy of the bundle to be put inside Utilties
        Utilities.setBundleFromCurrencyFragment(onPauseBundle);

        //Lastly, call the parent class's equivalent method
        super.onSaveInstanceState(onPauseBundle);

        super.onPause();
    }

    public boolean hasTwelveHoursElapsed(long start, long end){
        long day = TWELVE_HOURS_IN_MILLIS;
        if(end - start >= day)
            return true;
        return false;
    }

    /*
        http://stackoverflow.com/questions/153724/how-to-round-a-number-to-n-decimal-places-in-java
        Truncates the number to n decimal places
     */
    public String truncateToNDecimalPlaces(double val, int n){
        //Create the string to be used in decimal format
        StringBuilder base = new StringBuilder("#.");
        for(int i = 0; i < n; i++){
            base.append("#");
        }

        //Operation
        DecimalFormat df = new DecimalFormat(base.toString());
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(val);
    }

    public View getCurrentView(){
        return currentView;
    }

    public void printHashMap(){
        //http://stackoverflow.com/questions/46898/how-to-efficiently-iterate-over-each-entry-in-a-map
        if(!currencies.isEmpty()){
            for(Map.Entry<String, Double> entry : currencies.entrySet()){
                Log.i(TAG, "(" + entry.getKey() + "," + entry.getValue() + ")");
            }
            Log.i(TAG, "Size of hashmap is " + currencies.size() + ", should be 30");
        }

        else{
            Log.i(TAG, "Size of hashmap is 0");
        }

    }

    /*
      Upon an error, pop this up to alert the user that they
      tried to do something bad
     */
    public void showAlertDialog(String message, View v){
        //pop open a window saying that you need to remove the thing
        Log.i(TAG, "showAlertDialog with message: " + message);
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext()); //getActivity().getApplicationContext());
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do things
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Created by Bryan on 12/17/2016.
     * http://stackoverflow.com/questions/29465996/how-to-get-json-object-using-httpurlconnection-instead-of-volley
     */

    public class DataRetriever extends AsyncTask<String, Void, String> {
        private static final String TAG = "UnitConverterTag";
        private static final String EXAMPLE = "http://api.androidhive.info/contacts/";
        private static final String LATEST_URL = "http://api.fixer.io/latest";
        private static final String BASE_URL = "http://api.fixer.io/latest?base=";
        private static final String SYMBOLS_URL = "http://api.fixer.io/latest?symbols=USD,GBP";
        private HttpURLConnection urlConnection;

//    HashMap<String, Double>

        @Override
        protected String doInBackground (String... params){
            Log.i(TAG, "doInBackground, current thread: " + Thread.currentThread().getName());
            Log.i(TAG, "DataRetriever doInBackground(), params: " + params[0] + "," + params[1]);
            StringBuilder builder = new StringBuilder();
            try{

                //1. Establish connection, put URL's inputstream --> BufferedReader
                URL url = new URL(BASE_URL + params[1].substring(0,3));     //only take first 3 letters
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                //2. Dump stuff from BufferedReader --> StringBuilder, so we can get it into a string
                String line;
                while((line = reader.readLine()) != null){
                    Log.i(TAG,"line: " + line);
                    builder.append(line);
                }

                //3. Parse JSONObjects from the string
                //Ex: {"base":"EUR","date":"2016-12-16","rates":{"AUD":1.4239,"BGN":1.9558,"BRL":3.5081,"CAD":1.396,"CHF":1.0751,"CNY":7.2635,"CZK":27.021,"DKK":7.434,"GBP":0.8389,"HKD":8.1032,"HRK":7.536,"HUF":312.45,"IDR":13977.82,"ILS":4.0425,"INR":70.7765,"JPY":123.38,"KRW":1239.53,"MXN":21.2528,"MYR":4.6715,"NOK":9.0623,"NZD":1.4892,"PHP":52.193,"PLN":4.42,"RON":4.5165,"RUB":64.3017,"SEK":9.789,"SGD":1.5065,"THB":37.413,"TRY":3.6601,"USD":1.0439,"ZAR":14.5876}}
                //See http://api.fixer.io/latest
                JSONObject start = new JSONObject(builder.toString());

                String base = start.getString("base");
                current_base = base;
                String date = start.getString("date");

                JSONObject rates = start.getJSONObject("rates");
                String[] curr = getResources().getStringArray(R.array.currencies_all);
                for(int i = 0; i < curr.length; i++){
                    String currency = curr[i].substring(0,3);
                    if(!currency.equals(base)){
                        //because we don't want to get the base currency, b/c it'll just be 1
                        Double worth = rates.getDouble(currency);
                        currencies.put(currency, worth);
                    }
                }
//                printHashMap();     //debug

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
            catch (JSONException e) {
                Log.i(TAG, "JSONException: " + e.getMessage());
                e.printStackTrace();
            }
//            catch(Exception e){
//                Log.i(TAG, "Exception e: " + e.getMessage());
//                e.printStackTrace();
//            }

            return params[0];

        }

        @Override
        protected void onPostExecute(String paramFromDoInBackground){
            Log.i(TAG, "onPostExecute output: " + paramFromDoInBackground);
            //4. Perform conversion, then fill in values for the EditTexts, ListView values
            //Have to set text here, because you can only touch Views in the main Thread
            double new_currency_multiplier = convert_from(paramFromDoInBackground);
            double curr_value = 0.0;
            String result = "";

            //5. Set EditTexts
            //Case 1: Have text in textA, want a result in textB
            if(paramFromDoInBackground.equals("optionA")){
                curr_value = Double.parseDouble(textA.getText().toString());
                result = truncateToNDecimalPlaces(curr_value * new_currency_multiplier, 2);
                textB.setText(result);

                //Update fields
                currentOptionBText = result;
                currentOptionAText = textA.getText().toString();
            }

            //Case 2: Have text in textB, want a result in textA
            else{
                curr_value = Double.parseDouble(textB.getText().toString());
                result = truncateToNDecimalPlaces(curr_value * new_currency_multiplier, 2);
                textA.setText(result);

                //Update fields
                currentOptionAText = result;
                currentOptionBText = textB.getText().toString();
            }

            //5. Update the time that we last updated to be NOW
            lastTimeUpdated = System.currentTimeMillis();
            printHashMap();
        }
    }

}
