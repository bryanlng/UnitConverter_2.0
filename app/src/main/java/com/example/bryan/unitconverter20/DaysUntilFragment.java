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

    private boolean isChecked = false;

    //AlertDialog1 stuff: starting
    private Spinner days_spinner;           //need to be declared here globally so it doesn't give us the error that prompts us to set it to final
    private Spinner month_spinner;          //need to be declared here globally so it doesn't give us the error that prompts us to set it to final
    private Spinner years_spinner;          //need to be declared here globally so it doesn't give us the error that prompts us to set it to final
    private View alertDialogCustomView_starting;     //need to be declared here globally so it doesn't give us the error that prompts us to set it to final
    private String alert_dialog_starting_current_day ="01";
    private String alert_dialog_starting_current_month ="Jan.";
    private String alert_dialog_starting_current_year ="2011";
    private boolean isDaysAdapterSet_starting = false;

    //AlertDialog2 stuff: ending
    private Spinner ending_days_spinner;           //need to be declared here globally so it doesn't give us the error that prompts us to set it to final
    private Spinner ending_month_spinner;          //need to be declared here globally so it doesn't give us the error that prompts us to set it to final
    private Spinner ending_years_spinner;          //need to be declared here globally so it doesn't give us the error that prompts us to set it to final
    private View alertDialogCustomView_ending;     //need to be declared here globally so it doesn't give us the error that prompts us to set it to final
    private String alert_dialog_ending_current_day ="01";
    private String alert_dialog_ending_current_month ="Jan.";
    private String alert_dialog_ending_current_year ="2011";
    private boolean isDaysAdapterSet_ending = false;


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


        /*
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



        //Instantiate Calendar instances first so we can set the texts of startingDay and endingDay to be the current day
        //Also, make endingDate be a clone() of startingDate so that their millisecond values will be exactly the same
        //starting day
        startingDate = Calendar.getInstance();
        endingDate = (Calendar)startingDate.clone();
//        endingDate = Calendar.getInstance();


        //Set elements from xml layout file
        from = (TextView)rootView.findViewById(R.id.from);
        starting_day = (TextView)rootView.findViewById(R.id.starting_day);  //Need to set this text to be the current day, by default;
        starting_day.setText(dateFormatter.format(startingDate.getTime())); //Set starting_day's text to be the current day by default
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
                years_spinner.setSelection(0);  //set initial default value to be the first value
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
                month_spinner.setSelection(0);  //set initial default value to be the first value
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
                    days_spinner.setSelection(0);  //set initial default value to be the first value
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
                set Calendar object endingDate to be the new data
             */
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
//                starting_day.setText(dateFormatter.format(newDate.getTime()));
                startingDate.set(year, monthOfYear, dayOfMonth);
                starting_day.setText(dateFormatter.format(startingDate.getTime()));
                Log.i(TAG, "New date on startingDay: " + dateFormatter.format(startingDate.getTime()));
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


        to = (TextView)rootView.findViewById(R.id.to);

        ending_day = (TextView)rootView.findViewById(R.id.ending_day);   //Need to set this text to be the current day, by default
        ending_day.setText(dateFormatter.format(endingDate.getTime())); //Set ending_day's text to be the current day by default
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
                ending_years_spinner.setSelection(0);  //set initial default value to be the first value
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
                ending_month_spinner.setSelection(0);  //set initial default value to be the first value
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
                    ending_days_spinner.setSelection(0);  //set initial default value to be the first value
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
                set Calendar object endingDate to be the new data
             */
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
//                ending_day.setText(dateFormatter.format(newDate.getTime()));
                endingDate.set(year, monthOfYear, dayOfMonth);
                ending_day.setText(dateFormatter.format(endingDate.getTime()));
                Log.i(TAG, "New date on ending_day: " + dateFormatter.format(endingDate.getTime()));
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


        result_text = (TextView)rootView.findViewById(R.id.result_text);   //Need to set this text to be the BLANK by default. Also, create some method to set the text in the correct format
//        result_text.setText("");
        result_text_details = (TextView)rootView.findViewById(R.id.result_text_details); //Need to set this text to be the BLANK by default
//        result_text_details.setText("");

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
                Log.i(TAG, "Calendar 1's starting date: " + dateFormatter.format(startingDate.getTime()));
                Log.i(TAG, "Calendar 2's starting date: " + dateFormatter.format(endingDate.getTime()));
                Log.i(TAG, "Calendar 1's starting date in milliseconds: " + startingDate.getTimeInMillis());
                Log.i(TAG, "Calendar 2's starting date in milliseconds: " + endingDate.getTimeInMillis());
                Log.i(TAG, "Time difference = " + difference);  //compareTo() generates either -1, 0 , or 1
                if(difference == 0){
//                if(starting_day.getText().equals(ending_day.getText())){
                    //Case #1: Same date
                    if(!isChecked){
                        //don't include end date ==> regular
                        result_text.setText(setResultText(0));
                        result_text_details.setText("Or 0 days excluding the end date");
                    }
                    else{
                        //include end date ==> add one day
                        result_text.setText(setResultText(1));
                        result_text_details.setText("Or 1 day including the end date");
                    }

                }
                else if(difference > 0){
                    //Case #2: Starting date is past the ending date
                    //for result_text. show the absolute value and have it look like: "Results: ... days PAST"
                    //for result_text_details, same
                    int[] data = extractDaysMonthsYears(difference);
                    result_text.setText(setResultText(data[2]));
                    result_text_details.setText(setResultDetailsText(data[0], data[1], data[2], isChecked));
                }
                else{
                    //Case #3: Normal (ending date is past the starting date)
                    int[] data = extractDaysMonthsYears(difference);
                    result_text.setText(setResultText(data[2]));
                    result_text_details.setText(setResultDetailsText(data[0], data[1], data[2], isChecked));
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
    public boolean checkFormat(int year, int month, int day){
        boolean isGood = true;
        GregorianCalendar gcal = new GregorianCalendar(year, month, day);
        if(gcal.isLeapYear(year) && month == 2){
            //if february and a leap year, we can allow 29, but not 30,31
            if(day == 30 || day == 31){
                isGood = false;
            }
        }
        else{
            if(month == 2 || month == 4 || month == 6 || month == 9 || month == 11){  //months with only 30 days
                if(month == 2){
                    if(day == 29 || day == 30 || day == 31){
                        isGood = false;
                    }
                }
                else{
                    if(day == 31){
                        isGood = false;
                    }
                }
            }
        }


        return isGood;   //temp
    }

    /*
        Before setting stuff, checks for correct format
        Sets the string from the AlertDialog ==> to the correct TextView
        Also, set the internal Calendar value too

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
            startingDate.set(year, month, day);

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
            endingDate.set(year,month,day);

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
        Need to throw out extra milliseconds, seconds, minutes, hours
        int[0] = years
        int[1] = months
        int[2] = days
        http://pastebin.com/aRZYpEJg
     */
    //TODO fix this
    public int[] extractDaysMonthsYears(long diff){
        int[] data = new int[3];

        //extract and throw out milliseconds
        long millis = diff % 1000;
        diff -= millis;

        //extract and throw out seconds
        long secs = diff % 60;
        diff -= secs;

        //extract and throw out minutes
        long min = diff % 60;
        diff -= min;

        //extract and throw out hours
        long hour = diff % 60;
        diff -= hour;

        return data;
    }

    /*
        Creates the String to set the result text
        Method created so that there could be a more uniform way of setting the texts
        instead of having to create a unique text for each condition

        If days is negative (aka Case #2: Starting date is past the ending date),
        then show the absolute value, but also include the word "past"
     */
    public String setResultText(int days){
        if(days >= 0){
            if(days == 1){
                return "Result: " + days + " day";
            }
            else{
                return "Result: " + days + " days";
            }

        }
        else{
            return "Result: " + Math.abs(days) + " days past";
        }

    }

    /*
        Creates the String to set the result_details text
        isChecked will go inside isInclude
        Method created so that there could be a more uniform way of setting the texts
        instead of having to create a unique text for each condition
        AKA we might not have to write years, months, for every case
     */
    public String setResultDetailsText(int years, int months, int days, boolean isInclude){
        String text = "";
        if(!isInclude){
            //..do stuff here
            text += " excluding the end date";
        }
        else{
            text += " including the end date";
        }

        return text;
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
    /* Problem:
        1. Dates on the Textviews ==> reverting back to date of today on configuration change
        2. Result texts  ==> reverting back to original defaults in string.xml on configuration change
        3. days Spinner adapters ==> keep compounding on each other on configuration change
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        //Save stuff here

        super.onSaveInstanceState(savedInstanceState);
    }




}
