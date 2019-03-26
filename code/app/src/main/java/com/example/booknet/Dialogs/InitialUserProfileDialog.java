package com.example.booknet.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.booknet.Model.CurrentUser;
import com.example.booknet.R;

// https://developer.android.com/guide/topics/ui/dialogs#java
public class InitialUserProfileDialog extends DialogFragment {


    public interface InitialUserProfileListener {
        public void onDialogPositiveClick(DialogFragment dialog,String email, String phone, String username);
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
        EditText emailEditText = dialogView.findViewById(R.id.initial_profile_email);
        emailEditText.setText(CurrentUser.getInstance().getDefaultEmail());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton("yeah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        EditText emailEditText = dialogView.findViewById(R.id.initial_profile_email);
                        EditText phoneEditText = dialogView.findViewById(R.id.phonenumber);
                        EditText usernameEditText = dialogView.findViewById(R.id.username);


                        String phonenumber = phoneEditText.getText().toString();
                        String username = usernameEditText.getText().toString();
                        String email = emailEditText.getText().toString();

                        listener.onDialogPositiveClick(InitialUserProfileDialog.this, email, phonenumber, username);
                    }
                })
                .setNegativeButton("nope", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick(InitialUserProfileDialog.this);
                    }
                });
        setCancelable(false);
        return builder.create();
    }




}
