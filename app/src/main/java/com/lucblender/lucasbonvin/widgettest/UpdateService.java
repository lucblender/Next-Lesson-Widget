package com.lucblender.lucasbonvin.widgettest;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

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
        //get the view of the widget
        RemoteViews view = new RemoteViews(getPackageName(), com.lucblender.lucasbonvin.widgettest.R.layout.widget);
        String [] lessonData = DataCsvManager.getInstance().nextLessonFromCsv( this);

        if(lessonData!=null)
        {
            //fulfill the widget with correct data
            view.setTextViewText(com.lucblender.lucasbonvin.widgettest.R.id.date,staticDays.get(lessonData[0]));
            view.setTextViewText(com.lucblender.lucasbonvin.widgettest.R.id.time,lessonData[3]+ " - "+lessonData[4]);
            view.setTextViewText(com.lucblender.lucasbonvin.widgettest.R.id.lesson,lessonData[1]);
            view.setTextViewText(com.lucblender.lucasbonvin.widgettest.R.id.location,lessonData[5]);
            view.setTextViewText(com.lucblender.lucasbonvin.widgettest.R.id.room,lessonData[2]);
        }
        else
        {
            //didn't find any lesson --> fulfill with warning message
            setWarningText(view);
        }


        //update the widget
        ComponentName theWidget = new ComponentName(this, WidgetLesson.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.updateAppWidget(theWidget, view);
    }

    private void setWarningText(RemoteViews view)
    {
        //warning message to show on the widget in any case of error
        view.setTextViewText(com.lucblender.lucasbonvin.widgettest.R.id.date,getString(com.lucblender.lucasbonvin.widgettest.R.string.warning));
        view.setTextViewText(com.lucblender.lucasbonvin.widgettest.R.id.time," - ");
        view.setTextViewText(com.lucblender.lucasbonvin.widgettest.R.id.lesson,getString(com.lucblender.lucasbonvin.widgettest.R.string.file_error));
        view.setTextViewText(com.lucblender.lucasbonvin.widgettest.R.id.location,getString(com.lucblender.lucasbonvin.widgettest.R.string.click_to));
        view.setTextViewText(com.lucblender.lucasbonvin.widgettest.R.id.room,getString(com.lucblender.lucasbonvin.widgettest.R.string.change_file));
        view.setTextViewText(com.lucblender.lucasbonvin.widgettest.R.id.nextLessonWidget, getString(com.lucblender.lucasbonvin.widgettest.R.string.next_lesson));
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