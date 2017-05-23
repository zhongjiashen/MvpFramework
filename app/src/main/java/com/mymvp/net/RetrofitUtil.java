package com.mymvp.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mymvp.net.api.ApiService;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 1363655717 on 2017-05-22.
 */

public class RetrofitUtil {
    private static RetrofitUtil instance;
    private Retrofit retrofit;
    private static Gson gson;

    private RetrofitUtil(){
        OkHttpClient client = new OkHttpClient();
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.108:8080//Jsjdjkxmssm/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .client(client)
                .build();
    }

    public static RetrofitUtil getInstance(){
        if(instance == null){
            synchronized (RetrofitUtil.class){
                if(instance == null){
                    instance = new RetrofitUtil();
                }
            }
        }
        return instance;
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new TypeAdapter<Date>() {
                        @Override
                        public void write(JsonWriter out, Date value) throws IOException {
                            out.value(value == null ? null : value.getTime());
                        }

                        @Override
                        public Date read(JsonReader in) throws IOException {
                            if (in.peek() == JsonToken.NULL) {
                                in.nextNull();
                                return null;
                            }
                            return new Date(in.nextLong());
                        }
                    })
                    .create();
        }
        return gson;
    }
    public ApiService getOrderApi(){
        return retrofit.create(ApiService.class);
    }
    //获取血压数据
//    public void findByCustomerIdOrderList(Subscriber<Content<Order>> subscriber, int offset, int max){
//        Observable<Content<Order>> observable = getOrderApi().findByCustomerIdOrderList( offset, max).map(new HttpResultFunc<Content<Order>>());
//        toSubscribe(observable,subscriber);
//
//    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T>   Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<KmResult<T>,T> {

        @Override
        public T call(KmResult<T> kmResult) {
            if (kmResult.getCode() == -1) {
                throw new ApiException(100);
            }
            return kmResult.getData();
        }
    }
}
