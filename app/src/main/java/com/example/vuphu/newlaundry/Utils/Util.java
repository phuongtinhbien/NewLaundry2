package com.example.vuphu.newlaundry.Utils;

import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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



}
