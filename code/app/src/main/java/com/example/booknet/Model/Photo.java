package com.example.booknet.Model;

import android.net.Uri;

/**
 * Class for handling images todo more describe
 */
public class Photo {

    //private Bitmap imageData;
    private String localPath;
    private Uri fullPath;

    public Photo() {
    }

    public Photo(Uri fullPath) {
        this.fullPath = fullPath;
    }


    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public Uri getFullPath() {
        return fullPath;
    }

    public void setFullPath(Uri fullPath) {
        this.fullPath = fullPath;
    }

    //todo complete
}
