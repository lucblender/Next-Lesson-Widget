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

package com.lucblender.lucasbonvin.widgettest.UserInterface;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lucblender.lucasbonvin.widgettest.Data.DataCsvManager;
import com.lucblender.lucasbonvin.widgettest.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddLessonDialog extends Dialog implements View.OnClickListener{

    private static final String TAG = AddLessonDialog.class.getName();

    private EditText editTextLessonName;
    private EditText editTextCity;
    private EditText editTextEndHour;
    private EditText editTextStartHour;
    private EditText editTextRoom;
    private Spinner spinnerDay;

    private String dayModify;
    private String lessonName;
    private String startHour;
    private String endHour;
    private String city;
    private String room;
    private int lessonLineToDelete;

    private Map<String, String> mapDynLanguageToEnglish;
    private Map<String, String> mapDynEnglishToLanguage;


    enum modifyMode {
        MODIFY,
        ADD,
        DUPLICATE
    }

    private modifyMode mode;


    public AddLessonDialog(Context context) {
        super(context);
        mode = modifyMode.ADD;
    }

    public void initEditText(String dayModify, String lessonName, String startHour, String endHour, String city, String room, int lessonLineToDelete)
    {
        mode = modifyMode.MODIFY;
        this.dayModify = dayModify;
        this.lessonName = lessonName;
        this.startHour = startHour;
        this.endHour = endHour;
        this.city = city;
        this.room = room;
        this.lessonLineToDelete = lessonLineToDelete;
    }

    public void initEditText(String dayModify, String lessonName, String startHour, String endHour, String city, String room)
    {
        mode = modifyMode.DUPLICATE;
        this.dayModify = dayModify;
        this.lessonName = lessonName;
        this.startHour = startHour;
        this.endHour = endHour;
        this.city = city;
        this.room = room;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.addlessondialog);

        spinnerDay = findViewById(R.id.spinnerDay);
        editTextLessonName = findViewById(R.id.editTextLessonName);
        editTextCity = findViewById(R.id.editTextCity);
        editTextStartHour = findViewById(R.id.editTextStartHour);
        editTextEndHour = findViewById(R.id.editTextEndHour);
        editTextRoom = findViewById(R.id.editTextRoom);

        Button buttonAdd = findViewById(R.id.buttonAdd);
        Button buttonCancel = findViewById(R.id.buttonCancel);

        ArrayList<String> list = new ArrayList<>();
        list.add(getContext().getString(R.string.mon));
        list.add(getContext().getString(R.string.tue));
        list.add(getContext().getString(R.string.wed));
        list.add(getContext().getString(R.string.thu));
        list.add(getContext().getString(R.string.fri));
        list.add(getContext().getString(R.string.sat));
        list.add(getContext().getString(R.string.sun));

        mapDynLanguageToEnglish = new HashMap<>();
        mapDynLanguageToEnglish.put(getContext().getString(R.string.mon),"Mon");
        mapDynLanguageToEnglish.put(getContext().getString(R.string.tue),"Tue");
        mapDynLanguageToEnglish.put(getContext().getString(R.string.wed),"Wed");
        mapDynLanguageToEnglish.put(getContext().getString(R.string.thu),"Thu");
        mapDynLanguageToEnglish.put(getContext().getString(R.string.fri),"Fri");
        mapDynLanguageToEnglish.put(getContext().getString(R.string.sat),"Sat");
        mapDynLanguageToEnglish.put(getContext().getString(R.string.sun),"Sun");

        mapDynEnglishToLanguage = new HashMap<>();
        mapDynEnglishToLanguage.put("Mon",getContext().getString(R.string.mon));
        mapDynEnglishToLanguage.put("Tue",getContext().getString(R.string.tue));
        mapDynEnglishToLanguage.put("Wed",getContext().getString(R.string.wed));
        mapDynEnglishToLanguage.put("Thu",getContext().getString(R.string.thu));
        mapDynEnglishToLanguage.put("Fri",getContext().getString(R.string.fri));
        mapDynEnglishToLanguage.put("Sat",getContext().getString(R.string.sat));
        mapDynEnglishToLanguage.put("Sun",getContext().getString(R.string.sun));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),R.layout.spinner_item, list);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerDay.setAdapter(arrayAdapter);


        //time picker will open when clicked on edit text linked to time
        editTextStartHour.setOnClickListener(v -> {
            int themeId = R.style.MyDialogThemeShared;

            String timeEndStart[] = editTextStartHour.getText().toString().split(":");
            String timeEndEnd[] = editTextEndHour.getText().toString().split(":");

            int hourEnd = 8;
            int minEnd = 0;

            if(timeEndStart.length == 2)
            {
                hourEnd = Integer.valueOf(timeEndStart[0]);
                minEnd = Integer.valueOf(timeEndStart[1]);
            }else if(timeEndEnd.length == 2)
            {
                hourEnd = Integer.valueOf(timeEndEnd[0])-1;
                minEnd = Integer.valueOf(timeEndEnd[1]);
                if(hourEnd < 0) {
                    hourEnd = 0;
                    minEnd = 0;
                }

            }

            TimePickerDialog mTimePicker = new TimePickerDialog(getContext(),
                    themeId,
                    (timePicker, selectedHour, selectedMinute) -> editTextStartHour.setText(String.format(Locale.US, "%02d:%02d",selectedHour,selectedMinute)),
                    hourEnd,
                    minEnd,
                    true);//24 hour time
            mTimePicker.setTitle(getContext().getString(R.string.select_start_time));
            mTimePicker.show();
        });

        editTextEndHour.setOnClickListener(v -> {
            int themeId = R.style.MyDialogThemeShared;

            String timeEndStart[] = editTextStartHour.getText().toString().split(":");
            String timeEndEnd[] = editTextEndHour.getText().toString().split(":");

            int hourEnd = 8;
            int minEnd = 0;

            if(timeEndEnd.length == 2)
            {
                hourEnd = Integer.valueOf(timeEndEnd[0]);
                minEnd = Integer.valueOf(timeEndEnd[1]);
            }
            else if(timeEndStart.length == 2)
            {
                hourEnd = Integer.valueOf(timeEndStart[0])+1;
                minEnd = Integer.valueOf(timeEndStart[1]);
                if(hourEnd > 23 ) {
                    hourEnd = 23;
                    minEnd = 59;
                }
            }

            TimePickerDialog mTimePicker = new TimePickerDialog(getContext(),
                    themeId,
                    (timePicker, selectedHour, selectedMinute) -> editTextEndHour.setText(String.format(Locale.US, "%02d:%02d",selectedHour,selectedMinute)),
                    hourEnd,
                    minEnd,
                    true);//24 hour time
            mTimePicker.setTitle(getContext().getString(R.string.select_end_time));
            mTimePicker.show();
        });



        buttonAdd.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);

        if(mode == modifyMode.DUPLICATE)
        {
            ((TextView)findViewById(R.id.addLessonTitle)).setText(R.string.duplicate_lesson);
            buttonAdd.setText(R.string.add);
            spinnerDay.setSelection(arrayAdapter.getPosition(mapDynEnglishToLanguage.get(dayModify)));
            editTextLessonName.setText(lessonName);
            editTextCity.setText(city);
            editTextStartHour.setText(startHour);
            editTextEndHour.setText(endHour);
            editTextRoom.setText(room);
        }
        else if(mode == modifyMode.MODIFY)
        {
            ((TextView)findViewById(R.id.addLessonTitle)).setText(R.string.modify_title);
            buttonAdd.setText(R.string.modify_button);
            spinnerDay.setSelection(arrayAdapter.getPosition(mapDynEnglishToLanguage.get(dayModify)));
            editTextLessonName.setText(lessonName);
            editTextCity.setText(city);
            editTextStartHour.setText(startHour);
            editTextEndHour.setText(endHour);
            editTextRoom.setText(room);
        }
    }


    //show a toast message
    private void message(String s)
    {
        Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonAdd:
                String startHour = editTextStartHour.getText().toString();
                String endHour = editTextEndHour.getText().toString();
                String lesson = editTextLessonName.getText().toString();
                String room = editTextRoom.getText().toString();
                String city = editTextCity.getText().toString();
                String day = mapDynLanguageToEnglish.get(spinnerDay.getSelectedItem().toString());

                switch (mode)
                {
                    case ADD:
                        //if not in modify --> just try to add the lesson
                        if (DataCsvManager.getInstance().addLesson(getContext(), startHour, endHour, lesson, room, city, day)) {
                            this.dismiss();
                        } else {
                            message(getContext().getString(R.string.lesson_format_csv_error));
                        }
                        break;
                    case MODIFY:
                        //if in modify --> check if we can add lesson, then delete previous lesson to add the modified one
                        if(DataCsvManager.getInstance().isLessonAddable(getContext(), startHour, endHour)) {
                            DataCsvManager.getInstance().deleteLine(getContext(), dayModify, lessonLineToDelete);
                            DataCsvManager.getInstance().addLesson(getContext(), startHour, endHour, lesson, room, city, day);
                            this.dismiss();
                        }
                        else {
                            message(getContext().getString(R.string.lesson_format_csv_error));
                        }
                        break;
                    case DUPLICATE:
                        //if not in modify --> just try to add the lesson
                        if (DataCsvManager.getInstance().addLesson(getContext(), startHour, endHour, lesson, room, city, day)) {
                            this.dismiss();
                        } else {
                            message(getContext().getString(R.string.lesson_format_csv_error));
                        }
                        break;
                }
                break;
            case R.id.buttonCancel:
                this.dismiss();
                break;
        }
    }
}
