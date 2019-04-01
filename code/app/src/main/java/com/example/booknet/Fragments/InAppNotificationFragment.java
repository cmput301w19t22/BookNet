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
import com.example.booknet.Adapters.InAppNotificationAdapter;
import com.example.booknet.Model.CurrentUser;
import com.example.booknet.Model.InAppNotification;
import com.example.booknet.Model.InAppNotificationList;
import com.example.booknet.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class InAppNotificationFragment extends Fragment {

    private InAppNotificationList inAppNotificationList;
    private RecyclerView notificationsListView;
    private InAppNotificationAdapter inAppNotificationAdapter;
    private ValueEventListener notificationListener;
    private DatabaseReference notificationRef;

    DatabaseManager manager = DatabaseManager.getInstance();

    public static InAppNotificationFragment newInstance() {
        InAppNotificationFragment myFragment = new InAppNotificationFragment();

        Bundle args = new Bundle();
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notifications, container, false);

        inAppNotificationList = manager.readNotifications(CurrentUser.getInstance().getUsername());

        Log.d("seanTag", "onCreateView InAppNotification");

        notificationsListView = view.findViewById(R.id.inAppNotificationList);
        notificationsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        inAppNotificationAdapter = new InAppNotificationAdapter(inAppNotificationList, getActivity());
        notificationsListView.setAdapter(inAppNotificationAdapter);
        notificationsListView.addItemDecoration(new SpaceDecoration(12,16));

        return view;
    }

    public void removeInAppNotification(InAppNotification inAppNotification) {
        inAppNotificationList.removeNotification(inAppNotification);
        notifyDataSetChanged();
    }

    /**
     * Called when the activity starts
     * Tells the list's adapter to update.
     */
    @Override
    public void onStart() {
        super.onStart();
        inAppNotificationAdapter.notifyDataSetChanged();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void notifyDataSetChanged() {
        inAppNotificationAdapter.notifyDataSetChanged();
    }
}
