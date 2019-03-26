package com.example.booknet.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.booknet.DatabaseManager;
import com.example.booknet.Model.BookListing;
import com.example.booknet.Model.Photo;
import com.example.booknet.R;

import static android.app.Activity.RESULT_OK;


/**
 * Activity for viewing and editing a photo
 * Based on tutorial: https://developer.android.com/training/camera/photobasics
 */
//todo convert to dialogfragment
public class PhotoEditDialog extends DialogFragment {


    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_FILE = 2;


    //Layout Objects
    private ImageView photoView;
    private ImageButton leaveButton;
    private ImageButton cameraButton;
    private ImageButton selectFileButton;
    private ImageButton deleteButton;
    private View dialogView;

    //Dialog Data
    private BookListing listing;
    private DatabaseManager manager = DatabaseManager.getInstance();


    /**
     * Creates a new instance of this dialog.
     *
     * @param listing The listing which is to be edited.
     * @return The new PhotoEditDialog instance
     */
    public static PhotoEditDialog newInstance(BookListing listing) {
        PhotoEditDialog fragment = new PhotoEditDialog();
        fragment.listing = listing;//todo cant we just do this?
        Bundle args = new Bundle();
        //args.putString("reviewer", reviewer);
        //todo put extras for data
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when creating the dialog.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //field = getArguments().getString("key");
            //todo get listing data
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create the Dialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.activity_photo_edit, null);
        builder.setView(dialogView);

        //Get Layout Objects
        photoView = dialogView.findViewById(R.id.currentPhoto);
        cameraButton = dialogView.findViewById(R.id.cameraButton);
        selectFileButton = dialogView.findViewById(R.id.selectFileButton);
        deleteButton = dialogView.findViewById(R.id.deleteButton);
        leaveButton = dialogView.findViewById(R.id.leave_button);

        Bitmap photoBitmap = listing.getPhotoBitmap();
        if (photoBitmap != null) {
            photoView.setImageBitmap(photoBitmap);
        } else {
            photoView.setImageResource(R.drawable.ic_book_default);
        }


        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestTakePhoto();
            }
        });

        selectFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromFile();
            }
        });


        //Copied from Jamie's assignment 1
        //Create the dialog for the Delete button
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setMessage("Are you sure you want to delete this item?");
        alertBuilder.setTitle("Confirm");
        alertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePhoto();
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), "Did not delete.", Toast.LENGTH_SHORT).show();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Confirm Delete with a Dialog
                alertBuilder.create().show();
            }
        });

        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }


    /**
     * Responds to the camera activity on it's completion
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("jamie", "returning photo...");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            savePhoto(imageBitmap);
            Photo photo = new Photo(imageBitmap);

            listing.setPhoto(photo);
            try {
                manager.writeUserBookListing(listing);
            } catch (Exception e) {
                Log.d("jamie", "Failed write photo:\n" + e.getLocalizedMessage());
            }


            photoView.setImageBitmap(imageBitmap);
        }
        if (requestCode == REQUEST_IMAGE_FILE && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(getActivity(), "File Not Selected", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), data.getDataString(), Toast.LENGTH_LONG).show();
                Log.d("jamie", data.getDataString());
                Uri dataPath = data.getData();
                photoView.setImageURI(dataPath);
            }
        }
    }

    /**
     * Creates a request to take a photo.
     */
    public void requestTakePhoto() {
        Log.d("jamie", "taking photo");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            //todo save image to file
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    /**
     * Obtains a photo from a file.
     */
    private void selectImageFromFile() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_FILE);
    }

    /**
     * Saves a photo bitmap to file/database.
     *
     * @param photo The bitmap of the photo to be saved
     */
    private void savePhoto(Bitmap photo) {
        //todo revise signature and implement
    }

    /**
     * Deletes the photo from the listing.
     */
    private void deletePhoto() {
        listing.deletePhoto();
        Toast.makeText(getActivity(), "Deleted Photo", Toast.LENGTH_LONG).show();
        dismiss();
    }

    /**
     * Called when the dialog is dismissed. Tells the source activity the dialog dismissed
     * if it has the DialogCloseListener interface.
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //Tell the source activity the dialog closed so it can update
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener) activity).onDialogClose();
        }
    }
}
