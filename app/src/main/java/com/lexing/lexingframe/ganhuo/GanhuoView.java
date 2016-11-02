package com.lexing.lexingframe.ganhuo;

import java.util.List;

/**
 * Author: mopel
 * Date : 2016/11/2
 */
public interface GanhuoView {
    void onGanhuoListLoadMore(List<GanhuoAndroid> ganhuoAndroids);
    void onGanhuoListRefresh(List<GanhuoAndroid> ganhuoAndroids);
}
