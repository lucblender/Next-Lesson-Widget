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

package com.lucblender.lucasbonvin.widgettest;

import android.app.Application;
import android.content.Context;

import org.acra.ACRA;
import org.acra.annotation.AcraCore;
import org.acra.annotation.AcraDialog;
import org.acra.annotation.AcraMailSender;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.DialogConfigurationBuilder;
import org.acra.data.StringFormat;


@AcraCore(buildConfigClass = BuildConfig.class)

//Configured like so: E-mail from dialog box
@AcraMailSender(mailTo = "lucasbonvin@hotmail.com")
@AcraDialog(resText = com.lucblender.lucasbonvin.widgettest.R.string.resText,resTitle = com.lucblender.lucasbonvin.widgettest.R.string.resTitle)

public class MyApplication extends Application {

    //application only used for bug reporting with ACRA

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this);

        builder.getPluginConfigurationBuilder(DialogConfigurationBuilder.class).setEnabled(true);

        builder.setBuildConfigClass(BuildConfig.class).setReportFormat(StringFormat.JSON);
        ACRA.init(this, builder);

    }
}
