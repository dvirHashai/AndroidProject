package com.cambio.finalprojectandroid;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cambio.finalprojectandroid.model.Event;
import com.cambio.finalprojectandroid.model.Model;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;


public class EventListFragment extends Fragment {

    ListView list;
    List<Event> data = new LinkedList<>();
    EventListAdapter adapter;

    static final int REQUEST_WRITE_STORAGE = 11;
    private OnFragmentInteractionListener mListener;


    // This method will be called when a MessageEvent is posted
    // (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Model.EventUpdateEvent event) {
        Toast.makeText(getActivity(), "got new event item event", Toast.LENGTH_SHORT).show();
        boolean exist = false;
        switch(event.stateChange){
            case ADDED:
                data.add(event.event);
                break;
            case REMOVED:
                for (Event eventItem : data) {
                    if (eventItem.getId().equals(event.event.getId())) {

                        data.remove(eventItem);
                    }
                }
                break;
            case CHANGED:
                for (Event eventItem : data) {
                    if (eventItem.getId().equals(event.event.getId())) {
                        Log.d("TAG","onMessageEvent " + eventItem.getId() + "equals: " + event.event.getId());
                        eventItem.setEvent(event.event);
                        break;
                    }
                }
                break;

        }

        if (!exist) {
            data.add(event.event);
        }
        adapter.notifyDataSetChanged();
        list.setSelection(adapter.getCount() - 1);

    }


    public EventListFragment() {
        // Required empty public constructor
    }

    public static EventListFragment newInstance() {
        EventListFragment fragment = new EventListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        checkPermissions();
        EventBus.getDefault().register(this);


        getActivity().setTitle("Event List");
        // Inflate the layout for this fragment
        View contextView = inflater.inflate(R.layout.fragment_event_list, container, false);

        list = (ListView) contextView.findViewById(R.id.event_list);
        adapter = new EventListAdapter();
        list.setAdapter(adapter);
        Model.getInstance();
        if (Model.instace != null) {

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("TAG", "row item was clicked at position: " + position);
                    String eventId = data.get(position).getId();
                    mListener.onEventClickInteraction(eventId);
                }
            });


            Model.instace.getAllEvents(new Model.GetAllEventsAndObserveCallback() {
                @Override
                public void onComplete(List<Event> list) {
                    data = list;
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancel() {

                }
            });
        }


//TODO getActivity().invalidateOptionsMenu();
        return contextView;
    }


    public void checkPermissions() {
        boolean hasPermission = (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }
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
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onEventClickInteraction(String eventID);
    }

    class EventListAdapter extends BaseAdapter {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.event_list_row, null);
            }

            TextView name = (TextView) convertView.findViewById(R.id.event_list_row_name);
            TextView date = (TextView) convertView.findViewById(R.id.event_list_row_date);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.event_list_row_image);
            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.event_list_row_progressBar);

            if (date != null) {
                final Event event = data.get(position);
                name.setText(event.getName());
                date.setText(event.getDate().toString());
                imageView.setTag(event.getImageUrl());
                imageView.setImageResource(R.drawable.avatar);

                if (event.getImageUrl() != null && !event.getImageUrl().isEmpty() && !event.getImageUrl().equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    Model.instace.getImage(event.getImageUrl(), new Model.GetImageListener() {
                        @Override
                        public void onSuccess(Bitmap image) {
                            String tagUrl = imageView.getTag().toString();
                            if (tagUrl.equals(event.getImageUrl())) {
                                imageView.setImageBitmap(image);
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFail() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }

            }

            return convertView;
        }

    }

}
