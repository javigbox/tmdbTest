package com.gboxapps.tmdbtest.util;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

import java.io.InputStream;

import retrofit.mime.TypedInput;

public class Util {

    public static String getIsoCode(Activity activity){
        TelephonyManager tm = (TelephonyManager)activity.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimCountryIso();
    }

    /**
     * Obtiene el string de la respuesta del servicio
     *
     * @param input
     * @return
     */
    public static String getString(TypedInput input) {
        int ch;
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = input.in();
            while ((ch = is.read()) != -1) sb.append((char) ch);
        } catch (Exception e) {
            return null;
        }
        return sb.toString();
    }

}
