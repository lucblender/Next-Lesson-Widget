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