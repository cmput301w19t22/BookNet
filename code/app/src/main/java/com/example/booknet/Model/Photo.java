package com.example.booknet.Model;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;

/**
 * Class for handling images todo more describe
 */
public class Photo implements Serializable {

    private Bitmap imageData;
    private Uri fullPath;

    public Photo(Bitmap imageBitmap) {
        this.imageData = imageBitmap;
    }

    public Bitmap getBitmap() {
        return imageData;
    }

    public void setBitmap(Bitmap imageData) {
        this.imageData = imageData;
    }

//todo complete
}
