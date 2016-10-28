package com.lexing.lrecyclerview.adapter;

import java.util.List;

/**
 * Author: mopel
 * Date : 2016/10/25
 */

interface CRUD<T> {
    void add(T item);

    void addAll(List<T> items);

    void remove(T item);


    void removeAll(List<T> items);

    void retainAll(List<T> items);


    void replaceAll(List<T> items);

    boolean contains(T item);

    boolean containsAll(List<T> items);

    void clear();

    void diff(DefaultDiffCallback<T> callback);
}
