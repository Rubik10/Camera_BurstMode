package com.rubik.appcameraburst.model;

import android.graphics.Bitmap;

/**
 * Created by Rubik on 7/10/16.
 */

public class CamFile {
    private Bitmap bitmap;
    private String tittle;

    public CamFile(Bitmap bitmap, String tittle) {
        this.bitmap = bitmap;
        this.tittle = tittle;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }
}
