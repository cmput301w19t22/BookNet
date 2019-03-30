package com.example.booknet.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.booknet.Model.Portrait;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * Activity to scan barcodes. Only scans and returns the result via OnActivityResult
 */
public class ScanActivity extends AppCompatActivity {

    public static final int SCAN_REQUEST = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();


        scanNow();
    }

    /**
     * Starts this activity for the caller.
     *
     * @param caller The activity that called and wants the result from this activity.
     */
    public static void requestScan(Activity caller) {
        Intent scanIntent = new Intent(caller.getApplicationContext(), ScanActivity.class);
        caller.startActivityForResult(scanIntent, SCAN_REQUEST);
    }


    /**
     * Get the scan result and pass back to caller.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Log.d("scan", "result started");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Intent resultIntent = new Intent();
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getApplicationContext(), "Result Not Found", Toast.LENGTH_SHORT).show();
                resultIntent.putExtra("result", "");
                setResult(RESULT_CANCELED, resultIntent);
            } else {
                Log.d("isbn scan", result.getContents());

                resultIntent.putExtra("result", result.getContents());
                setResult(RESULT_OK, resultIntent);
            }
        }
        finish();
    }

    /**
     * Runs the code needed for the use of the ISBN scanner
     *
     * @author Andi Aspin - https://www.youtube.com/watch?v=PRIVHoEyeL0&t=41s
     */
    private final void scanNow() {
        Activity source = this;
        IntentIntegrator integrator = new IntentIntegrator(source);
        integrator.setCaptureActivity(Portrait.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan Your Barcode");
        integrator.initiateScan();
    }
}
