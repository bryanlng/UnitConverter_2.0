package com.example.bryan.unitconverter20;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Bryan on 8/19/2016.
 */
public class DaysUntilFragment extends Fragment {
    private static final String TAG = "UnitConverterTag";
    private TextView test2;
    public DaysUntilFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.daysuntil_fragment_layout, container, false);
        test2 = (TextView)rootView.findViewById(R.id.days_text);
        return rootView;
//        return inflater.inflate(R.layout.daysuntil_fragment_layout, container, false);
    }

}
