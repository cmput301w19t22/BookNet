package com.example.booknet;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A dialog fragment to create a review for a user.
 *
 * @author Jamie
 */
public class ReviewCreateDialog extends DialogFragment {

    //Layout Objects
    private RatingBar ratingInput;
    private TextView ratingFeedback;
    private TextView remainingTextLabel;
    private EditText commentField;
    private ImageButton confirmButton;
    private ImageButton cancelButton;

    //Dialog Data
    private String reviewer = "";
    private String reviewed = "";

    /**
     * Create an instance of this dialog.
     *
     * @param reviewer User making the review
     * @param reviewed User receiving the review
     * @return A new instance of ReviewCreateDialog.
     */
    public static ReviewCreateDialog newInstance(String reviewer, String reviewed) {
        ReviewCreateDialog fragment = new ReviewCreateDialog();
        Bundle args = new Bundle();
        args.putString("reviewer", reviewer);
        args.putString("reviewed", reviewed);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when creating the dialog.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reviewer = getArguments().getString("reviewer");
            reviewed = getArguments().getString("reviewed");
        }
    }

    /**
     * Called when creating the dialog.
     * Sets up the layout of the dialog and the input listeners.
     *
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //Create the Dialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.fragment_review_create, null);
        builder.setView(dialogView);

        //Setup Layout
        ratingInput = dialogView.findViewById(R.id.ratingInput);
        ratingFeedback = dialogView.findViewById(R.id.ratingFeedback);
        remainingTextLabel = dialogView.findViewById(R.id.remainingTextLabel);
        commentField = dialogView.findViewById(R.id.commentField);
        confirmButton = dialogView.findViewById(R.id.confirmButton);
        cancelButton = dialogView.findViewById(R.id.cancelButton);

        //Set Listeners
        ratingInput.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    ratingFeedback.setText(String.format("%1.1f", rating));
                }
            }
        });

        commentField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int characters = s.toString().length();
                int remaining = R.integer.ratingCommentMaxSize - characters;
                remainingTextLabel.setText("(" + remaining + ")");
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createReview();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }


    /**
     * Creates a review from the data in the dialog
     */
    private void createReview() {
        //Get Info To Create Review
        int score = (int) ratingInput.getRating();
        String message = commentField.getText().toString();

        //Create review
        Review review = new Review(reviewer, reviewed, score, message);

        //Write Review to Database
        //todo send review to db
        Toast.makeText(getContext(), "Review Sent\n(Not Really)", Toast.LENGTH_LONG).show();

        //Close the dialog
        dismiss();
    }

}
