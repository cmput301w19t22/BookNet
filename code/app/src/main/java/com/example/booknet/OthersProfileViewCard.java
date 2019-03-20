package com.example.booknet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

// https://developer.android.com/guide/topics/ui/dialogs#java
public class OthersProfileViewCard extends DialogFragment {


    public interface InitialUserProfileListener {
        public void onDialogPositiveClick(DialogFragment dialog, String phone, String username);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    InitialUserProfileListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (InitialUserProfileListener) context;

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_initial_profile, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView)
                // Add action buttons


                .setPositiveButton("yeah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        EditText phoneEditText = dialogView.findViewById(R.id.phonenumber);
                        EditText usernameEditText = dialogView.findViewById(R.id.username);


                        String phonenumber = phoneEditText.getText().toString();
                        String username = usernameEditText.getText().toString();

                        listener.onDialogPositiveClick(OthersProfileViewCard.this, phonenumber, username);
                    }
                })
                .setNegativeButton("nope", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick(OthersProfileViewCard.this);
                    }
                });
        setCancelable(false);
        return builder.create();
    }




}
