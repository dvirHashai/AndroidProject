package com.cambio.finalprojectandroid.widget;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by Chen on 17/05/2017.
 */

interface MyOnDateSetListener {
    void onDateSet(int year, int month, int dayOfMonth);
}


public class MyDatePicker extends EditText implements MyOnDateSetListener {
    int year = 1991;
    int month = 11;
    int dayOfMonth = 2;

    public MyDatePicker(Context context) {
        super(context);
        setInputType(0);
    }

    public MyDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        setInputType(0);
    }

    public MyDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setInputType(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d("TAG", "event.getAction() == MotionEvent.ACTION_DOWN");
            MyDatePickerDialog tpd = MyDatePickerDialog.newInstance(getId());
            tpd.listener = this;
            tpd.show(((Activity) getContext()).getFragmentManager(), "TAG");
            return true;
        }
        return true;
    }

    @Override
    public void onDateSet(int year, int month, int dayOfMonth) {
        setText(year + " " + month + " " + dayOfMonth);
        this.year = year;
        this.month = month +1;
        this.dayOfMonth = dayOfMonth;
    }


    public static class MyDatePickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private static final String ARG_CONTAINER_EDIT_TEXT_VIEW = "edit_text_container";
        MyOnDateSetListener listener;

        public static MyDatePickerDialog newInstance(int tag) {
            MyDatePickerDialog timePickerDialog = new MyDatePickerDialog();
            Bundle args = new Bundle();
            args.putInt(ARG_CONTAINER_EDIT_TEXT_VIEW, tag);
            timePickerDialog.setArguments(args);
            return timePickerDialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            if (getArguments() != null) {
                int tag = getArguments().getInt(ARG_CONTAINER_EDIT_TEXT_VIEW);
                listener = (MyOnDateSetListener) getActivity().findViewById(tag);
            }

            return new DatePickerDialog(getActivity(), this, year, month, day);

        }


        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.d("TAG", "dialog destroyed");
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Log.d("TAG", "onDateSet " + year + " " + month + " " + dayOfMonth);
            listener.onDateSet(year, month +1, dayOfMonth);
        }
    }

}
