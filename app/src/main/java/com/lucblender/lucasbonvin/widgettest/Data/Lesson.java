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

package com.lucblender.lucasbonvin.widgettest.Data;

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

    private boolean isDayToday()
    {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dayOfWeek = new SimpleDateFormat("EE", Locale.ENGLISH).format(date);

        return dayOfWeek.compareTo(this.day) == 0;
    }

    public boolean isLessonNow()
    {

        if(isDayToday())
        {
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

                return todayHours.after(lessonHoursStart) && todayHours.before(lessonHoursEnd);
            }
            else
            {
                return false;
            }
        }
        else {
            return false;
        }
    }
}
