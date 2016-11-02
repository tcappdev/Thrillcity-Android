package com.android.mobile.common.utils;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.android.mobile.common.services.CallBackAction;
import com.android.mobile.common.services.RetrofitException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Utils {

    public static boolean isFragmentAlive(Fragment fragment) {
        return fragment != null && fragment.getActivity() != null && fragment.isAdded() && !fragment.isDetached();
    }

    public static boolean isTaskNotRunning(AsyncTask task) {
        return task == null || task.getStatus() != AsyncTask.Status.RUNNING || task.isCancelled();
    }

    public static boolean isTaskRunning(AsyncTask task) {
        return !isTaskNotRunning(task);
    }

    /**
     * Subscribe to the observable without consideration of host lifecycle
     * or observing thread
     * will remain subscribed until service call completes or fails.
     *
     * @see Observable#subscribe(Observer)
     */
    public static <T> void subscribe(final Observable<T> observable,
                                     final CallBackAction<T> callback) {
        observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(T responseObject) {
                        if (callback != null) {
                            callback.onSuccess(responseObject);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (callback != null) {
                            try {
                                if (throwable instanceof RetrofitException) {
                                    callback.onError(((RetrofitException) throwable));
                                } else {
                                    callback.onError(RetrofitException.unexpectedError(throwable));
                                }
                            } catch (NullPointerException npe) {
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
