package com.example.lucasbonvin.widgettest.UserInterface;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lucasbonvin.widgettest.R;
import com.example.lucasbonvin.widgettest.UpdateService;

import java.io.File;

public class AppPreferenceFragment extends PreferenceFragmentCompat{

    private static final String TAG = AppPreferenceFragment.class.getName();
    private static final int requestCodeFilePicker = 125;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //Set the preference from xml
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //set the onclick listener to choose csvfile
        Preference filePicker = (Preference) findPreference("filePicker");
        filePicker.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //create an intent to open a file browser
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //Intent to start openIntents File Manager
                intent.setType("file/*");
                startActivityForResult(intent, requestCodeFilePicker);
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
                SharedPreferences preferences = getPreferenceScreen().getSharedPreferences();;
                File toto = new File(preferences.getString("filePicker","NA"));
                myIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                myIntent.setData(FileProvider.getUriForFile(getContext(),
                        getContext().getPackageName()+".com.example.lucasbonvin",
                        toto));
                startActivity(myIntent);
                return true;
            }
        });

        //set onclick listener for the help button
        Preference help = (Preference) findPreference("help");
        help.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //create a popup with help message
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Help");
                builder.setMessage("Your csv file must be formated as specified:\n" +
                        "\t• every line must describe a lesson\n" +
                        "\t• all lessons -> chronological order\n" +
                        "\t• the format of a line is: \n" +
                        "DDD,lessonName,Room,hh:mm start,hh:mm end,Location\n" +
                        "\t• lesson eg: \n" +
                        "Wed,Sin,A205,8:45,11:10,Sion\n\n" +
                        "Main user interface:\n" +
                        "\t• Widget, next lesson\n" +
                        "Secondary user interface:\n" +
                        "\t• Swipe right, full planner\n" +
                        "\t\t• Planner editable\n" +
                        "\t\t• Long click to delete\n" +
                        "\t\t• Plus button to add\n");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
                builder.show();
                return true;
            }
        });


        //set onclick listener for the about button
        Preference about = (Preference) findPreference("about");
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //create a popup with Lesson message
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("About");
                builder.setMessage("Lesson planning widget\n" +
                        "Made by Lucas Bonvin\n" +
                        "Code available on github: lucblender\n" +
                        "Free to use and modify");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
                builder.show();
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
                    message("Permission granted");
                }
                else
                {
                    message("Permission to access your media not granted!");
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
        if(requestCode == requestCodeFilePicker)
        {
            //if the intent return data
            if(data!= null){
                //convert the URI to a correct file path
                String newValue = data.getData().getPath().replace("/storage_root", Environment.getExternalStorageDirectory().toString());
                //Get back the storage reference and store the file path
                SharedPreferences preferences = getPreferenceScreen().getSharedPreferences();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("filePicker", newValue);
                editor.apply();
                Preference pref = findPreference("filePicker");
                pref.setSummary(preferences.getString("filePicker", "NA"));
                //call the service that update the widget from the selected file
                getActivity().startService(new Intent(getActivity(), UpdateService.class));
            }

        }
    }
}