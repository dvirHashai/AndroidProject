package com.cambio.finalprojectandroid.widget;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TimePicker;

import com.cambio.finalprojectandroid.utils.Time;

/**
 * Created by Chen on 18/05/2017.
 */

interface MyOnTimeSetListener {
    void onTimeSet(Time time);
}

public class MyTimePicker extends EditText implements MyOnTimeSetListener {
    Time time;


    public MyTimePicker(Context context) {
        super(context);
        this.time = new Time(1, 2);
        setInputType(0);
    }

    public MyTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.time = new Time(1, 2);
        setInputType(0);
    }

    public MyTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.time = new Time(1, 2);
        setInputType(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d("TAG", "event.getAction() == MotionEvent.ACTION_DOWN");
            MyTimePickerDialog tpd = MyTimePickerDialog.newInstance(getId());
            tpd.listener = this;
            tpd.show(((Activity) getContext()).getFragmentManager(), "TAG");
            return true;
        }
        return true;
    }

    @Override
    public void onTimeSet(Time time) {
        setText(time.toString());
        this.time = new Time(1, 2);
        this.time.setHour(time.getHour());
        this.time.setMinute(time.getMinute());

    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public static class MyTimePickerDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        private static final String ARG_CONTAINER_EDIT_TEXT_VIEW = "edit_text_container";
        MyOnTimeSetListener listener;

        public static MyTimePickerDialog newInstance(int tag) {
            MyTimePickerDialog timePickerDialog = new MyTimePickerDialog();
            Bundle args = new Bundle();
            args.putInt(ARG_CONTAINER_EDIT_TEXT_VIEW, tag);
            timePickerDialog.setArguments(args);
            return timePickerDialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);
            Dialog timePicker = new TimePickerDialog(getActivity(), this, 22, 44, false);

            if (getArguments() != null) {
                int tag = getArguments().getInt(ARG_CONTAINER_EDIT_TEXT_VIEW);
                listener = (MyOnTimeSetListener) getActivity().findViewById(tag);
            }

            return timePicker;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Log.d("TAG", "onTimeSet " + hourOfDay + ":" + minute);
            listener.onTimeSet(new Time(hourOfDay, minute));
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.d("TAG", "dialog destroyed");
        }
    }
}
