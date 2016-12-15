package com.example.bryan.unitconverter20;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = "UnitConverterTag";
    private String[] fragmentTitles;
    private DrawerLayout mDrawerlayout;
    private ListView mDrawerList;
    private UnitFragment unitFragment;
    private DaysUntilFragment daysUntilFragment;
    private CurrencyFragment currencyFragment;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String previousFragmentAction = "";
    private boolean isUnitCurrentFragment = false;
    private boolean isDaysUntilFragment = false;
    private int selectItemCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Restore previous state if there was one
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            isUnitCurrentFragment = savedInstanceState.getBoolean("isUnitCurrentFragment");
            isDaysUntilFragment = savedInstanceState.getBoolean("isDaysUntilFragment");
            fragmentTitles = savedInstanceState.getStringArray("fragmentTitles");
            selectItemCurrentPosition = savedInstanceState.getInt("selectItemCurrentPosition");
            setTitle(fragmentTitles[selectItemCurrentPosition]);
        }

        //Initialize elements
        setContentView(R.layout.activity_main);
        Log.i(TAG, "Entered onCreate()");
        fragmentTitles = getResources().getStringArray(R.array.fragmentTitles);
        mDrawerlayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.left_drawer);
        selectItemCurrentPosition = 0;

        //Set the adapter for the ListView, which will be an ArrayAdapter
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item,fragmentTitles));

        //Set the ListView's clickListener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "Entered onItemClick(), with position: " + position);
                selectItem(position);
            }
        });

        //Set DrawerList's default item to be the
        mDrawerList.setSelection(0);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerlayout,         /* DrawerLayout object */
                //R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret. This was deprecated in v7, use setDrawerIndicatorEnabled instead**/
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ){
            public void onDrawerClosed(View view) {
                Log.i(TAG, "Entered onDrawerClosed()");
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                Log.i(TAG, "Entered onDrawerOpened()");
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerlayout.setDrawerListener(mDrawerToggle);

    }

    @Override
    public void setTitle(CharSequence title) {
        Log.i(TAG, "Entered setTitle()");
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**Swaps fragments in the main content View**/
    private void selectItem(int position){
        Log.i(TAG, "Entered selectItem() with position: " + position);
        Log.i(TAG, "BEFORE OPERATION: isUnitCurrentFragment: " + isUnitCurrentFragment + " ,isDaysUntilFragment:" + isDaysUntilFragment);
        selectItemCurrentPosition = position;

        //Initialize Fragment stuff
        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Create the 3 Fragments
        unitFragment = new UnitFragment();
        daysUntilFragment = new DaysUntilFragment();
        currencyFragment = new CurrencyFragment();

        //Case 1: Want to go to Unit Fragment
        if(position == 0){
            // Add the fragment to the activity, pushing this transaction
            // on to the back stack.
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, unitFragment);
            ft.addToBackStack(null);
            ft.commit();
            fragmentManager.executePendingTransactions();
        }

        //Case 2: Want to go to DaysUntil Fragment
        else if(position == 1){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, daysUntilFragment);
            ft.addToBackStack(null);
            ft.commit();
            fragmentManager.executePendingTransactions();
        }

        //Case 3: Want to go to Currency Fragment
        else{
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, currencyFragment);
            ft.addToBackStack(null);
            ft.commit();
            fragmentManager.executePendingTransactions();
        }

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(fragmentTitles[position]);
        mDrawerlayout.closeDrawer(mDrawerList);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view.
        // Aka, hide the OptionsMenu stuff
        Log.i(TAG, "Entered onPrepareOptionsMenu");
        boolean drawerOpen = mDrawerlayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     * This allows the animated gif above the icon to sync better with the drawer
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.i(TAG, "Entered onPostCreate");
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        Log.i(TAG, "Entered onConfigurationChanged");
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /* Things to save:
        isDaysUntilFragment, isUnitCurrentFragment
        Fragments will solve themselves by calling setRetainInstance(true)
        Fix the title so it doesn't say UnitConverter2.0
            -Need to redo setTitle(fragmentTitles[position]);
            -This involves saving:
                -fragmentTitles
                -position
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putBoolean("isUnitCurrentFragment",
                isUnitCurrentFragment);
        savedInstanceState.putBoolean("isDaysUntilFragment",
                isDaysUntilFragment);
        savedInstanceState.putInt("selectItemCurrentPosition", selectItemCurrentPosition);
        savedInstanceState.putStringArray("fragmentTitles",fragmentTitles);
        super.onSaveInstanceState(savedInstanceState);
    }




    /*************************************************************Menu Stuff***********************************/
    /*
        Create the Options Menu by calling the inflater, then inflate()
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
        On item id click ==> do something
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "This does nothing", Toast.LENGTH_LONG).show();
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
