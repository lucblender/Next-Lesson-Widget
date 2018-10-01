package com.example.lucasbonvin.widgettest;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.example.lucasbonvin.widgettest.Data.DataCsvManager;
import com.example.lucasbonvin.widgettest.UserInterface.WidgetLesson;

import java.util.HashMap;
import java.util.Map;


public class UpdateService extends Service {

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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //get the view of the widget
        RemoteViews view = new RemoteViews(getPackageName(), R.layout.widget);
        String [] lessonData = DataCsvManager.getInstance().nextLessonFromCsv(this);

        if(lessonData!=null)
        {
            //fulfill the widget with correct data
            view.setTextViewText(R.id.date,staticDays.get(lessonData[0]));
            view.setTextViewText(R.id.time,lessonData[3]+ " - "+lessonData[4]);
            view.setTextViewText(R.id.lesson,lessonData[1]);
            view.setTextViewText(R.id.location,lessonData[5]);
            view.setTextViewText(R.id.room,lessonData[2]);
        }
        else
        {
            //didn't find any lesson --> fulfill with warning message
            setWarningText(view);
        }


        //update the widget
        ComponentName theWidget = new ComponentName(this, WidgetLesson.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(theWidget, view);

        return super.onStartCommand(intent, flags, startId);
    }

    private void setWarningText(RemoteViews view)
    {
        //warning message to show on the widget in any case of error
        view.setTextViewText(R.id.date,"Warning");
        view.setTextViewText(R.id.time," - ");
        view.setTextViewText(R.id.lesson,"File Error");
        view.setTextViewText(R.id.location,"click to");
        view.setTextViewText(R.id.room,"change file");
    }

}