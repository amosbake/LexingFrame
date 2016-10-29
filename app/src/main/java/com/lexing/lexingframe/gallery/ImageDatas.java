package com.lexing.lexingframe.gallery;

import com.lexing.lrecyclerview.mutlitype.Item;

/**
 * Author: mopel
 * Date : 2016/10/29
 */
public class ImageDatas implements Item{
    public ImageDatas(ImageData[] imageDatas) {
        mImageDatas = imageDatas;
    }

    ImageData[] mImageDatas;

}
