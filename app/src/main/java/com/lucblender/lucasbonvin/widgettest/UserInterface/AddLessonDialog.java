package com.lucblender.lucasbonvin.widgettest.UserInterface;

import android.app.Activity;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.lucblender.lucasbonvin.widgettest.Data.DataCsvManager;
import com.lucblender.lucasbonvin.widgettest.R;

import java.util.ArrayList;

public class AddLessonDialog extends Dialog implements View.OnClickListener{

    private static final String TAG = AddLessonDialog.class.getName();

    private EditText editTextLessonName;
    private EditText editTextCity;
    private EditText editTextEndHour;
    private EditText editTextStartHour;
    private EditText editTextRoom;
    private Spinner spinnerDay;

    private Button buttonAdd;
    private Button buttonCancel;

    private String dayModify;
    private String lessonName;
    private String startHour;
    private String endHour;
    private String city;
    private String room;
    private int lessonLineToDelete;
    private boolean modifyMode;


    public AddLessonDialog(Activity activity) {
        super(activity);
        modifyMode = false;
    }

    public AddLessonDialog(Context context) {
        super(context);
        modifyMode = false;
    }

    public void initEditText(String dayModify, String lessonName, String startHour, String endHour, String city, String room, int lessonLineToDelete)
    {
        modifyMode = true;
        this.dayModify = dayModify;
        this.lessonName = lessonName;
        this.startHour = startHour;
        this.endHour = endHour;
        this.city = city;
        this.room = room;
        this.lessonLineToDelete = lessonLineToDelete;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.addlessondialog);
        ArrayList<String> list = new ArrayList<String>();
        list.add("Mon");
        list.add("Tue");
        list.add("Wed");
        list.add("Thu");
        list.add("Fri");
        list.add("Sat");
        list.add("Sun");

        spinnerDay = (Spinner) findViewById(R.id.spinnerDay);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),R.layout.spinner_item, list);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerDay.setAdapter(arrayAdapter);

        editTextLessonName = (EditText) findViewById(R.id.editTextLessonName);
        editTextCity = (EditText) findViewById(R.id.editTextCity);
        editTextStartHour = (EditText) findViewById(R.id.editTextStartHour);
        editTextEndHour = (EditText) findViewById(R.id.editTextEndHour);
        editTextRoom = (EditText) findViewById(R.id.editTextRoom);

        //time picker will open when clicked on edit text linked to time
        editTextStartHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker = new TimePickerDialog(getContext(),R.style.Theme_AppCompat, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editTextStartHour.setText(String.format("%02d:%02d",selectedHour,selectedMinute));
                    }
                }, 8, 0, true);//24 hour time
                mTimePicker.setTitle(getContext().getString(R.string.select_start_time));
                mTimePicker.show();
            }
        });

        editTextEndHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker = new TimePickerDialog(getContext(),R.style.Theme_AppCompat, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editTextEndHour.setText(String.format("%02d:%02d",selectedHour,selectedMinute));
                    }
                }, 8, 0, true);//24 hour time
                mTimePicker.setTitle(getContext().getString(R.string.select_end_time));
                mTimePicker.show();
            }
        });

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);

        buttonAdd.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);

        if(modifyMode)
        {
            ((TextView)findViewById(R.id.addLessonTitle)).setText(R.string.modify_title);
            buttonAdd.setText(R.string.modify_button);
            spinnerDay.setSelection(arrayAdapter.getPosition(dayModify));
            editTextLessonName.setText(lessonName);
            editTextCity.setText(city);
            editTextStartHour.setText(startHour);
            editTextEndHour.setText(endHour);
            editTextRoom.setText(room);
        }
    }


    //show a toast message
    void message(String s)
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
                String day = spinnerDay.getSelectedItem().toString();

                if(modifyMode)
                {
                    //if in modify --> check if we can add lesson, then delete previous lesson to add the modified one
                    if(DataCsvManager.getInstance().isLessonAddable(getContext(), startHour, endHour)) {
                        DataCsvManager.getInstance().deleteLine(getContext(), dayModify, lessonLineToDelete);
                        DataCsvManager.getInstance().addLesson(getContext(), startHour, endHour, lesson, room, city, day);
                        this.dismiss();
                    }
                    else {
                        message(getContext().getString(R.string.lesson_format_csv_error));
                    }
                }
                else {
                    //if not in modify --> just try to add the lesson
                    if (DataCsvManager.getInstance().addLesson(getContext(), startHour, endHour, lesson, room, city, day)) {
                        this.dismiss();
                    } else {
                        message(getContext().getString(R.string.lesson_format_csv_error));
                    }
                }
                break;
            case R.id.buttonCancel:
                this.dismiss();
                break;
        }

    }
}
