package com.example.lucasbonvin.widgettest;

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
@AcraDialog(resText = R.string.resText,resTitle = R.string.resTitle)

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
