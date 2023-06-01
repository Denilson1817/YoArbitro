package com.denilsonperez.yoarbitro.modelo;

import android.view.View;

public class FileinModel {

    String filename, fileurl;

    public FileinModel() {
    }

    public FileinModel(String filename, String fileurl) {
        this.filename = filename;
        this.fileurl = fileurl;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public static interface ItemClickListerner {
        void onClick(View view, int position, boolean isLongClick);
    }
}