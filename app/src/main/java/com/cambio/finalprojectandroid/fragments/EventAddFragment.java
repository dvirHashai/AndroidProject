package com.cambio.finalprojectandroid.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.cambio.finalprojectandroid.utils.Date;
import com.cambio.finalprojectandroid.utils.Time;
import com.cambio.finalprojectandroid.widget.MyDatePicker;
import com.cambio.finalprojectandroid.widget.MyTimePicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;


public class EventAddFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    ImageView imageView;
    Bitmap imageBitmap;
    ProgressBar progressBar;
    public static final int IMAGE_GALLERY_REQUEST = 20;

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
                        if (Model.instance != null) {
                            final Event event = new Event(Model.instance.getModelFirebase().getFirebaseEventEntityId(), name.getText().toString(), date, time, price.getText().toString(), location.getText().toString(), "", 0);
                            if (imageBitmap != null) {
                                Model.instance.saveImage(imageBitmap, event.getId() + "jpeg", new CallBackInterface.SaveImageListener() {
                                    @Override
                                    public void complete(String url) {
                                        event.setImageUrl(url);
                                        Model.instance.addEvent(event);
                                        progressBar.setVisibility(GONE);
                                    }

                                    @Override
                                    public void fail() {
                                        progressBar.setVisibility(GONE);

                                    }
                                });
                            } else {
                                Model.instance.addEvent(event);
                                progressBar.setVisibility(GONE);

                            }
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
    static final int REQUEST_GALLERY_CAPTURE = 0;

    private void dispatchTakePictureIntent() {
      /*  Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
        /*if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        } else*/
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
}
