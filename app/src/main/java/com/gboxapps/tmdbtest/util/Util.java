package com.gboxapps.tmdbtest.util;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

public class Util {

    public static String getIsoCode(Activity activity){
        TelephonyManager tm = (TelephonyManager)activity.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimCountryIso();
    }

}
