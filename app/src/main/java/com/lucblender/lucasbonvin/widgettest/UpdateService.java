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

package com.lucblender.lucasbonvin.widgettest;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.lucblender.lucasbonvin.widgettest.Data.CustomPreferencesManager;
import com.lucblender.lucasbonvin.widgettest.Data.DataCsvManager;
import com.lucblender.lucasbonvin.widgettest.UserInterface.WidgetLesson;

import java.util.HashMap;
import java.util.Map;


public class UpdateService extends JobService {

    private static final String TAG = UpdateService.class.getName();

    public static final Map<String, String> staticDays = new HashMap<>();

    static {
        staticDays.put("Mon", "Monday");
        staticDays.put("Tue", "Tuesday");
        staticDays.put("Wed", "Wednesday");
        staticDays.put("Thu", "Thursday");
        staticDays.put("Fri", "Friday");
        staticDays.put("Sat", "Saturday");
        staticDays.put("Sun", "Sunday");
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStartJob(JobParameters params) {

        updateWidget();
        scheduleRefresh();
        jobFinished(params,false);

        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        updateWidget();

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWidget()
    {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIDs = appWidgetManager
                .getAppWidgetIds(new ComponentName(getApplicationContext(), WidgetLesson.class));

        Log.e(TAG, "updateWidget: ");
        for (int i = 0 ; i < appWidgetIDs.length ; i++) {
            //get the view of the widget
            RemoteViews view = new RemoteViews(getPackageName(), R.layout.widget);

            Map<String, String> pathWidgetmap = CustomPreferencesManager.getInstance().loadMap(getApplicationContext(), "widgetPathMap");
            Map<String, String> nextTextMap = CustomPreferencesManager.getInstance().loadMap(getApplicationContext(), "widgetNextTextMap");

            String[] lessonData;
            if(pathWidgetmap.get(String.valueOf(appWidgetIDs[i]))!=null)
            {
                lessonData = DataCsvManager.getInstance().nextLessonFromCsv(this, pathWidgetmap.get(String.valueOf(appWidgetIDs[i])));
            }else {
                lessonData = DataCsvManager.getInstance().nextLessonFromCsv(this);
            }


            if(nextTextMap.get(String.valueOf(appWidgetIDs[i]))!=null)
                view.setTextViewText(R.id.nextLessonWidget,"Next "+nextTextMap.get(String.valueOf(appWidgetIDs[i])));
            else
                view.setTextViewText(R.id.nextLessonWidget,"Next Lesson");

            if (lessonData != null) {
                //fulfill the widget with correct data
                view.setTextViewText(R.id.date, staticDays.get(lessonData[0]));
                view.setTextViewText(R.id.time, lessonData[3] + " - " + lessonData[4]);
                view.setTextViewText(R.id.lesson, lessonData[1]);
                view.setTextViewText(R.id.location, lessonData[5]);
                view.setTextViewText(R.id.room, lessonData[2]);
            } else {
                //didn't find any lesson --> fulfill with warning message
                setWarningText(view);
            }


            //update the widget
            ComponentName theWidget = new ComponentName(this, WidgetLesson.class);

            appWidgetManager.updateAppWidget(appWidgetIDs[i], view);
        }
    }

    private void setWarningText(RemoteViews view)
    {
        //warning message to show on the widget in any case of error
        view.setTextViewText(R.id.date,getString(com.lucblender.lucasbonvin.widgettest.R.string.warning));
        view.setTextViewText(R.id.time," - ");
        view.setTextViewText(R.id.lesson,getString(com.lucblender.lucasbonvin.widgettest.R.string.file_error));
        view.setTextViewText(R.id.location,getString(com.lucblender.lucasbonvin.widgettest.R.string.click_to));
        view.setTextViewText(R.id.room,getString(com.lucblender.lucasbonvin.widgettest.R.string.change_file));
        view.setTextViewText(R.id.nextLessonWidget, getString(com.lucblender.lucasbonvin.widgettest.R.string.next_lesson));
    }

    private void scheduleRefresh() {

        JobInfo jobInfo = new JobInfo.Builder(10,
                new ComponentName(getApplicationContext(), UpdateService.class))
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                .setPersisted(true)
                .setMinimumLatency(1*60*1000).build();

        JobScheduler jobScheduler = (JobScheduler)getApplicationContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
    }


}