package com.example.bryan.unitconverter20;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CurrencyFragment extends Fragment {

    public CurrencyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.currency_fragment_layout, container, false);






        return rootView;
//        return inflater.inflate(R.layout.currency_fragment_layout, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){

    }


    @Override
    public void onPause(){

    }

}
