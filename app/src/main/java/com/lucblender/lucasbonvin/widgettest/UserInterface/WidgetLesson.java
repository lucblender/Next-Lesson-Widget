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
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;

import com.lucblender.lucasbonvin.widgettest.Data.CustomPreferencesManager;
import com.lucblender.lucasbonvin.widgettest.R;
import com.lucblender.lucasbonvin.widgettest.UpdateService;

import java.util.Map;

public class WidgetLesson extends AppWidgetProvider {

    private static final String TAG = UpdateService.class.getName();

    private PendingIntent pendingIntent = null;

    public static final String WIDGET_IDS_KEY ="mywidgetproviderwidgetids";


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        for (int i = 0; i < appWidgetIds.length; i++) {
            CustomPreferencesManager.getInstance().removeWidgetColor(context, appWidgetIds[i]);
            CustomPreferencesManager.getInstance().removeWidgetPathMap(context, appWidgetIds[i]);
            CustomPreferencesManager.getInstance().removeWidgetnextText(context, appWidgetIds[i]);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Map<String, String> colorIdMap = CustomPreferencesManager.getInstance().loadMap(context, "widgetColorMap");


        for (int i = 0 ; i < appWidgetIds.length ; i++) {
            //get remote views
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            //create an intent to open the main activity class

            int backgroundColor = Color.argb(0,0,0,0);

            if(colorIdMap.get(String.valueOf(appWidgetIds[i])) != null)
                backgroundColor = Integer.valueOf(colorIdMap.get(String.valueOf(appWidgetIds[i])));
            else
                colorIdMap.put(String.valueOf(appWidgetIds[i]), String.valueOf(backgroundColor));

            views.setInt(R.id.widgetBackground, "setColorFilter", backgroundColor);
            views.setInt(R.id.widgetBackground, "setImageAlpha", Color.alpha(backgroundColor));

            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setComponent(new ComponentName(context.getPackageName(), ScreenSliderActivity.class.getName()));
            PendingIntent pendingIntent = PendingIntent.getActivity(
            context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.mainLayout, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }

        final Intent i = new Intent(context, UpdateService.class);

        if (pendingIntent == null) {
            pendingIntent = PendingIntent.getService(context, 125, i, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        JobInfo jobInfo = new JobInfo.Builder(10,
                new ComponentName(context, UpdateService.class))
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                .setPersisted(true)
                .setMinimumLatency(1*60*1000).build();

        JobScheduler jobScheduler = (JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);

        try {
            context.startService(new Intent(context, UpdateService.class));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);

        if (intent.hasExtra(WIDGET_IDS_KEY)) {
            int[] ids = intent.getExtras().getIntArray(WIDGET_IDS_KEY);
            this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
        }
    }
}
