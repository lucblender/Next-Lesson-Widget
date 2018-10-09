package com.lucblender.lucasbonvin.widgettest.Data;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Lesson {
    //all parameter of lesson
    public String day;
    public String hourStart;
    public String hourEnd;
    public String lessonName;
    public String city;
    public String room;

    //create an empty lesson
    public Lesson()
    {
        day = "";
        hourStart= "";
        hourEnd= "";
        lessonName= "-";
        city= "";
        room= "";
    }

    //create a lesson with parameter
    public Lesson(String day, String hourStart, String hourEnd, String lessonName, String city, String room)
    {
        this.day = day;
        this.hourStart= hourStart;
        this.hourEnd= hourEnd;
        this.lessonName= lessonName;
        this.city = city;
        this.room= room;
    }

    public boolean isDayToday()
    {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dayOfWeek = new SimpleDateFormat("EE", Locale.ENGLISH).format(date);

        if(dayOfWeek.compareTo(this.day)==0)
        {
            Log.e("a", "isDayToday: "+this.day+dayOfWeek );
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isLessonNow()
    {

        if(isDayToday())
        {
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();

            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);

            //create a clendar with just the 'hour of now'
            Calendar todayHours = Calendar.getInstance();
            todayHours.set(Calendar.HOUR_OF_DAY,hours);
            todayHours.set(Calendar.MINUTE, min);
            todayHours.set(Calendar.SECOND,0);

            Calendar lessonHoursStart = Calendar.getInstance();
            String[] partsStart = hourStart.split(":");

            Calendar lessonHoursEnd = Calendar.getInstance();
            String[] partsEnd = hourEnd.split(":");

            if(partsStart.length == 2 && partsEnd.length == 2)
            {
                lessonHoursStart.set(Calendar.HOUR_OF_DAY, Integer.parseInt(partsStart[0]));
                lessonHoursStart.set(Calendar.MINUTE, Integer.parseInt(partsStart[1]));
                lessonHoursStart.set(Calendar.SECOND, 0);

                lessonHoursEnd.set(Calendar.HOUR_OF_DAY, Integer.parseInt(partsEnd[0]));
                lessonHoursEnd.set(Calendar.MINUTE, Integer.parseInt(partsEnd[1]));
                lessonHoursEnd.set(Calendar.SECOND, 0);

                if (todayHours.after(lessonHoursStart) && todayHours.before(lessonHoursEnd)) {
                    Log.e("a", "isLessonNow: true" );
                    return true;
                }
                else
                {
                    Log.e("a", "isLessonNow: 1" );
                    return false;
                }
            }
            else
            {
                Log.e("a", "isLessonNow: 2" );
                return false;
            }
        }
        else {
            Log.e("a", "isLessonNow: 3" );
            return false;
        }
    }
}
