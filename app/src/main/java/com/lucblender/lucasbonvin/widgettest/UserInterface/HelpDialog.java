package com.lucblender.lucasbonvin.widgettest.UserInterface;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import com.lucblender.lucasbonvin.widgettest.R;

public class HelpDialog extends Dialog {

    public HelpDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.helpdialog);
    }

}

