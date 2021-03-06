package com.gboxapps.tmdbtest.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    /**
     * Gets current language code depending on the phone language.
     * @return
     */
    public static String getLanguageCode(){
        return Locale.getDefault().getLanguage();
    }

    /**
     * Gets current ISO code depending on the phone country.
     * @return
     */
    public static String getCountryCode(){
        return Locale.getDefault().getCountry();
    }

    /**
     * Gets the web service response string
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

    /**
     * Transforms a date into a year
     * @param date
     * @return
     */
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

    /**
     * Checks internet connection
     * @param activity
     * @return
     */
    public static boolean isConnectedInternet(Activity activity) {
        ConnectivityManager cm;
        cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
