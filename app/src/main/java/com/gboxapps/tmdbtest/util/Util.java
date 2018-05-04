package com.gboxapps.tmdbtest.util;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit.mime.TypedInput;

public class Util {

    public static String getIsoCode(Activity activity){
        return Locale.getDefault().getLanguage();
    }

    /**
     * Obtiene el string de la respuesta del servicio
     *
     * @param input
     * @return
     */
    public static String getString(TypedInput input) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = input.in();
            InputStreamReader inputStreamReader = new InputStreamReader((InputStream)is, "UTF-8");
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            return null;
        }
        return sb.toString();

    }

    public static String getYearFromDate(String date){

        DecimalFormat mFormat= new DecimalFormat("00");
        mFormat.setRoundingMode(RoundingMode.DOWN);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date dateFrom = null;
        try {
            dateFrom = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cFrom = Calendar.getInstance();
        cFrom.setTime(dateFrom);

        return mFormat.format(cFrom.get(Calendar.YEAR));
    }

}
