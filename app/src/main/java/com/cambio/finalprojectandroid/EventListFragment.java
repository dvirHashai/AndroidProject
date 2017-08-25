package com.cambio.finalprojectandroid;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cambio.finalprojectandroid.model.Event;
import com.cambio.finalprojectandroid.model.Model;

import java.util.List;


public class EventListFragment extends Fragment {

    ListView list;
    List<Event> data;
    EventListAdapter adapter;

    private OnFragmentInteractionListener mListener;

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
        getActivity().setTitle("Event List");
        // Inflate the layout for this fragment
        View contextView = inflater.inflate(R.layout.fragment_event_list, container, false);

        data = Model.instace.getModelMem().getAllEvents();
        list = (ListView) contextView.findViewById(R.id.event_list);
        adapter = new EventListAdapter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", "row item was clicked at position: " + position);
                String eventId = data.get(position).getId();
                mListener.onEventClickInteraction(eventId);
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


            Event event = data.get(position);
            name.setText(event.getName());
            date.setText(event.getDate().toString());

            return convertView;
        }
    }

}
