package com.lexing.lexingframe.ganhuo;

import android.support.annotation.NonNull;

import com.lexing.lexingframe.RxUnitTestTools;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Single;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Author: mopel
 * Date : 2016/11/2
 */
public class IGanhuoPresenterTest {
    GanhuoPresenter mGanhuoPresenter;
    GanhuoView mGanhuoView;
    GanhuoService mGanhuoService;

    @Before
    public void setUp() throws Exception{
        RxUnitTestTools.openRxTools();
        mGanhuoView = mock(GanhuoView.class);
        mGanhuoService = mock(GanhuoService.class);

    }

    @Test
    public void refreshGanhuoList() throws Exception {
        mGanhuoPresenter = new GanhuoPresenter(mGanhuoService,mGanhuoView);
        GanhuoData<List<GanhuoAndroid>> _data = getMockData();
        when(mGanhuoService.fetchAndroidGanhuo(anyInt(),anyInt()))
                .thenReturn(Single.just(_data));
        mGanhuoPresenter.refreshGanhuoList();
        ArgumentCaptor<List> _captor;
        _captor = ArgumentCaptor.forClass(List.class);
        verify(mGanhuoService).fetchAndroidGanhuo(10,1);
        verify(mGanhuoView).onGanhuoListRefresh(_captor.capture());
    }

    @Test
    public void loadMoreGanhuoList() throws Exception {
        mGanhuoPresenter = new GanhuoPresenter(mGanhuoService,mGanhuoView);
        GanhuoData<List<GanhuoAndroid>> _data = getMockData();
        when(mGanhuoService.fetchAndroidGanhuo(anyInt(),anyInt()))
                .thenReturn(Single.just(_data));
        mGanhuoPresenter.loadMoreGanhuoList();
        ArgumentCaptor<List> _captor;
        _captor = ArgumentCaptor.forClass(List.class);
        verify(mGanhuoService).fetchAndroidGanhuo(10,2);
        verify(mGanhuoView).onGanhuoListLoadMore(_captor.capture());
    }




    @NonNull
    private GanhuoData<List<GanhuoAndroid>> getMockData() {
        final GanhuoAndroid _android = new GanhuoAndroid();
        _android.author="author";
        _android.createDate=new Date(1987,12,12);
        _android.desc="desc";
        _android.id = "1";
        _android.imgs = new ArrayList<String>(){
            {
                add("aaa");
                add("bbb");
            }
        };
        _android.publishDate=new Date(2016,12,12);
        _android.type="android";
        _android.url="http://www.baidu.com";
        _android.source = "ddd";
        List<GanhuoAndroid> _ganhuoAndroids = new ArrayList<GanhuoAndroid>(){
            {
                add(_android);
            }
        };
        GanhuoData<List<GanhuoAndroid>> _data = new GanhuoData<>();
        _data.data = _ganhuoAndroids;
        return _data;
    }

}