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

import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.lucblender.lucasbonvin.widgettest.Data.CustomPreferencesManager;
import com.lucblender.lucasbonvin.widgettest.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WidgetParametersActivity extends AppCompatActivity {

    private static final String TAG = WidgetParametersActivity.class.getName();

    private int mAppWidgetId = -1;
    private int backgroundColor = 0x00ffffff;
    private ImageView widgetBackground;

    private TextView previewNextLesson;
    private TextView previewDay;
    private TextView previewHour;
    private TextView previewLessonName;
    private TextView previewLocation;
    private TextView previewRoom;
    private ImageView colorCircle;
    private Spinner spinnerParamFile;
    private Spinner spinnerNextText;

    private Map<String, String> spinnerItemToPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String theme = sharedPreferences.getString("param_theme", "dark");

        switch (theme) {
            case "light":
                setTheme(R.style.AppThemeLight);
                break;
            case "dark":
                setTheme(R.style.AppThemeDark);
                break;
            case "blueneon":
                setTheme(R.style.AppThemeBlueNeon);
                break;
            case "test":
                setTheme(R.style.AppThemeDebug);
                break;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widgetparameter);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_legacy_only);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        findViewById(R.id.widgetParamCancel).setOnClickListener(v -> finish());

        findViewById(R.id.widgetParamOk).setOnClickListener(v -> {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            RemoteViews views = new RemoteViews(this.getPackageName(),R.layout.widget);

            Intent intentClick = new Intent("android.intent.action.MAIN");
            intentClick.addCategory("android.intent.category.LAUNCHER");

            intentClick.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intentClick.setComponent(new ComponentName(this.getPackageName(), ScreenSliderActivity.class.getName()));
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this, 0, intentClick, 0);
            views.setOnClickPendingIntent(R.id.mainLayout, pendingIntent);
            views.setInt(R.id.widgetBackground, "setColorFilter", backgroundColor);
            views.setInt(R.id.widgetBackground, "setImageAlpha", Color.alpha(backgroundColor));
            views.setTextViewText(R.id.nextLessonWidget,"Next "+spinnerNextText.getSelectedItem().toString());

            appWidgetManager.updateAppWidget(mAppWidgetId, views);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            Log.e(TAG, "onCreate id: " + String.valueOf(mAppWidgetId) );
            CustomPreferencesManager.getInstance().addWidgetColor(getBaseContext(), mAppWidgetId, backgroundColor);
            CustomPreferencesManager.getInstance().addWidgetPathMap(getBaseContext(), mAppWidgetId, spinnerItemToPath.get(spinnerParamFile.getSelectedItem().toString()));
            CustomPreferencesManager.getInstance().addWidgetNextText(getBaseContext(), mAppWidgetId, spinnerNextText.getSelectedItem().toString());
            Log.e(TAG, "onCreate id: " + String.valueOf(backgroundColor) );
            finish();
        });

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();

        previewNextLesson = findViewById(R.id.previewNextLesson);
        previewDay = findViewById(R.id.previewDay);
        previewHour = findViewById(R.id.previewHour);
        previewLessonName = findViewById(R.id.previewLessonName);
        previewLocation = findViewById(R.id.previewLocation);
        previewRoom = findViewById(R.id.previewRoom);
        colorCircle = findViewById(R.id.colorCircle);
        colorCircle.setColorFilter(backgroundColor);

        widgetBackground = findViewById(R.id.widgetExample);

        widgetBackground.setColorFilter(backgroundColor);
        widgetBackground.setImageAlpha(Color.alpha(backgroundColor));

        spinnerParamFile = findViewById(R.id.spinnerParamFile);
        spinnerNextText = findViewById(R.id.spinnerNextText);

        SharedPreferences preferences = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);

        Set<String> favoritePath = preferences.getStringSet("favoritePath",null);
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> listNext = new ArrayList<>();
        spinnerItemToPath = new HashMap<>();

        for (String str : favoritePath) {
            String[] splited = str.split("/");
            list.add(splited[splited.length-1]);
            spinnerItemToPath.put(splited[splited.length-1],str);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item, list);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerParamFile.setAdapter(arrayAdapter);

        String[] splitedPath = preferences.getString("filePicker", "NA").split("/");
        spinnerParamFile.setSelection(arrayAdapter.getPosition(splitedPath[splitedPath.length-1]));

        listNext.add("Lesson");
        listNext.add("Activity");
        listNext.add("Task");

        ArrayAdapter<String> arrayAdapterNext = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item, listNext);
        arrayAdapterNext.setDropDownViewResource(R.layout.spinner_item);
        spinnerNextText.setAdapter(arrayAdapterNext);

        spinnerNextText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                previewNextLesson.setText("Next " + spinnerNextText.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ImageView imageBackground = findViewById(R.id.imageBackground);
        imageBackground.setImageDrawable(wallpaperDrawable);

        SeekBar seekBar = findViewById(R.id.testSeek);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                backgroundColor = Color.argb(255*progress/100,
                        Color.red(backgroundColor),
                        Color.green(backgroundColor),
                        Color.blue(backgroundColor));


                widgetBackground.setColorFilter(backgroundColor);
                widgetBackground.setImageAlpha(Color.alpha(backgroundColor));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.colorTest).setOnClickListener(v -> {
            ColorPickerDialogBuilder
                    .with(this, R.style.MyDialogThemeShared)
                    .setTitle(R.string.choose_color)
                    .initialColor(Color.argb(255,
                            Color.red(backgroundColor),
                            Color.green(backgroundColor),
                            Color.blue(backgroundColor)))
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .showAlphaSlider(false)
                    .setOnColorSelectedListener(selectedColor -> {
                    })
                    .setPositiveButton(R.string.ok, (dialog, selectedColor, allColors) -> {
                        backgroundColor = Color.argb(Color.alpha(backgroundColor),
                                Color.red(selectedColor),
                                Color.green(selectedColor),
                                Color.blue(selectedColor));
                        colorCircle.setColorFilter(selectedColor);
                        widgetBackground.setColorFilter(backgroundColor);
                        widgetBackground.setImageAlpha(Color.alpha(backgroundColor));
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    })
                    .build()
                    .show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.parameterButton) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setComponent(new ComponentName(getPackageName(), ScreenSliderActivity.class.getName()));

            startActivity(intent);
        }
        else if(id == R.id.helpButton) {
            CustomLayoutSimpleDialog helpDialog = new CustomLayoutSimpleDialog(this, R.layout.helpdialogwidgetparam);
            helpDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void message(String s)
    {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }
}
