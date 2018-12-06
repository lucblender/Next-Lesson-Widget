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
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.lucblender.lucasbonvin.widgettest.R;
import com.lucblender.lucasbonvin.widgettest.UpdateService;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ScreenSliderActivity  extends AppCompatActivity implements AppPreferenceFragment.PreferenceListener, EditorFragment.screenScrollInterface {

    private static final String TAG = ScreenSliderActivity.class.getName();

    private static final int requestCodeFilePicker = 125;

    private static final int NUM_PAGES = 2;

    private ViewPager mPager;

    private int currentPageTemp = 0;
    private int currentPage = 0;
    private boolean sliding = false;

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


        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(currentPage == 1 && !sliding) {
                    sliding = true;
                    List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

                    for(Fragment fragment : fragmentList) {
                        if (fragment.getView().getTag() != null){
                            if (fragment.getView().getTag().equals("EditorFragmentView")) {
                                ((EditorFragment) fragment).hideFloatingActionMenu();
                            }
                        }
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                currentPageTemp = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if(state == 0) {
                    currentPage = currentPageTemp;
                    sliding = false;
                    if (currentPage == 1) {
                        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

                        for(Fragment fragment : fragmentList) {
                            if (fragment.getView().getTag() != null){
                                if (fragment.getView().getTag().equals("EditorFragmentView")) {
                                    ((EditorFragment) fragment).showFloatingActionMenu();
                                }
                            }
                        }
                    }
                }
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mPager, true);

        // Instantiate a ViewPager and a PagerAdapter.
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), this);
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
                    this.finish();
                }
                break;
            }
        }
    }

    @Override
    public void delegateScreenSlide() {
        mPager.setCurrentItem(0, true);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public EditorFragment.screenScrollInterface delegate;

        public ScreenSlidePagerAdapter(FragmentManager fm, EditorFragment.screenScrollInterface delegate) {
            super(fm);
            this.delegate = delegate;
        }

        @Override
        public Fragment getItem(int position) {
            //depending of position get one fragment or the other
            if(position == 1) {
                return new EditorFragment();
            }
            else
                return new AppPreferenceFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenuslide, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.helpSlideButton) {
            CustomLayoutSimpleDialog helpDialog = new CustomLayoutSimpleDialog(this, R.layout.helpdialog);
            helpDialog.show();
        }
        return super.onOptionsItemSelected(item);
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