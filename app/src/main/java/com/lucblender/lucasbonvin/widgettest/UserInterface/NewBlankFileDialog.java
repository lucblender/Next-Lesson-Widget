package com.lucblender.lucasbonvin.widgettest.UserInterface;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lucblender.lucasbonvin.widgettest.Data.DataCsvManager;
import com.lucblender.lucasbonvin.widgettest.R;

public class NewBlankFileDialog  extends Dialog implements View.OnClickListener{

    private static final String TAG = AddLessonDialog.class.getName();

    private EditText editTextFileName;


    public NewBlankFileDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.newblankfiledialog);

        editTextFileName = findViewById(R.id.editTextNewFileName);

        Button buttonNewFileCreate = findViewById(R.id.buttonNewFileCreate);
        Button buttonNewFileCancel = findViewById(R.id.buttonNewFileCancel);

        buttonNewFileCreate.setOnClickListener(this);
        buttonNewFileCancel.setOnClickListener(this);
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
            case R.id.buttonNewFileCreate:

                String fileName = editTextFileName.getText().toString();
                if(fileName.equals("")){
                    message(getContext().getString(R.string.invalid_file_name));
                }
                else {
                    String isExtension = fileName.substring(fileName.length() - 4, fileName.length());

                    if(isExtension.equalsIgnoreCase(".csv"))
                    {
                        //end with '.csv' but has some
                        fileName = fileName.substring(0, fileName.length() - 4);
                    }

                    fileName = fileName.replaceAll("[^a-zA-Z0-9_\\-]", "_");

                    //127 char for filename --> security --> 124 + .csv
                    if(fileName.length() > 124)
                        fileName = fileName.substring(0, 124);

                    fileName = fileName + ".csv";

                    if(!DataCsvManager.getInstance().createFile(getContext(), fileName))
                        message(getContext().getString(R.string.error_create_file)+fileName);
                    else {
                        message(getContext().getString(R.string.new_planning_created));
                        this.dismiss();
                    }
                }

                break;
            case R.id.buttonNewFileCancel:
                this.dismiss();
                break;
        }

    }
}