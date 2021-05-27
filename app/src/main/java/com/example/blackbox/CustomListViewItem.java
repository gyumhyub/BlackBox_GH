package com.example.blackbox;

import android.graphics.Bitmap;

public class CustomListViewItem {
    private String filename;
    // 영상의 파일명을 저장
    private String description;

    private Bitmap iconBitmap;
    // 영상의 thumbnail을 저장

    public void setIcon(Bitmap icon) {
        iconBitmap = icon ;
    }
    public void setFilename(String name) {
        filename = name;
    }

    public Bitmap getIcon() {
        return this.iconBitmap ;
    }
    public String getFilename() {
        return this.filename ;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}