package com.example.bryan.unitconverter20;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bryan on 8/19/2016.
 * Only show years, months, days. Keep it simple, cause that's how it's most effective.
 * Too hard to understand ==> user gives up easily
 */
public class DaysUntilFragment extends Fragment {
    private static final String TAG = "UnitConverterTag";
    private TextView from;
    private TextView starting_day;
    private TextView to;
    private TextView ending_day;
    private CheckBox checkBox;
    private TextView result_text;
    private TextView result_text_details;

    private Calendar startingDate;
    private Calendar endingDate;
    private ImageButton calendar1;
    private ImageButton calendar2;
    private DatePickerDialog calendar1datePickerDialog;
    private DatePickerDialog calendar2datePickerDialog;

    private SimpleDateFormat dateFormatter;

    private Button compute;

    private boolean isChecked = false;      //For Checkbox

    //AlertDialog1 stuff: starting
    private Spinner days_spinner;           //need to be declared here globally so it doesn't give us the error that prompts us to set it to final
    private Spinner month_spinner;          //need to be declared here globally so it doesn't give us the error that prompts us to set it to final
    private Spinner years_spinner;          //need to be declared here globally so it doesn't give us the error that prompts us to set it to final
    private View alertDialogCustomView_starting;     //need to be declared here globally so it doesn't give us the error that prompts us to set it to final
    private String alert_dialog_starting_current_day =""; //01
    private String alert_dialog_starting_current_month =""; //Still a word ("Jan.") anymore. The ACTUAL value of the month, not the Android definition.
    private String alert_dialog_starting_current_year =""; //11
    private boolean isDaysAdapterSet_starting = false;  //false

    //AlertDialog2 stuff: ending
    private Spinner ending_days_spinner;           //need to be declared here globally so it doesn't give us the error that prompts us to set it to final
    private Spinner ending_month_spinner;          //need to be declared here globally so it doesn't give us the error that prompts us to set it to final
    private Spinner ending_years_spinner;          //need to be declared here globally so it doesn't give us the error that prompts us to set it to final
    private View alertDialogCustomView_ending;     //need to be declared here globally so it doesn't give us the error that prompts us to set it to final
    private String alert_dialog_ending_current_day =""; //01
    private String alert_dialog_ending_current_month =""; //Still a word ("Jan.") anymore. The ACTUAL value of the month, not the Android definition.
    private String alert_dialog_ending_current_year =""; //11
    private boolean isDaysAdapterSet_ending = false;    //false


    //Dynamic arraylists for dynamic adding in of days depending on month,year
    private ArrayList<String> days_28 = new ArrayList<String>();
    private ArrayList<String> days_29 = new ArrayList<String>();
    private ArrayList<String> days_30 = new ArrayList<String>();
    private ArrayList<String> days_31 = new ArrayList<String>();


    public DaysUntilFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.daysuntil_fragment_layout, container, false);
        dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

        //Instantiate Calendar instances first so we can set the texts of startingDay and endingDay to be the current day
        //Also, make endingDate be a clone() of startingDate so that their millisecond values will be exactly the same
        //starting day
        startingDate = Calendar.getInstance();
        endingDate = (Calendar)startingDate.clone();

        //Set some elements from xml layout file so we they can be used for the logic of the instance state restore
        from = (TextView)rootView.findViewById(R.id.from);
        starting_day = (TextView)rootView.findViewById(R.id.starting_day);  //Need to set this text to be the current day, by default;
        to = (TextView)rootView.findViewById(R.id.to);
        ending_day = (TextView)rootView.findViewById(R.id.ending_day);   //Need to set this text to be the current day, by default

        result_text = (TextView)rootView.findViewById(R.id.result_text);   //Need to set this text to be the BLANK by default. Also, create some method to set the text in the correct format
        result_text_details = (TextView)rootView.findViewById(R.id.result_text_details); //Need to set this text to be the BLANK by default

        checkBox = (CheckBox)rootView.findViewById(R.id.checkBox);  //If checked, include ending day ==> +1 day
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://developer.android.com/guide/topics/ui/controls/checkbox.html
                //Set the global boolean variable isChecked
                //this will be used later in the date calculation
                //Since there's only 1 checkbox, don't have to worry about the getting the id
                isChecked = ((CheckBox)v).isChecked();
                Log.i(TAG, "New isChecked: " + isChecked);
            }
        });

        //Debug for when I was implementing configuration save states: Checking if stuff is empty
//        if(days_28.isEmpty()){
//            Log.i(TAG, "days_28 is empty");
//        }
//        else{
//            Log.i(TAG, "days_28 is NOT empty");
//        }
//        if(days_29.isEmpty()){
//            Log.i(TAG, "days_29 is empty");
//        }
//        else{
//            Log.i(TAG, "days_29 is NOT empty");
//        }
//        if(days_30.isEmpty()){
//            Log.i(TAG, "days_30 is empty");
//        }
//        else{
//            Log.i(TAG, "days_30 is NOT empty");
//        }
//        if(days_31.isEmpty()){
//            Log.i(TAG, "days_31 is empty");
//        }
//        else{
//            Log.i(TAG, "days_31 is NOT empty");
//        }
//        if(days_spinner != null){
//            Log.i(TAG, "days spinner is not null");
//        }
//        else{
//            Log.i(TAG, "days spinner is null");
//        }
//        if(month_spinner != null){
//            Log.i(TAG, "month spinner is not null");
//        }
//        else{
//            Log.i(TAG, "month spinner is null");
//        }
//        if(years_spinner != null){
//            Log.i(TAG, "years spinner is not null");
//        }
//        else{
//            Log.i(TAG, "years spinner is null");
//        }
//        if(ending_days_spinner != null){
//            Log.i(TAG, "e days spinner is not null");
//        }
//        else{
//            Log.i(TAG, "e days spinner is null");
//        }
//        if(ending_month_spinner != null){
//            Log.i(TAG, "e month spinner is not null");
//        }
//        else{
//            Log.i(TAG, "e month spinner is null");
//        }
//        if(ending_years_spinner != null){
//            Log.i(TAG, "e year spinner is not null");
//        }
//        else{
//            Log.i(TAG, "e year spinner is null");
//        }

        Bundle utilitiesBundle = Utilities.getBundleFromDaysUntilFragment();
        if(utilitiesBundle == null){
            Log.i(TAG,"Utilities bundle is null");
        }
        else{
            Log.i(TAG,"Utilities bundle HERE AND NOT NULL");
        }
        if( savedInstanceState != null || utilitiesBundle != null){
            /* Problem
               DaysUntilFragment loaded ==> then fragment change to UnitFragment ==> then fragment change back to daysUntilFragment
               Means that values are not null, but savedInstanceState == null

               Solution:
               Make a Utilities class that houses a global variable
               And our new if conditional will be: if(savedInstanceState != null || <some condition on the global variable>)
             */

            //In the case where a fragment change back to daysUntilFragment, meaning that values are not null, but savedInstanceState == null
            if(savedInstanceState == null && utilitiesBundle != null){
                //get the bundle from the Utilities class ==> make it to be savedInstanceState
                Log.i(TAG,"savedInstanceState == null, so we must get from Utilities, then restore");
                savedInstanceState = Utilities.getBundleFromDaysUntilFragment();
            }
            else{
                Log.i(TAG,"savedInstanceState != null, not getting from Utilities, so we must restore");
            }



            //restore stuff
            //1. ArrayLists so we don't have to recreate them
            days_28 = savedInstanceState.getStringArrayList("days_28");
            days_29 = savedInstanceState.getStringArrayList("days_29");
            days_30 = savedInstanceState.getStringArrayList("days_30");
            days_31 = savedInstanceState.getStringArrayList("days_31");

            //2. alert dialog values
            alert_dialog_starting_current_day = savedInstanceState.getString("alert_dialog_starting_current_day");
            alert_dialog_starting_current_month = savedInstanceState.getString("alert_dialog_starting_current_month");  //Still a word. The ACTUAL value of the month, not the Android definition.
            alert_dialog_starting_current_year = savedInstanceState.getString("alert_dialog_starting_current_year");
            isDaysAdapterSet_starting = savedInstanceState.getBoolean("isDaysAdapterSet_starting");

            alert_dialog_ending_current_day = savedInstanceState.getString("alert_dialog_ending_current_day");
            alert_dialog_ending_current_month = savedInstanceState.getString("alert_dialog_ending_current_month");  //Still a word. The ACTUAL value of the month, not the Android definition.
            alert_dialog_ending_current_year = savedInstanceState.getString("alert_dialog_ending_current_year");
            isDaysAdapterSet_ending = savedInstanceState.getBoolean("isDaysAdapterSet_ending");

            //3. set calendars to their correct dates
            //remember, month needs to be set to month-1, so that it can be in Android value
            //Basic format: set(year, month-1, day);
            startingDate.set(Integer.parseInt(alert_dialog_starting_current_year),
                    convertWordMonth(alert_dialog_starting_current_month) -1,
                    Integer.parseInt(alert_dialog_starting_current_day));

            endingDate.set(Integer.parseInt(alert_dialog_ending_current_year),
                    convertWordMonth(alert_dialog_ending_current_month) -1,
                    Integer.parseInt(alert_dialog_ending_current_day));

            //4. Set textViews (startingDay, endingDay) to date specified
            //CHANGED: It turns out, we can set the TextViews outside of the if-else conditional b/c either way. startingDate/endingDate will already be set with the correct value
//            starting_day.setText(alert_dialog_starting_current_month + "-" + alert_dialog_starting_current_day
//                            + "-" + alert_dialog_starting_current_year);
//            ending_day.setText(alert_dialog_ending_current_month + "-" + alert_dialog_ending_current_day
//                    + "-" + alert_dialog_ending_current_year);

            //5. result text and result details text
            result_text.setText(savedInstanceState.getString("result_text"));
            result_text_details.setText(savedInstanceState.getString("result_text_details"));

            //6. Set spinners adapters to be null, so they don't keep compounding on each other
            //  Once an AlertDIalog is created, the Spinner objects stay alive forever.
            //  However, there is the case situation where the user doesn't do anything but orientation change ==> spinners stay null
            //  Thus, we would only check if the Spinner is not null ==> then set its adapter to null
            if(days_spinner != null){
                Log.i(TAG, "days spinner is not null");
                days_spinner.setAdapter(null);
            }
            if(month_spinner != null){
                Log.i(TAG, "month spinner is not null");
                month_spinner.setAdapter(null);
            }
            if(years_spinner != null){
                Log.i(TAG, "years spinner is not null");
                years_spinner.setAdapter(null);
            }
            if(ending_days_spinner != null){
                Log.i(TAG, "e days spinner is not null");
                ending_days_spinner.setAdapter(null);
            }
            if(ending_month_spinner != null){
                Log.i(TAG, "e month spinner is not null");
                ending_month_spinner.setAdapter(null);
            }
            if(ending_years_spinner != null){
                Log.i(TAG, "e year spinner is not null");
                ending_years_spinner.setAdapter(null);
            }

            //7. Checkbox's isChecked
            isChecked = savedInstanceState.getBoolean("isChecked");
            checkBox.setChecked(isChecked);

        }
        else{
            Log.i(TAG,"savedInstanceState == null, so we must create original values");
            /* Problem
               DaysUntilFragment loaded ==> then fragment change to UnitFragment ==> then fragment change back to daysUntilFragment
               Means that values are not null, but savedInstanceState == null
             */

            /* Create original values of the following:
            1. ArrayLists (days_28, days_29, days_30, days_31)
            2. Alert Dialog values (to be today). No need to set isAdapterSet boolean values.
            3. No need to set calendars, b/c they're already on today's date
            4. Set textViews (startingDay, endingDay) to current date
             */

            /* 1. Create ArrayLists
            Instead of grabbing a static array list, we dynamically create 4 arrays for the arrayadapter
            days_28: 01,02,03....28
            days_29: 01,02,03....29
            days_30: 01,02,03....30
            days_31: 01,02,03....31
            */
            for(int i = 1; i <= 31; i++){
                String inside = "";
                if(i < 10){
                    inside = "0"+i;
                }
                else{
                    inside = ""+i;
                }

                if(i > 28){
                    if(i == 29){
                        days_29.add(inside);
                        days_30.add(inside);
                        days_31.add(inside);
                    }
                    else if (i == 30){
                        days_30.add(inside);
                        days_31.add(inside);
                    }
                    else{   //i == 31
                        days_31.add(inside);
                    }
                }
                else{   //27 or less, every array gets a number
                    days_28.add(inside);
                    days_29.add(inside);
                    days_30.add(inside);
                    days_31.add(inside);
                }
            }

            //2. Fill in AlertDialog values for today
            alert_dialog_starting_current_day = "" + startingDate.get(Calendar.DAY_OF_MONTH);
            alert_dialog_starting_current_month = convertnumberMonthWord(startingDate.get(Calendar.MONTH) + 1);  //have to +1 b/c Android's values are 1 behind. Ex: January is 0
            alert_dialog_starting_current_year = "" + startingDate.get(Calendar.YEAR);

            alert_dialog_ending_current_day = "" + endingDate.get(Calendar.DAY_OF_MONTH);
            alert_dialog_ending_current_month = convertnumberMonthWord(endingDate.get(Calendar.MONTH) + 1);  //have to +1 b/c Android's values are 1 behind. Ex: January is 0
            alert_dialog_ending_current_year = "" + endingDate.get(Calendar.YEAR);

            //4. TextViews will be set outside below b/c it doesn't matter whether we had an instance state restore or not,
            //   startingDate, endingDate will be set already

            //5. result text and result details text
            result_text.setText("");
            result_text_details.setText("");

//            //6. Set spinners adapters to be null, so they don't keep compounding on each other
//            //  Once an AlertDIalog is created, the Spinner objects stay alive forever.
//            //  However, there is the case situation where the user CHANGES TO A DIFFERENT FRAGMENT. However, the spinner objects still stay alive
//            //  This means that savedInstanceState == null ===> so it would go inside this else conditional
//            //  Still only check if the Spinner is not null ==> then set its adapter to null
//            if(days_spinner != null){
//                Log.i(TAG, "days spinner is not null");
//                days_spinner.setAdapter(null);
//            }
//            if(month_spinner != null){
//                Log.i(TAG, "month spinner is not null");
//                month_spinner.setAdapter(null);
//            }
//            if(years_spinner != null){
//                Log.i(TAG, "years spinner is not null");
//                years_spinner.setAdapter(null);
//            }
//            if(ending_days_spinner != null){
//                Log.i(TAG, "e days spinner is not null");
//                ending_days_spinner.setAdapter(null);
//            }
//            if(ending_month_spinner != null){
//                Log.i(TAG, "e month spinner is not null");
//                ending_month_spinner.setAdapter(null);
//            }
//            if(ending_years_spinner != null){
//                Log.i(TAG, "e year spinner is not null");
//                ending_years_spinner.setAdapter(null);
//            }

        }

        //4. Set textViews (startingDay, endingDay) to current date
        starting_day.setText(dateFormatter.format(startingDate.getTime())); //Set starting_day's text to be the current day by default
        ending_day.setText(dateFormatter.format(endingDate.getTime())); //Set ending_day's text to be the current day by default



        //Why is it showing up as 8/5/16 instead of 9/5/16 (correct date)?
        //https://developer.android.com/reference/java/util/Calendar.html#MONTH
        /* Aka, in Android calendar, January is stored as "0" ==> thus all months are -1
          Thus we need to internally, we need to do this on (calendar_object).set(year,month-1,day) instead
         */
        Log.i(TAG, "Before anything: startingDate's year: " + startingDate.get(Calendar.YEAR));
        Log.i(TAG,"Before anything: startingDate's month: " + startingDate.get(Calendar.MONTH));
        Log.i(TAG, "Before anything: startingDate's day: " + startingDate.get(Calendar.DAY_OF_MONTH));
        Log.i(TAG, "Before anything: endingDate's year: " + endingDate.get(Calendar.YEAR));
        Log.i(TAG,"Before anything: endingDate's month: " + endingDate.get(Calendar.MONTH));
        Log.i(TAG, "Before anything: endingDate's day: " + endingDate.get(Calendar.DAY_OF_MONTH));

        //These elements moved up to the top so we could do the instance restore
//        from = (TextView)rootView.findViewById(R.id.from);
//        starting_day = (TextView)rootView.findViewById(R.id.starting_day);  //Need to set this text to be the current day, by default;
//        starting_day.setText(dateFormatter.format(startingDate.getTime())); //Set starting_day's text to be the current day by default

        //Set rest of elements from xml layout file
        starting_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "New AlertDialog: startingDay");
                //Open up a new AlertDialog, which has Spinners for Day, Month, Year, save button, and cancel button
                //Once user presses save button ==> exit menu and save the date
                //Once user presses cancel button ==> simply exit
                //need to use rootView.getContext() instead of getActivity().getApplicationContext()
                //https://www.mkyong.com/android/android-alert-dialog-example/
                //https://developer.android.com/guide/topics/ui/dialogs.html
                //http://www.mkyong.com/android/android-prompt-user-input-dialog-example/
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                //Get alert_dialog_customs_layout.xml View
                LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
                alertDialogCustomView_starting = layoutInflater.inflate(R.layout.alert_dialog_custom_layout,null);

                //Set AlertDialog.Builder's content view to be the view
                builder.setView(alertDialogCustomView_starting);

                /*
                    I ended up not doing the idea where I would dynamically fill in the days spinner with values depending on the months spinner.
                    This is b/c in the case of the leap year, we would need to call GregorianCalendar's isLeapYear() method, which would require us
                    having the values year,month,day from the original Calendar in order to create the GregorianCalendar instance. Which we can't get
                    just from the year

                    Problem:
                    However, this leaves a kind of big problem, as we're giving the user some options that will fail. Which is BAD.
                    But there's not really any other way.

                    solution:
                    Actually I just thought about this before I went to sleep. The main thing hindering this was the situation that we wouldn't be able to
                    get the year,month,day values at the same time, because some of them would be null. Well, I thought, why don't we just set default values
                    to the year,month,day initially on startup?

                    Flowchart:
                    1. Year--> dynamically affects days (in the case of a leap year). Aka if month == 2 and its a leap year
                    2. Month --> dynamically affects days.
                    Thus we would set the year component first, then the month component, then the days component
                 */
                //Get the Spinner components and set their associated adapters, which are created to hold a list of items specified in strings.xml
                //Have to use alertDialogCustomView_starting instead of View v, b/c that's the View that actually contains the Spinners

                //First, print out current status of variables
                Log.i(TAG,"Before anything: alert_dialog_starting_current_day: " + alert_dialog_starting_current_day);
                Log.i(TAG,"Before anything: alert_dialog_starting_current_month: " + alert_dialog_starting_current_month);
                Log.i(TAG,"Before anything: alert_dialog_starting_current_year: " + alert_dialog_starting_current_year);

//                Spinner years_spinner = (Spinner)alertDialogCustomView_starting.findViewById(R.id.alert_dialog_years);
//                Spinner month_spinner = (Spinner)alertDialogCustomView_starting.findViewById(R.id.alert_dialog_months);
//                Spinner days_spinner = (Spinner)alertDialogCustomView_starting.findViewById(R.id.alert_dialog_days);
                 years_spinner = (Spinner)alertDialogCustomView_starting.findViewById(R.id.alert_dialog_years);
                 month_spinner = (Spinner)alertDialogCustomView_starting.findViewById(R.id.alert_dialog_months);
                 days_spinner = (Spinner)alertDialogCustomView_starting.findViewById(R.id.alert_dialog_days);

                //3. Years
                ArrayAdapter<String> yearsAdapter = new ArrayAdapter<String>(
                        alertDialogCustomView_starting.getContext(), R.layout.dropdown_item, getResources().getStringArray(R.array.alert_dialog_years));
                yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                years_spinner.setAdapter(yearsAdapter);  //set the Adapter
                years_spinner.setSelection(yearsAdapter.getPosition(alert_dialog_starting_current_year));  //set initial default value to be the current year, or the year it was on before the configuration change. -1 b/c setSelection(0) is the first element
                years_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    /*
                        1. Set the year
                        2. Check if it's a leap year and if the month is 2. If it is,
                     */
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.i(TAG, "AlertDialog startingDay: years spinner");
                        alert_dialog_starting_current_year = parent.getItemAtPosition(position).toString();
                        int year = Integer.parseInt(alert_dialog_starting_current_year);
                        int month = convertWordMonth(alert_dialog_starting_current_month);
                        int day = Integer.parseInt(alert_dialog_starting_current_day);
                        GregorianCalendar gcal = new GregorianCalendar(year,month,day);
                        if(gcal.isLeapYear(year) && month == 2){
                            //load days_29 into day spinner
                            //but first, clear the spinner of all entries
                            days_spinner.setAdapter(null);

                            ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>(
                                    alertDialogCustomView_starting.getContext(), R.layout.dropdown_item, days_29);
                            daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            days_spinner.setAdapter(daysAdapter);  //set the Adapter

                            if(day > 29){
                                //if day is over 29, fix it
                                alert_dialog_starting_current_day = "29";
                            }
                            days_spinner.setSelection(Integer.parseInt(alert_dialog_starting_current_day) - 1); //-1 because setSelection(0) gets the first entry, and the first entry is "01"

                            isDaysAdapterSet_starting = true;    //notify that the days adapter has been set, so we don't have to reset it later
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                //2. Months

                ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(
                        alertDialogCustomView_starting.getContext(), R.layout.dropdown_item, getResources().getStringArray(R.array.alert_dialog_months));
                monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                month_spinner.setAdapter(monthAdapter);  //set the Adapter
                month_spinner.setSelection(convertWordMonth(alert_dialog_starting_current_month) - 1);  //set initial default value to be the current month, or the month it was on before the configuration change. -1 b/c setSelection(0) is the first element
                month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    /*
                        1. Set the month
                        2. For that month, load the correct # of days into the days adapter
                     */
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.i(TAG, "AlertDialog startingDay: months spinner");
                        alert_dialog_starting_current_month = parent.getItemAtPosition(position).toString();
                        int month = convertWordMonth(alert_dialog_starting_current_month);
                        int day = Integer.parseInt(alert_dialog_starting_current_day);

                        //but first, clear the spinner of all entries
                        days_spinner.setAdapter(null);

                        if(month == 2 || month == 4 || month == 6 || month == 9 || month == 11){  //months with only 30 days
                            if(month == 2){
                                //regularly, february only has 28 days

                                ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>(
                                        alertDialogCustomView_starting.getContext(), R.layout.dropdown_item, days_28);
                                daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                days_spinner.setAdapter(daysAdapter);  //set the

                                //now, we set the current day and fix it if necessary
                                if(day > 28){
                                    //if day is over 29, fix it
                                    alert_dialog_starting_current_day = "28";
                                }
                                days_spinner.setSelection(Integer.parseInt(alert_dialog_starting_current_day) - 1); //-1 because setSelection(0) gets the first entry, and the first entry is "01"
                            }
                            else{
                                ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>(
                                        alertDialogCustomView_starting.getContext(), R.layout.dropdown_item, days_30);
                                daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                days_spinner.setAdapter(daysAdapter);  //set the Adapter

                                //now, we set the current day and fix it if necessary
                                if(day > 30){
                                    //if day is over 29, fix it
                                    alert_dialog_starting_current_day = "30";
                                }
                                days_spinner.setSelection(Integer.parseInt(alert_dialog_starting_current_day) - 1); //-1 because setSelection(0) gets the first entry, and the first entry is "01"
                            }


                        }
                        else{   //months with 31 days
                            ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>(
                                    alertDialogCustomView_starting.getContext(), R.layout.dropdown_item, days_31);
                            daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            days_spinner.setAdapter(daysAdapter);  //set the Adapter
                            days_spinner.setSelection(Integer.parseInt(alert_dialog_starting_current_day) - 1); //-1 because setSelection(0) gets the first entry, and the first entry is "01"

                        }

                        isDaysAdapterSet_starting = true;    //notify that the days adapter has been set, so we don't have to reset it later
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                //1. Days
                //If days adapter hasn't been already set by either month or year, set the array adapter for days
                if(!isDaysAdapterSet_starting){
                    Log.i(TAG, "AlertDialog startingDay: days spinner, hasn't been set before");
                    ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>(
                            alertDialogCustomView_starting.getContext(), R.layout.dropdown_item, getResources().getStringArray(R.array.alert_dialog_days));
                    daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    days_spinner.setAdapter(daysAdapter);  //set the Adapter
                    days_spinner.setSelection(Integer.parseInt(alert_dialog_starting_current_day)-1);  //set initial default value to be the current day, or the day it was on before the configuration change. -1 b/c setSelection(0) is the first element
                }
                days_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.i(TAG, "AlertDialog startingDay: days spinner, already set before");
                        alert_dialog_starting_current_day = parent.getItemAtPosition(position).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });



                //Set title
                builder.setTitle(getResources().getString(R.string.alert_dialog_title));
                builder.setCancelable(false);
//                builder.setMessage("Need some spinners here");

                //set Positive button to be "save"
                builder.setPositiveButton(getResources().getString(R.string.alert_dialog_save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Set the startingDay's text to be the date, then close the AlertDialog
                        //http://stackoverflow.com/questions/4336470/how-do-i-close-an-android-alertdialog
                        setNewDateFromAlertDialog("startingDate");
                        dialog.cancel();
                    }
                });
                //set Negative button to be "cancel"
                builder.setNegativeButton(getResources().getString(R.string.alert_dialog_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the AlertDialog
                        //http://stackoverflow.com/questions/4336470/how-do-i-close-an-android-alertdialog
                        dialog.cancel();
                    }
                });

                //Create AlertDialog from the AlertDialog.Builder ==> then show
                AlertDialog alert = builder.create();
                alert.show();



            }
        });

        //need to use rootView.getContext() instead of getActivity().getApplicationContext()
        calendar1datePickerDialog = new DatePickerDialog(rootView.getContext(), new DatePickerDialog.OnDateSetListener() {
            /*
                1. set Calendar object endingDate to be the new data
                2. set textview to have the appropriate values
                3. Set starting AlertDialog values so they can be updated ==> thus saved later in onSaveInstance()
             */
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
//                starting_day.setText(dateFormatter.format(newDate.getTime()));
                startingDate.set(year, monthOfYear, dayOfMonth);
                starting_day.setText(dateFormatter.format(startingDate.getTime()));
                alert_dialog_starting_current_year = ""+year;
                alert_dialog_starting_current_month= convertnumberMonthWord(monthOfYear+1);
                alert_dialog_starting_current_day  = ""+dayOfMonth;
                Log.i(TAG, "New date on startingDate, year: " + startingDate.get(Calendar.YEAR));
                Log.i(TAG, "New date on startingDate, month: " + startingDate.get(Calendar.MONTH));
                Log.i(TAG, "New date on startingDate, day: " + startingDate.get(Calendar.DAY_OF_MONTH));
                Log.i(TAG, "New date on startingDay, in milliseconds: " + startingDate.getTimeInMillis());
            }
        }, startingDate.get(Calendar.YEAR), startingDate.get(Calendar.MONTH), startingDate.get(Calendar.DAY_OF_MONTH));


        calendar1 = (ImageButton)rootView.findViewById(R.id.calendar1);
        calendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open up a new menu with a CalendarView inside it. Also have a save button
                //Once user presses save button ==> exit menu and save the date
                //http://androidopentutorials.com/android-datepickerdialog-on-edittext-click-event/
                //Use a DatePickerDialog instead. May have to implement onSaveInstanceState
                //Use constructor
                // DatePickerDialog(Context context, DatePickerDialog.OnDateSetListener listener, int year, int month, int dayOfMonth)

                calendar1datePickerDialog.show();
            }
        });


//        to = (TextView)rootView.findViewById(R.id.to);
//
//        ending_day = (TextView)rootView.findViewById(R.id.ending_day);   //Need to set this text to be the current day, by default
//        ending_day.setText(dateFormatter.format(endingDate.getTime())); //Set ending_day's text to be the current day by default
        ending_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "New AlertDialog: endingDay");
                //Open up a new AlertDialog, which has Spinners for Day, Month, Year, save button, and cancel button
                //Once user presses save button ==> exit menu and save the date
                //Once user presses cancel button ==> simply exit
                //need to use rootView.getContext() instead of getActivity().getApplicationContext()
                //https://www.mkyong.com/android/android-alert-dialog-example/
                //https://developer.android.com/guide/topics/ui/dialogs.html
                //http://www.mkyong.com/android/android-prompt-user-input-dialog-example/
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                //Get alert_dialog_customs_layout.xml View
                LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
                alertDialogCustomView_ending = layoutInflater.inflate(R.layout.alert_dialog_custom_layout,null);

                //Set AlertDialog.Builder's content view to be the view
                builder.setView(alertDialogCustomView_ending);

                /*
                  Exact same thing as startingDay dialog
                */

                //First, print out current status of variables
                Log.i(TAG,"Before anything: alert_dialog_ending_current_day: " + alert_dialog_ending_current_day);
                Log.i(TAG,"Before anything: alert_dialog_ending_current_month: " + alert_dialog_ending_current_month);
                Log.i(TAG,"Before anything: alert_dialog_ending_current_year: " + alert_dialog_ending_current_year);

//                Spinner years_spinner = (Spinner)alertDialogCustomView_starting.findViewById(R.id.alert_dialog_years);
//                Spinner month_spinner = (Spinner)alertDialogCustomView_starting.findViewById(R.id.alert_dialog_months);
//                Spinner days_spinner = (Spinner)alertDialogCustomView_starting.findViewById(R.id.alert_dialog_days);
                ending_years_spinner = (Spinner)alertDialogCustomView_ending.findViewById(R.id.alert_dialog_years);
                ending_month_spinner = (Spinner)alertDialogCustomView_ending.findViewById(R.id.alert_dialog_months);
                ending_days_spinner = (Spinner)alertDialogCustomView_ending.findViewById(R.id.alert_dialog_days);

                //3. Years
                ArrayAdapter<String> yearsAdapter = new ArrayAdapter<String>(
                        alertDialogCustomView_ending.getContext(), R.layout.dropdown_item, getResources().getStringArray(R.array.alert_dialog_years));
                yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ending_years_spinner.setAdapter(yearsAdapter);  //set the Adapter
                ending_years_spinner.setSelection(yearsAdapter.getPosition(alert_dialog_ending_current_year));  //set initial default value to be the current year, or the year it was on before the configuration change. -1 b/c setSelection(0) is the first element
                ending_years_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    /*
                        1. Set the year
                        2. Check if it's a leap year and if the month is 2. If it is,
                     */
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.i(TAG, "AlertDialog endingDay: years spinner");
                        alert_dialog_ending_current_year = parent.getItemAtPosition(position).toString();
                        int year = Integer.parseInt(alert_dialog_ending_current_year);
                        int month = convertWordMonth(alert_dialog_ending_current_month);
                        int day = Integer.parseInt(alert_dialog_ending_current_day);
                        GregorianCalendar gcal = new GregorianCalendar(year,month,day);
                        if(gcal.isLeapYear(year) && month == 2){
                            //load days_29 into day spinner
                            //but first, clear the spinner of all entries
                            ending_days_spinner.setAdapter(null);

                            ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>(
                                    alertDialogCustomView_ending.getContext(), R.layout.dropdown_item, days_29);
                            daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            ending_days_spinner.setAdapter(daysAdapter);  //set the Adapter

                            if(day > 29){
                                //if day is over 29, fix it
                                alert_dialog_ending_current_day = "29";
                            }
                            ending_days_spinner.setSelection(Integer.parseInt(alert_dialog_ending_current_day) - 1); //-1 because setSelection(0) gets the first entry, and the first entry is "01"

                            isDaysAdapterSet_ending = true;    //notify that the days adapter has been set, so we don't have to reset it later
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                //2. Months

                ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(
                        alertDialogCustomView_ending.getContext(), R.layout.dropdown_item, getResources().getStringArray(R.array.alert_dialog_months));
                monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ending_month_spinner.setAdapter(monthAdapter);  //set the Adapter
                ending_month_spinner.setSelection(convertWordMonth(alert_dialog_ending_current_month) - 1);  //set initial default value to be the current month, or the month it was on before the configuration change. -1 b/c setSelection(0) is the first element
                ending_month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    /*
                        1. Set the month
                        2. For that month, load the correct # of days into the days adapter
                     */
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.i(TAG, "AlertDialog endingDay: months spinner");
                        alert_dialog_ending_current_month= parent.getItemAtPosition(position).toString();
                        int month = convertWordMonth(alert_dialog_ending_current_month);
                        int day = Integer.parseInt(alert_dialog_ending_current_day);

                        //but first, clear the spinner of all entries
                        ending_days_spinner.setAdapter(null);

                        if(month == 2 || month == 4 || month == 6 || month == 9 || month == 11){  //months with only 30 days
                            if(month == 2){
                                //regularly, february only has 28 days

                                ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>(
                                        alertDialogCustomView_ending.getContext(), R.layout.dropdown_item, days_28);
                                daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ending_days_spinner.setAdapter(daysAdapter);  //set the

                                //now, we set the current day and fix it if necessary
                                if(day > 28){
                                    //if day is over 29, fix it
                                    alert_dialog_ending_current_day = "28";
                                }
                                ending_days_spinner.setSelection(Integer.parseInt(alert_dialog_ending_current_day) - 1); //-1 because setSelection(0) gets the first entry, and the first entry is "01"
                            }
                            else{
                                ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>(
                                        alertDialogCustomView_ending.getContext(), R.layout.dropdown_item, days_30);
                                daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ending_days_spinner.setAdapter(daysAdapter);  //set the Adapter

                                //now, we set the current day and fix it if necessary
                                if(day > 30){
                                    //if day is over 29, fix it
                                    alert_dialog_ending_current_day = "30";
                                }
                                ending_days_spinner.setSelection(Integer.parseInt(alert_dialog_ending_current_day) - 1); //-1 because setSelection(0) gets the first entry, and the first entry is "01"
                            }


                        }
                        else{   //months with 31 days
                            ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>(
                                    alertDialogCustomView_ending.getContext(), R.layout.dropdown_item, days_31);
                            daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            ending_days_spinner.setAdapter(daysAdapter);  //set the Adapter
                            ending_days_spinner.setSelection(Integer.parseInt(alert_dialog_ending_current_day) - 1); //-1 because setSelection(0) gets the first entry, and the first entry is "01"

                        }

                        isDaysAdapterSet_ending = true;    //notify that the days adapter has been set, so we don't have to reset it later
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                //1. Days
                //If days adapter hasn't been already set by either month or year, set the array adapter for days
                if(!isDaysAdapterSet_ending){
                    Log.i(TAG, "AlertDialog startingDay: days spinner, hasn't been set before");
                    ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>(
                            alertDialogCustomView_ending.getContext(), R.layout.dropdown_item, getResources().getStringArray(R.array.alert_dialog_days));
                    daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ending_days_spinner.setAdapter(daysAdapter);  //set the Adapter
                    ending_days_spinner.setSelection(Integer.parseInt(alert_dialog_ending_current_day)-1);  //set initial default value to be the current day, or the day it was on before the configuration change. -1 b/c setSelection(0) is the first element
                }
                ending_days_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.i(TAG, "AlertDialog endingDay: days spinner, already set before");
                        alert_dialog_ending_current_day = parent.getItemAtPosition(position).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });



                //Set title
                builder.setTitle(getResources().getString(R.string.alert_dialog_title));
                builder.setCancelable(false);
//                builder.setMessage("Need some spinners here");

                //set Positive button to be "save"
                builder.setPositiveButton(getResources().getString(R.string.alert_dialog_save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Set the startingDay's text to be the date, then close the AlertDialog
                        //http://stackoverflow.com/questions/4336470/how-do-i-close-an-android-alertdialog
                        setNewDateFromAlertDialog("endingDate");
                        dialog.cancel();
                    }
                });
                //set Negative button to be "cancel"
                builder.setNegativeButton(getResources().getString(R.string.alert_dialog_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the AlertDialog
                        //http://stackoverflow.com/questions/4336470/how-do-i-close-an-android-alertdialog
                        dialog.cancel();
                    }
                });

                //Create AlertDialog from the AlertDialog.Builder ==> then show
                AlertDialog alert = builder.create();
                alert.show();



            }
        });

        //ending day
        //need to use rootView.getContext() instead of getActivity().getApplicationContext()
        calendar2datePickerDialog = new DatePickerDialog(rootView.getContext(), new DatePickerDialog.OnDateSetListener() {
            /*
                1. set Calendar object endingDate to be the new data
                2. set textview to have the appropriate values
                3. Set ending AlertDialog values so they can be updated ==> thus saved later in onSaveInstance()
             */
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
//                ending_day.setText(dateFormatter.format(newDate.getTime()));
                endingDate.set(year, monthOfYear, dayOfMonth);
                ending_day.setText(dateFormatter.format(endingDate.getTime()));
                alert_dialog_ending_current_year = ""+year;
                alert_dialog_ending_current_month= convertnumberMonthWord(monthOfYear+1);
                alert_dialog_ending_current_day  = ""+dayOfMonth;
                Log.i(TAG, "New date on endingDate, year: " + endingDate.get(Calendar.YEAR));
                Log.i(TAG, "New date on endingDate, month: " + endingDate.get(Calendar.MONTH));
                Log.i(TAG, "New date on endingDate, day: " + endingDate.get(Calendar.DAY_OF_MONTH));
                Log.i(TAG, "New date on ending_day, in milliseconds: " + endingDate.getTimeInMillis());
            }
        }, endingDate.get(Calendar.YEAR), endingDate.get(Calendar.MONTH), endingDate.get(Calendar.DAY_OF_MONTH));

        calendar2 = (ImageButton)rootView.findViewById(R.id.calendar2);
        calendar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open up a new menu with a CalendarView inside it. Also have a save button
                //Once user presses save button ==> exit menu and save the date
                //http://androidopentutorials.com/android-datepickerdialog-on-edittext-click-event/
                //Use a DatePickerDialog instead. May have to implement onSaveInstanceState
                //Use constructor
                // DatePickerDialog(Context context, DatePickerDialog.OnDateSetListener listener, int year, int month, int dayOfMonth)

                calendar2datePickerDialog.show();
            }
        });

        //Moved to top of page so that we could save/restore values of it from onSaveInstanceState()
//        checkBox = (CheckBox)rootView.findViewById(R.id.checkBox);  //If checked, include ending day ==> +1 day
//        checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //https://developer.android.com/guide/topics/ui/controls/checkbox.html
//                //Set the global boolean variable isChecked
//                //this will be used later in the date calculation
//                //Since there's only 1 checkbox, don't have to worry about the getting the id
//                isChecked = ((CheckBox)v).isChecked();
//                Log.i(TAG, "New isChecked: " + isChecked);
//            }
//        });

        //Moved to top of page so that we could save/restore values of it from onSaveInstanceState()
//        result_text = (TextView)rootView.findViewById(R.id.result_text);   //Need to set this text to be the BLANK by default. Also, create some method to set the text in the correct format
////        result_text.setText("");
//        result_text_details = (TextView)rootView.findViewById(R.id.result_text_details); //Need to set this text to be the BLANK by default
////        result_text_details.setText("");

        compute = (Button)rootView.findViewById(R.id.calculate_duration);
        compute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Compute button pressed");
                //Precondition: Both dates are valid here
                /* Cases
                    1. Same date
                    2. Starting date is past the ending date
                    3. Normal (ending date is past the starting date)
                 */
//                int difference = startingDate.getTime().compareTo(endingDate.getTime());    //use Date's compareTo method, b/c initially endingDate is a clone() of startingDate so they should have the same milliseconds
                long difference = startingDate.getTimeInMillis() - endingDate.getTimeInMillis();    //if we used compareTo, if would mean we would have to do an additional unnecessary operation
                Log.i(TAG, "Calendar 1's starting date in milliseconds: " + startingDate.getTimeInMillis());
                Log.i(TAG, "Calendar 2's starting date in milliseconds: " + endingDate.getTimeInMillis());
                Log.i(TAG, "Time difference = " + difference);  //compareTo() generates either -1, 0 , or 1


                if(difference == 0){
//                if(starting_day.getText().equals(ending_day.getText())){
                    //Case #1: Same date
                    if(!isChecked){
                        //don't include end date ==> regular
                        result_text.setText(setResultText(0,false));
                        result_text_details.setText("Or 0 days excluding the end date");
                    }
                    else{
                        //include end date ==> add one day
                        result_text.setText(setResultText(1,false));
                        result_text_details.setText("Or 1 day including the end date");
                    }

                }
                else if(difference > 0){
                    //Case #2: Starting date is past the ending date
                    //for result_text. show the absolute value and have it look like: "Results: ... days PAST"
                    //for result_text_details, same
                    //Data contains: double[0] = totalDays, double[1] = years, double[2] = months, double[3] = days
                    double[] data = extractDaysMonthsYears(difference, true);
                    result_text.setText(setResultText(data[0],true));
                    result_text_details.setText(setResultDetailsText(data[1], data[2], data[3], data[4], isChecked));
                }
                else{
                    //Case #3: Normal (ending date is past the starting date)
                    double[] data = extractDaysMonthsYears(difference, false);
                    result_text.setText(setResultText(data[0], false));
                    result_text_details.setText(setResultDetailsText(data[1], data[2], data[3], data[4],  isChecked));
                }


            }
        });

        return rootView;
//        return inflater.inflate(R.layout.daysuntil_fragment_layout, container, false);
    }

    /*
        Checks if the date is valid
        Parameter: String textview for which textview it will affect, startingDay or endingDay
        This will be called inside AlertDialog.Builder's  setPositiveButton onClick() method

        Situations to check for:
            1. Is it a leap year (extra day in feb; feb 29) and month = february?
               http://stackoverflow.com/questions/7395699/calculate-leap-year-in-java
                -to do this w/o coming up with some extravagant formula, we need to make use of GregorianCalendar's isLeapYear(year) method
                -Thus, we would create a new GregorianCalendar using values from the current calendar
            2. Else is it a valid day ( don't want a month w/ only days to have a 31st day)

        Months:
        Jan: 31
        feb: <varies, but case covered specifically>
        mar: 31
        apr: 30
        may: 31
        jun: 30
        july: 31
        aug: 31
        sept: 30
        oct: 31
        nov: 30
        dec: 31

     */
//    public boolean checkFormat(int year, int month, int day){
//        boolean isGood = true;
//        GregorianCalendar gcal = new GregorianCalendar(year, month, day);
//        if(gcal.isLeapYear(year) && month == 2){
//            //if february and a leap year, we can allow 29, but not 30,31
//            if(day == 30 || day == 31){
//                isGood = false;
//            }
//        }
//        else{
//            if(month == 2 || month == 4 || month == 6 || month == 9 || month == 11){  //months with only 30 days
//                if(month == 2){
//                    if(day == 29 || day == 30 || day == 31){
//                        isGood = false;
//                    }
//                }
//                else{
//                    if(day == 31){
//                        isGood = false;
//                    }
//                }
//            }
//        }
//
//
//        return isGood;   //temp
//    }

    /*
        Before setting stuff, checks for correct format
        Sets the string from the AlertDialog ==> to the correct TextView
        Also, set the internal Calendar value too

        Month = the actual value, not the internal Android calendar value
        However, calendar is set to (month-1) ==> so it becomes the actual calendar value
        Thus, when we restore on a configuration change, we would save alert_dialog_starting_current_month,alert_dialog_ending_current_month, but then
        when setting startingDate,endingDate, we would set it to be that month -1

     */
    public void setNewDateFromAlertDialog(String textview){
        String newdate = "";
        String monthString = "";
        int day = 0;
        int month = 0;
        int year = 0;


        //Need to first convert days word (ex: "Jan.") ==> into a number
        //Although it's inefficient here if we used word month names, it makes it easier for the user
        //This is for both textviews, so do this first to save code
        if(textview.equals("startingDate")){
            monthString = alert_dialog_starting_current_month;
        }
        else{
            monthString = alert_dialog_ending_current_month;
        }
        switch(monthString){
            case "Jan.":
                newdate += "01-";
                month = 1;
                break;
            case "Feb.":
                newdate += "02-";
                month = 2;
                break;
            case "Mar.":
                newdate += "03-";
                month = 3;
                break;
            case "Apr.":
                newdate += "04-";
                month = 4;
                break;
            case "May":
                newdate += "05-";
                month = 5;
                break;
            case "Jun.":
                newdate += "06-";
                month = 6;
                break;
            case "Jul.":
                newdate += "07-";
                month = 7;
                break;
            case "Aug.":
                newdate += "08-";
                month = 8;
                break;
            case "Sept.":
                newdate += "09-";
                month = 9;
                break;
            case "Oct.":
                newdate += "10-";
                month = 10;
                break;
            case "Nov.":
                newdate += "11-";
                month = 11;
                break;
            case "Dec.":
                newdate += "12-";
                month = 12;
                break;
            default:break;
        }

        Log.i(TAG, "month: " + month);

        //add day and year into both the new string and temporary values that will be used later to set the calendar


        if(textview.equals("startingDate")){
            //month
            newdate += alert_dialog_starting_current_day + "-";
            day = Integer.parseInt(alert_dialog_starting_current_day);
            Log.i(TAG, "day: " + day);

            //year
            newdate += alert_dialog_starting_current_year;
            year = Integer.parseInt(alert_dialog_starting_current_year);
            Log.i(TAG, "year: " + year);

            starting_day.setText(newdate);
            startingDate.set(year, month-1, day);   //have to do -1 here b/c January is registered as "0" in Android Calendar
            Log.i(TAG, "New date on startingDate, year: " + startingDate.get(Calendar.YEAR));
            Log.i(TAG, "New date on startingDate, month: " + startingDate.get(Calendar.MONTH));
            Log.i(TAG, "New date on startingDate, day: " + startingDate.get(Calendar.DAY_OF_MONTH));


            //if format is good, set stuff
//            if(checkFormat(year, month, day)){
////                set text and new Calendar value
//                starting_day.setText(newdate);
//                startingDate.set(year,month,day);
//            }
//            else{
//                //if not, then we have a problem
//                showAlertDialog(getResources().getString(R.string.incorrect_format), getView());
//            }




        }
        else{
            //month
            newdate += alert_dialog_ending_current_day + "-";
            day = Integer.parseInt(alert_dialog_ending_current_day);
            Log.i(TAG, "day: " + day);

            //year
            newdate += alert_dialog_ending_current_year;
            year = Integer.parseInt(alert_dialog_ending_current_year);
            Log.i(TAG, "year: " + year);

            ending_day.setText(newdate);
            endingDate.set(year,month-1,day); //have to do -1 here b/c January is registered as "0" in Android Calendar
            Log.i(TAG, "New date on endingDate, year: " + endingDate.get(Calendar.YEAR));
            Log.i(TAG, "New date on endingDate, month: " + endingDate.get(Calendar.MONTH));
            Log.i(TAG, "New date on endingDate, day: " + endingDate.get(Calendar.DAY_OF_MONTH));

//            //if format is good, set stuff
//            if(checkFormat(year, month, day)){
//                //set text and new Calendar value
//                ending_day.setText(newdate);
//                endingDate.set(year,month,day);
//            }
//            else{
//                //if not, then we have a problem
//                showAlertDialog(getResources().getString(R.string.incorrect_format), getView());
//            }


        }
    }

    /*
        Extract the days, months, years, from the millisecond difference
        Where diff = difference between starting date and ending date in milliseconds

        double[0] = totalDays
        double[1] = years
        double[2] = months
        double[3] = weeks
        double[4] = days
        Concept:
        So let's say you're given a value in seconds, and you want to find hours. Ex: 1 hr, 3 min, 20 seconds ==> 3600 + 180 + 20 = 3800
        To find hours, do integer division total/# seconds in and hour ==> 3800 / 3600 ==> 1. Integer division truncates the decimal

        isStartingDatePastEndingDate: whether or not startingDate is past endingDate ==> which would mean that the difference would be negative
            Normal: False
            Past: True

        More info here: http://pastebin.com/XWBgWZBm
     */
    //TODO fix this
    public double[] extractDaysMonthsYears(long diff, boolean isStartingDatePastEndingDate){
        Log.i(TAG, "extractDaysMonthsYears, isStartingDatePastEndingDate: " + isStartingDatePastEndingDate);
        Log.i(TAG, "startingDate's year: " + startingDate.get(Calendar.YEAR));
        Log.i(TAG,"startingDate's month: " + startingDate.get(Calendar.MONTH));
        Log.i(TAG, "startingDate's day: " + startingDate.get(Calendar.DAY_OF_MONTH));
        Log.i(TAG, "endingDate's year: " + endingDate.get(Calendar.YEAR));
        Log.i(TAG,"endingDate's month: " + endingDate.get(Calendar.MONTH));
        Log.i(TAG, "endingDate's day: " + endingDate.get(Calendar.DAY_OF_MONTH));

        double[] data = new double[5];
        double temp = Math.abs(new Long(diff).doubleValue()); //convert long to a double value, then get the abs value of it
        //extract total days (if we didn't try to extract years,months)
        //86400000 milliseconds in a year (1000*60*60*24)
        double totalDays = Math.floor(temp / 86400000.0);   //to truncate the decimal, we use Math.floor();
        if(isChecked){
            ++totalDays;
        }
        data[0] = totalDays;

        //extract years
        //31536000000 milliseconds in a year (1000*60*60*24*365)
        double years = Math.floor(temp / 31536000000.0);
        data[1] = years;
        Log.i(TAG, "years: " + years);

        //take #years* (milliseconds per year) out of temp so we can properly extract months in the next step
        temp -= (years*31536000000.0);

        double months = 0.0;
        double weeks = 0.0;
        double days = 0.0;

        if(!isStartingDatePastEndingDate){
            Log.i(TAG, "NORMAL: startingDate is before endingDate");
            //Normal date: Starting date is before the ending date
            //We want to move up the starting date (years) amount of years so we can extract months later
            //However, we don't want this adjustment to affect the original Calendar object, so we create a temporary Calendar object
            Calendar adjust = (Calendar) startingDate.clone();
            adjust.set(adjust.get(Calendar.YEAR) + (int) years, adjust.get(Calendar.MONTH), adjust.get(Calendar.DAY_OF_MONTH));
            Log.i(TAG, "startingDate's year: " + startingDate.get(Calendar.YEAR));
            Log.i(TAG,"startingDate's month: " + startingDate.get(Calendar.MONTH));
            Log.i(TAG, "startingDate's day: " + startingDate.get(Calendar.DAY_OF_MONTH));
            Log.i(TAG, "copy's year: " + adjust.get(Calendar.YEAR));
            Log.i(TAG,"copy's month: " + adjust.get(Calendar.MONTH));
            Log.i(TAG, "copy's day: " + adjust.get(Calendar.DAY_OF_MONTH));

            //Now,extract months. See steps at http://pastebin.com/XWBgWZBm
            /*
                1. First, take starting date, clone it, and move the clone forwards to the next closest month
                    a) Case 1: Clone date and ending date are in the SAME month.
                        If this is the case, then DON'T move it forwards to the next closest month, and instead calculate difference in days,
                        from which we can extract weeks and days from
                    b) Case 2: Clone date and ending date are in different months.
                        Proceed with moving it forwards to the next closest month
             */

            //If Clone date and ending date are in the SAME month.
            if(adjust.get(Calendar.MONTH) == endingDate.get(Calendar.MONTH)){
                Log.i(TAG, "adjust's month == endingDate's month, aka months = 0");
                //# milliseconds in a week: 1000*60*60*24*7 = 604800000
                //# milliseconds in a day: 1000*60*60*24    = 86400000
                Log.i(TAG, "months: " + months);    //should be 0

                //extract weeks, then take them out of total milliseconds
                weeks = Math.floor(temp / 604800000.0);
                Log.i(TAG, "weeks: " + weeks);
                temp -= (weeks*604800000.0);

                //extract weeks, then take them out of total milliseconds
                days = Math.floor(temp / 86400000.0);
                Log.i(TAG, "days: " + days);

                //check if checkbox was set, and rebalance values if necessary
                if(isChecked){
                    ++days;
                    if(days == 7.0){    //if day value is now 7, increment week and make days 0
                        days = 0.0;
                        ++weeks;
                    }

                    Log.i(TAG, "total weeks after isChecked checkbox rebalancing: " + weeks);
                    Log.i(TAG, "total days after isChecked checkbox rebalancing: " + days);
                }

                data[2] = months;
                data[3] = weeks;
                data[4] = days;



            }

            //If Clone date and ending date are in different months.
            else{
                Log.i(TAG, "adjust's month < endingDate's month, so we have to first move forward to the first day of tne next month");
                /*
                    2. Then, enter a while loop
                        -Get # of days in the current month
                        a)If Clone date millis + (# days in current month in millis) < endingDate millis
                            -Difference in time is still greater than a month, so let's move up a month
                            -Move up clone date
                            -Increment # of months ( keep track of this in a variable)
                        b)else
                            -(  If Clone date millis + (# days in current month in millis) > endingDate millis )
                            -AKA Difference in time is less than a month now, so we need to break out of loop
                            -End loop
                            -From here, we can do as we did above and now extract weeks,days
                 */
                double numDaysInCurrentMonth = getDaysInMonth(adjust);
                double addmonth = adjust.getTimeInMillis() + numDaysInCurrentMonth*86400000.0;
                double endingMillis = endingDate.getTimeInMillis();
                while(addmonth < endingMillis){
                    //"add a month" by subtracting a month off of temp
                    temp -= numDaysInCurrentMonth*86400000.0;
                    adjust.set(adjust.get(Calendar.YEAR), adjust.get(Calendar.MONTH) + 1, adjust.get(Calendar.DAY_OF_MONTH));   //set the calendar value to be the first day of the next month. adjust.get(Calendar.DAY_OF_MONTH) should equal 1
                    numDaysInCurrentMonth = getDaysInMonth(adjust); //refresh value for the new month
                    addmonth = adjust.getTimeInMillis() + numDaysInCurrentMonth*86400000.0;
                    months++;
                }

                //From here, we can do as we did above and now extract weeks,days
                //# milliseconds in a week: 1000*60*60*24*7 = 604800000
                //# milliseconds in a day: 1000*60*60*24    = 86400000
                Log.i(TAG, "months: " + months);    //should be 0

                //extract weeks, then take them out of total milliseconds
                weeks = Math.floor(temp / 604800000.0);
                Log.i(TAG, "total weeks: " + weeks);
                temp -= (weeks*604800000.0);

                //extract weeks, then take them out of total milliseconds
                days = Math.floor(temp / 86400000.0);
                Log.i(TAG, "total days: " + days);

                //check if checkbox was set, and rebalance values if necessary
                if(isChecked){
                    ++days;
                    if(days == 7.0){    //if day value is now 7, increment week and make days 0
                        days = 0.0;
                        ++weeks;
                    }

                    Log.i(TAG, "total weeks after isChecked checkbox rebalancing: " + weeks);
                    Log.i(TAG, "total days after isChecked checkbox rebalancing: " + days);
                }

                //Put data into array
                data[2] = months;
                data[3] = weeks;
                data[4] = days;

            }

        }

        else{
            Log.i(TAG, "SPECIAL: startingDate is past endingDate");
            //Starting date is past the ending date
            //We want to move up the ending date (years) amount of years so we can extract months later
            //Basically a copy of everything above, except:
            /*
                1. Adjust = endingDate
                2. the "end date" = startingDate
                3. the "start date" = endingDate
                Basically, replace every "startingDate" with endingDate and every "endingDate" with startingDate
             */

            Calendar adjust = (Calendar) endingDate.clone();
            adjust.set(adjust.get(Calendar.YEAR) + (int) years, adjust.get(Calendar.MONTH), adjust.get(Calendar.DAY_OF_MONTH));
            Log.i(TAG, "endingDate's year: " + endingDate.get(Calendar.YEAR));
            Log.i(TAG,"endingDate's month: " + endingDate.get(Calendar.MONTH));
            Log.i(TAG, "endingDate's day: " + endingDate.get(Calendar.DAY_OF_MONTH));
            Log.i(TAG, "copy's year: " + adjust.get(Calendar.YEAR));
            Log.i(TAG,"copy's month: " + adjust.get(Calendar.MONTH));
            Log.i(TAG, "copy's day: " + adjust.get(Calendar.DAY_OF_MONTH));

            //Now,extract months. See steps at http://pastebin.com/XWBgWZBm
            /*
                1. First, take starting date, clone it, and move the clone forwards to the next closest month
                    a) Case 1: Clone date and ending date are in the SAME month.
                        If this is the case, then DON'T move it forwards to the next closest month, and instead calculate difference in days,
                        from which we can extract weeks and days from
                    b) Case 2: Clone date and ending date are in different months.
                        Proceed with moving it forwards to the next closest month
             */
            if(adjust.get(Calendar.MONTH) == startingDate.get(Calendar.MONTH)){
                Log.i(TAG, "adjust's month == statingDate's month, aka months = 0");
                //# milliseconds in a week: 1000*60*60*24*7 = 604800000
                //# milliseconds in a day: 1000*60*60*24    = 86400000
                Log.i(TAG, "months: " + months);    //should be 0

                //extract weeks, then take them out of total milliseconds
                weeks = Math.floor(temp / 604800000.0);
                Log.i(TAG, "weeks: " + weeks);
                temp -= (weeks*604800000.0);

                //extract weeks, then take them out of total milliseconds
                days = Math.floor(temp / 86400000.0);
                Log.i(TAG, "days: " + days);

                //check if checkbox was set, and rebalance values if necessary
                if(isChecked){
                    ++days;
                    if(days == 7.0){    //if day value is now 7, increment week and make days 0
                        days = 0.0;
                        ++weeks;
                    }

                    Log.i(TAG, "total weeks after isChecked checkbox rebalancing: " + weeks);
                    Log.i(TAG, "total days after isChecked checkbox rebalancing: " + days);
                }

                data[2] = months;
                data[3] = weeks;
                data[4] = days;

            }
            else{
                Log.i(TAG, "adjust's month < statingDate's month, so we have to first move forward to the first day of tne next month");
                /*
                    2. Then, enter a while loop
                        -Get # of days in the current month
                        a)If Clone date millis + (# days in current month in millis) < endingDate millis
                            -Difference in time is still greater than a month, so let's move up a month
                            -Move up clone date
                            -Increment # of months ( keep track of this in a variable)
                        b)else
                            -(  If Clone date millis + (# days in current month in millis) > endingDate millis )
                            -AKA Difference in time is less than a month now, so we need to break out of loop
                            -End loop
                            -From here, we can do as we did above and now extract weeks,days
                 */
                double numDaysInCurrentMonth = getDaysInMonth(adjust);
                double addmonth = adjust.getTimeInMillis() + numDaysInCurrentMonth*86400000.0;
                double endingMillis = startingDate.getTimeInMillis();
                while(addmonth < endingMillis){
                    //"add a month" by subtracting a month off of temp
                    temp -= numDaysInCurrentMonth*86400000.0;
                    adjust.set(adjust.get(Calendar.YEAR), adjust.get(Calendar.MONTH) + 1, adjust.get(Calendar.DAY_OF_MONTH));   //set the calendar value to be the first day of the next month. adjust.get(Calendar.DAY_OF_MONTH) should equal 1
                    numDaysInCurrentMonth = getDaysInMonth(adjust); //refresh value for the new month
                    addmonth = adjust.getTimeInMillis() + numDaysInCurrentMonth*86400000.0;
                    months++;
                }

                //From here, we can do as we did above and now extract weeks,days
                //# milliseconds in a week: 1000*60*60*24*7 = 604800000
                //# milliseconds in a day: 1000*60*60*24    = 86400000
                Log.i(TAG, "months: " + months);    //should be 0

                //extract weeks, then take them out of total milliseconds
                weeks = Math.floor(temp / 604800000.0);
                Log.i(TAG, "total weeks: " + weeks);
                temp -= (weeks*604800000.0);

                //extract weeks, then take them out of total milliseconds
                days = Math.floor(temp / 86400000.0);
                Log.i(TAG, "total days: " + days);

                //check if checkbox was set, and rebalance values if necessary
                if(isChecked){
                    ++days;
                    if(days == 7.0){    //if day value is now 7, increment week and make days 0
                        days = 0.0;
                        ++weeks;
                    }

                    Log.i(TAG, "total weeks after isChecked checkbox rebalancing: " + weeks);
                    Log.i(TAG, "total days after isChecked checkbox rebalancing: " + days);
                }

                //Put data into array
                data[2] = months;
                data[3] = weeks;
                data[4] = days;

            }



        }

        return data;
    }

    /*
        Creates the String to set the result text
        Method created so that there could be a more uniform way of setting the texts
        instead of having to create a unique text for each condition

        days = total amount of days
        days = always guaranteed to be positive

        cast to an int to make it look nicer
     */
    public String setResultText(double totalDays, boolean isStartingDatePastEndingDate){
        if(isStartingDatePastEndingDate){
            if(totalDays == 1){
                return "Result: " + (int)totalDays + " day past";
            }
            else{
                return "Result: " + (int)totalDays + " days past";
            }
        }
        else{   //For normal (startingDate < endingDate as well as 0 days)
            if(totalDays == 1){
                return "Result: " + (int)totalDays + " day";
            }
            else{
                return "Result: " + (int)totalDays + " days";
            }
        }


    }

    /*
        Creates the String to set the result_details text

        years,month,weeks,days <== all come from a double array generated in extractDaysMonthsYears
        isInclude: whether or not the checkbox is checked or not

        Method created so that there could be a more uniform way of setting the texts
        instead of having to create a unique text for each condition
        AKA we might not have to write years, months, for every case
            -In general, if a field isn't 0, we add it in
            -Cast to an int to make it look nice

     */
    public String setResultDetailsText(double years,double months, double weeks,
                                       double days, boolean isInclude){
        StringBuilder text = new StringBuilder("Or ");
        //set year
        if(years != 0.0){
            if(years == 1.0){
                text.append("" + (int)years + " year, ");
            }
            else{
                text.append("" + (int)years + " years, ");
            }

        }

        //set month
        if(months != 0.0){
            if(months == 1.0){
                text.append("" + (int)months + " month, ");
            }
            else{
                text.append("" + (int)months + " months, ");
            }

        }

        //set weeks
        if(weeks != 0.0){
            if(weeks == 1.0){
                text.append("" + (int)weeks + " week, ");
            }
            else{
                text.append("" + (int)weeks + " weeks, ");
            }

        }

        //set day
        if(days != 0.0){
            if(days == 1.0){
                text.append("" + (int)days + " day ");
            }
            else{
                text.append("" + (int)days + " days ");
            }

        }

        //set checkbox
        if(!isInclude){
            //if the checkbox isn't checked, append this to the end of the text
            //actual math was done previously in extractDaysMonthsYears
            text.append("excluding the end date");
        }
        else{
            text.append("including the end date");
        }

        return text.toString();
    }

    /*
        Show an AlertDialog as an error message
        Primarily called by checkFormat
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

    /*
        Converts the word in month (Ex: Jan.) ==> into its number representation
     */
    public int convertWordMonth(String month){
        switch(month){
            case "Jan.": return 1;
            case "Feb.": return 2;
            case "Mar.": return 3;
            case "Apr.": return 4;
            case "May":  return 5;
            case "Jun.": return 6;
            case "Jul.": return 7;
            case "Aug.": return 8;
            case "Sept.":return 9;
            case "Oct.": return 10;
            case "Nov.": return 11;
            case "Dec.": return 12;
            default:break;
        }
        return -1;
    }

    /*
        Converts the # in month to a word. Ex: its number representation ==> Jan
        Where the # is the month's REAL value, not the Android representation of it.
     */
    public String convertnumberMonthWord(int month){
        switch(month){
            case 1: return "Jan.";
            case 2: return "Feb.";
            case 3: return "Mar.";
            case 4: return "Apr.";
            case 5: return "May.";
            case 6: return "Jun.";
            case 7: return "Jul.";
            case 8: return "Aug.";
            case 9: return "Sept.";
            case 10: return "Oct.";
            case 11: return "Nov.";
            case 12: return "Dec.";
             default:break;
        }
        return "";
    }
    /* Problem:
        1. Dates on the Textviews ==> reverting back to date of today on configuration change
        2. Result texts  ==> reverting back to original defaults in string.xml on configuration change
        3. days Spinner adapters ==> keep compounding on each other on configuration change

        Things to save:
        1. All the days_# arraylist<string>
        2. All the alert_dialog_values for starting and ending
            alert_dialog_starting_current_day
            alert_dialog_starting_current_month:    This will be the ACTUAL value, not the Android set value
            alert_dialog_starting_current_year
            isDaysAdapterSet_starting
            alert_dialog_ending_current_day
            alert_dialog_ending_current_month:    This will be the ACTUAL value, not the Android set value
            alert_dialog_ending_current_year
            isDaysAdapterSet_ending

        3. resultText text
        4. resultDetail text



     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        //Save stuff here
        //days ArrayList<String>
        savedInstanceState.putStringArrayList("days_28", days_28);
        savedInstanceState.putStringArrayList("days_29", days_29);
        savedInstanceState.putStringArrayList("days_30", days_30);
        savedInstanceState.putStringArrayList("days_31", days_31);

        //alert dialog values
        savedInstanceState.putString("alert_dialog_starting_current_day", alert_dialog_starting_current_day);
        savedInstanceState.putString("alert_dialog_starting_current_month", alert_dialog_starting_current_month);
        savedInstanceState.putString("alert_dialog_starting_current_year", alert_dialog_starting_current_year);
        savedInstanceState.putBoolean("isDaysAdapterSet_starting", isDaysAdapterSet_starting);

        savedInstanceState.putString("alert_dialog_ending_current_day", alert_dialog_ending_current_day);
        savedInstanceState.putString("alert_dialog_ending_current_month", alert_dialog_ending_current_month);
        savedInstanceState.putString("alert_dialog_ending_current_year", alert_dialog_ending_current_year);
        savedInstanceState.putBoolean("isDaysAdapterSet_ending", isDaysAdapterSet_ending);

        //Result and detailed result texts
        savedInstanceState.putString("result_text", result_text.getText().toString());
        savedInstanceState.putString("result_text_details", result_text_details.getText().toString());

        //Checkbox's isChecked
        savedInstanceState.putBoolean("isChecked", isChecked);

        //Create a copy of the bundle to be put inside Utilties
        Utilities.setBundleFromDaysUntilFragment((Bundle) savedInstanceState.clone());

        //Lastly, call the parent class's equivalent method
        super.onSaveInstanceState(savedInstanceState);
    }

    /* onPause functionality:
        Called when hosting Activity is visible, but it doesn't have focus
        https://developer.android.com/guide/components/fragments.html
        https://developer.android.com/guide/components/fragments.html#Lifecycle
        https://developer.android.com/reference/android/app/Activity.html#onPause()

        My override:
        Save all our stuff that we're saving in onSaveInstanceState

        Addresses situation:
        User clicks daysUntilFragment, makes changes, but DOESN'T DO A CONFIGURATION CHANGE, then goes to unitFragment,
        then goes back to daysUntilFragment

     */
    @Override
    public void onPause(){
        Bundle onPauseBundle = new Bundle();
        //Save stuff here
        //days ArrayList<String>
        onPauseBundle.putStringArrayList("days_28", days_28);
        onPauseBundle.putStringArrayList("days_29", days_29);
        onPauseBundle.putStringArrayList("days_30", days_30);
        onPauseBundle.putStringArrayList("days_31", days_31);

        //alert dialog values
        onPauseBundle.putString("alert_dialog_starting_current_day", alert_dialog_starting_current_day);
        onPauseBundle.putString("alert_dialog_starting_current_month", alert_dialog_starting_current_month);
        onPauseBundle.putString("alert_dialog_starting_current_year", alert_dialog_starting_current_year);
        onPauseBundle.putBoolean("isDaysAdapterSet_starting", isDaysAdapterSet_starting);

        onPauseBundle.putString("alert_dialog_ending_current_day", alert_dialog_ending_current_day);
        onPauseBundle.putString("alert_dialog_ending_current_month", alert_dialog_ending_current_month);
        onPauseBundle.putString("alert_dialog_ending_current_year", alert_dialog_ending_current_year);
        onPauseBundle.putBoolean("isDaysAdapterSet_ending", isDaysAdapterSet_ending);

        //Result and detailed result texts
        onPauseBundle.putString("result_text", result_text.getText().toString());
        onPauseBundle.putString("result_text_details", result_text_details.getText().toString());

        //Create a copy of the bundle to be put inside Utilties
        Utilities.setBundleFromDaysUntilFragment((Bundle)onPauseBundle.clone());

        super.onPause();
    }

    /*
        This will be called in extractDaysMonthsYears

        calendarmonth = int values that represent a month ,based on the Android calendar standard.
        Aka January = 0, Feb = 1..... Dec == 11. Kind of illogical imo but oh well.
        Jan: 31
        feb: <varies, but case covered specifically>
        mar: 31
        apr: 30
        may: 31
        jun: 30
        july: 31
        aug: 31
        sept: 30
        oct: 31
        nov: 30
        dec: 31

        cal = the Calendar object. Will either be startingDay or endingDay
     */
    public double getDaysInMonth(Calendar cal){
        double numdays = 0.0;
        int calendarmonth = cal.get(Calendar.MONTH);
        if(calendarmonth == 0 || calendarmonth == 2 || calendarmonth == 4 || calendarmonth == 6
                || calendarmonth == 7 || calendarmonth == 9 || calendarmonth == 11){
            //months with 31 days
            numdays = 31.0;
        }
        else if(calendarmonth == 3 || calendarmonth == 5 || calendarmonth == 8 || calendarmonth == 10){
            //months with 30 days
            numdays = 30.0;
        }
        else{
            //february, this depends on whether it's a leap year or not
            int year = cal.get(Calendar.YEAR);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            GregorianCalendar gcal = new GregorianCalendar(year,calendarmonth,day);
            if(gcal.isLeapYear(year)){
                numdays = 29.0;
            }
            else{
                numdays = 28.0;
            }
    }

        return numdays;
    }




}

/* http://pastebin.com/XWBgWZBm
data in case it ever gets lost
http://www.timeanddate.com/date/durationresult.html
New logic:
In order to not complicate this further enough, here's a much simpler way to approach the problem:
1. First, take starting date, clone it, and move the clone forwards to the next closest month
	a) Case 1: Clone date and ending date are in the SAME month.
		If this is the case, then DON'T move it forwards to the next closest month, and instead calculate difference in days,
		from which we can extract weeks and days from
	b) Case 2: Clone date and ending date are in different months.
		Proceed with moving it forwards to the next closest month

2. Then, enter a while loop
	-Get # of days in the current month
	a)If Clone date millis + (# days in current month in millis) < endingDate millis
		-Difference in time is still greater than a month, so let's move up a month
		-Move up clone date
		-Increment # of months ( keep track of this in a variable)
	b)else
		-(  If Clone date millis + (# days in current month in millis) > endingDate millis )
		-AKA Difference in time is less than a month now, so we need to break out of loop
		-End loop

3. Get # of days between Clone date and endingDate
	-From here, we can now extract weeks,days


Month will be defined as: the # of days it takes to get from (month1) day ==> (next month) day
		Jan: 31
        feb: <varies, but case covered specifically>
        mar: 31
        apr: 30
        may: 31
        jun: 30
        july: 31
        aug: 31
        sept: 30
        oct: 31
        nov: 30
        dec: 31

31->30:  31 days from Mar 5 to apr 5
30->31:  30 days from Apr 5 to may 5
31->31:  31 days from Dec 1 2016 - Jan 1 2017. (Dec- Jan, Jun - July)

Thus, we can see that the # of days considered to be in a "month" is the # of days from the STARTING MONTH.

Special cases:
1a. Dealing with transitions from months with 31 days --> months with 30 days
	1) Any regular day (aka not 31) ==> will just be # of days from the STARTING MONTH.
	2) However, if the starting day is 31, then # of days will just be the day value of the next month.
	   Aka 3/31 --> 4/15 ==> is 15 days
1b. Dealing with transitions from months with 30 days --> months with 31 days
	1) Any regular day (aka not 30) ==> will just be # of days from the STARTING MONTH.
	2) However, if the starting day is 30, then # of days will just be the day value of the next month.
	   Aka 4/30 --> 5/15 ==> is 15 days, 4/30 --> 5/31 ==> is 31 days

2. Leap Years
Jan -> Feb (31 - 29) leap year:
Jan -> Feb (31 - 28) non leap year:

Feb -> March leap year:
Feb -> March non leap year:



 */
