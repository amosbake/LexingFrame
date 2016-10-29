package com.lexing.lexingframe.gallery;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateFormat;

import com.lexing.common.assist.Check;
import com.lexing.lrecyclerview.mutlitype.Item;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author: mopel
 * Date : 2016/10/29
 */
public final class ImageHelper {

    @Nullable
    public static List<Item> spiltImageDataByDate(List<ImageData> imageDatas) {
        if (Check.isEmpty(imageDatas)) {
            return null;
        }
        ImageData tempData = null;
        ImageData[] _imageDatas = new ImageData[3];
        int arrayIndex = 0;
        List<Item> resultList = new ArrayList<>();
        for (int i = 0; i < imageDatas.size(); i++) {
            final ImageData currentData = imageDatas.get(i);
            if (tempData==null){
                tempData = currentData;
                _imageDatas[0] = tempData;
                arrayIndex = 1;
                resultList.add(new TitleData(DateFormat.format("yyyy-MM-dd",tempData.mDate).toString()));
                continue;
            }
            //如与暂存的数据日期相同,则生成3个一组的ImageData
            if (isSameDay(tempData.mDate,currentData.mDate)){
                if (arrayIndex <= 2){
                    _imageDatas[arrayIndex] =  imageDatas.get(i);
                    arrayIndex ++;
                }
                if (arrayIndex > 2){
                    resultList.add(new ImageDatas(_imageDatas));
                    _imageDatas = new ImageData[3];
                    arrayIndex = 0;
                }
            }
            //如与暂存的数据日期不同,则记录不同点的位置,并另起一行
            else{
                if (_imageDatas[0]!=null){
                    resultList.add(new ImageDatas(_imageDatas));
                }
                _imageDatas = new ImageData[3];
                _imageDatas[0] = currentData;
                arrayIndex = 1;
                tempData = currentData;
                resultList.add(new TitleData(DateFormat.format("yyyy-MM-dd",tempData.mDate).toString()));
            }
        }
        return resultList;
    }

    static boolean isSameDay(Date date1,Date date2){
        CharSequence dateStr1 = DateFormat.format("yyyy-MM-dd",date1);
        CharSequence dateStr2 = DateFormat.format("yyyy-MM-dd",date2);
        return TextUtils.equals(dateStr1,dateStr2);
    }


}
