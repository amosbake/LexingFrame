package com.lexing.lexingframe.ganhuo;

import com.lexing.lexingframe.SchedlersCompat;

import java.util.List;

import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Author: mopel
 * Date : 2016/11/2
 */
public class GanhuoPresenter implements IGanhuoPresenter {
    GanhuoService mGanhuoService;
    GanhuoView mGanhuoView;
    private int currentIndex = 1;


    public GanhuoPresenter(GanhuoService ganhuoService, GanhuoView ganhuoView) {
        mGanhuoService = ganhuoService;
        mGanhuoView = ganhuoView;
    }

    @Override
    public void loadMoreGanhuoList() {
        mGanhuoService
                .fetchAndroidGanhuo(10, currentIndex+1)
                .map(new Func1<GanhuoData<List<GanhuoAndroid>>, List<GanhuoAndroid>>() {
                    @Override
                    public List<GanhuoAndroid> call(GanhuoData<List<GanhuoAndroid>> listGanhuoData) {
                        return listGanhuoData.getData();
                    }
                })
                .compose(SchedlersCompat.<List<GanhuoAndroid>>applySingleExecutorSchedulers())
                .subscribe(new Action1<List<GanhuoAndroid>>() {
                    @Override
                    public void call(List<GanhuoAndroid> ganhuoAndroids) {
                        currentIndex = currentIndex+1;
                        mGanhuoView.onGanhuoListLoadMore(ganhuoAndroids);
                    }
                });
    }

    @Override
    public void refreshGanhuoList() {
        mGanhuoService
                .fetchAndroidGanhuo(10, 1)
                .map(new Func1<GanhuoData<List<GanhuoAndroid>>, List<GanhuoAndroid>>() {
                    @Override
                    public List<GanhuoAndroid> call(GanhuoData<List<GanhuoAndroid>> listGanhuoData) {
                        return listGanhuoData.getData();
                    }
                })
                .compose(SchedlersCompat.<List<GanhuoAndroid>>applySingleExecutorSchedulers())
                .subscribe(new Action1<List<GanhuoAndroid>>() {
                    @Override
                    public void call(List<GanhuoAndroid> ganhuoAndroids) {
                        currentIndex = 1;
                        mGanhuoView.onGanhuoListRefresh(ganhuoAndroids);
                    }
                });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mGanhuoService = null;
    }
}
