package com.lucblender.lucasbonvin.widgettest.UserInterface;

import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.lucblender.lucasbonvin.widgettest.R;
import com.lucblender.lucasbonvin.widgettest.UpdateService;

public class WidgetLesson extends AppWidgetProvider {

    private static final String TAG = UpdateService.class.getName();

    private PendingIntent pendingIntent = null;

    public static final String WIDGET_IDS_KEY ="mywidgetproviderwidgetids";


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {


        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int i = 0 ; i < appWidgetIds.length ; i++) {
            //get remote views
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            //create an intent to open the main activity class

            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");

            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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
