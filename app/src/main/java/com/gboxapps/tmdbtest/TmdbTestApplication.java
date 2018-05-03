package com.gboxapps.tmdbtest;

import android.app.Application;
import android.content.Context;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class TmdbTestApplication extends Application {

    public Context context;

    private static TmdbTestApplication mSharedInstance;

    public TmdbTestApplication() {
        mSharedInstance = this;
    }

    public static TmdbTestApplication getInstance() {
        return mSharedInstance;
    }

    public void onCreate() {
        super.onCreate();

        context = this.getApplicationContext();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/SourceSansPro-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }

}
