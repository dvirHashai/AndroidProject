package com.cambio.finalprojectandroid;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cambio.finalprojectandroid.model.Event;
import com.cambio.finalprojectandroid.model.Model;
import com.cambio.finalprojectandroid.model.ModelFirebase;
import com.cambio.finalprojectandroid.utils.Date;
import com.cambio.finalprojectandroid.utils.Time;
import com.cambio.finalprojectandroid.widget.MyDatePicker;
import com.cambio.finalprojectandroid.widget.MyTimePicker;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;


public class EventAddFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    ImageView imageView;
    Bitmap imageBitmap;
    ProgressBar progressBar;

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
        setHasOptionsMenu(true);
        getActivity().setTitle("Add New Event");
        // Inflate the layout for this fragment
        View contextView = inflater.inflate(R.layout.fragment_event_add, container, false);
        final EditText name = (EditText) contextView.findViewById(R.id.event_add_name);
        final EditText price = (EditText) contextView.findViewById(R.id.event_add_price);
        final EditText location = (EditText) contextView.findViewById(R.id.event_add_location);
        final MyDatePicker datePicker = (MyDatePicker) contextView.findViewById(R.id.event_add_date);
        final MyTimePicker timePicker = (MyTimePicker) contextView.findViewById(R.id.event_add_time);

        progressBar = (ProgressBar) contextView.findViewById(R.id.mainProgressBar);
        progressBar.setVisibility(GONE);
        Button saveBtn = (Button) contextView.findViewById(R.id.event_add_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (datePicker.getDate() != null) {
                    if (timePicker.getTime() != null) {
                        Date date = new Date(datePicker.getDate());
                        Time time = new Time(timePicker.getTime());
                        final Event event = new Event(Model.instace.getModelFirebase().getFirebaseEntityId(), name.getText().toString(), date, time, price.getText().toString(), location.getText().toString(), "", "");
                        if (imageBitmap != null) {
                            Model.instace.saveImage(imageBitmap, event.getId() + "jpeg", new Model.SaveImageListener() {
                                @Override
                                public void complete(String url) {
                                    event.setImageUrl(url);
                                    Model.instace.addEvent(event);
                                    progressBar.setVisibility(GONE);
                                    getActivity().invalidateOptionsMenu();
                                    mListener.onAddEventInteraction();
                                }

                                @Override
                                public void fail() {
                                    progressBar.setVisibility(GONE);
                                    getActivity().invalidateOptionsMenu();
                                    mListener.onAddEventInteraction();
                                }
                            });
                        } else {
                            Model.instace.addEvent(event);
                            progressBar.setVisibility(GONE);
                            getActivity().invalidateOptionsMenu();
                            mListener.onAddEventInteraction();
                        }
                    }
                }
                getActivity().invalidateOptionsMenu();
                mListener.onAddEventInteraction();
            }
        });

        Button cancelBtn = (Button) contextView.findViewById(R.id.event_add_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().invalidateOptionsMenu();
                mListener.onAddEventInteraction();
            }
        });
        imageView = (ImageView) contextView.findViewById(R.id.event_add_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        getActivity().invalidateOptionsMenu();
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

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }
}
