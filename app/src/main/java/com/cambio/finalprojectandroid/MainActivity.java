package com.cambio.finalprojectandroid;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends Activity implements EventListFragment.OnFragmentInteractionListener, EventEditFragment.OnFragmentInteractionListener, EventAddFragment.OnFragmentInteractionListener, EventDetailsFragment.OnFragmentInteractionListener {
    EventListFragment eventListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         eventListFragment = EventListFragment.newInstance();
        FragmentTransaction tran = getFragmentManager().beginTransaction();
        tran.add(R.id.main_fragment_container, eventListFragment, "eventListFragment");
        tran.commit();
    }

    @Override
    public void onEventClickInteraction(String eventID) {
        EventDetailsFragment eventDetailsFragment = EventDetailsFragment.newInstance(eventID);
        FragmentTransaction tran = getFragmentManager().beginTransaction();
        tran.add(R.id.main_fragment_container, eventDetailsFragment, "eventDetailsFragment");
        tran.hide(eventListFragment);
        tran.show(eventDetailsFragment);
        tran.commit();

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
