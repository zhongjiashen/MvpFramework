package com.mymvp.net;

/**
 * Created by 1363655717 on 2017-05-22.
 */

public interface SubscriberOnNextListener<T> {
    void onNext(T t);
}