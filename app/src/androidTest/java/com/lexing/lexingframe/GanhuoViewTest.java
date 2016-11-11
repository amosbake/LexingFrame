package com.lexing.lexingframe;

import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.lexing.lexingframe.ganhuo.GanhuoAndroid;
import com.lexing.lexingframe.ganhuo.GanhuoFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Author: mopel
 * Date : 2016/11/3
 */
@RunWith(AndroidJUnit4.class)
public class GanhuoViewTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testView(){
        GanhuoFragment _fragment= new GanhuoFragment();
        _fragment.onGanhuoListRefresh(getMockData());
        
    }

    @NonNull
    private List<GanhuoAndroid> getMockData() {
        final GanhuoAndroid _android = new GanhuoAndroid("1",
                new Date(1987,12,12),
                new Date(2016,12,12),
                "desc",
                Arrays.asList(new String[]{"aaa"}),
                "ddd",
                "android",
                "http://www.baidu.com",
                "author",
                false);

        return new ArrayList<GanhuoAndroid>(){
            {
                add(_android);
            }
        };
    }
}
