package com.example.booknet.Model;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Class for handling images todo more describe
 */
public class Photo {

    //private Bitmap imageData;
    private Bitmap bitmap;


    public Photo(Bitmap bitmap) {
        this.bitmap = bitmap;
    }



    public Bitmap getBitmap() {
        return bitmap;
    }

}
