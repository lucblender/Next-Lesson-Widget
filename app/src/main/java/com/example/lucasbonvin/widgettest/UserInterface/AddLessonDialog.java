package com.example.lucasbonvin.widgettest.UserInterface;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lucasbonvin.widgettest.Data.DataCsvManager;
import com.example.lucasbonvin.widgettest.R;

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


    public AddLessonDialog(Activity activity) {
        super(activity);
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
                mTimePicker.setTitle("Select start lesson time");
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
                mTimePicker.setTitle("Select end lesson time");
                mTimePicker.show();
            }
        });

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);

        buttonAdd.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
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

                if(DataCsvManager.getInstance().addLesson(getContext(), startHour, endHour, lesson, room, city, day))
                {
                    this.dismiss();
                }
                else
                {
                    message("Lesson Format is not correct, check hours");
                }
                break;
            case R.id.buttonCancel:
                this.dismiss();
                break;
        }

    }
}
