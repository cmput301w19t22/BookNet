package com.example.booknet.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter class to customize the appearance of a spinner for filtering purposes.
 */
public class SpinnerAdapter<String> extends ArrayAdapter<String> {
    public SpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public SpinnerAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    /**
     * https://stackoverflow.com/questions/15479579/remove-text-from-spinner
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView label = view.findViewById(android.R.id.text1);
        label.setVisibility(View.INVISIBLE);//we don't want the text visible, only the icon

        return view;
    }


}
