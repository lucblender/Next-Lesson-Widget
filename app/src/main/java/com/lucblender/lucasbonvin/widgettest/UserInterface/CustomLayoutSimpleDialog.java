package com.lucblender.lucasbonvin.widgettest.UserInterface;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;


public class CustomLayoutSimpleDialog extends Dialog {

    private int layoutID;

    public CustomLayoutSimpleDialog(Context context, int layoutID) {
        super(context);
        this.layoutID = layoutID;
    }
    public CustomLayoutSimpleDialog(Activity activity, int layoutID) {
        super(activity);
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(layoutID);
    }
}