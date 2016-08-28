package com.example.bryan.unitconverter20;

import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Bryan on 8/19/2016.
 */
public class DaysUntilFragment extends Fragment {
    private static final String TAG = "UnitConverterTag";
    private TextView from;
    private TextView starting_day;
    private ImageButton calendar1;
    private TextView to;
    private TextView ending_day;
    private ImageButton calendar2;
    private CheckBox checkBox;
    private TextView result_text;
    private TextView result_text_details;

    public DaysUntilFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.daysuntil_fragment_layout, container, false);

        //Set elements from xml layout file
        from = (TextView)rootView.findViewById(R.id.from);
        starting_day = (TextView)rootView.findViewById(R.id.starting_day);  //Need to set this text to be the current day, by default;
        starting_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open up a new Menu, which has Spinners for Day, Month, Year and a save a button
                //Once user presses save button ==> exit menu and save the date

            }
        });

        calendar1 = (ImageButton)rootView.findViewById(R.id.calendar1);
        calendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open up a new menu with a CalendarView inside it. Also have a save button
                //Once user presses save button ==> exit menu and save the date
            }
        });

        to = (TextView)rootView.findViewById(R.id.to);

        ending_day = (TextView)rootView.findViewById(R.id.ending_day);   //Need to set this text to be the current day, by default
        ending_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open up a new Menu, which has Spinners for Day, Month, Year and a save a button
                //Once user presses save button ==> exit menu and save the date

            }
        });
        calendar2 = (ImageButton)rootView.findViewById(R.id.calendar2);
        calendar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open up a new menu with a CalendarView inside it. Also have a save button
                //Once user presses save button ==> exit menu and save the date
            }
        });

        checkBox = (CheckBox)rootView.findViewById(R.id.checkbox);  //If checked, include ending day ==> +1 day


        result_text = (TextView)rootView.findViewById(R.id.result_text);   //Need to set this text to be the BLANK by default. Also, create some method to set the text in the correct format
//        result_text.setText("");
        result_text_details = (TextView)rootView.findViewById(R.id.result_text_details); //Need to set this text to be the BLANK by default
//        result_text_details.setText("");


        return rootView;
//        return inflater.inflate(R.layout.daysuntil_fragment_layout, container, false);
    }

}
