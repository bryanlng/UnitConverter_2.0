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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    public DaysUntilFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        final View rootView = inflater.inflate(R.layout.daysuntil_fragment_layout, container, false);
        dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

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
                //Open up a new AlertDialog, which has Spinners for Day, Month, Year, save button, and cancel button
                //Once user presses save button ==> exit menu and save the date
                //Once user presses cancel button ==> simply exit
                //need to use rootView.getContext() instead of getActivity().getApplicationContext()
                //https://www.mkyong.com/android/android-alert-dialog-example/
                //https://developer.android.com/guide/topics/ui/dialogs.html
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                //Set title
                builder.setTitle(getResources().getString(R.string.alert_dialog_title));
                builder.setCancelable(false);
                builder.setMessage("Need some spinners here");

                //set Positive button to be "save"
                builder.setPositiveButton(getResources().getString(R.string.alert_dialog_save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Set the startingDay's text to be the date, then close the AlertDialog
                        //http://stackoverflow.com/questions/4336470/how-do-i-close-an-android-alertdialog
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
                //Open up a new AlertDialog, which has Spinners for Day, Month, Year and a save a button
                //Once user presses save button ==> exit menu and save the date

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
        Parameter: String or Calendar???????
        This will be called inside AlertDialog.Builder's  setPositiveButton onClick() method
     */
//    public boolean checkAlertDialogDateFormat(String date){
//
//    }

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
     */
//    public void showAlertDialog(String message, View v){
//        //pop open a window saying that you need to remove the thing
//        Log.i(TAG, "showAlertDialog with message: " + message);
//        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext()); //getActivity().getApplicationContext());
//        builder.setMessage(message);
//        builder.setCancelable(false);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                //do things
//            }
//        });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }


}
