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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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

        //get the csvfile
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String fileURI =  preferences.getString("filePicker","NA");
        String[] parsedUri = fileURI.split("\\.");
        String extension = parsedUri[parsedUri.length-1];

        //if extension is correct --> parse it
        if(extension.compareTo("csv") == 0)
        {
            try
            {
                CSVReader reader = new CSVReader(new FileReader(fileURI));

                String [] nextLine;
                String [] lessonData = null;
                boolean lessonFound = false;

                //create a calender to get the the day of the week and actual time
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String dayOfWeek = new SimpleDateFormat("EE", Locale.ENGLISH).format(date);
                String saveDayOfWeek = dayOfWeek;
                int hours = calendar.get(Calendar.HOUR);
                int min = calendar.get(Calendar.MINUTE);

                //create a clendar with just the 'hour of now'
                Calendar todayHours = Calendar.getInstance();
                todayHours.set(Calendar.HOUR_OF_DAY,hours);
                todayHours.set(Calendar.MINUTE, min);
                todayHours.set(Calendar.SECOND,0);

                Calendar lessonHours = Calendar.getInstance();

                //parse the csv file
                while((nextLine = reader.readNext()) != null)
                {
                    if(nextLine[0].compareTo(dayOfWeek)==0)
                    {
                        String[] parts = nextLine[3].split(":");
                        lessonHours.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                        lessonHours.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
                        lessonHours.set(Calendar.SECOND, 0);

                        //test if the lesson is before or after the actual time
                        // if before --> mean we found the next lesson
                        if (todayHours.before(lessonHours)) {
                            lessonFound = true;
                            lessonData = nextLine;
                            break;
                        }
                    }
                }
                reader.close();Date dt = new Date();

                //if lesson still not found -> mean it's maybe next day
                while (!lessonFound)
                {
                    Calendar c = Calendar.getInstance();
                    c.setTime(dt);
                    //increment one day
                    c.add(Calendar.DATE, 1);
                    dt = c.getTime();
                    dayOfWeek = new SimpleDateFormat("EE", Locale.ENGLISH).format(dt);

                    //if we are back to the day of today --> mean we already checked the full week
                    if(dayOfWeek.compareTo(saveDayOfWeek)==0)
                    {
                        lessonFound = true;
                    }
                    else {
                        //parse in the file the next lesson
                        reader = new CSVReader(new FileReader(fileURI));
                        while ((nextLine = reader.readNext()) != null) {
                            if (nextLine[0].compareTo(dayOfWeek) == 0) {
                                lessonFound = true;
                                lessonData = nextLine;
                                break;
                            }
                        }
                        reader.close();
                    }
                }

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
            }
            catch (Exception e)
            {
                //if any error in the file --> fulfill with warning message
                setWarningText(view);
                e.printStackTrace();

            }
        }
        else
        {
            //if file not csv --> fulfill with warning message
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