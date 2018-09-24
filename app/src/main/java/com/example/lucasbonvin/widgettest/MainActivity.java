package com.example.lucasbonvin.widgettest;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        Preference filePicker = (Preference) findPreference("filePicker");
        filePicker.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //Intent to start openIntents File Manager
                intent.setType("file/*");
                startActivityForResult(intent, 125);
                return true;
            }
        });

        Preference fileOpener = (Preference) findPreference("fileOpen");
        fileOpener.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW);

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                File toto = new File(preferences.getString("filePicker","NA"));
                myIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                myIntent.setData(FileProvider.getUriForFile(getBaseContext(),
                        getApplicationContext().getPackageName()+".com.example.lucasbonvin",
                        toto));
                startActivity(myIntent);
                return true;
            }
        });

        Preference help = (Preference) findPreference("help");
        help.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Help");
                builder.setMessage("Your csv file must be formated as specified:\n" +
                        "\t-every line must describe a lesson\n" +
                        "\t-all lessons -> chronological order\n" +
                        "\t-the format of a line is: \n" +
                        "DDD,lessonName,Room,hh:mm start,hh:mm end,Location\n" +
                        "\t-lesson eg: \n" +
                        "Wed,Sin,A205,8:45,11:10,Sion\n\n" +
                        "Main user interface: Widget");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        //action on dialog close
                    }

                });

                builder.show();
                return true;
            }
        });

        Preference about = (Preference) findPreference("about");
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("About");
                builder.setMessage("Lesson planning widget\n" +
                        "Made by Lucas Bonvin\n" +
                        "Code available on github: lucblender\n" +
                        "Free to use and modify");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        //action on dialog close
                    }

                });

                builder.show();
                return true;
            }
        });



        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {

        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Preference pref = findPreference("filePicker");
        pref.setSummary(preferences.getString("filePicker","NA"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case  0:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    message("Permission granted");
                }
                else
                {
                    message("Permission to access your media not granted!");
                    this.finish();
                }
                return;
            }
        }
    }

    void message(String s)
    {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //get the new value from Intent data

        if(requestCode == 125)
        {
            if(data!= null) {
                String newValue = data.getData().getPath().replace("/storage_root", Environment.getExternalStorageDirectory().toString());

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("filePicker", newValue);
                editor.commit();

                Preference pref = findPreference("filePicker");
                pref.setSummary(preferences.getString("filePicker", "NA"));

                startService(new Intent(this, UpdateService.class));
            }

        }
    }
}