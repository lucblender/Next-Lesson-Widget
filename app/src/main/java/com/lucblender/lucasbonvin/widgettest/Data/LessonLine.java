package com.lucblender.lucasbonvin.widgettest.Data;

public class LessonLine {

    //a lesson line is just a week of lesson
    public Lesson[] lessons;

    public LessonLine()
    {
        //create 7 lessons -> one week
        lessons = new Lesson[7];

        for(int i = 0; i<7;i++)
            lessons[i] = new Lesson();
    }
}
