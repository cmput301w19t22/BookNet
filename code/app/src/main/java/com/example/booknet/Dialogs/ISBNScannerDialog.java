package com.example.booknet.Dialogs;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;

import com.example.booknet.Activities.ScanActivity;

/**
 * Class for scanning and verifying ISBN numbers.
 *
 * @author Jamie
 * @version 1.0
 */
public abstract class ISBNScannerDialog extends DialogFragment {

    // ISBN Scanner

    /**
     * Handles the retrieval of the Data from the ISBN scanner
     *
     * @author Andi Aspin - https://www.youtube.com/watch?v=PRIVHoEyeL0&t=41s
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("isbn", "returned from scan");
        if (requestCode == ScanActivity.SCAN_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.d("isbn", "got scan results");
            if (data.hasExtra("result")) {
                String result = data.getStringExtra("result");
                if (result == null) {
                    Toast.makeText(getContext(), "Result Not Found", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("isbn dialog result:", result);
                    onScanResults(result);
                }
            }
        }
    }

    /**
     *
     */
    protected final void scanNow() {
        /*Activity source = getActivity();
        IntentIntegrator integrator = new IntentIntegrator(source);
        integrator.setCaptureActivity(Portrait.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan Your Barcode");
        integrator.initiateScan();*/
        Intent scanIntent = new Intent(getContext(), ScanActivity.class);
        startActivityForResult(scanIntent, ScanActivity.SCAN_REQUEST);
        //ScanActivity.requestScan(getActivity());
    }

    /**
     * Called when the isbn is scanned with the scanned isbn as an argument.
     * Meant to be overridden to provide specific functions per activity
     *
     * @param isbn The value that was scanned. Is not validated.
     */
    protected void onScanResults(String isbn) {
    }


    /**
     * Checks if the given string value is a valid ISBN format.
     * Does not check that the ISBN matches an existing book.
     *
     * @param value Value to validate
     * @return True if a valid format, False if not.
     */
    public static boolean isValidISBNFormat(String value) {
        //todo implement correct method
        //temp fake check
        if (value.length() <= 13 && value.length() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
