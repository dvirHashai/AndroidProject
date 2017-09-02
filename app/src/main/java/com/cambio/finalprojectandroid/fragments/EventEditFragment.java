package com.cambio.finalprojectandroid.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cambio.finalprojectandroid.R;
import com.cambio.finalprojectandroid.model.CallBackInterface;
import com.cambio.finalprojectandroid.model.Event;
import com.cambio.finalprojectandroid.model.Model;
import com.cambio.finalprojectandroid.widget.MyDatePicker;
import com.cambio.finalprojectandroid.widget.MyTimePicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static com.cambio.finalprojectandroid.fragments.EventAddFragment.IMAGE_GALLERY_REQUEST;


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
        final ProgressBar progressBar = (ProgressBar) contentView.findViewById(R.id.event_edit_progressBar);

        Model.instance.getEvent(eventId, new CallBackInterface.GetEventCallback() {
            @Override
            public void onComplete(Event event) {
                event1 = event;

                eventImage.setTag(event1.getImageUrl());
                eventImage.setImageResource(R.drawable.avatar);

                progressBar.setVisibility(View.VISIBLE);
                if (!event1.getImageUrl().equals("")) {
                    Model.instance.getImage(event1.getImageUrl(), new CallBackInterface.GetImageListener() {
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
                } else {
                    progressBar.setVisibility(View.GONE);
                }


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
        saveBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                try {
                    Integer.parseInt(eventPrice.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Invalid Price Format", Toast.LENGTH_SHORT).show();
                }
                if (eventDate.getDate() != null) {
                    if (eventTime.getTime() != null) {
                        final Event newEvent = new Event(event1.getId(), eventName.getText().toString(), eventDate.getDate(), eventTime.getTime(), eventPrice.getText().toString(), eventLocation.getText().toString(), eventImage.getTag().toString(), event1.getLastUpDateTime());
                        if (imageBitmap != null) {
                            Model.instance.saveImage(imageBitmap, newEvent.getId() + "jpeg", new CallBackInterface.SaveImageListener() {
                                @Override
                                public void complete(String url) {
                                    Log.d("TAG", "saveImage: url - " + url);
                                    newEvent.setImageUrl(url);
                                    Model.instance.addEvent(newEvent);
                                    progressBar.setVisibility(GONE);
                                    mListener.onSaveEventInteraction();
                                }

                                @Override
                                public void fail() {
                                    Log.d("TAG", "saveImage: FAIL  ");
                                    progressBar.setVisibility(GONE);

                                }
                            });
                        } else {
                            Model.instance.addEvent(newEvent);
                            progressBar.setVisibility(GONE);

                        }
                        getActivity().invalidateOptionsMenu();


                    } else {
                        Log.d("TAG", "Time is null");
                    }
                } else {
                    Log.d("TAG", "Date is null");
                }
            }
        });

        Button cancelBtn = (Button) contentView.findViewById(R.id.event_edit_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                getActivity().invalidateOptionsMenu();
                mListener.onCancelEventInteraction();

            }
        });

        imageView = (ImageView) contentView.findViewById(R.id.event_edit_image);
        imageView.setOnClickListener(new View.OnClickListener()

        {
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

        void onCancelEventInteraction();
    }


    private void dispatchTakePictureIntent() {
       /* Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }*/
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        // where do we want to find the data?
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        // finally, get a URI representation
        Uri data = Uri.parse(pictureDirectoryPath);
        // set the data and type.  Get all image types.
        photoPickerIntent.setDataAndType(data, "image/*");

        // we will invoke this activity, and get something back from it.
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      /*  if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }*/
        if (resultCode == RESULT_OK) {
            // if we are here, everything processed successfully.
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                // if we are here, we are hearing back from the image gallery.

                // the address of the image on the SD Card.
                Uri imageUri = data.getData();

                // declare a stream to read the image data from the SD Card.
                InputStream inputStream;

                // we are getting an input stream, based on the URI of the image.
                try {
                    inputStream = getActivity().getContentResolver().openInputStream(imageUri);

                    // get a bitmap from the stream.
                    imageBitmap = BitmapFactory.decodeStream(inputStream);


                    // show the image to the user
                    imageView.setImageBitmap(imageBitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // show a message to the user indictating that the image is unavailable.
                    Toast.makeText(getActivity(), "Unable to open image", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    public String getEventId() {
        return eventId;
    }
}
