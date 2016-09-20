package com.example.bryan.unitconverter20;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Stack;

/**
 * Created by Bryan on 8/19/2016.
 */
public class UnitFragment extends Fragment {
    private static final String TAG = "UnitConverterTag";
    private TextView displayMessage;
    private Spinner category;

    private EditText textA;
    private Spinner optionSpinnerA;
    private Button clearA;

    private EditText textB;
    private Spinner optionSpinnerB;
    private Button clearB;

    private Button compute;

    //Data to store into save instance
    private String currentCategory = "";
    private String currentOptionA = "";
    private String currentOptionAText = "";
    private String currentOptionB = "";
    private String currentOptionBText = "";

    private boolean onlyUSTimeZones = false;

    private int optionACurrentSelection = 0;
    private int optionBCurrentSelection = 0;
    private int currentArraySize = 0;

    //Booleans for setting custom inputType/ keyboard for hex
    //B/c for hex you need letters
    private boolean optionAHexSet = false;
    private boolean optionBHexSet = false;

    public UnitFragment(){
    }

    /* Alternative constructor for saveInstance

     */
//    public UnitFragment(){
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.unit_fragment_layout, container, false);
        setRetainInstance(true);

        /**********************First check if there was a configuration change. If there was, restore values**************************/
        if(savedInstanceState != null){
            optionACurrentSelection = savedInstanceState.getInt("optionACurrentSelection");
            optionBCurrentSelection = savedInstanceState.getInt("optionBCurrentSelection");
            optionAHexSet = savedInstanceState.getBoolean("optionAHexSet");
            optionBHexSet = savedInstanceState.getBoolean("optionBHexSet");
        }

        Log.i(TAG, "optionACurrentSelection before anything: " + optionACurrentSelection);
        Log.i(TAG, "optionBCurrentSelection before anything: " + optionBCurrentSelection);


        /******************************************Instantiate UI elements of the View************************************************/
        /****Default showing = Category = Weight, EditTexts have value 0, Option A = kg, Option B = lb*******************************/
        //TextView to display the result
//        displayMessage = (TextView) .findViewById(R.id.result_display);

        //Options EditTexts.
        textA = (EditText)rootView.findViewById(R.id.optionAText);
        textB = (EditText)rootView.findViewById(R.id.optionBText);

        //Check to see if hex was set (as values from savedInstanceState might have been brought back)
        //If it is true, set the keyboard to be the regular keyboard with alphabet
        //Else, leave the keyboard the way it is (number keyboard)
        if(optionAHexSet){
            textA.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        if(optionBHexSet){
            textB.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        //Category, optionA, optionB spinners
        category = (Spinner)rootView.findViewById(R.id.category);
        optionSpinnerA = (Spinner)rootView.findViewById(R.id.optionsA);
        optionSpinnerB = (Spinner)rootView.findViewById(R.id.optionsB);
        // Create an Adapter that holds a list of categories
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//                getActivity().getApplicationContext(), R.array.categories, dropdown_item.xml);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity().getApplicationContext(), R.layout.dropdown_item, getResources().getStringArray(R.array.categories));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the Adapter for the spinner
        category.setAdapter(adapter);

        category.setSelection(0);

        // Set an setOnItemSelectedListener on the spinner
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                currentCategory = parent.getItemAtPosition(pos).toString();
                Log.i(TAG,"currentCategory: " + currentCategory);
                Log.i(TAG, "Category's prompt: " + category.getPrompt());

                // Create an Adapter that holds a list of colors, then set the adapter
                switch (currentCategory) {
                    case "Weight":
                        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                                getActivity().getApplicationContext(), R.array.weight, R.layout.dropdown_item);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        optionSpinnerA.setAdapter(adapter2);
                        optionSpinnerB.setAdapter(adapter2);
                        currentArraySize = getResources().getStringArray(R.array.weight).length;
                        break;
                    case "Volume":
                        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
                                getActivity().getApplicationContext(), R.array.volume, R.layout.dropdown_item);
                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        optionSpinnerA.setAdapter(adapter3);
                        optionSpinnerB.setAdapter(adapter3);
                        currentArraySize = getResources().getStringArray(R.array.volume).length;
                        break;
                    case "Distance":
                        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(
                                getActivity().getApplicationContext(), R.array.distance, R.layout.dropdown_item);
                        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        optionSpinnerA.setAdapter(adapter4);
                        optionSpinnerB.setAdapter(adapter4);
                        currentArraySize = getResources().getStringArray(R.array.distance).length;
                        break;
                    case "Temperature":
                        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(
                                getActivity().getApplicationContext(), R.array.temperature, R.layout.dropdown_item);
                        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        optionSpinnerA.setAdapter(adapter5);
                        optionSpinnerB.setAdapter(adapter5);
                        currentArraySize = getResources().getStringArray(R.array.temperature).length;
                        break;
                    case "Programming":
                        ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(
                                getActivity().getApplicationContext(), R.array.programming, R.layout.dropdown_item);
                        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        optionSpinnerA.setAdapter(adapter6);
                        optionSpinnerB.setAdapter(adapter6);
                        currentArraySize = getResources().getStringArray(R.array.programming).length;
                        break;
                    default:
                        break;
                }

                Log.i(TAG, "Current array size of current category " + currentCategory + " is: " + currentArraySize);
                Log.i(TAG, "optionACurrentSelection: " + optionACurrentSelection);
                Log.i(TAG, "optionBCurrentSelection: " + optionBCurrentSelection);

                /*ArrayOutofBoundsException error likely here
                  Aka, if you're in a category where an option has 10 items and let's say you choose index 5,
                  but then switch to another category that has only 3 options,
                  when it tries to restore to an index 5 with only 3 options( index 0 -2)==> outofbounds

                  Solution:
                  Get the current size of the array that we put in
                  If our current selection id >= size of the array ==> set optionACurrentSelection or optionBCurrentSelection back to 0
                  depending on which one (or both) had bad indexes
                  Else, proceed with <spinner>.setSelection(<spinner>CurrentSelection)

                */
                if(optionACurrentSelection >= currentArraySize || optionBCurrentSelection >= currentArraySize){
                    //Only optionsA has bad index
                    if(optionACurrentSelection >= currentArraySize && optionBCurrentSelection < currentArraySize){
                        optionACurrentSelection = 0;
                    }

                    //Only optionsB has bad index
                    else if(optionACurrentSelection < currentArraySize && optionBCurrentSelection >= currentArraySize){
                        optionBCurrentSelection = 0;
                    }

                    //Both have bad indexes
                    else{
                        optionACurrentSelection = 0;
                        optionBCurrentSelection = 0;
                    }

                }


                optionSpinnerA.setSelection(optionACurrentSelection);
                optionSpinnerB.setSelection(optionBCurrentSelection);


            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Make the default category shown at first glance the item at position 0

        // Set an setOnItemSelectedListener on the spinners
        optionSpinnerA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Log.i(TAG, "optionA: picked item at position: " + pos);
                currentOptionA = parent.getItemAtPosition(pos).toString();
                optionSpinnerA.setPrompt(currentOptionA);

                //If it's hex, we need to change the keyboard from number (android:inputType = numberDecimal),
                //to a keyboard with letters
                //http://stackoverflow.com/questions/2586301/set-inputtype-for-an-edittext
                //https://developer.android.com/reference/android/widget/TextView.html#attr_android%3ainputType
                //android:inputType="text" is equivalent to  TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL
                //TYPE_CLASS_TEXT is equivalent to TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL, b/c
                //TYPE_CLASS_TEXT == 0x1 and TYPE_TEXT_VARIATION_NORMAL == 0x0, so a bit or would just be redundant

                if(currentOptionA.equals("Hex")){
                    textA.setInputType(InputType.TYPE_CLASS_TEXT);  //InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL
                    optionAHexSet = true;
                }

                //Situation: User was on Hex before, so we had to set a new inputType. However, they're on any of the other
                //ones right now, and they need the regular (number) keyboard
                //android:inputType="numberDecimal" is equivalent to InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
                else{
                    //only check if optionAHexSet is true, b/c it's redundant and wasteful to keep checking every time
                    if(optionAHexSet){
                        textA.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL );
                        optionAHexSet = false;
                    }
                }

                optionACurrentSelection = pos;  //save this so that on configuration change, spinner will revert to this value

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        optionSpinnerB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Log.i(TAG, "optionB picked item at position: " + pos);
                currentOptionB = parent.getItemAtPosition(pos).toString();
                optionSpinnerB.setPrompt(currentOptionB);

                //If it's hex, we need to change the keyboard from number (android:inputType = numberDecimal),
                //to a keyboard with letters
                //http://stackoverflow.com/questions/2586301/set-inputtype-for-an-edittext
                //https://developer.android.com/reference/android/widget/TextView.html#attr_android%3ainputType
                //android:inputType="text" is equivalent to  TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL
                //TYPE_CLASS_TEXT is equivalent to TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL, b/c
                //TYPE_CLASS_TEXT == 0x1 and TYPE_TEXT_VARIATION_NORMAL == 0x0, so a bit or would just be redundant

                if(currentOptionB.equals("Hex")){
                    textB.setInputType(InputType.TYPE_CLASS_TEXT);  //InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL
                    optionBHexSet = true;
                }

                //Situation: User was on Hex before, so we had to set a new inputType. However, they're on any of the other
                //ones right now, and they need the regular (number) keyboard
                //android:inputType="numberDecimal" is equivalent to InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
                else{
                    //only check if optionAHexSet is true, b/c it's redundant and wasteful to keep checking every time
                    if(optionBHexSet){
                        textB.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL );
                        optionBHexSet = false;
                    }
                }

                optionBCurrentSelection = pos;  //save this so that on configuration change, spinner will revert to this value
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //EditTexts moved to the top so they can be referenced.
        //Done so that we can implement a different inputType whenever "hex" option is selected

        //Clear Buttons
        clearA = (Button)rootView.findViewById(R.id.clearA);
        clearA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textA.setText("");
            }
        });
        clearB = (Button)rootView.findViewById(R.id.clearB);
        clearB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textB.setText("");
            }
        });

        //Compute Button
        compute = (Button)rootView.findViewById(R.id.compute_button);
        compute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                computeResult(v);
            }
        });
        return rootView;
//        return inflater.inflate(R.layout.unit_fragment_layout, container, false);
    }

    /*
        Compute result using text from optionA and optionB
        First check that only one textbook has text in it. If not, then show an alert dialog
        Then, check for correct format in
     */

    public void computeResult(View v){
        String optionA = textA.getText().toString();
        String optionB = textB.getText().toString();
        Log.i(TAG, "computeResult, where optionA: " + optionA + ", optionB: " + optionB);
        if(!optionA.equals("") && optionB.equals("")){  //optionA has text in it, optionB has nothing in it
            Log.i(TAG, "computeResult, optionA = good, optionB = empty");
            computeResultHelper("optionA", optionA, optionB, v);
//            if(currentCategory.equals("Programming")){
//                Log.i(TAG, "computeResult, Programming");
//                boolean correctFormat = checkFormat(true, currentOptionA, currentOptionB);
//                if(correctFormat){
//                    if(currentOptionA.equals(currentOptionB)){
//                        showAlertDialog(getResources().getString(R.string.same_category), v);    //"Same category, please choose a different category"
//                    }
//                    else{
//                        /* Since there's only 3P2 permutations (aka 6 permutations), there doesn't need to be 2 conversions
//                           Also. since binary --> hex is really just (binary --> decimal, decimal --> hex), and
//                           hex --> binary is really also just (hex --> decimal, decimal --> binary),
//                           we just need to create functions for (binary --> decimal, decimal --> hex, hex --> decimal, decimal --> binary),
//                           which we already have from UnitConverter 1.0
//                         */
//
//                        //1st Conversion: optionA ==> in decimal
//                        if(currentOptionA.equals("Binary") && currentOptionB.equals("Decimal")){    //binary -->
//                            Double value = binaryToDecimal(optionA);
//                            textB.setText(""+truncateToNDecimalPlaces(value,4));
//                        }
//                        else if(currentOptionA.equals("Binary") && currentOptionB.equals("Hex")){  //binary --> hex
//                            //is really just binary --> decimal, then decimal --> hex
//                            textB.setText(decimalToHex(String.valueOf(binaryToDecimal(optionA))));
//                        }
//                        else if(currentOptionA.equals("Decimal") && currentOptionB.equals("Binary")){ //decimal --> binary
//                            textB.setText(decimalToBinary(optionA));
//
//                        }
//                        else if(currentOptionA.equals("Decimal") && currentOptionB.equals("Hex")){ //decimal --> binary
//                            textB.setText(decimalToHex(optionA));
//                        }
//                        else if(currentOptionA.equals("Hex") && currentOptionB.equals("Decimal")){  //hex --> decimal
//                            Double value = hexToDecimal(optionA);
//                            textB.setText(""+truncateToNDecimalPlaces(value,4));
//                        }
//                        else{   //Hex --> binary
//                            //is really just hex --> decimal, decimal --> binary
//                            textB.setText(decimalToBinary(String.valueOf(hexToDecimal(optionA))));
//                        }
//
//                    }
//                }
//                else{
//                    showAlertDialog(getResources().getString(R.string.incorrect_format), v);    //"Incorrect format"
//                }
//            }
//
//            else{   //Non-programming: General unit converter
//                boolean correctFormat = checkFormat(false, "", "");
//                if(correctFormat){
//                    if(currentOptionA.equals(currentOptionB)){
//                        showAlertDialog(getResources().getString(R.string.same_category), v);    //"Same category, please choose a different category"
//                    }
//                    else{
//                        /* 2 Conversions
//                            Common unit needs to be small, or else if converting from small form --> large form, it will become 0
//                            optionA <==> common unit <==> optionB
//                            Common units:
//                                1. Weight:          microgram
//                                2. Volume:          mL
//                                3. Distance:        micrometer
//                                4. Temperature:     Celsius
//                         */
//                        if(currentCategory.equals("Weight")){   //Convert from unit in form from optionA ==> unit in microgram ==> unit in form from optionB
//                            Log.i(TAG, "computeResult, Weight");
//                            Double middlemanValue = 0.0;
//                            switch(currentOptionA){
//                                case "Kilogram":        //kg --> microgram
//                                    Log.i(TAG, "computeResult, Weight: OptionA = " + currentOptionA);
//                                    Double kg = Double.parseDouble(optionA);
//                                    Log.i(TAG, "computeResult, Weight: kg = " + kg);
//                                    middlemanValue = kg * 1000000000;
//                                    break;
//                                case "Gram":            //g --> microgram
//                                    Log.i(TAG, "computeResult, Weight: OptionA = " + currentOptionA);
//                                    Double g = Double.parseDouble(optionA);
//                                    middlemanValue = g * 1000000;
//                                    break;
//                                case "Milligram":       //mg --> microgram
//                                    Log.i(TAG, "computeResult, Weight: OptionA = " + currentOptionA);
//                                    Double mg = Double.parseDouble(optionA);
//                                    middlemanValue = mg  * 1000;
//                                    break;
//                                case "Metric ton":      //metric ton --> microgram. 1 metric ton = 1000kg
//                                    Log.i(TAG, "computeResult, Weight: OptionA = " + currentOptionA);
//                                    Double mton = Double.parseDouble(optionA);
//                                    middlemanValue = mton * 1000000000000.0;
//                                    break;
//                                case "Stone":           //stone --> microgram
//                                    Log.i(TAG, "computeResult, Weight: OptionA = " + currentOptionA);
//                                    Double stone = Double.parseDouble(optionA);
//                                    middlemanValue = stone * 6350290000.0;   //6350290000
//                                    break;
//                                case "US Ton":          //us ton --> microgram
//                                    Log.i(TAG, "computeResult, Weight: OptionA = " + currentOptionA);
//                                    Double uston = Double.parseDouble(optionA);
//                                    middlemanValue = uston * 907185000000.0;    //907185000000
//                                    break;
//                                case "Pound":          //pound --> microgram
//                                    Log.i(TAG, "computeResult, Weight: OptionA = " + currentOptionA);
//                                    Double lb = Double.parseDouble(optionA);
//                                    middlemanValue = lb * 453592000;
//                                    break;
//                                case "Ounce":           //ounce --> microgram
//                                    Log.i(TAG, "computeResult, Weight: OptionA = " + currentOptionA);
//                                    Double oz = Double.parseDouble(optionA);
//                                    middlemanValue = oz * 28349500;
//                                    break;
//                                default: middlemanValue = Double.parseDouble(optionA); break; //If already in microgram, dont' do anything:
//
//                            }
//
//                            Log.i(TAG, "computeResult, Weight: middlemanValue = " + middlemanValue);
//
//                            switch(currentOptionB){
//                                case "Kilogram":        //microgram --> kg
//                                    Log.i(TAG, "computeResult, Weight: OptionB = " + currentOptionB);
//                                    middlemanValue /= 1000000000;
//                                    break;
//                                case "Gram":            //microgram --> g
//                                    Log.i(TAG, "computeResult, Weight: OptionB = " + currentOptionB);
//                                    middlemanValue /= 1000000;
//                                    break;
//                                case "Milligram":       //microgram --> mg
//                                    Log.i(TAG, "computeResult, Weight: OptionB = " + currentOptionB);
//                                    middlemanValue /= 1000;
//                                    break;
//                                case "Metric ton":      //microgram --> metric ton
//                                    Log.i(TAG, "computeResult, Weight: OptionB = " + currentOptionB);
//                                    middlemanValue /= 1000000000000.0;
//                                    break;
//                                case "Stone":           //microgram --> stone
//                                    Log.i(TAG, "computeResult, Weight: OptionB = " + currentOptionB);
//                                    middlemanValue /= 6350290000.0;   //6350290000
//                                    break;
//                                case "US Ton":          //microgram --> us ton
//                                    Log.i(TAG, "computeResult, Weight: OptionB = " + currentOptionB);
//                                    middlemanValue /= 907185000000.0;    //907185000000
//                                    break;
//                                case "Pound":          //microgram --> pound
//                                    Log.i(TAG, "computeResult, Weight: OptionB = " + currentOptionB);
//                                    middlemanValue /= 453592000;
//                                    break;
//                                case "Ounce":           //microgram --> ounce
//                                    Log.i(TAG, "computeResult, Weight: OptionB = " + currentOptionB);
//                                    middlemanValue /= 28349500;
//                                    break;
//                                default: break; //If already in microgram, just set text
//                            }
////                            middlemanValue = truncateToNDecimalPlaces(middlemanValue);
////                            textB.setText(""+middlemanValue);
//                            textB.setText(""+truncateToNDecimalPlaces(middlemanValue,4));
//
//                        }
//                        else if(currentCategory.equals("Volume")){
//                            Log.i(TAG, "computeResult, Volume");
//                            Double middlemanValue = 0.0;
//                            switch(currentOptionA){
//                                case "US gallon":       // US gallon --> mL
//                                    Double gal= Double.parseDouble(optionA);
//                                    middlemanValue = gal*3785.41;
//                                    break;
//                                case "US quart":       // US quart --> mL
//                                    Double quart = Double.parseDouble(optionA);
//                                    middlemanValue = quart * 946.353;
//                                    break;
//                                case "US pint":       // US pint --> mL
//                                    Double pint = Double.parseDouble(optionA);
//                                    middlemanValue = pint * 473.176;
//                                    break;
//                                case "US cup":       // US cup --> mL
//                                    Double cup = Double.parseDouble(optionA);
//                                    middlemanValue = cup *236.588;
//                                    break;
//                                case "US fluid ounce":  // US fluid ounce --> mL
//                                    Double fl= Double.parseDouble(optionA);
//                                    middlemanValue = fl* 29.5735;
//                                    break;
//                                case "US tbsp":       // US tbsp --> mL
//                                    Double tbsp = Double.parseDouble(optionA);
//                                    middlemanValue = tbsp * 14.7868;
//                                    break;
//                                case "US tsp":       // US tsp --> mL
//                                    Double tsp = Double.parseDouble(optionA);
//                                    middlemanValue = tsp * 4.92892;
//                                    break;
//                                case "Liters":       // Liters --> mL
//                                    Double liters= Double.parseDouble(optionA);
//                                    middlemanValue = liters *1000;
//                                    break;
//                                default: middlemanValue = Double.parseDouble(optionA); break; //If already in mL, dont' do anything:
//
//                            }
//                            switch (currentOptionB){
//                                case "US gallon":       // mL --> US gallon
//                                    middlemanValue /= 3785.41;
//                                    break;
//                                case "US quart":       // mL --> US quart
//                                    middlemanValue /= 946.353;
//                                    break;
//                                case "US pint":       //  mL --> US pint
//                                    middlemanValue /= 473.176;
//                                    break;
//                                case "US cup":       //  mL --> US cup
//                                    middlemanValue /= 236.588;
//                                    break;
//                                case "US fluid ounce":  //  mL --> S fluid ounce L
//                                    middlemanValue /= 29.5735;
//                                    break;
//                                case "US tbsp":       //  mL --> US tbsp
//                                    middlemanValue /= 14.7868;
//                                    break;
//                                case "US tsp":       //  mL --> US tsp
//                                    middlemanValue /= 4.92892;
//                                    break;
//                                case "Liters":       //  mL --> Liters
//                                    middlemanValue /= 1000;
//                                    break;
//                                default: break; //If already in mL, just set text
//                            }
////                            middlemanValue = truncateToNDecimalPlaces(middlemanValue);
////                            textB.setText(""+middlemanValue);
//                            textB.setText(""+truncateToNDecimalPlaces(middlemanValue,4));
//
//                        }
//                        else if(currentCategory.equals("Distance")){    //Convert from unit in form from optionA ==> unit in micrometer ==> unit in form from optionB
//                            Double middlemanValue = 0.0;
//                            Log.i(TAG, "computeResult, Distance");
//                            switch(currentOptionA){
//                                case "Kilometer":   //km --> micrometer
//                                    Double km = Double.parseDouble(optionA);
//                                    middlemanValue = km*1000000000;
//                                    break;
//                                case "Meter":   //km --> micrometer
//                                    Double m = Double.parseDouble(optionA);
//                                    middlemanValue = m*1000000;
//                                    break;
//                                case "Centimeter":  //cm --> micrometer
//                                    Double cm = Double.parseDouble(optionA);
//                                    middlemanValue = cm*10000;
//                                    break;
//                                case "Millimeter":  //mm --> micrometer
//                                    Double mm = Double.parseDouble(optionA);
//                                    middlemanValue = mm*1000;
//                                    break;
//                                case "Nanometer":   //nm --> micrometer
//                                    Double nm = Double.parseDouble(optionA);
//                                    middlemanValue = nm/1000;
//                                    break;
//                                case "Knots":   //knots --> micrometer
//                                    Double knots = Double.parseDouble(optionA);
//                                    middlemanValue = knots * 1852000000;
//                                    break;
//                                case "Mile":    //mile --> micrometer
//                                    Double mile = Double.parseDouble(optionA);
//                                    middlemanValue = mile * 1609340000;
//                                    break;
//                                case "Yard":    //yard --> micrometer
//                                    Double yard = Double.parseDouble(optionA);
//                                    middlemanValue = yard * 914400;
//                                    break;
//                                case "Feet":    //feet --> micrometer
//                                    Double feet = Double.parseDouble(optionA);
//                                    middlemanValue = feet * 304800;
//                                    break;
//                                case "Inch":    //inch --> micrometer
//                                    Double inch = Double.parseDouble(optionA);
//                                    middlemanValue = inch * 25400;
//                                    break;
//                                default: middlemanValue = Double.parseDouble(optionA); break; //If already in micrometer, dont' do anything:
//                            }
//                            switch(currentOptionB){
//                                case "Kilometer":   //micrometer --> km
//                                    middlemanValue /= 1000000000;
//                                    break;
//                                case "Meter":       //micrometer --> m
//                                    middlemanValue /= 1000000;
//                                    break;
//                                case "Centimeter":  //micrometer --> cm
//                                    middlemanValue /= 10000;
//                                    break;
//                                case "Millimeter":  //micrometer --> mm
//                                    middlemanValue /= 1000;
//                                    break;
//                                case "Nanometer":   //micrometer --> nm
//                                    middlemanValue *= 1000;
//                                    break;
//                                case "Knots":       //micrometer --> knots
//                                    middlemanValue /= 1852000000;
//                                    break;
//                                case "Mile":        //micrometer --> mile
//                                    middlemanValue /= 1609340000;
//                                    break;
//                                case "Yard":        //micrometer --> yard
//                                    middlemanValue /= 914400;
//                                    break;
//                                case "Feet":        //micrometer --> feet
//                                    middlemanValue /= 304800;
//                                    break;
//                                case "Inch":        //micrometer --> inch
//                                    middlemanValue /= 25400;
//                                    break;
//                                default: break; //If already in nanometer, just set text
//                            }
////                            middlemanValue = truncateToNDecimalPlaces(middlemanValue);
////                            textB.setText(""+middlemanValue);
//                            textB.setText(""+truncateToNDecimalPlaces(middlemanValue,4));
//
//
//                        }
//                        else{   //Temperature. Convert from unit in form from optionA ==> unit in celsius ==> unit in form from optionB
//                            Double middlemanValue = 0.0;
//                            Log.i(TAG, "computeResult, Temperature");
//                            switch(currentOptionA){
//                                case "Fahrenheit":  //fahrenheit --> celsius
//                                    Double dFahr = Double.parseDouble(optionA);
//                                    middlemanValue = dFahr - 32.0;
//                                    middlemanValue *= 5.0;
//                                    middlemanValue /= 9.0;
//                                    break;
//                                case "Kelvin":  //kelvin --> celsius
//                                    Double kelvin = Double.parseDouble(optionA);
//                                    middlemanValue = kelvin - 273.15;
//                                    break;
//                                default: middlemanValue = Double.parseDouble(optionA); break; //If already in celsius, dont' do anything
//                            }
//
//                            switch(currentOptionB){
//                                case "Fahrenheit":  //celsius --> fahrenheit
//                                    middlemanValue *= 9.0;
//                                    middlemanValue /= 5.0;
//                                    middlemanValue += 32;
//                                    break;
//                                case "Kelvin":  //celsius to kelvin
//                                    middlemanValue += 273.15;
//                                    break;
//                                default: break; //If already celsius, just set text
//                            }
////                            middlemanValue = truncateToNDecimalPlaces(middlemanValue);
////                            textB.setText(""+middlemanValue);
//                            textB.setText(""+truncateToNDecimalPlaces(middlemanValue,4));
//
//                        }
//
//                    }
//                }
//                else{
//                    showAlertDialog(getResources().getString(R.string.incorrect_format), v);    //"Incorrect format"
//                }
//            }
        }

        //optionB has text in it, optionA has nothing in it. Code is pretty much a mirror of the above code, except instead of setting text onto optionB, set text onto optionA
        else if(optionA.equals("") && !optionB.equals("")){
            computeResultHelper("optionB", optionA, optionB, v);
        }
        else{
            if(optionA.equals("") && optionB.equals("")){
                //both EditTexts are null
                showAlertDialog("Please input text into one of the text fields!", v);
            }
            if(!optionA.equals("") && !optionB.equals("")){
                //both EditTexts are full
                showAlertDialog("Need to clear one of the text fields!", v);
            }
        }
    }

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



    /*
        Checks the format of the inputs
        Returns true if good format, false if bad format
     */
    public boolean checkFormat(boolean isProgramming, String programmingInput, String programmingOutput ){
        boolean isNegative = false;
        boolean isGoodFormat = true;
        if(isProgramming){
            //Have to check both optionA's text and optionB's text
            String optionAcheck = textA.getText().toString();
            String optionBcheck = textB.getText().toString();
            /***********************************************Check optionA text**/
            Log.i(TAG, "Check optionA text");
            if(!optionAcheck.equals("") && optionBcheck.equals("")){    //If there's only text in optionA and no text in optionB
                optionAcheck = optionAcheck.trim();
                if(programmingInput.equals("Decimal")){
                    int i = 0;
                    boolean containsBadCharacter = false;
                    while(i < optionAcheck.length() && !containsBadCharacter){
                        int ascii = (int)optionAcheck.charAt(i);
                        if(i == 0){
                            //checking for a "-" at the beginning. This is the only time where - is acceptable
                            if(ascii >= 48 && ascii <= 57 || ascii == 46 || ascii == 45){
                                //if it's in range
                                Log.i(TAG, "checkFormat: " + optionAcheck.charAt(i) + " is in range");
                            }

                            else{
                                Log.i(TAG, "checkFormat: " + optionAcheck.charAt(i) + " is NOT in range");
                                containsBadCharacter = true;
                            }
                        }
                        else{
                            if(ascii >= 48 && ascii <= 57 || ascii == 46){
                                //if it's in range
                                Log.i(TAG, "checkFormat: " + optionAcheck.charAt(i) + " is in range");
                            }

                            else{
                                Log.i(TAG, "checkFormat: " + optionAcheck.charAt(i) + " is NOT in range");
                                containsBadCharacter = true;
                            }
                        }


                        i++;
                    }
                    if(containsBadCharacter){
                        isGoodFormat = false;
                    }
                }
                else if(programmingInput.equals("Binary")){
                    int i = 0;
                    boolean containsBadCharacter = false;
                    while(i < optionAcheck.length() && !containsBadCharacter){
                        int ascii = (int)optionAcheck.charAt(i);
                        if(i == 0){
                            //checking for a "-" at the beginning. This is the only time where - is acceptable
                            if(ascii == 48 || ascii <= 49 || ascii == 46 || ascii == 45){
                                //if it's in range
                                Log.i(TAG, "checkFormat: " + optionAcheck.charAt(i) + " is in range");
                            }

                            else{
                                Log.i(TAG, "checkFormat: " + optionAcheck.charAt(i) + " is NOT in range");
                                containsBadCharacter = true;
                            }
                        }
                        else{
                            if(ascii == 48 || ascii <= 49 || ascii == 46){
                                //if it's in range
                                Log.i(TAG, "checkFormat: " + optionAcheck.charAt(i) + " is in range");
                            }

                            else{
                                Log.i(TAG, "checkFormat: " + optionAcheck.charAt(i) + " is NOT in range");
                                containsBadCharacter = true;
                            }
                        }

                        i++;
                    }
                    if(containsBadCharacter){
                        isGoodFormat = false;
                    }
                }
                else{
                    //hex
                    //If there's an "0x" and that' it's at the front of optionAcheck
                    if(optionAcheck.contains("0x") && optionAcheck.charAt(0) == '0' && optionAcheck.charAt(1) == 'x'){
                        optionAcheck = optionAcheck.substring(2,optionAcheck.length());
                    }
                    int i = 0;
                    boolean containsBadCharacter = false;
                    while(i < optionAcheck.length() && !containsBadCharacter){
                        int ascii = (int)optionAcheck.charAt(i);
                        //if ascii is either #, A-F, a-f, or .
                        if(i == 0){
                            //checking for a "-" at the beginning. This is the only time where - is acceptable
                            if((ascii >= 48 && ascii <= 57) || (ascii >= 65 && ascii <= 70) || (ascii >= 97 && ascii <= 102) || ascii == 46 || ascii == 45){
                                //if it's in range
                                Log.i(TAG, "checkFormat: " + optionAcheck.charAt(i) + " is in range");
                            }

                            else{
                                Log.i(TAG, "checkFormat: " + optionAcheck.charAt(i) + " is NOT in range, ascii: " + ascii);
                                containsBadCharacter = true;
                            }
                        }
                        else{
                            if((ascii >= 48 && ascii <= 57) || (ascii >= 65 && ascii <= 70) || (ascii >= 97 && ascii <= 102) || ascii == 46){
                                //if it's in range
                                Log.i(TAG, "checkFormat: " + optionAcheck.charAt(i) + " is in range");
                            }

                            else{
                                Log.i(TAG, "checkFormat: " + optionAcheck.charAt(i) + " is NOT in range, ascii: " + ascii);
                                containsBadCharacter = true;
                            }
                        }

                        i++;
                    }
                    if(containsBadCharacter){
                        isGoodFormat = false;
                    }

                }
            }

            /***********************************************Check optionB text. This is a mirror copy of the code above pretty much*/
            Log.i(TAG, "checkFormat: Check optionB text");
            if(optionAcheck.equals("") && !optionBcheck.equals("")){    //If there's only text in optionB and no text in optionA
                optionBcheck = optionBcheck.trim();
                if(programmingOutput.equals("decimal")){
                    int i = 0;
                    boolean containsBadCharacter = false;
                    while(i < optionBcheck.length() && !containsBadCharacter){
                        int ascii = (int)optionBcheck.charAt(i);
                        if(i == 0){
                            //checking for a "-" at the beginning. This is the only time where - is acceptable
                            if(ascii >= 48 && ascii <= 57 || ascii == 46 || ascii == 45){
                                //if it's in range
                                Log.i(TAG, "checkFormat: " + optionBcheck.charAt(i) + " is in range");
                            }

                            else{
                                Log.i(TAG, "checkFormat: " + optionBcheck.charAt(i) + " is NOT in range");
                                containsBadCharacter = true;
                            }
                        }
                        else{
                            if(ascii >= 48 && ascii <= 57 || ascii == 46){
                                //if it's in range
                                Log.i(TAG, "checkFormat: " + optionBcheck.charAt(i) + " is in range");
                            }

                            else{
                                Log.i(TAG, "checkFormat: " + optionBcheck.charAt(i) + " is NOT in range");
                                containsBadCharacter = true;
                            }
                        }

                        i++;
                    }
                    if(containsBadCharacter){
                        isGoodFormat = false;
                    }
                }
                else if(programmingOutput.equals("binary")){
                    int i = 0;
                    boolean containsBadCharacter = false;
                    while(i < optionBcheck.length() && !containsBadCharacter){
                        int ascii = (int)optionBcheck.charAt(i);
                        if(i == 0){
                            //checking for a "-" at the beginning. This is the only time where - is acceptable
                            if(ascii == 48 || ascii <= 49 || ascii == 46 || ascii == 45){
                                //if it's in range
                                Log.i(TAG, "checkFormat: " + optionBcheck.charAt(i) + " is in range");
                            }

                            else{
                                Log.i(TAG, "checkFormat: " + optionBcheck.charAt(i) + " is NOT in range");
                                containsBadCharacter = true;
                            }
                        }
                        else{
                            if(ascii == 48 || ascii <= 49 || ascii == 46){
                                //if it's in range
                                Log.i(TAG, "checkFormat: " + optionBcheck.charAt(i) + " is in range");
                            }

                            else{
                                Log.i(TAG, "checkFormat: " + optionBcheck.charAt(i) + " is NOT in range");
                                containsBadCharacter = true;
                            }
                        }


                        i++;
                    }
                    if(containsBadCharacter){
                        isGoodFormat = false;
                    }
                }
                else{
                    //hex
                    //If there's an "0x" and that' it's at the front of optionBcheck
                    if(optionBcheck.contains("0x") && optionBcheck.charAt(0) == '0' && optionBcheck.charAt(1) == 'x'){
                        optionBcheck = optionBcheck.substring(2,optionBcheck.length());
                    }
                    int i = 0;
                    boolean containsBadCharacter = false;
                    while(i < optionBcheck.length() && !containsBadCharacter){
                        //if ascii is either #, A-F, a-f, or .
                        int ascii = (int)optionBcheck.charAt(i);
                        if(i == 0){
                            //checking for a "-" at the beginning. This is the only time where - is acceptable
                            if((ascii >= 48 && ascii <= 57) || (ascii >= 65 && ascii <= 70) || (ascii >= 97 && ascii <= 102) || ascii == 46 || ascii == 45){
                                //if it's in range
                                Log.i(TAG, "checkFormat: " + optionBcheck.charAt(i) + " is in range");
                            }

                            else{
                                Log.i(TAG, "checkFormat: " + optionBcheck.charAt(i) + " is NOT in range, ascii: " + ascii);
                                containsBadCharacter = true;
                            }
                        }
                        else{
                            if((ascii >= 48 && ascii <= 57) || (ascii >= 65 && ascii <= 70) || (ascii >= 97 && ascii <= 102) || ascii == 46){
                                //if it's in range
                                Log.i(TAG, "checkFormat: " + optionBcheck.charAt(i) + " is in range");
                            }

                            else{
                                Log.i(TAG, "checkFormat: " + optionBcheck.charAt(i) + " is NOT in range, ascii: " + ascii);
                                containsBadCharacter = true;
                            }
                        }


                        i++;
                    }
                    if(containsBadCharacter){
                        isGoodFormat = false;
                    }

                }
            }


        }


        /***!isProgramming ==> General Unit Converter**/
        else{
            //General unit converter
            //No alphabet, no special characters except period, only numbers
            String optionAcheck = textA.getText().toString();
            String optionBcheck = textB.getText().toString();

            Log.i(TAG, "checkFormat: Check optionA text");
            if(!optionAcheck.equals("") && optionBcheck.equals("")){    //If there's only text in optionA and no text in optionB
                //Check optionAtext
                optionAcheck = optionAcheck.trim();
                int i = 0;
                boolean containsBadCharacter = false;
                while(i < optionAcheck.length() && !containsBadCharacter){
                    int ascii = (int)optionAcheck.charAt(i);
                    if(i == 0){
                        //checking for a "-" at the beginning. This is the only time where - is acceptable
                        if((ascii >= 48 && ascii <= 57) || ascii == 46 || ascii == 45){
                            //if it's in range
                            Log.i(TAG, "checkFormat: " + optionAcheck.charAt(i) + " is in range");
                        }

                        else{
                            Log.i(TAG, "checkFormat: " + optionAcheck.charAt(i) + " is NOT in range, ascii: " + ascii);
                            containsBadCharacter = true;
                        }
                    }
                    else{
                        if((ascii >= 48 && ascii <= 57) || ascii == 46){
                            //if it's in range
                            Log.i(TAG, "checkFormat: " + optionAcheck.charAt(i) + " is in range");
                        }

                        else{
                            Log.i(TAG, "checkFormat: " + optionAcheck.charAt(i) + " is NOT in range, ascii: " + ascii);
                            containsBadCharacter = true;
                        }
                    }


                    i++;
                }
                if(containsBadCharacter){
                    isGoodFormat = false;
                }
            }

            Log.i(TAG, "Check optionB text");
            if(optionAcheck.equals("") && !optionBcheck.equals("")){    //If there's only text in optionB and no text in optionA
                //Check optionBtext
                optionBcheck = optionAcheck.trim();
                int k = 0;
                boolean containsBadCharacter2 = false;
                while(k < optionBcheck.length() && !containsBadCharacter2){
                    int ascii = (int)optionBcheck.charAt(k);
                    if(k == 0){
                        //checking for a "-" at the beginning. This is the only time where - is acceptable
                        if((ascii >= 48 && ascii <= 57) || ascii == 46 || ascii == 45){
                            //if it's in range
                            Log.i(TAG, "checkFormat: " + optionBcheck.charAt(k) + " is in range");
                        }

                        else{
                            Log.i(TAG, "checkFormat: " + optionBcheck.charAt(k) + " is NOT in range, ascii: " + ascii);
                            containsBadCharacter2 = true;
                        }
                    }
                    else{
                        if((ascii >= 48 && ascii <= 57) || ascii == 46){
                            //if it's in range
                            Log.i(TAG, "checkFormat: " + optionBcheck.charAt(k) + " is in range");
                        }

                        else{
                            Log.i(TAG, "checkFormat: " + optionBcheck.charAt(k) + " is NOT in range, ascii: " + ascii);
                            containsBadCharacter2 = true;
                        }
                    }


                    k++;
                }
                if(containsBadCharacter2){
                    isGoodFormat = false;
                }
            }



        }



        return isGoodFormat;
    }

    /*
        http://stackoverflow.com/questions/153724/how-to-round-a-number-to-n-decimal-places-in-java
        Truncates the number to n decimal places
     */
    public String truncateToNDecimalPlaces(Double val, int n){
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

    /*
        Created to re-use code
     */
    public String decimalDoubleToBinaryString(Double middlemanValue){
        //Use old method from UnitConverter 1.0
        //assumption here = no decimal part
        long temp = middlemanValue.longValue();
        Stack<String> mods = new Stack<>();
        while (temp != 0) {
            long remainder = temp % 2;
            mods.add("" + remainder);
            temp /= 2;
        }

        StringBuilder bin = new StringBuilder("");
        //start removing values
        while (!mods.isEmpty()) {
            bin.append(mods.pop());
        }

        return bin.toString();
    }

    /*
        Converts a fractional decimal ==> binary representation
        Also checks for infinite binary numbers: (Ex: 1/3)
        If there's more than 50 passes, we zcan reasonably assume that the number
        we're trying to calculate is an infinite binary # (1/3)
     */
    public String decimalFractionalDoubleToBinaryString(Double fracValue){
        //http://cs.furman.edu/digitaldomain/more/ch6/dec_frac_to_bin.htm
        //Basically doing what the above link does
        StringBuilder builder = new StringBuilder("");
        Double currentValue = fracValue;
        int infiniteCounter = 0;
        boolean isOne = false;
        while(infiniteCounter <= 50 && !isOne){
            currentValue *= 2;
            if(currentValue >= 1){
                builder.append("1");
                if(currentValue == 1){
                    isOne = true;
                }
                else{
                    currentValue -= 1;
                }
            }
            else{
                builder.append("0");
            }

            infiniteCounter++;
        }

        return builder.toString();

    }

    /*
    Created to re-use code
 */
    public String decimalDoubleToHexString(Double middlemanValue){
        long value = middlemanValue.longValue();    //this is okay b/c this method assumes the double has no decimal part
        Stack<String> mods = new Stack<>();
        while (value != 0) {
            long remainder = value % 16;
            if (remainder >= 10) {
                if(remainder == 10) {
                    mods.add("A");
                }
                else if(remainder == 11) {
                    mods.add("B");
                }
                else if(remainder == 12) {
                    mods.add("C");
                }
                else if(remainder == 13) {
                    mods.add("D");
                }
                else if(remainder == 14) {
                    mods.add("E");
                }
                else {  //remainder == 15
                    mods.add("F");
                }
            }
            else {
                mods.add("" + remainder);
            }
            value /= 16;
        }

        StringBuilder hex = new StringBuilder("0x");
        //start removing values
        while (!mods.isEmpty()) {
            hex.append(mods.pop());
        }

        return hex.toString();
    }

    /*
        Converts a fractional decimal ==> hex representation
        Also checks for infinite binary numbers: (Ex: 1/3)
        If there's more than 50 passes, we can reasonably assume that the number
        we're trying to calculate is an infinite binary # (1/3)
     */
    public String decimalFractionalDoubleToHexString(Double fracValue){
        //http://www.schoolelectronic.com/2013/09/convert-decimal-fraction-to-hexadecimal.html
        //Basically doing what the above link does
        StringBuilder builder = new StringBuilder("");
        Double currentValue = fracValue;
        int infiniteCounter = 0;
        boolean isOne = false;
        while(infiniteCounter <= 10 && !isOne){
            currentValue *= 16;
            if(currentValue >= 1){
                //Need to split up whole number part and fractional part and do separately

                if(currentValue == 1){
                    isOne = true;
                }
                else{
                    Double fracvalue = Math.abs(currentValue - Math.floor(currentValue));    //get the difference
                    Double regvalue = currentValue - fracvalue;
                    String regvaluestring = String.valueOf(regvalue.longValue());
                    Log.i(TAG, "whole number part calculated value: " + regvalue);
                    Log.i(TAG, "whole number part calculated value string: " + regvaluestring);
                    Log.i(TAG, "fractional part calculated value: " + fracvalue);

                    if(regvalue < 10.0){
                        builder.append(regvaluestring);
                    }
                    else{
                        switch (regvaluestring){
                            case "10": builder.append("A"); break;
                            case "11": builder.append("B"); break;
                            case "12": builder.append("C"); break;
                            case "13": builder.append("D"); break;
                            case "14": builder.append("E"); break;
                            case "15": builder.append("F"); break;
                            default: break;
                        }
                    }
                    currentValue = fracValue;
                }
            }
            else{
                builder.append("0");
            }

            infiniteCounter++;
        }

        return builder.toString();
    }



    public Double binaryToDecimal(String optionA){
        Double endValue = 0.0;
        //Need to check if it contains fractional binary (aka there's a period and there's stuff behind the period)
        if(optionA.contains(".")){
            //Use old method copied from original UnitConverter
            //reverse the string
            Log.i(TAG, "computeResult, Programming: Binary, fractional");
            int periodIndex = optionA.indexOf(".");

            //Regular part reversing string
            StringBuilder regularCopy = new StringBuilder("");
            for(int c = periodIndex - 1; c >= 0; c--){
                regularCopy.append(optionA.charAt(c));
            }

            String fractionCopy = optionA.substring(periodIndex+1, optionA.length());
//                                //Fractional part reversing string
//                                StringBuilder fractionalCopy = new StringBuilder("");
//                                for(int c = optionA.length() - 1; c > periodIndex; c--){
//                                    fractionalCopy.append(optionA.charAt(c));
//                                }
            Log.i(TAG, "reversed string regular part: " + regularCopy.toString());
            Log.i(TAG, "reversed string fractional part: " + fractionCopy);

            //Convert regular part ==> decimal
            Double regvalue = 0.0;
            for(int r = regularCopy.length() - 1; r >= 0; r--){
                Log.i(TAG, "current r: " + r );
                Log.i(TAG, "current value: " + regvalue );
                //full formula = 2^index * value
                //here, we get the 2^index part
                Double pow = 1.0;
                for(int p = r; p > 0; p--){
                    pow *= 2;
                }

                Log.i(TAG, "current pow: " + pow );
                Log.i(TAG, "current char: " + regularCopy.charAt(r) );
                //here, we get the value
                if(regularCopy.charAt(r) == '1'){
                    regvalue += pow;
                }

            }

            //Convert fractional part ==> decimal
            Double fracvalue = 0.0;
            for(int r = 0; r < fractionCopy.length(); r++){
                Log.i(TAG, "fcurrent r: " + r);
                Log.i(TAG, "fcurrent value: " + fracvalue );
                //full formula = 2^index * value
                //here, we get the 2^index part
                Double pow = 1.0;
                for(int p = r + 1; p > 0; p--){   //b/c it starts at 2^(-1), 2^(-2), 2^(-3)
                    Log.i(TAG, "fcurrent p: " + p );
                    pow = pow /2;
                }

                Log.i(TAG, "fcurrent pow2: " + pow);
                Log.i(TAG, "fcurrent char: " + fractionCopy.charAt(r) );
                //here, we get the value
                if(fractionCopy.charAt(r) == '1'){
                    fracvalue += pow;
                }

            }

            Log.i(TAG, "regular part calculated value: " + regvalue);
            Log.i(TAG, "fractional part calculated value: " + fracvalue);
            endValue = regvalue + fracvalue;

        }
        else{
            //Use old method copied from original UnitConverter
            //reverse the string
            Log.i(TAG, "computeResult, Programming: Binary, regular");
            StringBuilder copy = new StringBuilder("");
            for(int c = optionA.length() - 1; c >= 0; c--){
                copy.append(optionA.charAt(c));
            }
            Log.i(TAG, "reversed string: " + copy.toString() );
            //
            Double value = 0.0;
            for(int r = copy.length() - 1; r >= 0; r--){
                Log.i(TAG, "current r: " + r );
                Log.i(TAG, "current value: " + value );
                //full formula = 2^index * value
                //here, we get the 2^index part
                Double pow = 1.0;
                for(int p = r; p > 0; p--){
                    pow *= 2;
                }

                Log.i(TAG, "current pow: " + pow );
                Log.i(TAG, "current char: " + copy.charAt(r) );
                //here, we get the value
                if(copy.charAt(r) == '1'){
                    value += pow;
                }

            }
            Log.i(TAG, "regular part calculated value: " + value);
            endValue = value;
        }

        return endValue;
    }

    public Double hexToDecimal(String optionA){
        Double endValue = 0.0;
        //Use old UnitConverter method
        //here, we reverse the string and add on by
//        if(optionA.contains("0x")){
//            optionA = optionA.substring(2,optionA.length());
//        }
        if(optionA.contains(".")){
            //Use old method copied from original UnitConverter
            //reverse the string
            Log.i(TAG, "computeResult, Programming: Binary, fractional");
            int periodIndex = optionA.indexOf(".");

            //Regular part reversing string
            StringBuilder regularCopy = new StringBuilder("");
            for(int c = periodIndex - 1; c >= 0; c--){
                regularCopy.append(optionA.charAt(c));
            }

            String fractionCopy = optionA.substring(periodIndex+1, optionA.length());
//                                //Fractional part reversing string
//                                StringBuilder fractionalCopy = new StringBuilder("");
//                                for(int c = optionA.length() - 1; c > periodIndex; c--){
//                                    fractionalCopy.append(optionA.charAt(c));
//                                }
            Log.i(TAG, "reversed string regular part: " + regularCopy.toString());
            Log.i(TAG, "reversed string fractional part: " + fractionCopy);

            //Convert regular part ==> decimal
            Double regvalue = 0.0;
            for(int r = regularCopy.length() - 1; r >= 0; r--){
                Log.i(TAG, "current r: " + r );
                Log.i(TAG, "current value: " + regvalue );
                //full formula = 2^index * value
                //here, we get the 2^index part
                Double pow = 1.0;
                for(int p = r; p > 0; p--){
                    pow *= 16;
                }

                Log.i(TAG, "current pow: " + pow );
                Log.i(TAG, "current char: " + regularCopy.charAt(r) );
                //here, we get the value
                switch(regularCopy.charAt(r))
                {
                    //case 0 just means do nothing
                    case '1': regvalue += pow; break;
                    case '2': pow *= 2; regvalue += pow; break;
                    case '3': pow *= 3; regvalue += pow; break;
                    case '4': pow *= 4; regvalue += pow; break;
                    case '5': pow *= 5; regvalue += pow; break;
                    case '6': pow *= 6; regvalue += pow; break;
                    case '7': pow *= 7; regvalue += pow; break;
                    case '8': pow *= 8; regvalue += pow; break;
                    case '9': pow *= 9; regvalue += pow; break;
                    case 'A': pow *= 10; regvalue += pow; break;
                    case 'B': pow *= 11; regvalue += pow; break;
                    case 'C': pow *= 12; regvalue += pow; break;
                    case 'D': pow *= 13; regvalue += pow; break;
                    case 'E': pow *= 14; regvalue += pow; break;
                    case 'F': pow *= 15; regvalue += pow; break;
                    case 'a': pow *= 10; regvalue += pow; break;
                    case 'b': pow *= 11; regvalue += pow; break;
                    case 'c': pow *= 12; regvalue += pow; break;
                    case 'd': pow *= 13; regvalue += pow; break;
                    case 'e': pow *= 14; regvalue += pow; break;
                    case 'f': pow *= 15; regvalue += pow; break;
                    default: break;


                }

            }

            //Convert fractional part ==> decimal
            Double fracvalue = 0.0;
            for(int r = 0; r < fractionCopy.length(); r++){
                Log.i(TAG, "fcurrent r: " + r);
                Log.i(TAG, "fcurrent value: " + fracvalue );
                //full formula = 2^index * value
                //here, we get the 2^index part
                Double pow = 1.0;
                for(int p = r + 1; p > 0; p--){   //b/c it starts at 2^(-1), 2^(-2), 2^(-3)
                    Log.i(TAG, "fcurrent p: " + p );
                    pow = pow /16;
                }

                Log.i(TAG, "fcurrent pow2: " + pow);
                Log.i(TAG, "fcurrent char: " + fractionCopy.charAt(r) );
                //here, we get the value
                switch(fractionCopy.charAt(r))
                {
                    //case 0 just means do nothing
                    case '1': fracvalue += pow; break;
                    case '2': pow *= 2; fracvalue += pow; break;
                    case '3': pow *= 3; fracvalue += pow; break;
                    case '4': pow *= 4; fracvalue += pow; break;
                    case '5': pow *= 5; fracvalue += pow; break;
                    case '6': pow *= 6; fracvalue += pow; break;
                    case '7': pow *= 7; fracvalue += pow; break;
                    case '8': pow *= 8; fracvalue += pow; break;
                    case '9': pow *= 9; fracvalue += pow; break;
                    case 'A': pow *= 10; fracvalue += pow; break;
                    case 'B': pow *= 11; fracvalue += pow; break;
                    case 'C': pow *= 12; fracvalue += pow; break;
                    case 'D': pow *= 13; fracvalue += pow; break;
                    case 'E': pow *= 14; fracvalue += pow; break;
                    case 'F': pow *= 15; fracvalue += pow; break;
                    case 'a': pow *= 10; fracvalue += pow; break;
                    case 'b': pow *= 11; fracvalue += pow; break;
                    case 'c': pow *= 12; fracvalue += pow; break;
                    case 'd': pow *= 13; fracvalue += pow; break;
                    case 'e': pow *= 14; fracvalue += pow; break;
                    case 'f': pow *= 15; fracvalue += pow; break;
                    default: break;


                }

            }

            Log.i(TAG, "regular part calculated value: " + regvalue);
            Log.i(TAG, "fractional part calculated value: " + fracvalue);
            endValue = regvalue + fracvalue;
        }
        else{
            Double value = 0.0;
            //reversing the string
            StringBuilder copy = new StringBuilder("");
            for(int c = optionA.length() - 1; c >= 0; c--){
                copy.append(optionA.charAt(c));
            }
            //actual add operation
            for(int r = copy.length() - 1; r >= 0; r--){
                Log.i(TAG, "current r: " + r );
                Log.i(TAG, "current value: " + value );
                //full formula = 16^index * value
                //here, we get the 16^index part
                Double pow = 1.0;
                for(int p = r; p > 0; p--){
                    pow *= 16;
                }
                Log.i(TAG, "current pow: " + pow );
                Log.i(TAG, "current char: " + copy.charAt(r) );
                //here, we get the value
                switch(copy.charAt(r))
                {
                    //case 0 just means do nothing
                    case '1': value += pow; break;
                    case '2': pow *= 2; value += pow; break;
                    case '3': pow *= 3; value += pow; break;
                    case '4': pow *= 4; value += pow; break;
                    case '5': pow *= 5; value += pow; break;
                    case '6': pow *= 6; value += pow; break;
                    case '7': pow *= 7; value += pow; break;
                    case '8': pow *= 8; value += pow; break;
                    case '9': pow *= 9; value += pow; break;
                    case 'A': pow *= 10; value += pow; break;
                    case 'B': pow *= 11; value += pow; break;
                    case 'C': pow *= 12; value += pow; break;
                    case 'D': pow *= 13; value += pow; break;
                    case 'E': pow *= 14; value += pow; break;
                    case 'F': pow *= 15; value += pow; break;
                    case 'a': pow *= 10; value += pow; break;
                    case 'b': pow *= 11; value += pow; break;
                    case 'c': pow *= 12; value += pow; break;
                    case 'd': pow *= 13; value += pow; break;
                    case 'e': pow *= 14; value += pow; break;
                    case 'f': pow *= 15; value += pow; break;
                    default: break;


                }
            }
            endValue = value;
        }
        return endValue;
    }

    public String decimalToHex(String optionA){
        String ret = "";
        Double temp = Double.parseDouble(optionA);
        if(optionA.contains(".")){
            //Need to split up regular part and fractional part and do separately
            Double fracvalue = Math.abs(temp - Math.floor(temp));    //get the difference
            Double regvalue = temp - fracvalue;
            Log.i(TAG, "regular part calculated value: " + regvalue);
            Log.i(TAG, "fractional part calculated value: " + fracvalue);

            String regPart = decimalDoubleToHexString(regvalue);
            String fractPart = decimalFractionalDoubleToHexString(fracvalue);
            Log.i(TAG, "regular part string value: " + regPart);
            Log.i(TAG, "fractional part string value: " + fractPart);
            ret = "" + regPart + "." + fractPart;
        }
        else{
            ret = decimalDoubleToHexString(temp);
        }

        return ret;


    }

    public String decimalToBinary(String optionA){
        String ret = "";
        Double temp = Double.parseDouble(optionA);
        if(optionA.contains(".")){
            //Need to split up regular part and fractional part and do separately
            Double fracvalue = Math.abs(temp - Math.floor(temp));    //get the difference
            Double regvalue = temp - fracvalue;
            Log.i(TAG, "regular part calculated value: " + regvalue);
            Log.i(TAG, "fractional part calculated value: " + fracvalue);

            String regPart = decimalDoubleToBinaryString(regvalue);
            String fractPart = decimalFractionalDoubleToBinaryString(fracvalue);
            Log.i(TAG, "regular part string value: " + regPart);
            Log.i(TAG, "fractional part string value: " + fractPart);
            ret = "" + regPart + "." + fractPart;
        }
        else{
            ret = decimalDoubleToBinaryString(temp);
        }

        return ret;
    }

    /*
        Problem:
        On configuration change, categories inside optionA, optionB are not being saved and are being reverted back to the value at setSelection(0)
        For some reason though, values inside the editTexts are being saved. Maybe b/c setRetainInstance(true)?
        Save the items that were selected in optionA spinner and optionB spinner's onItemSelected method
        More specifically, save the position they were so that on a configuration change ==> we can do setSelection(previous item #)


     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        //Save stuff here
        savedInstanceState.putInt("optionACurrentSelection", optionACurrentSelection);
        savedInstanceState.putInt("optionBCurrentSelection", optionBCurrentSelection);
        savedInstanceState.putBoolean("optionAHexSet", optionAHexSet);
        savedInstanceState.putBoolean("optionBHexSet", optionBHexSet);

        super.onSaveInstanceState(savedInstanceState);
    }

    /*
        Basically, I dumped all the code that was previously in computeResult here
        so that it could be done for either situations 1) optionA != null, optionB == null and 2) optionA == null, optionB != null
        Since the code was so long in the computeResult, doing it twice would not only make it harder to read, but also
        waste a lot of code.
        whichOption = which option is not null.
            if whichOption.equals("optionA") ==> optionA != null, optionB == null
            if whichOption.equals("optionB") ==> optionA == null, optionB != null

        parameter optionA:
            the optionA string from the original computeResult method, which is really just textA.getText().toString();

        parameter optionB:
            the optionB string from the original computeResult method, which is really just textB.getText().toString();

        parameter v:
            the View v that was passed in from the original parameter, which was passed in from the onClick() in the compute button
            Not really efficient, but
     */
    public void computeResultHelper(String whichOption, String optionA, String optionB, View v){
        Log.i(TAG, "computeResultHelper, whichOption: " + whichOption + ", optionA: " + optionA + ", optionB: " + optionB);
        if(currentCategory.equals("Programming")){
            Log.i(TAG, "computeResultHelper, Programming");
            boolean correctFormat = checkFormat(true, currentOptionA, currentOptionB);
            if(correctFormat){
                Log.i(TAG, "computeResultHelper, Programming GOOD FORMAT");
                if(currentOptionA.equals(currentOptionB)){
                    showAlertDialog(getResources().getString(R.string.same_category), v);    //"Same category, please choose a different category"
                }
                else{
                        /* Since there's only 3P2 permutations (aka 6 permutations), there doesn't need to be 2 conversions
                           Also. since binary --> hex is really just (binary --> decimal, decimal --> hex), and
                           hex --> binary is really also just (hex --> decimal, decimal --> binary),
                           we just need to create functions for (binary --> decimal, decimal --> hex, hex --> decimal, decimal --> binary),
                           which we already have from UnitConverter 1.0
                         */

                    //1st Conversion: optionA ==> in decimal
                    if(currentOptionA.equals("Binary") && currentOptionB.equals("Decimal")){    //binary -->
                        if(whichOption.equals("optionA")){
                            Double value = binaryToDecimal(optionA);
                            textB.setText(""+truncateToNDecimalPlaces(value,4));
                        }
                        else{
                            //decimal --> binary
                            textA.setText(decimalToBinary(optionB));
                        }

                    }
                    else if(currentOptionA.equals("Binary") && currentOptionB.equals("Hex")){  //binary --> hex
                        //is really just binary --> decimal, then decimal --> hex
//                        textB.setText(decimalToHex(String.valueOf(binaryToDecimal(optionA))));
                        if(whichOption.equals("optionA")){
                            textB.setText(decimalToHex(String.valueOf(binaryToDecimal(optionA))));
                        }
                        else{
                            //hex --> binary
                            textA.setText(decimalToBinary(String.valueOf(hexToDecimal(optionB))));
                        }
                    }
                    else if(currentOptionA.equals("Decimal") && currentOptionB.equals("Binary")){ //decimal --> binary
//                        textB.setText(decimalToBinary(optionA));
                        if(whichOption.equals("optionA")){
                            textB.setText(decimalToBinary(optionA));
                        }
                        else{
                            //binary --> decimal
                            Double value = binaryToDecimal(optionB);
                            textA.setText(""+truncateToNDecimalPlaces(value,4));
                        }

                    }
                    else if(currentOptionA.equals("Decimal") && currentOptionB.equals("Hex")){ //decimal --> binary
//                        textB.setText(decimalToHex(optionA));
                        if(whichOption.equals("optionA")){
                            textB.setText(decimalToHex(optionA));
                        }
                        else{
                            //hex --> decimal
                            Double value = hexToDecimal(optionB);
                            textA.setText(""+truncateToNDecimalPlaces(value,4));
                        }
                    }
                    else if(currentOptionA.equals("Hex") && currentOptionB.equals("Decimal")){  //hex --> decimal
                        if(whichOption.equals("optionA")){
                            Double value = hexToDecimal(optionA);
                            textB.setText(""+truncateToNDecimalPlaces(value,4));
                        }
                        else{
                            //decimal --> hex
                            textA.setText(decimalToHex(optionB));
                        }
                    }
                    else{   //Hex --> binary
                        //is really just hex --> decimal, decimal --> binary
//                        textB.setText(decimalToBinary(String.valueOf(hexToDecimal(optionA))));
                        if(whichOption.equals("optionA")){
                            textB.setText(decimalToBinary(String.valueOf(hexToDecimal(optionA))));
                        }
                        else{
                            //binary --> hex
                            textA.setText(decimalToHex(String.valueOf(binaryToDecimal(optionB))));
                        }
                    }

                }
            }
            else{
                showAlertDialog(getResources().getString(R.string.incorrect_format), v);    //"Incorrect format"
            }
        }

        else{   //Non-programming: General unit converter
            Log.i(TAG, "computeResultHelper, General unit converter");
            boolean correctFormat = checkFormat(false, "", "");
            if(correctFormat){
                Log.i(TAG, "computeResultHelper, General unit converter GOOD FORMAT");
                if(currentOptionA.equals(currentOptionB)){
                    showAlertDialog(getResources().getString(R.string.same_category), v);    //"Same category, please choose a different category"
                }
                else{
                        /* 2 Conversions
                            Common unit needs to be small, or else if converting from small form --> large form, it will become 0
                            optionA <==> common unit <==> optionB
                            Common units:
                                1. Weight:          microgram
                                2. Volume:          mL
                                3. Distance:        micrometer
                                4. Temperature:     Celsius

                            Updated:
                            Updated methods to consolidate code and not have to re-copy this entire thing again for optionB implementation
                            createStartingmiddlemanValue ==> gets the value from terms of unit A ==> common unit
                            createEndingmiddlemanValue ==> gets the value from terms of the common unit ==> unit B
                            truncateToNDecimalPlaces: truncates resulting number into only having 4 decimal places
                         */
                      Double mmvalue = 0.0;
                      if(whichOption.equals("optionA")){
                          mmvalue = createStartingmiddlemanValue(currentOptionA, optionA);
                          mmvalue = createEndingmiddlemanValue(currentOptionB, mmvalue);
                          textB.setText(""+truncateToNDecimalPlaces(mmvalue,4));
                      }
                      else{
                          mmvalue = createStartingmiddlemanValue(currentOptionB, optionB);
                          mmvalue = createEndingmiddlemanValue(currentOptionA, mmvalue);
                          textA.setText(""+truncateToNDecimalPlaces(mmvalue,4));
                      }
                }
            }
            else{
                showAlertDialog(getResources().getString(R.string.incorrect_format), v);    //"Incorrect format"
            }
        }
    }

    /*
        Generates the starting middlemanValue for ALL options, despite category
        Aka I dumped all the categories into this method
        May be a little bit insecure if currentOptionA and currentOptionB every have the wrong value

        option: it's either going to be currentOptionA or currentOptionB.


     */
    public Double createStartingmiddlemanValue(String option, String numberInStringForm){
        Double middlemanValue = 0.0;
        Log.i(TAG, "createStartingmiddlemanValue, middlemanValue before: " + middlemanValue);
        switch(option){
            //Weight
            case "Kilogram":        //kg --> microgram
                Log.i(TAG, "createStartingmiddlemanValue, Weight: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double kg = Double.parseDouble(numberInStringForm);
                Log.i(TAG, "createStartingmiddlemanValue, Weight: kg = " + kg);
                middlemanValue = kg * 1000000000;
                break;
            case "Gram":            //g --> microgram
                Log.i(TAG, "createStartingmiddlemanValue, Weight: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double g = Double.parseDouble(numberInStringForm);
                middlemanValue = g * 1000000;
                break;
            case "Milligram":       //mg --> microgram
                Log.i(TAG, "createStartingmiddlemanValue, Weight: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double mg = Double.parseDouble(numberInStringForm);
                middlemanValue = mg  * 1000;
                break;
            case "Metric ton":      //metric ton --> microgram. 1 metric ton = 1000kg
                Log.i(TAG, "createStartingmiddlemanValue, Weight: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double mton = Double.parseDouble(numberInStringForm);
                middlemanValue = mton * 1000000000000.0;
                break;
            case "Stone":           //stone --> microgram
                Log.i(TAG, "createStartingmiddlemanValue, Weight: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double stone = Double.parseDouble(numberInStringForm);
                middlemanValue = stone * 6350290000.0;   //6350290000
                break;
            case "US Ton":          //us ton --> microgram
                Log.i(TAG, "createStartingmiddlemanValue, Weight: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double uston = Double.parseDouble(numberInStringForm);
                middlemanValue = uston * 907185000000.0;    //907185000000
                break;
            case "Pound":          //pound --> microgram
                Log.i(TAG, "createStartingmiddlemanValue, Weight: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double lb = Double.parseDouble(numberInStringForm);
                middlemanValue = lb * 453592000;
                break;
            case "Ounce":           //ounce --> microgram
                Log.i(TAG, "createStartingmiddlemanValue, Weight: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double oz = Double.parseDouble(numberInStringForm);
                middlemanValue = oz * 28349500;
                break;
//        default: middlemanValue = Double.parseDouble(numberInStringForm); break; //If already in microgram, dont' do anything:

            //Volume
            case "US gallon":       // US gallon --> mLR
                Log.i(TAG, "createStartingmiddlemanValue, Volume: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double gal= Double.parseDouble(numberInStringForm);
                middlemanValue = gal*3785.41;
                break;
            case "US quart":       // US quart --> mL
                Log.i(TAG, "createStartingmiddlemanValue, Volume: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double quart = Double.parseDouble(numberInStringForm);
                middlemanValue = quart * 946.353;
                break;
            case "US pint":       // US pint --> mL
                Log.i(TAG, "createStartingmiddlemanValue, Volume: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double pint = Double.parseDouble(numberInStringForm);
                middlemanValue = pint * 473.176;
                break;
            case "US cup":       // US cup --> mL
                Log.i(TAG, "createStartingmiddlemanValue, Volume: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double cup = Double.parseDouble(numberInStringForm);
                middlemanValue = cup *236.588;
                break;
            case "US fluid ounce":  // US fluid ounce --> mL
                Log.i(TAG, "createStartingmiddlemanValue, Volume: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double fl= Double.parseDouble(numberInStringForm);
                middlemanValue = fl* 29.5735;
                break;
            case "US tbsp":       // US tbsp --> mL
                Log.i(TAG, "createStartingmiddlemanValue, Volume: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double tbsp = Double.parseDouble(numberInStringForm);
                middlemanValue = tbsp * 14.7868;
                break;
            case "US tsp":       // US tsp --> mL
                Log.i(TAG, "createStartingmiddlemanValue, Volume: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double tsp = Double.parseDouble(numberInStringForm);
                middlemanValue = tsp * 4.92892;
                break;
            case "Liters":       // Liters --> mL
                Log.i(TAG, "createStartingmiddlemanValue, Volume: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double liters= Double.parseDouble(numberInStringForm);
                middlemanValue = liters *1000;
                break;
//        default: middlemanValue = Double.parseDouble(numberInStringForm); break; //If already in microgram, dont' do anything:

            //Distance
            case "Kilometer":   //km --> micrometer
                Log.i(TAG, "createStartingmiddlemanValue, Distance: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double km = Double.parseDouble(numberInStringForm);
                middlemanValue = km*1000000000;
                break;
            case "Meter":   //km --> micrometer
                Log.i(TAG, "createStartingmiddlemanValue, Distance: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double m = Double.parseDouble(numberInStringForm);
                middlemanValue = m*1000000;
                break;
            case "Centimeter":  //cm --> micrometer
                Log.i(TAG, "createStartingmiddlemanValue, Distance: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double cm = Double.parseDouble(numberInStringForm);
                middlemanValue = cm*10000;
                break;
            case "Millimeter":  //mm --> micrometer
                Log.i(TAG, "createStartingmiddlemanValue, Distance: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double mm = Double.parseDouble(numberInStringForm);
                middlemanValue = mm*1000;
                break;
            case "Nanometer":   //nm --> micrometer
                Log.i(TAG, "createStartingmiddlemanValue, Distance: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double nm = Double.parseDouble(numberInStringForm);
                middlemanValue = nm/1000;
                break;
            case "Knots":   //knots --> micrometer
                Log.i(TAG, "createStartingmiddlemanValue, Distance: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double knots = Double.parseDouble(numberInStringForm);
                middlemanValue = knots * 1852000000;
                break;
            case "Mile":    //mile --> micrometer
                Log.i(TAG, "createStartingmiddlemanValue, Distance: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double mile = Double.parseDouble(numberInStringForm);
                middlemanValue = mile * 1609340000;
                break;
            case "Yard":    //yard --> micrometer
                Log.i(TAG, "createStartingmiddlemanValue, Distance: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double yard = Double.parseDouble(numberInStringForm);
                middlemanValue = yard * 914400;
                break;
            case "Feet":    //feet --> micrometer
                Log.i(TAG, "createStartingmiddlemanValue, Distance: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double feet = Double.parseDouble(numberInStringForm);
                middlemanValue = feet * 304800;
                break;
            case "Inch":    //inch --> micrometer
                Log.i(TAG, "createStartingmiddlemanValue, Distance: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double inch = Double.parseDouble(numberInStringForm);
                middlemanValue = inch * 25400;
                break;
//        default: middlemanValue = Double.parseDouble(numberInStringForm); break; //If already in microgram, dont' do anything:

            //Temperature
            case "Fahrenheit":  //fahrenheit --> celsius
                Log.i(TAG, "createStartingmiddlemanValue, Temperature: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double dFahr = Double.parseDouble(numberInStringForm);
                middlemanValue = dFahr - 32.0;
                middlemanValue *= 5.0;
                middlemanValue /= 9.0;
                break;
            case "Kelvin":  //kelvin --> celsius
                Log.i(TAG, "createStartingmiddlemanValue, Temperature: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                Double kelvin = Double.parseDouble(numberInStringForm);
                middlemanValue = kelvin - 273.15;
                break;
            default: //If already in celsius, don't do anything
                Log.i(TAG, "createStartingmiddlemanValue, Temperature: option = " + currentOptionA + ", numberInStringForm: " + numberInStringForm);
                middlemanValue = Double.parseDouble(numberInStringForm);
                break;
        }
        Log.i(TAG, "createStartingmiddlemanValue, middlemanValue after: " + middlemanValue);
        return middlemanValue;

    }

    /*
        Generates the ending middlemanValue for ALL options, despite category
        Aka I dumped all the categories into this method
        May be a little bit insecure if currentOptionA and currentOptionB every have the wrong value

        option: it's either going to be optionA or optionB.
        The beauty of putting it inside the parameter ==> don't have to check inside whether it's optionA or optionB

        middlemanValue: The middlemanValue generated from createStartingmiddlemanValue
     */
    public Double createEndingmiddlemanValue(String option, Double middlemanValue){
        Log.i(TAG, "createEndingmiddlemanValue, middlemanValue before: " + middlemanValue);
        switch (option){

            //Weight
            case "Kilogram":        //microgram --> kg
                Log.i(TAG, "createEndingmiddlemanValue, Weight: OptionB = " + currentOptionB);
                middlemanValue /= 1000000000;
                break;
            case "Gram":            //microgram --> g
                Log.i(TAG, "createEndingmiddlemanValue, Weight: OptionB = " + currentOptionB);
                middlemanValue /= 1000000;
                break;
            case "Milligram":       //microgram --> mg
                Log.i(TAG, "createEndingmiddlemanValue, Weight: OptionB = " + currentOptionB);
                middlemanValue /= 1000;
                break;
            case "Metric ton":      //microgram --> metric ton
                Log.i(TAG, "createEndingmiddlemanValue, Weight: OptionB = " + currentOptionB);
                middlemanValue /= 1000000000000.0;
                break;
            case "Stone":           //microgram --> stone
                Log.i(TAG, "createEndingmiddlemanValue, Weight: OptionB = " + currentOptionB);
                middlemanValue /= 6350290000.0;   //6350290000
                break;
            case "US Ton":          //microgram --> us ton
                Log.i(TAG, "createEndingmiddlemanValue, Weight: OptionB = " + currentOptionB);
                middlemanValue /= 907185000000.0;    //907185000000
                break;
            case "Pound":          //microgram --> pound
                Log.i(TAG, "createEndingmiddlemanValue, Weight: OptionB = " + currentOptionB);
                middlemanValue /= 453592000;
                break;
            case "Ounce":           //microgram --> ounce
                Log.i(TAG, "createEndingmiddlemanValue, Weight: OptionB = " + currentOptionB);
                middlemanValue /= 28349500;
                break;
//            default: break; //If already in microgram, just set text

            //Volume
            case "US gallon":       // mL --> US gallon
                Log.i(TAG, "createEndingmiddlemanValue, Volume: OptionB = " + currentOptionB);
                middlemanValue /= 3785.41;
                break;
            case "US quart":       // mL --> US quart
                Log.i(TAG, "createEndingmiddlemanValue, Volume: OptionB = " + currentOptionB);
                middlemanValue /= 946.353;
                break;
            case "US pint":       //  mL --> US pint
                Log.i(TAG, "createEndingmiddlemanValue, Volume: OptionB = " + currentOptionB);
                middlemanValue /= 473.176;
                break;
            case "US cup":       //  mL --> US cup
                Log.i(TAG, "createEndingmiddlemanValue, Volume: OptionB = " + currentOptionB);
                middlemanValue /= 236.588;
                break;
            case "US fluid ounce":  //  mL --> S fluid ounce L
                Log.i(TAG, "createEndingmiddlemanValue, Volume: OptionB = " + currentOptionB);
                middlemanValue /= 29.5735;
                break;
            case "US tbsp":       //  mL --> US tbsp
                Log.i(TAG, "createEndingmiddlemanValue, Volume: OptionB = " + currentOptionB);
                middlemanValue /= 14.7868;
                break;
            case "US tsp":       //  mL --> US tsp
                Log.i(TAG, "createEndingmiddlemanValue, Volume: OptionB = " + currentOptionB);
                middlemanValue /= 4.92892;
                break;
            case "Liters":       //  mL --> Liters
                Log.i(TAG, "createEndingmiddlemanValue, Volume: OptionB = " + currentOptionB);
                middlemanValue /= 1000;
                break;
//            default: break; //If already in mL, just set text

            //Distance
            case "Kilometer":   //micrometer --> km
                Log.i(TAG, "createEndingmiddlemanValue, Distance: OptionB = " + currentOptionB);
                middlemanValue /= 1000000000;
                break;
            case "Meter":       //micrometer --> m
                Log.i(TAG, "createEndingmiddlemanValue, Distance: OptionB = " + currentOptionB);
                middlemanValue /= 1000000;
                break;
            case "Centimeter":  //micrometer --> cm
                Log.i(TAG, "createEndingmiddlemanValue, Distance: OptionB = " + currentOptionB);
                middlemanValue /= 10000;
                break;
            case "Millimeter":  //micrometer --> mm
                Log.i(TAG, "createEndingmiddlemanValue, Distance: OptionB = " + currentOptionB);
                middlemanValue /= 1000;
                break;
            case "Nanometer":   //micrometer --> nm
                Log.i(TAG, "createEndingmiddlemanValue, Distance: OptionB = " + currentOptionB);
                middlemanValue *= 1000;
                break;
            case "Knots":       //micrometer --> knots
                Log.i(TAG, "createEndingmiddlemanValue, Distance: OptionB = " + currentOptionB);
                middlemanValue /= 1852000000;
                break;
            case "Mile":        //micrometer --> mile
                Log.i(TAG, "createEndingmiddlemanValue, Distance: OptionB = " + currentOptionB);
                middlemanValue /= 1609340000;
                break;
            case "Yard":        //micrometer --> yard
                Log.i(TAG, "createEndingmiddlemanValue, Distance: OptionB = " + currentOptionB);
                middlemanValue /= 914400;
                break;
            case "Feet":        //micrometer --> feet
                Log.i(TAG, "createEndingmiddlemanValue, Distance: OptionB = " + currentOptionB);
                middlemanValue /= 304800;
                break;
            case "Inch":        //micrometer --> inch
                Log.i(TAG, "createEndingmiddlemanValue, Distance: OptionB = " + currentOptionB);
                middlemanValue /= 25400;
                break;
//            default: break; //If already in nanometer, just set text

            //Temperature
            case "Fahrenheit":  //celsius --> fahrenheit
                Log.i(TAG, "createEndingmiddlemanValue, Temperature: OptionB = " + currentOptionB);
                middlemanValue *= 9.0;
                middlemanValue /= 5.0;
                middlemanValue += 32;
                break;
            case "Kelvin":  //celsius to kelvin
                Log.i(TAG, "createEndingmiddlemanValue, Temperature: OptionB = " + currentOptionB);
                middlemanValue += 273.15;
                break;
            default:
                Log.i(TAG, "createEndingmiddlemanValue, Temperature: OptionB = " + currentOptionB);
                break; //If already celsius, just set text
        }

        Log.i(TAG, "createEndingmiddlemanValue, middlemanValue after: " + middlemanValue);
        return middlemanValue;
    }





}
