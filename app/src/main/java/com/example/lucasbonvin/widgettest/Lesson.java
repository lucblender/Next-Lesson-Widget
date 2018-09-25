package com.example.lucasbonvin.widgettest;

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
}
