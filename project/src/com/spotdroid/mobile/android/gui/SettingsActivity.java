package com.spotdroid.mobile.android.gui;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

import com.spotdroid.mobile.android.R;

/**
 * User Settings. 
 */
public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener
{

    /** Tag for log tracking. */
    private static final String TAG = SettingsActivity.class.getSimpleName();

    private SharedPreferences   settings;

    private EditTextPreference  usernamePreference;
    private EditTextPreference  passwordPreference;
    private EditTextPreference  urlPreference;
    private ListPreference      formatPreference;
    private EditTextPreference  timePreference;
    private EditTextPreference  distancePreference;
    private ListPreference      intervallPreference;
    private EditTextPreference  maxPreference;
    private CheckBoxPreference  modusPreference;
    private CheckBoxPreference  logPreference;

    /* (non-Javadoc)
     * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Load the XML preferences file
        this.addPreferencesFromResource(R.xml.settings);

        settings = getAppSettings(this);

        usernamePreference = getEditTextPreference(PreferenceData.PREF_USERNAME);
        passwordPreference = getEditTextPreference(PreferenceData.PREF_PASSWORD);
        urlPreference = getEditTextPreference(PreferenceData.PREF_URL);
        formatPreference = getListPreference(PreferenceData.PREF_FORMAT);
        timePreference = getEditTextPreference(PreferenceData.PREF_TIME);
        distancePreference = getEditTextPreference(PreferenceData.PREF_DISTANCE);
        intervallPreference = getListPreference(PreferenceData.PREF_INTERVALL);
        maxPreference = getEditTextPreference(PreferenceData.PREF_MAX);
        modusPreference = getCheckBoxPreference(PreferenceData.PREF_MODUS);
        logPreference = getCheckBoxPreference(PreferenceData.PREF_LOG);

    }

    /**
     * Utility method to get instance of EditTextPreference for key. 
     * @param key from {@link PreferenceData}.
     * @return EditTextPreference object 
     */
    private EditTextPreference getEditTextPreference(String key)
    {
        return (EditTextPreference) getPreferenceScreen().findPreference(key);
    }

    /**
     * Utility method to get instance of CheckBoxPreference for key. 
     * @param key from {@link PreferenceData}.
     * @return CheckBoxPreference object 
     */
    private CheckBoxPreference getCheckBoxPreference(String key)
    {
        return (CheckBoxPreference) getPreferenceScreen().findPreference(key);
    }

    /**
     * Utility method to get instance of ListPreference for key. 
     * @param key from {@link PreferenceData}.
     * @return ListPreference object 
     */
    private ListPreference getListPreference(String key)
    {
        return (ListPreference) getPreferenceScreen().findPreference(key);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume()
    {
        super.onResume();

        //update values on the screen 
        updateSummariesFromFile();

        // Register for updates on changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause()
    {
        super.onPause();

        // Register for updates on changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * Loads the saved values from Shared Preferences file and show them in summaries. 
     */
    private void updateSummariesFromFile()
    {
        usernamePreference.setSummary(getSettingsString(PreferenceData.PREF_USERNAME,
                R.string.key_pref_cat_user_username_summary));
        passwordPreference.setSummary(getSettingsString(PreferenceData.PREF_PASSWORD,
                R.string.key_pref_cat_user_password_summary));
        urlPreference.setSummary(getSettingsString(PreferenceData.PREF_URL,
                R.string.key_pref_cat_user_server_url_summary));
        formatPreference.setSummary(getSettingsString(PreferenceData.PREF_FORMAT,
                R.string.key_pref_cat_user_gps_format_summary));
        timePreference.setSummary(getSettingsString(PreferenceData.PREF_TIME, R.string.key_pref_cat_send_time_summary));
        distancePreference.setSummary(getSettingsString(PreferenceData.PREF_DISTANCE,
                R.string.key_pref_cat_send_distance_summary));
        intervallPreference.setSummary(getSettingsString(PreferenceData.PREF_INTERVALL,
                R.string.key_pref_cat_send_intervall_summary));
        maxPreference.setSummary(getSettingsString(PreferenceData.PREF_MAX, R.string.key_pref_cat_send_max_summary));
        modusPreference.setSummary(getSettingsString(PreferenceData.PREF_MODUS,
                R.string.key_pref_cat_send_modus_on_summary));
        logPreference.setSummary(getSettingsString(PreferenceData.PREF_LOG, R.string.key_pref_cat_send_log_on_summary));
    }

    /**
     * Gets the shared preferences file. 
     * @param Context class.
     * @return SharedPreferences file.
     */
    public static final SharedPreferences getAppSettings(final ContextWrapper ctx)
    {
        return ctx.getSharedPreferences("com.spotdroid.mobile.android" + "_preferences", Activity.MODE_PRIVATE);
    }

    /**
     * Read the key/value pair from preferences file. 
     * @param key of the pair.
     * @param resId of default summary.
     * @return summary value.
     */
    private String getSettingsString(String key, int resId)
    {
        String defaultSummary = getText(resId).toString();

        String value = settings.getString(key, defaultSummary);
        if (value.length() == 0)
        {
            value = defaultSummary;
        }
        return value;
    }

    /* (non-Javadoc)
     * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s)
    {

        //update values on the screen 
        updateSummariesFromFile();

    }

}
