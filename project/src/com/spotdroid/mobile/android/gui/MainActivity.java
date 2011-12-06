package com.spotdroid.mobile.android.gui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.spotdroid.mobile.android.R;

/**
 * Main activity as a start point for all app activities. 
 */
public class MainActivity extends Activity
{

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause()
    {
        super.onPause();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume()
    {
        super.onResume();
    }

    /**
     * Method defined in layout for button click. 
     * @param view
     */
    public void onClickShowLocation(View view)
    {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }

    /**
     * Method defined in layout for button click. 
     * @param view
     */
    public void onClickShowSettings(View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Method defined in layout for button click. 
     * @param view
     */
    public void onClickShowSendData(View view)
    {
        //TODO
    }

    /**
     * Method defined in layout for button click. 
     * @param view
     */
    public void onClickShowActivitiesIndex(View view)
    {
        Intent intent = new Intent(this, ActivityIndexActivity.class);
        startActivity(intent);
    }

    /**
     * Method defined in layout for button click. 
     * @param view
     */
    public void onClickShowWebsite(View view)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.getString(R.string.key_url_website)));
        startActivity(intent);
    }

}
