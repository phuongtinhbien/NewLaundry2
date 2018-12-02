package com.example.vuphu.newlaundry.Utils;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.vuphu.newlaundry.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.vuphu.newlaundry.Utils.StringKey.PENDING;
import static com.example.vuphu.newlaundry.Utils.StringKey.PENDING_SERVING;

public class Util {


    private static Calendar calendar = Calendar.getInstance();
    public static void showHideCursor(final EditText edt){

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    edt.setFocusable(true);
                    edt.setCursorVisible(true);
                    edt.setError(null);
            }
        };
        edt.setOnClickListener(clickListener);
        if (edt.hasFocus()){
            edt.setCursorVisible(true);
        }
        else {
            edt.setCursorVisible(false);
        }
    }

    public static boolean isEmptyorNull(String s){
        if (s == "" || s == null)
            return true;
        return false;
    }

    public static String formatDate (String format, Date date){
        String returnValue = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        returnValue = simpleDateFormat.format(date);
        return returnValue;
    }

    public static String parseDate(String date, String formatold, String formatnew) {
        SimpleDateFormat simpleDateFormatOld = new SimpleDateFormat(formatold);
        SimpleDateFormat simpleDateFormatNew = new SimpleDateFormat(formatnew);
        String result = "";
        try {
            Date d = simpleDateFormatOld.parse(date);
            result = simpleDateFormatNew.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  result;
    }


    public static Date getDate (){
        return calendar.getTime();
    }

    public static int getDayOfMonth(){
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getToDay(){
        return calendar.DATE;
    }


    public static int getMonth(){
        return calendar.MONTH;
    }
    public static int getYear (){
        return calendar.YEAR;
    }

    public static boolean checkDuplicateClothes(String str1, String str2) {
        if(str1 != null && str2 != null) {
            if(str1.equals(str2)) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            if(str1 == null && str2 == null) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    public static String translateStatus(String status, Context context) {
        String result = status;
        result  = context.getResources().getString(context.getResources().getIdentifier(status, "string", context.getPackageName()));
        return result;

    }

    public static String formatDecimal(double d) {
        String result = "";
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###");
        result = decimalFormat.format(d);
        return result;
    }

}
