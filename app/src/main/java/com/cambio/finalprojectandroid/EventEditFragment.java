package com.cambio.finalprojectandroid;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.cambio.finalprojectandroid.model.Event;
import com.cambio.finalprojectandroid.model.Model;
import com.cambio.finalprojectandroid.utils.Date;
import com.cambio.finalprojectandroid.utils.Time;
import com.cambio.finalprojectandroid.widget.MyDatePicker;
import com.cambio.finalprojectandroid.widget.MyTimePicker;


public class EventEditFragment extends Fragment {

    private static final String EVENT_ID = "eventId";

    private String eventId;

    private OnFragmentInteractionListener mListener;

    public EventEditFragment() {
        // Required empty public constructor
    }

    public static EventEditFragment newInstance(String eventId) {
        EventEditFragment fragment = new EventEditFragment();
        Bundle args = new Bundle();
        args.putString(EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getString(EVENT_ID);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Edit Event");
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_event_edit, container, false);
        final ImageView eventImage = (ImageView) contentView.findViewById(R.id.event_edit_image);
        final EditText eventName = (EditText) contentView.findViewById(R.id.event_edit_name);
        final MyDatePicker eventDate = (MyDatePicker) contentView.findViewById(R.id.event_edit_date);
        final MyTimePicker eventTime = (MyTimePicker) contentView.findViewById(R.id.event_edit_time);
        final EditText eventLocation = (EditText) contentView.findViewById(R.id.event_edit_location);
        final EditText eventPrice = (EditText) contentView.findViewById(R.id.event_edit_price);
        final Event event = Model.instace.getEvent(eventId);

        eventName.setText(event.getName());
        eventDate.setText(event.getDate().toString());
        eventTime.setText(event.getTime().toString());
        eventLocation.setText(event.getLocation());
        eventPrice.setText(event.getPrice());
        Button saveBtn = (Button) contentView.findViewById(R.id.event_add_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Date date = new Date(eventDate.getDate());
                Time time = new Time(eventTime.getTime());

                Event newEvent = new Event(event);
                Event oldEvent = Model.instace.getEvent(newEvent.getId());
                oldEvent.setName(newEvent.getName());
                oldEvent.setDate(newEvent.getDate());
                oldEvent.setTime(newEvent.getTime());
                oldEvent.setImageUrl(newEvent.getImageUrl());
                oldEvent.setLocation(newEvent.getLocation());
                oldEvent.setPrice(newEvent.getPrice());
                mListener.onSaveEventInteraction();
            }
        });

        Button cancelBtn = (Button) contentView.findViewById(R.id.event_edit_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelEventInteraction();

            }
        });

        getActivity().invalidateOptionsMenu();
        return contentView;
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
        void onSaveEventInteraction();

        void onDeleteEventInteraction();

        void onCancelEventInteraction();
    }
}
