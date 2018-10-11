package com.lucblender.lucasbonvin.widgettest.UserInterface;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lucblender.lucasbonvin.widgettest.Data.DataCsvManager;
import com.lucblender.lucasbonvin.widgettest.R;
import com.nbsp.materialfilepicker.MaterialFilePicker;

import java.io.File;
import java.util.regex.Pattern;

public class AppPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = AppPreferenceFragment.class.getName();
    private static final int requestCodeFilePicker = 125;

    private PreferenceListener mListener;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        SharedPreferences preferences = getPreferenceScreen().getSharedPreferences();
        switch (key)
        {
            case "filePicker" :
                Preference pref_file = findPreference("filePicker");
                pref_file.setSummary(preferences.getString("filePicker","NA"));
                break;
        }
    }

    public interface PreferenceListener{
        //interface used to transmit to the main activity the parameters update
        public void updateLanguage(String lang);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //Set the preference from xml
        setPreferencesFromResource(R.xml.preferences, rootKey);

        //get the language field and build a preference change listener
        ListPreference languagePref = (ListPreference) findPreference("param_language");
        languagePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String newValueStr = (String) newValue;
                mListener.updateLanguage(newValueStr);
                return true;
            }
        });


        SharedPreferences preferences = getPreferenceScreen().getSharedPreferences();
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (PreferenceListener) context;
        } catch ( ClassCastException e) {
            throw new ClassCastException( context.toString()
                    + " must implement AppPreferenceFragment.PreferenceListener") ;
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //set the onclick listener to choose csvfile
        Preference filePicker = (Preference) findPreference("filePicker");
        filePicker.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //create an intent to open a file browser
                /*
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //Intent to start openIntents File Manager
                intent.setType("text/csv");
                startActivityForResult(intent, requestCodeFilePicker);*/
                new MaterialFilePicker()
                        .withActivity(getActivity())
                        .withRequestCode(requestCodeFilePicker)
                        .withFilter(Pattern.compile(".*\\.csv$")) // Filtering files and directories by file name using regexp
                        .withFilterDirectories(false) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();

                return true;
            }
        });

        //set the onclick listener to open the chosen csv file
        Preference fileOpener = (Preference) findPreference("fileOpen");
        fileOpener.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //create an intent to open a file
                Intent myIntent = new Intent(Intent.ACTION_VIEW);
                //get the path of file stored in the preference and launch the intent
                SharedPreferences preferences = getPreferenceScreen().getSharedPreferences();
                File toto = new File(preferences.getString("filePicker","NA"));
                myIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                myIntent.setData(FileProvider.getUriForFile(getContext(),
                        getContext().getPackageName()+".com.lucblender.lucasbonvin",
                        toto));
                startActivity(myIntent);
                return true;
            }
        });

        //set the onclick listener to send csvfile
        Preference sendFile = (Preference) findPreference("sendFile");
        sendFile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                String myFilePath = DataCsvManager.getInstance().getCsvFile(getContext()).URI;
                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                File fileWithinMyDir = new File(myFilePath);

                if(fileWithinMyDir.exists()) {

                    File toto = new File(myFilePath);

                    intentShareFile.setType("text/*");
                    intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intentShareFile.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getContext(),
                            getContext().getPackageName()+".com.lucblender.lucasbonvin",
                            toto));
                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                            "Sharing Planning File from Lesson Planning Widget app");
                    intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sent from Lesson Planning Widget app on android");

                    startActivity(Intent.createChooser(intentShareFile, "Share File"));
                }
                else
                {
                    message("File does not exist");
                }

                return true;
            }
        });

        //set onclick listener for the help button
        Preference help = (Preference) findPreference("help");
        help.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //create a popup with help message
                HelpDialog helpDialog = new HelpDialog(getActivity());
                helpDialog.show();
                return true;
            }
        });


        //set onclick listener for the about button
        Preference about = (Preference) findPreference("about");
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //create a popup with Lesson message
                AboutDialog aboutDialog = new AboutDialog(getActivity());
                aboutDialog.show();
                return true;
            }
        });

        //check the external storage permission, if not granted --> open the request permission window
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }

        //update the summary of the first preference --> the path of file
        SharedPreferences preferences = getPreferenceScreen().getSharedPreferences();
        Preference pref = findPreference("filePicker");
        pref.setSummary(preferences.getString("filePicker","NA"));

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //get the result of the permission request
        switch (requestCode){
            case  0:{
                //show a message depending of the result of the request
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    message(getString(R.string.permission_granted));
                }
                else
                {
                    message(getString(R.string.permission_not_granted));
                    getActivity().finish();
                }
                break;
            }
        }
    }

    //show a toast message
    void message(String s)
    {
        Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


    }
}