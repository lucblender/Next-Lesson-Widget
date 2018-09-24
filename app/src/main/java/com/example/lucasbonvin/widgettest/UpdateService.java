package com.example.lucasbonvin.widgettest;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class UpdateService extends Service {

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

        RemoteViews view = new RemoteViews(getPackageName(), R.layout.widget);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String fileURI =  preferences.getString("filePicker","NA");
        String[] parsedUri = fileURI.split("\\.");
        String extension = parsedUri[parsedUri.length-1];

        if(extension.compareTo("csv") == 0)
        {
            try
            {
                CSVReader reader = new CSVReader(new FileReader(fileURI));

                String [] nextline;
                String [] lessonData = null;

                boolean lessonFound = false;

                int l = 0;

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String dayOfWeek = new SimpleDateFormat("EE", Locale.ENGLISH).format(date);
                String saveDayOfWeek = dayOfWeek;
                int hours = calendar.get(Calendar.HOUR);
                int min = calendar.get(Calendar.MINUTE);

                Calendar todayHours = Calendar.getInstance();
                todayHours.set(Calendar.HOUR_OF_DAY,hours);
                todayHours.set(Calendar.MINUTE, min);
                todayHours.set(Calendar.SECOND,0);

                Calendar lessonHours = Calendar.getInstance();


                while((nextline = reader.readNext()) != null)
                {
                    if(nextline[0].compareTo(dayOfWeek)==0)
                    {
                        String[] parts = nextline[3].split(":");
                        lessonHours.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                        lessonHours.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
                        lessonHours.set(Calendar.SECOND, 0);

                        if (todayHours.before(lessonHours)) {
                            lessonFound = true;
                            lessonData = nextline;
                            break;
                        }
                    }


                    l = l+1;
                }

                reader.close();Date dt = new Date();

                while (lessonFound == false)
                {

                    Calendar c = Calendar.getInstance();
                    c.setTime(dt);
                    c.add(Calendar.DATE, 1);
                    dt = c.getTime();
                    dayOfWeek = new SimpleDateFormat("EE", Locale.ENGLISH).format(dt);

                    if(dayOfWeek.compareTo(saveDayOfWeek)==0)
                    {
                        lessonFound = true;
                    }
                    else {

                        reader = new CSVReader(new FileReader(fileURI));
                        while ((nextline = reader.readNext()) != null) {
                            if (nextline[0].compareTo(dayOfWeek) == 0) {
                                lessonFound = true;
                                lessonData = nextline;
                                break;
                            }
                        }
                        reader.close();
                    }
                }

                if(lessonData!=null)
                {
                    view.setTextViewText(R.id.date,staticDays.get(lessonData[0]));
                    view.setTextViewText(R.id.time,lessonData[3]+ " - "+lessonData[4]);
                    view.setTextViewText(R.id.lesson,lessonData[1]);
                    view.setTextViewText(R.id.location,lessonData[5]);
                    view.setTextViewText(R.id.room,lessonData[2]);
                }
                else
                {
                    setWarningText(view);
                }


            }
            catch (Exception e)
            {
                setWarningText(view);
                e.printStackTrace();

            }
        }
        else
        {
            setWarningText(view);
        }




        ComponentName theWidget = new ComponentName(this, WidgetTest.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(theWidget, view);

        return super.onStartCommand(intent, flags, startId);
    }

    private void setWarningText(RemoteViews view)
    {
        view.setTextViewText(R.id.date,"Warning");
        view.setTextViewText(R.id.time," - ");
        view.setTextViewText(R.id.lesson,"File Error");
        view.setTextViewText(R.id.location,"click to");
        view.setTextViewText(R.id.room,"change file");
    }

}