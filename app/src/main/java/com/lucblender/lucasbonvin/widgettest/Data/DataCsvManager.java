package com.lucblender.lucasbonvin.widgettest.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataCsvManager extends Observable{

    private static final String TAG = DataCsvManager.class.getName();

    private static DataCsvManager instance;

    public static final Map<String, Integer> staticDays = new HashMap<>();

    static {
        staticDays.put("Mon", 0);
        staticDays.put("Tue", 1);
        staticDays.put("Wed", 2);
        staticDays.put("Thu", 3);
        staticDays.put("Fri", 4);
        staticDays.put("Sat", 5);
        staticDays.put("Sun", 6);
    }

    class FileUriExtension
    {
        public String URI;
        public String Extension;
    }

    private DataCsvManager()
    {

    }

    public static DataCsvManager getInstance() {
        if(instance==null)
            instance = new DataCsvManager();
        return instance;
    }

    public ArrayList<LessonLine> getArrayLesson(Context context){
        ArrayList<LessonLine> mLessonLines = new ArrayList<>();
        //clear the lessonlines ArrayList
        mLessonLines.clear();

        //get the csv file from preferences
        FileUriExtension  fileUriExtension = getCsvFile(context);

        //array used to know how many lesson there is per day
        int[] numberLessonPerDay = new int[7];
        for (int i = 0; i < 7; i++) //fill with 0
            numberLessonPerDay[i] = 0;

        //if file is csv, parse it
        if (fileUriExtension.Extension.compareTo("csv") == 0) {
            try {
                CSVReader reader = new CSVReader(new FileReader(fileUriExtension.URI));
                String[] nextLine;
                //read csv line by line
                while ((nextLine = reader.readNext()) != null) {
                    //if length = 6 --> mean we have a correct format
                    if (nextLine.length == 6) {
                        //increment the number of course from the correct day
                        numberLessonPerDay[staticDays.get(nextLine[0])] += 1;

                        //check if the array list is long enough for the new lesson
                        while (mLessonLines.size() < numberLessonPerDay[staticDays.get(nextLine[0])]) {
                            mLessonLines.add(new LessonLine());
                        }
                        //Create the lesson object
                        Lesson lesson = new Lesson(nextLine[0], nextLine[3], nextLine[4], nextLine[1], nextLine[5], nextLine[2]);

                        //store it in the correct place in the array
                        mLessonLines.get(numberLessonPerDay[staticDays.get(nextLine[0])] - 1).lessons[staticDays.get(nextLine[0])] = lesson;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mLessonLines;
    }


    public boolean addLesson(Context context, String startHour, String endHour, String lesson, String room, String city, String day)
    {
        Pattern p = Pattern.compile(".*([01]?[0-9]|2[0-3]):[0-5][0-9].*");
        Matcher mStart = p.matcher(startHour);
        Matcher mEnd = p.matcher(endHour);
        if(mStart.matches() && mEnd.matches()) {
            if(lesson.compareTo("")==0)
                lesson="-";
            if(room.compareTo("")==0)
                room="-";
            if(city.compareTo("")==0)
                city="-";
            String[] csvLine = new String[6];
            csvLine[0]=day;
            csvLine[1]=lesson;
            csvLine[2]=room;
            csvLine[3]=startHour;
            csvLine[4]=endHour;
            csvLine[5]=city;
            //String csvLine= String.format("%s,%s,%s,%s,%s,%s",day,lesson,room,startHour,endHour,city);

            FileUriExtension  fileUriExtension = getCsvFile(context);

            try {

                int lineToAppend = 0;
                boolean found = false;
                CSVReader totoR = new CSVReader(new FileReader(fileUriExtension.URI));
                List<String[]> lines = totoR.readAll();
                totoR.close();

                if(lines.size()==0) {
                    lineToAppend = 0;
                    found = true;
                }
                else
                {
                    for(int i=0; i<lines.size(); i++)
                    {
                        lineToAppend = i;
                        if(staticDays.get(lines.get(i)[0]) > staticDays.get(day)) {
                            found = true;
                            break;
                        }
                        else if(staticDays.get(lines.get(i)[0]) == staticDays.get(day))
                        {
                            Calendar newLessonCalendar = Calendar.getInstance();
                            newLessonCalendar.set(Calendar.HOUR_OF_DAY,Integer.valueOf(startHour.split(":")[0]));
                            newLessonCalendar.set(Calendar.MINUTE, Integer.valueOf(startHour.split(":")[1]));
                            newLessonCalendar.set(Calendar.SECOND,0);

                            Calendar lessonHours = Calendar.getInstance();
                            String parts[] = lines.get(i)[3].split(":");
                            lessonHours.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                            lessonHours.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
                            lessonHours.set(Calendar.SECOND, 0);

                            //test if the lesson is before or after the actual time
                            // if before --> mean we found the next lesson
                            if (newLessonCalendar.before(lessonHours)) {

                                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                                lineToAppend = lineToAppend;
                                found = true;
                                break;
                            }

                        }
                    }
                }

                //if lesson wasn't found --> add 1 to append at the end of the file
                if(found == false)
                    lineToAppend = lineToAppend+1;

                lines.add(lineToAppend,csvLine);
                //lines.add(lineToAppend,csvLine.split(","));
                File outFile = new File(fileUriExtension.URI);

                // delete out file if it exists
                if (outFile.exists()) {
                    outFile.delete();
                }

                CSVWriter totoW = new CSVWriter(new FileWriter(fileUriExtension.URI, false));
                totoW.writeAll(lines);
                totoW.close();
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            setChanged();
            notifyObservers();

            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isLessonAddable(Context context, String startHour, String endHour)
    {
        Pattern p = Pattern.compile(".*([01]?[0-9]|2[0-3]):[0-5][0-9].*");
        Matcher mStart = p.matcher(startHour);
        Matcher mEnd = p.matcher(endHour);
        FileUriExtension  fileUriExtension = getCsvFile(context);

        if(fileUriExtension.Extension.compareTo("csv") == 0) {
            try {
                CSVReader totoR = new CSVReader(new FileReader(fileUriExtension.URI));
                totoR.close();
            } catch (Exception e) {
                //cannot open file
                return false;
            }
        }
        else
        {
            //file not csv
            return false;
        }

        if(mStart.matches() && mEnd.matches())
        {
            return true;
        }
        else {
            //hours wrong format
            return false;
        }
    }

    public boolean deleteLine(Context context, String day, int lessonLine)
    {
        FileUriExtension  fileUriExtension = getCsvFile(context);

        try {
            CSVReader totoR = new CSVReader(new FileReader(fileUriExtension.URI));
            List<String[]> lines = totoR.readAll();
            totoR.close();

            int lessonOnDay = 0;
            int i = 0;
            int lineToDelete = -1;
            for(String[] line : lines)
            {
                if(line[0].compareTo(day)==0)
                {
                    if(lessonOnDay == lessonLine)
                    {
                        lineToDelete = i;
                        break;
                    }
                    lessonOnDay = lessonOnDay + 1;
                }
                i++;
            }

            if(lineToDelete != -1)
                lines.remove(lineToDelete);
            else {
                return false;
            }

            File outFile = new File(fileUriExtension.URI);

            // delete out file if it exists
            if (outFile.exists()) {
                outFile.delete();
            }

            CSVWriter totoW = new CSVWriter(new FileWriter(fileUriExtension.URI, false));
            totoW.writeAll(lines);
            totoW.close();

            setChanged();
            notifyObservers();

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public String[] nextLessonFromCsv(Context context)
    {
        String [] lessonData = null;

        FileUriExtension  fileUriExtension = getCsvFile(context);

        //if extension is correct --> parse it
        if(fileUriExtension.Extension.compareTo("csv") == 0)
        {
            try
            {
                CSVReader reader = new CSVReader(new FileReader(fileUriExtension.URI));

                String [] nextLine;
                boolean lessonFound = false;

                //create a calender to get the the day of the week and actual time
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String dayOfWeek = new SimpleDateFormat("EE", Locale.ENGLISH).format(date);

                int hours = calendar.get(Calendar.HOUR_OF_DAY);
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
                reader.close();
                Date dt = new Date();

                int incrNumber = 0;

                //if lesson still not found -> mean it's maybe next day
                while (!lessonFound)
                {
                    Calendar c = Calendar.getInstance();
                    c.setTime(dt);
                    //increment one day
                    c.add(Calendar.DATE, 1);
                    dt = c.getTime();
                    dayOfWeek = new SimpleDateFormat("EE", Locale.ENGLISH).format(dt);

                    //if we incremented more than 7 time --> we are again checking 'tomorrow'
                    if(incrNumber > 7)
                    {
                        lessonFound = true;
                    }
                    else {
                        //parse in the file the next lesson
                        reader = new CSVReader(new FileReader(fileUriExtension.URI));
                        while ((nextLine = reader.readNext()) != null) {
                            if (nextLine[0].compareTo(dayOfWeek) == 0) {
                                lessonFound = true;
                                lessonData = nextLine;
                                break;
                            }
                        }
                        reader.close();
                    }
                    incrNumber = incrNumber+1;
                }

                if(lessonData!=null)
                {
                    //return correct data --> will fulfill the widget with correct data
                    return lessonData;
                }
                else
                {
                    //didn't find any lesson --> fulfill with warning message
                    return null;
                }
            }
            catch (Exception e)
            {
                //if any error in the file --> fulfill with warning message
                e.printStackTrace();
                return null;

            }
        }
        else
        {
            //if file not csv --> fulfill with warning message
            return null;
        }
    }

    public FileUriExtension getCsvFile(Context context)
    {
        FileUriExtension prefFile = new FileUriExtension();

        //get the csv file from preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        prefFile.URI  = preferences.getString("filePicker", "NA");

        if(prefFile.URI=="NA")
        {
            File folder = new File(Environment.getExternalStorageDirectory()+"/data");
            if(!folder.exists())
                folder.mkdir();

            File file = new File(Environment.getExternalStorageDirectory()+"/data/myFirstPlanning.csv");
            try {
                if (!file.exists())
                    file.createNewFile();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            prefFile.URI = file.getAbsolutePath();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("filePicker", prefFile.URI);
            editor.apply();
        }



        String[] parsedUri = prefFile.URI.split("\\.");
        //get the extension
        prefFile.Extension = parsedUri[parsedUri.length - 1];

        return prefFile;
    }


}
