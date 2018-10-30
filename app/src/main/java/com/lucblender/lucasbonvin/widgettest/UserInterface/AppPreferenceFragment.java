package com.lucblender.lucasbonvin.widgettest.UserInterface;

import android.Manifest;
import android.app.Activity;
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
import com.lucblender.lucasbonvin.widgettest.UpdateService;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

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
        void updateLanguage(String lang);
        void updateTheme(String themeKey);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //Set the preference from xml
        setPreferencesFromResource(R.xml.preferences, rootKey);

        //get the language field and build a preference change listener
        ListPreference languagePref = (ListPreference) findPreference("param_language");
        languagePref.setOnPreferenceChangeListener((preference, newValue) -> {
            String newValueStr = (String) newValue;
            mListener.updateLanguage(newValueStr);
            return true;
        });

        ListPreference themePref = (ListPreference) findPreference("param_theme");
        themePref.setOnPreferenceChangeListener((preference, newValue) -> {
            String newValueStr = (String) newValue;
            mListener.updateTheme(newValueStr);
            return true;
        });

        SharedPreferences preferences = getPreferenceScreen().getSharedPreferences();

        SharedPreferences.Editor editor = preferences.edit();
        Set<String> favoritePath = preferences.getStringSet("favoritePath",null);

        if(favoritePath == null) {
            editor.putStringSet("favoritePath", new LinkedHashSet<>() );
            editor.apply();
        }

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
    public void onResume() {
        super.onResume();

        try {
            getContext().startService(new Intent(getContext(), UpdateService.class));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //set the onclick listener to choose csvfile
        Preference filePicker = findPreference("filePicker");
        filePicker.setOnPreferenceClickListener(preference -> {
            //create a popup with Choose file message message
            ChooseCalendarDialog chooseCalendarDialog = new ChooseCalendarDialog(getActivity(),getActivity());
            chooseCalendarDialog.show();
            return true;
        });

        //set the onclick listener to open the chosen csv file
        Preference fileOpener = findPreference("fileOpen");
        fileOpener.setOnPreferenceClickListener(preference -> {
            try {
                //create an intent to open a file
                Intent myIntent = new Intent(Intent.ACTION_VIEW);
                //get the path of file stored in the preference and launch the intent
                SharedPreferences preferences = getPreferenceScreen().getSharedPreferences();
                File toto = new File(preferences.getString("filePicker", "NA"));
                myIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                myIntent.setData(FileProvider.getUriForFile(getContext(),
                        getContext().getPackageName() + ".com.lucblender.lucasbonvin",
                        toto));
                startActivity(myIntent);
            }
            catch (Exception e)
            {
                message("No application found to open csv file");
            }
            return true;
        });

        //set the onclick listener to send csvfile
        Preference sendFile = findPreference("sendFile");
        sendFile.setOnPreferenceClickListener(preference -> {

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

                startActivity(Intent.createChooser(intentShareFile, "Share File"));
            }
            else
            {
                message("File does not exist");
            }

            return true;
        });

        //set onclick listener for the help button
        Preference help = findPreference("help");
        help.setOnPreferenceClickListener(preference -> {
            //create a popup with help message
            CustomLayoutSimpleDialog helpDialog = new CustomLayoutSimpleDialog(getActivity(), R.layout.helpdialog);
            helpDialog.show();
            return true;
        });

        //set onclick listener for the about button
        Preference about = findPreference("about");
        about.setOnPreferenceClickListener(preference -> {
            //create a popup with Lesson message
            AboutDialog aboutDialog = new AboutDialog(getActivity());
            aboutDialog.show();
            return true;
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
        //get the new value from Intent data
        if (requestCode == requestCodeFilePicker && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

            SharedPreferences preferences = getPreferenceScreen().getSharedPreferences();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("filePicker", filePath);
            editor.apply();

            Set<String> favoritePath = preferences.getStringSet("favoritePath",null);

            favoritePath.add(filePath);
            editor.putStringSet("favoritePath",  favoritePath);
            editor.apply();

            //call the service that update the widget from the selected file
            try{
                getContext().startService(new Intent(getContext(), UpdateService.class));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}