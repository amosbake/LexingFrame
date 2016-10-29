package com.lexing.lexingframe.gallery;

import android.text.format.DateFormat;

import java.util.Date;

/**
 * Author: mopel
 * Date : 2016/10/29
 */
public class ImageData{
    public ImageData(String url, Date date) {
        this.url = url;
        mDate = date;
    }

    public ImageData() {
    }

    String url;
    Date mDate;

    @Override
    public String toString() {
        return url+" : "+ DateFormat.format("MM-dd",mDate);
    }
}
