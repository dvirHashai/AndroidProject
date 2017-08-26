package com.cambio.finalprojectandroid;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cambio.finalprojectandroid.model.Event;
import com.cambio.finalprojectandroid.model.Model;
import com.cambio.finalprojectandroid.utils.Date;
import com.cambio.finalprojectandroid.utils.Time;
import com.cambio.finalprojectandroid.widget.MyDatePicker;
import com.cambio.finalprojectandroid.widget.MyTimePicker;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;


public class EventEditFragment extends Fragment {
     Event event1;
    ImageView imageView;
    Bitmap imageBitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
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
        setHasOptionsMenu(true);
        getActivity().setTitle("Edit Event");
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_event_edit, container, false);
        final ImageView eventImage = (ImageView) contentView.findViewById(R.id.event_edit_image);
        final EditText eventName = (EditText) contentView.findViewById(R.id.event_edit_name);
        final MyDatePicker eventDate = (MyDatePicker) contentView.findViewById(R.id.event_edit_date);
        final MyTimePicker eventTime = (MyTimePicker) contentView.findViewById(R.id.event_edit_time);
        final EditText eventLocation = (EditText) contentView.findViewById(R.id.event_edit_location);
        final EditText eventPrice = (EditText) contentView.findViewById(R.id.event_edit_price);
        final ProgressBar progressBar = (ProgressBar)contentView.findViewById(R.id.event_edit_progressBar);

         Model.instace.getEvent(eventId, new Model.GetEventCallback() {
             @Override
             public void onComplete(Event event) {
                 event1 = event;

                 eventImage.setTag(event1.getImageUrl());
                 eventImage.setImageResource(R.drawable.avatar);

                     progressBar.setVisibility(View.VISIBLE);
                     Model.instace.getImage(event1.getImageUrl(), new Model.GetImageListener() {
                         @Override
                         public void onSuccess(Bitmap image) {
                             String tagUrl = eventImage.getTag().toString();
                             if (tagUrl.equals(event1.getImageUrl())) {
                                 eventImage.setImageBitmap(image);
                                 imageBitmap = image;
                                 progressBar.setVisibility(View.GONE);

                             }
                         }

                         @Override
                         public void onFail() {
                             progressBar.setVisibility(View.GONE);
                         }
                     });


        eventName.setText(event.getName());
        eventDate.setText(event.getDate().toString());
        eventTime.setText(event.getTime().toString());
        eventLocation.setText(event.getLocation());
        eventPrice.setText(event.getPrice());
             }

             @Override
             public void onCancel() {

             }
         });

        Button saveBtn = (Button) contentView.findViewById(R.id.event_edit_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(eventDate.getDate() != null) {
                    if (eventTime.getTime() != null) {
                       /* Date date = new Date(eventDate.getDate());
                        Time time = new Time(eventTime.getTime());*/


                            final Event newEvent = new Event(event1.getId(),eventName.getText().toString(),eventDate.getDate(),eventTime.getTime(),eventPrice.getText().toString(),eventLocation.getText().toString(),eventImage.getTag().toString(),event1.getLastUpDateTime());
                            if (imageBitmap != null) {
                                Model.instace.saveImage(imageBitmap, newEvent.getId() + "jpeg", new Model.SaveImageListener() {
                                    @Override
                                    public void complete(String url) {
                                        Log.d("TAG","saveImage: url - " + url);
                                        newEvent.setImageUrl(url);
                                        Model.instace.addEvent(newEvent);
                                        progressBar.setVisibility(GONE);
                                    }

                                    @Override
                                    public void fail() {
                                        Log.d("TAG","saveImage: FAIL  " );
                                        progressBar.setVisibility(GONE);

                                    }
                                });
                            } else {
                                Model.instace.addEvent(newEvent);
                                progressBar.setVisibility(GONE);

                            }
                            getActivity().invalidateOptionsMenu();
                            mListener.onSaveEventInteraction();

                    } else {
                        Log.d("TAG", "Time is null");
                    }
                } else {
                    Log.d("TAG", "Date is null");
                }
            }
        });

        Button cancelBtn = (Button) contentView.findViewById(R.id.event_edit_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().invalidateOptionsMenu();
                mListener.onCancelEventInteraction();

            }
        });

         imageView = (ImageView) contentView.findViewById(R.id.event_edit_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
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
