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

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.lucblender.lucasbonvin.widgettest.R;
import com.lucblender.lucasbonvin.widgettest.UpdateService;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.util.Locale;
import java.util.Set;

public class ScreenSliderActivity  extends AppCompatActivity implements AppPreferenceFragment.PreferenceListener {

    private static final String TAG = ScreenSliderActivity.class.getName();

    private static final int requestCodeFilePicker = 125;

    private static final int NUM_PAGES = 2;

    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


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

        String language = sharedPreferences.getString("param_language", "en");
        Resources res = getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(language);
                res.updateConfiguration(conf, dm);

        setContentView(R.layout.screenslider);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_legacy_only);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        mPager = findViewById(R.id.pager);
        TabLayout tabLayout = findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mPager, true);

        // Instantiate a ViewPager and a PagerAdapter.
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

        int[] appWidgetIDs = appWidgetManager
                .getAppWidgetIds(new ComponentName(getApplicationContext(), WidgetLesson.class));

        if(appWidgetIDs.length==0)
        {
            messageHTML(getString(R.string.warning_no_widget));
        }

        updateMyWidgets(getApplicationContext());

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //depending of position get one fragment or the other
            if(position == 1)
                return new EditorFragment();
            else
                return new AppPreferenceFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public void updateLanguage(String lang) {
        setLocale(lang);
    }

    @Override
    public void updateTheme(String themeKey) {
        Intent refresh = new Intent(this, ScreenSliderActivity.class);
        startActivity(refresh);
        finish();
    }


    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, ScreenSliderActivity.class);
        startActivity(refresh);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //get the new value from Intent data
        if (requestCode == requestCodeFilePicker && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("filePicker", filePath);
            editor.apply();

            Set<String> favoritePath = preferences.getStringSet("favoritePath",null);

            favoritePath.add(filePath);
            editor.putStringSet("favoritePath",  favoritePath);
            editor.apply();
            //call the service that update the widget from the selected file
            try{
                startService(new Intent(this, UpdateService.class));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void updateMyWidgets(Context context) {
        AppWidgetManager man = AppWidgetManager.getInstance(context);
        int[] ids = man.getAppWidgetIds(
                new ComponentName(context,WidgetLesson.class));
        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(WidgetLesson.WIDGET_IDS_KEY, ids);
        context.sendBroadcast(updateIntent);
    }

    //show a toast message
    private void message(String s)
    {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }
    //show a toast message
    private void messageHTML(String s)
    {
            Toast.makeText(this, Html.fromHtml(s), Toast.LENGTH_LONG).show();
    }
}