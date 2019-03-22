package com.example.booknet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Class for scanning and verifying ISBN numbers.
 *
 * @author Jamie
 * @version 1.0
 */
public abstract class ISBNScannerActivity extends AppCompatActivity {

    // ISBN Scanner
    /**
     * Handles the retrieval of the Data from the ISBN scanner
     * @author Andi Aspin - https://www.youtube.com/watch?v=PRIVHoEyeL0&t=41s
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this,"Result Not Found", Toast.LENGTH_SHORT).show();
            } else {
                onScanResults(result.getContents());
            }
        }
    }
    /**
     * Runs the code needed for the use of the ISBN scanner
     * @author Andi Aspin - https://www.youtube.com/watch?v=PRIVHoEyeL0&t=41s
     */
    protected final void scanNow() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(Portrait.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan Your Barcode");
        integrator.initiateScan();
    }

    /**
     * Called when the isbn is scanned with the scanned isbn as an argument.
     * Meant to be overridden to provide specific functions per activity
     * @param isbn
     */
    protected void onScanResults(String isbn){
        //throw new NoSuchMethodException("Must Override this method");
    }


    /**
     * Checks if the given string value is a valid ISBN format.
     * Does not check that the ISBN matches an existing book.
     *
     * @param value Value to validate
     * @return True if a valid format, False if not.
     */
    public static boolean isValidISBNFormat(String value) {
        //todo implement correct method, part 5 task
        //temp fake check
        if (value.length() <= 13) {
            return true;
        } else {
            return false;
        }
    }
}
