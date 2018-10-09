package com.lucblender.lucasbonvin.widgettest.UserInterface;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.RemoteViews;

import com.lucblender.lucasbonvin.widgettest.R;
import com.lucblender.lucasbonvin.widgettest.UpdateService;

import java.util.Calendar;
import java.util.Date;

public class WidgetLesson extends AppWidgetProvider {

    private PendingIntent pendingIntent = null;


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
            Intent intent = new Intent(context, ScreenSliderActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            // Get the layout for the App Widget and attach an on-click listener to the button
            views.setOnClickPendingIntent(R.id.mainLayout, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }

        //create the alarmManager that will update the widget with the help of a service
        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent i = new Intent(context, UpdateService.class);

        if (pendingIntent == null) {
            pendingIntent = PendingIntent.getService(context, 125, i, PendingIntent.FLAG_CANCEL_CURRENT);
        }
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE,1);
        //manager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 1*60*1000, pendingIntent);
        manager.setRepeating(AlarmManager.RTC_WAKEUP,SystemClock.elapsedRealtime(),1*60*1000, pendingIntent);
        //manager.setRepeating(AlarmManager.RTC_WAKEUP,SystemClock.elapsedRealtime(),AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
        context.startService(new Intent(context, UpdateService.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}
