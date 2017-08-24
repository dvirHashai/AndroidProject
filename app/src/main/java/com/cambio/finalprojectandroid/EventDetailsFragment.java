package com.cambio.finalprojectandroid;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cambio.finalprojectandroid.model.Event;
import com.cambio.finalprojectandroid.model.Model;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String EVENT_ID = "eventId";


    // TODO: Rename and change types of parameters
    private String eventId;


    private OnFragmentInteractionListener mListener;

    public EventDetailsFragment() {
        // Required empty public constructor
    }


    public static EventDetailsFragment newInstance(String eventId) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putString(EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        setHasOptionsMenu(true);
        getActivity().setTitle("Event Details");
        View contentView = inflater.inflate(R.layout.fragment_event_details, container, false);
        final ImageView eventImage = (ImageView) contentView.findViewById(R.id.event_details_image);
        final  TextView eventName = (TextView) contentView.findViewById(R.id.event_details_name);
        final TextView eventDate = (TextView) contentView.findViewById(R.id.event_details_date);
        final TextView eventTime = (TextView) contentView.findViewById(R.id.event_details_time);
        final TextView eventLocation = (TextView) contentView.findViewById(R.id.event_details_location);
        final TextView eventPrice = (TextView) contentView.findViewById(R.id.event_details_price);


        final Event event = Model.instace.getEvent(eventId);
        //TODO set image in top details
        eventName.setText(event.getName());
        eventDate.setText(event.getDate().toString());
        eventTime.setText(event.getTime().toString());
        eventLocation.setText(event.getLocation());
        eventPrice.setText(event.getPrice());
        //TODO interface method to transfer the id of the student to the edit fragment by option menu in the main activity
        Model.instace.setEventId(eventId);
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TAG","On Destroy details fragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("TAG","On Destroy View details fragment");
    }

    public interface OnFragmentInteractionListener {

    }
}
