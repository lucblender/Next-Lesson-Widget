package com.example.lucasbonvin.widgettest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditorFragment extends Fragment {

    private static final String TAG = EditorFragment.class.getName();

    //array of lessons line that gonna be displayed by the recycler view
    private ArrayList<LessonLine> mLessonLines = new ArrayList<>();

    private RecyclerView rv;
    private LessonAdapter lessonAdapter;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //get the view from layout
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.editorlayout, container, false);

        //init recyclerview and fill the lessonlines ArrayList with createLessonLineFromCSV
        rv = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        createLessonLineFromCSV();
        //create adapter for recycler view and link them
        lessonAdapter = new LessonAdapter(mLessonLines);
        rv.setAdapter(lessonAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //when fragment is visible (cause of slider activity), update the lessonlines ArrayList
        if(isVisibleToUser)
        {
            createLessonLineFromCSV();
            lessonAdapter.notifyDataSetChanged();
        }
    }

    private void createLessonLineFromCSV()
    {
        //clear the lessonlines ArrayList
        mLessonLines.clear();

        //get the csv file from preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String fileURI =  preferences.getString("filePicker","NA");
        Log.e(TAG, "createLessonLineFromCSV: "+fileURI );
        String[] parsedUri = fileURI.split("\\.");
        //get the extension
        String extension = parsedUri[parsedUri.length-1];

        //array used to know how many lesson there is per day
        int[] numberLessonPerDay = new int[7];
        for(int i=0;i<7;i++) //fill with 0
            numberLessonPerDay[i]=0;

        //if file is csv, parse it
        if(extension.compareTo("csv") == 0) {
            try {
                CSVReader reader = new CSVReader(new FileReader(fileURI));
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
    }
}
