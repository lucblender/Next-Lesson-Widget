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

public class AboutDialog extends Dialog implements View.OnClickListener {


    public AboutDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aboutdialog);

        TextView textViewLucasBonvin = findViewById(R.id.textViewLucasBonvin);
        TextView textViewLucblender = findViewById(R.id.textViewLucblender);
        TextView textViewVersion = findViewById(R.id.about_version);
        textViewVersion.setText(BuildConfig.VERSION_NAME);

        findViewById(R.id.lb_logo).setOnClickListener(this);
        textViewLucasBonvin.setOnClickListener(this);
        textViewLucblender.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.textViewLucasBonvin:
            case R.id.lb_logo:
                Intent browserLucasIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.lucas-bonvin.com"));
                getContext().startActivity(browserLucasIntent);
                break;

            case R.id.textViewLucblender:
                Intent browserGithubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/lucblender"));
                getContext().startActivity(browserGithubIntent);
                break;

            default:
                break;
        }
    }
}

