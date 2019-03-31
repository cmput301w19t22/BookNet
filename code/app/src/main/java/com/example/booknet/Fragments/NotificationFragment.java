package com.example.booknet.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.booknet.Adapters.SpaceDecoration;
import com.example.booknet.DatabaseManager;
import com.example.booknet.Adapters.NotificationAdapter;
import com.example.booknet.Model.InAppNotifications;
import com.example.booknet.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class NotificationFragment extends Fragment {

    private InAppNotifications inAppNotifications;
    private RecyclerView notificationsListView;
    private NotificationAdapter notificationAdapter;
    private ValueEventListener notificationListener;
    private DatabaseReference notificationRef;

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

        inAppNotifications = manager.getAllNotifications();

        Log.d("seanTag", "onCreateView InAppNotification");

        notificationsListView = view.findViewById(R.id.inAppNotifications);
        notificationsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        notificationAdapter = new NotificationAdapter(inAppNotifications, getActivity());
        notificationsListView.setAdapter(notificationAdapter);
        notificationsListView.addItemDecoration(new SpaceDecoration(12,16));

        return view;
    }

    /**
     * Called when the activity starts
     * Tells the list's adapter to update.
     */
    @Override
    public void onStart() {
        super.onStart();
        notificationAdapter.notifyDataSetChanged();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void notifyDataSetChanged() {
        notificationAdapter.notifyDataSetChanged();
    }
}
