package com.example.booknet.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.booknet.DatabaseManager;
import com.example.booknet.Adapters.NotificationAdapter;
import com.example.booknet.Model.Notifications;
import com.example.booknet.R;

public class NotificationFragment extends Fragment {

    private Notifications notifications;
    private RecyclerView notificationsListView;
    private NotificationAdapter notificationAdapter;

    DatabaseManager manager = DatabaseManager.getInstance();

    public static NotificationFragment newInstance() {
        NotificationFragment myFragment = new NotificationFragment();

        Bundle args = new Bundle();
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notifications, container, false);

        notifications = manager.getAllNotifications();

        Log.d("seanTag", "onCreateView Notification");

        notificationsListView = view.findViewById(R.id.notifications);
        notificationsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        notificationAdapter = new NotificationAdapter(notifications, getActivity());
        notificationsListView.setAdapter(notificationAdapter);

        return view;
    }

    /**
     * Called when the activity starts
     * Tells the list's adapter to update.
     */
    @Override
    public void onStart() {
        super.onStart();
        //Log.d("seanTag", "onStart notifications");
        notificationAdapter.notifyDataSetChanged();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void notifyDataSetChanged() {
        notificationAdapter.notifyDataSetChanged();
    }
}
