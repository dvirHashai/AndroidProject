package com.cambio.finalprojectandroid;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity implements EventListFragment.OnFragmentInteractionListener, EventEditFragment.OnFragmentInteractionListener, EventAddFragment.OnFragmentInteractionListener, EventDetailsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onEventClickInteraction(String eventID) {

    }

    @Override
    public void onSaveEventInteraction() {

    }

    @Override
    public void onDeleteEventInteraction() {

    }

    @Override
    public void onCancelEventInteraction() {

    }

    @Override
    public void onAddEventInteraction() {

    }


    @Override
    public void onEditEventInteraction() {

    }
}
