package com.cambio.finalprojectandroid;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cambio.finalprojectandroid.utils.Date;
import com.cambio.finalprojectandroid.widget.MyDatePicker;
import com.cambio.finalprojectandroid.widget.MyTimePicker;


public class EventAddFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public EventAddFragment() {
        // Required empty public constructor
    }


    public static EventAddFragment newInstance() {
        EventAddFragment fragment = new EventAddFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Add New Event");
        // Inflate the layout for this fragment
        View contextView = inflater.inflate(R.layout.fragment_event_add, container, false);
        final EditText name = (EditText) contextView.findViewById(R.id.event_add_name);
        final EditText price = (EditText) contextView.findViewById(R.id.event_add_price);
        final EditText location = (EditText) contextView.findViewById(R.id.event_add_location);
        final MyDatePicker datePicker = (MyDatePicker) contextView.findViewById(R.id.event_add_date);
        final MyTimePicker timePicker = (MyTimePicker) contextView.findViewById(R.id.event_add_time);

        Button saveBtn = (Button) contextView.findViewById(R.id.event_add_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date(datePicker.getDate().getYear(),datePicker.getDate().getMonth(),datePicker.getDate().getDayOfMonth());
                //Time time = new Time(timePicker)

               // Event event = new Event(null,name.getText().toString(),date,time,price.getText().toString(),location.getText().toString(),"","");
               // Model.instace.addEvent(event);
                mListener.onAddEventInteraction();
            }
        });

        Button cancelBtn = (Button) contextView.findViewById(R.id.event_add_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddEventInteraction();
            }
        });

//TODO getActivity().invalidateOptionsMenu();
        return contextView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onAddEventInteraction();
    }
}
