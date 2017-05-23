package com.mymvp.net;

import android.content.Context;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * Created by 1363655717 on 2017-05-22.
 */

public class MySubscriber<T> extends Subscriber<T> {
    private SubscriberOnNextListener<T> mSubscriberOnNextListener;
    private Context mContext;
    public MySubscriber(SubscriberOnNextListener<T> subscriberOnNextListener,Context context){
        mSubscriberOnNextListener = subscriberOnNextListener;
        mContext = context;
    }
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(mContext, "网络中断1，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(mContext, "网络中断2，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }
    }
}