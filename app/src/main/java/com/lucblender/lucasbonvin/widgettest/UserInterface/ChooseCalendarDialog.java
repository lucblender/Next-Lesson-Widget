/*
Copyright © 2018, Lucas Bonvin

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the “Software”), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Software.

The Software is provided “as is”, without warranty of any kind, express or implied, including but
not limited to the warranties of merchantability, fitness for a particular purpose and
noninfringement. In no event shall the authors or copyright holders be liable for any claim,
damages or other liability, whether in an action of contract, tort or otherwise, arising from,
out of or in connection with the software or the use or other dealings in the Software.

Except as contained in this notice, the name of Lucas Bonvin shall not be used in
advertising or otherwise to promote the sale, use or other dealings in this Software without
prior written authorization from Lucas Bonvin.
 */

package com.lucblender.lucasbonvin.widgettest.UserInterface;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.lucblender.lucasbonvin.widgettest.Data.DataCsvManager;
import com.lucblender.lucasbonvin.widgettest.PathAdaptater;
import com.lucblender.lucasbonvin.widgettest.PathItem;
import com.lucblender.lucasbonvin.widgettest.R;
import com.nbsp.materialfilepicker.MaterialFilePicker;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class ChooseCalendarDialog extends Dialog {

    private final String TAG = ChooseCalendarDialog.class.getName();
    private static final int requestCodeFilePicker = 125;

    private Activity activity;

    public ChooseCalendarDialog(Context context, Activity activity) {
        super(context);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choosecalendardialog);

        ListView listView = findViewById(R.id.listPath);
        LinearLayout addNewPathLayout = findViewById(R.id.addNewPathLayout);

        ArrayList<PathItem> dataModels = new ArrayList<>();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);

        SharedPreferences.Editor editor = preferences.edit();
        Set<String> favoritePath = preferences.getStringSet("favoritePath",null);

        if(favoritePath == null) {
            Set<String> pathSet = new LinkedHashSet<>();
            pathSet.add(DataCsvManager.getInstance().getCsvFile(getContext()).URI);
            editor.putStringSet("favoritePath",  pathSet);
            editor.apply();
        }
        else {
            if(favoritePath.size()==0)
            {
                Set<String> pathSet = new LinkedHashSet<>();
                pathSet.add(DataCsvManager.getInstance().getCsvFile(getContext()).URI);
                editor.putStringSet("favoritePath",  pathSet);
                editor.apply();
                favoritePath = preferences.getStringSet("favoritePath",null);
            }

            for (String s : favoritePath) {
                if (s.equals(DataCsvManager.getInstance().getCsvFile(getContext()).URI))
                    dataModels.add(new PathItem(s, true));
                else
                    dataModels.add(new PathItem(s, false));
            }
        }


        PathAdaptater adapter = new PathAdaptater(dataModels, getContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {


        });

        findViewById(R.id.choose_calendar_help).setOnClickListener(v -> {
            CustomLayoutSimpleDialog chooseCalendarHelpDialog = new CustomLayoutSimpleDialog(getContext(), R.layout.choosecalendarhelpdialog);
            chooseCalendarHelpDialog.show();
        });

        addNewPathLayout.setOnClickListener(v -> {
            dismiss();
            new MaterialFilePicker()
                    .withRootPath("/storage/")
                    .withActivity(activity)
                    .withRequestCode(requestCodeFilePicker)
                    .withFilter(Pattern.compile(".*\\.csv$")) // Filtering files and directories by file name using regexp
                    .withFilterDirectories(false) // Set directories filterable (false by default)
                    .withHiddenFiles(true) // Show hidden files and folders
                    .start();
        });
    }

    //show a toast message
    void message(String s)
    {
        Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
    }
}
