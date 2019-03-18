package com.example.booknet;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class NotificationFragment extends Fragment {

    public static NotificationFragment newInstance() {
        NotificationFragment myFragment = new NotificationFragment();

        Bundle args = new Bundle();
        myFragment.setArguments(args);

        return myFragment;
    }


}
