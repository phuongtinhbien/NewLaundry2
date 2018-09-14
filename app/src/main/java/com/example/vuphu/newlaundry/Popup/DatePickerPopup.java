package com.example.vuphu.newlaundry.Popup;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.widget.EditText;
import com.example.vuphu.newlaundry.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerPopup {

    private Calendar myCalendar = Calendar.getInstance();
    private Activity activity;
    private EditText editText;

    public DatePickerPopup(Activity activity, EditText editText) {
        this.activity = activity;
        this.editText = editText;
    }

    public void show() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                returnValue();
            }

        };
        new DatePickerDialog(activity, R.style.datepicker, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void returnValue() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        editText.setText((sdf.format(myCalendar.getTime())));
    }


}
