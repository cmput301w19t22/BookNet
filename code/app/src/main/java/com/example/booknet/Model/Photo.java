package com.example.booknet.Model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Class for handling images todo more describe
 */
public class Photo implements Serializable {

    private Bitmap imageData;

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
