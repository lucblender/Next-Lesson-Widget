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

