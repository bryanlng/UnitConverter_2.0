package com.example.bryan.unitconverter20;

import android.os.Bundle;
import android.util.Log;

/**
 * Class created for the purpose of creating global variables
 * Created to solve the problem where you go back into DaysUntilFragment
 *      -Values are not null, but savedInstanceState == null
 *      -Thus, DaysUntilFragment needs to be able to access values in MainActivity
 *      -The only way we can do this (b/c both class have private variables and for the most part, I don't want classes touching each other's variables)
 *      -   is to create a global "utilities" class where MainActivity sets values and DaysUntilFragment gets them
 */


public class Utilities {
    private static final String TAG = "UnitConverterTag";
    public static Bundle unitFragmentBundleCopy = null;
    public static Bundle daysUntilFragmentBundleCopy = null;

    public static Bundle getBundleFromUnitFragment(){
        Log.i(TAG, "Utilities: getBundleFromUnitFragment()");
        if(unitFragmentBundleCopy == null) Log.i(TAG, "Utilities: getBundleFromUnitFragment() bundle is null");
        else Log.i(TAG, "Utilities: getBundleFromUnitFragment() bundle is NOT NULL");
        return unitFragmentBundleCopy;
    }
    public static void setBundleFromUnitFragment(Bundle b){
        Log.i(TAG, "Utilities: setBundleFromUnitFragment()");
        if(unitFragmentBundleCopy == null) Log.i(TAG, "Utilities: setBundleFromUnitFragment() bundle is currently null");
        else Log.i(TAG, "Utilities: setBundleFromUnitFragment() bundle is currently NOT NULL");
        unitFragmentBundleCopy = b;
    }
    public static Bundle getBundleFromDaysUntilFragment(){
        return daysUntilFragmentBundleCopy;
    }
    public static void setBundleFromDaysUntilFragment(Bundle b){
        daysUntilFragmentBundleCopy = b;
    }



}
