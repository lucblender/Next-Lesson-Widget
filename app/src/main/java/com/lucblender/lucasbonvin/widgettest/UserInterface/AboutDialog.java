package com.lucblender.lucasbonvin.widgettest.UserInterface;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.lucblender.lucasbonvin.widgettest.BuildConfig;
import com.lucblender.lucasbonvin.widgettest.R;

public class AboutDialog extends Dialog {


    private TextView textViewLucasBonvin;
    private TextView textViewLucblender;
    private TextView textViewVersion;

    public AboutDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aboutdialog);

        textViewLucasBonvin = findViewById(R.id.textViewLucasBonvin);
        textViewLucblender = findViewById(R.id.textViewLucblender);
        textViewVersion = findViewById(R.id.about_version);
        textViewVersion.setText(BuildConfig.VERSION_NAME);

        textViewLucasBonvin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.lucas-bonvin.com"));
                getContext().startActivity(browserIntent);
            }
        });

        findViewById(R.id.lb_logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.lucas-bonvin.com"));
                getContext().startActivity(browserIntent);
            }
        });

        textViewLucblender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/lucblender"));
                getContext().startActivity(browserIntent);
            }
        });


    }

}

