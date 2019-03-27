package com.example.booknet.Dialogs;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.booknet.BuildConfig;
import com.example.booknet.DatabaseManager;
import com.example.booknet.Model.BookListing;
import com.example.booknet.Model.Photo;
import com.example.booknet.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private Button submitButton;
    private View dialogView;

    private String photoLocalPath;
    private Uri photoUri;

    private Bitmap viewingBitmap;

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
        fragment.listing = listing;//todo cant we just do this? // I don't know -matt
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
        dialogView = inflater.inflate(R.layout.dialog_photo_edit, null);
        builder.setView(dialogView);

        //Get Layout Objects
        photoView = dialogView.findViewById(R.id.currentPhoto);
        cameraButton = dialogView.findViewById(R.id.cameraButton);
        selectFileButton = dialogView.findViewById(R.id.selectFileButton);
        deleteButton = dialogView.findViewById(R.id.deleteButton);
        leaveButton = dialogView.findViewById(R.id.leave_button);
        submitButton = dialogView.findViewById(R.id.thumbnail_submit_button);

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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitThumbNail();
                dismiss();
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
            photoView.setImageBitmap(imageBitmap);

            viewingBitmap = imageBitmap;

//            savePhoto(imageBitmap);
//            Photo photo = new Photo(imageBitmap);
//            photoView.setImageBitmap(imageBitmap);

        }

        if (requestCode == REQUEST_IMAGE_FILE && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(getActivity(), "File Not Selected", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(getActivity(), data.getDataString(), Toast.LENGTH_LONG).show();
                Log.d("jamie", data.getDataString());
                Uri dataPath = data.getData();
                photoView.setImageURI(dataPath);


                try {
                    viewingBitmap = Bitmap.createBitmap(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), dataPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Creates a request to take a photo.
     * The photo will also be saved to the local picture directory.
     * <p>
     * Uses code from: https://stackoverflow.com/questions/6448856/android-camera-intent-how-to-get-full-sized-photo
     */
    public void requestTakePhoto() {
        Log.d("jamie", "taking photo");
        //Only continue if we have permissions

        if (!checkPermissions()) {
            requestPermissions(1);
        }else{
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                //todo save image to file
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
//                //Get the directory for photos and create a new file
//                //File pictures = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Camera");
//                File dcim = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
//                String photoFilename = makePhotoFilename(listing);
//                //File output = new File(pictures, photoFilename);
//                File output = null;
//                Log.d("jamie", "photo temp path: " + dcim.toString() + ", " + photoFilename + ".jpg");
//                //output = File.createTempFile(photoFilename, ".jpg", dcim);
//                output = new File(dcim, photoFilename + ".jpg");
//                if (output != null) {
//                    //Create the Uri for where to output the photo
//                    Uri outputUri = FileProvider.getUriForFile(getActivity(),
//                            BuildConfig.APPLICATION_ID + ".provider", output);
//
//                    //Save the path for future use
//                    this.photoLocalPath = "file:" + output.getAbsolutePath();
//                    Log.d("jamie", "photo filepath: " + output.getAbsolutePath());
//                    Log.d("jamie", "photo uri " + outputUri.isAbsolute() + ": " + outputUri.toString());
//                    //output.delete();
//
//                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
//                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
//                }


        }



        }
    }

    /**
     * Creates a filename for a new photo based on a listing.
     * The returned name format is: [isbn]_[title]_on[timestamp]
     *
     * @param listing The listing to use to make the name.
     * @return A presumably unique filename.
     */
    private String makePhotoFilename(BookListing listing) {
        String format = "%s_%s_on%s";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormat.format(new Date());
        String path = String.format(format, listing.getISBN(),
                listing.getBook().getTitle().replaceAll("\\s", "_"), timestamp);
        return path;
    }


    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {




            return true;
        }

        boolean perm1 = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean perm2 = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean perm3 = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        Log.d("jamie", "perm1: "+perm1);
        Log.d("jamie", "perm2: "+perm2);
        Log.d("jamie", "perm3: "+perm3);
        Log.d("jamie", "failed permissions");
        return false;
    }





    /**
     * Obtains a photo from a file.
     */
    public void selectImageFromFile() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_FILE);
    }

    private void submitThumbNail(){
        final Activity sourceActivity = getActivity();
        if (viewingBitmap == null){
            Toast.makeText(getActivity(), "Thumbnail not changed", Toast.LENGTH_LONG).show();
        }
        else{
            manager.writeThumbnailForListing(listing, viewingBitmap,

                    new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(sourceActivity, "Thumnail changed", Toast.LENGTH_SHORT).show();
                }
            },

                    new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(sourceActivity, "Thumnail change failed", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }


    private void requestPermissions(int request) {
        Log.d("jamie", getActivity().getLocalClassName());
        ActivityCompat.requestPermissions(getActivity()
                , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, request);

        Log.d("jamie", "requesting permissions");
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
